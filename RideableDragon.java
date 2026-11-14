package com.dragonriding.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Tameable, saddle-able dragon that grows up.
 *
 * Growth:  baby (can't be ridden) -> young (ridable, short flames) -> adult (fast, long flames).
 *          Time passes on its own; feeding fish to a tamed dragon speeds it up.
 * Riding:  mouse = steer (flies where you look), W/S forward/brake, A/D strafe,
 *          Jump = rise, Sprint = boost (uses energy), look steeply down = dive (extra speed),
 *          Attack (left-click, held) = fire breath.
 */
public class RideableDragon extends TamableAnimal {
    private static final EntityDataAccessor<Boolean> SADDLED =
            SynchedEntityData.defineId(RideableDragon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> GROWTH =
            SynchedEntityData.defineId(RideableDragon.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> BREATHING =
            SynchedEntityData.defineId(RideableDragon.class, EntityDataSerializers.BOOLEAN);

    // growth thresholds in ticks (20 ticks = 1 second)
    public static final int YOUNG_AT = 24000;   // 20 minutes
    public static final int ADULT_AT = 72000;   // 60 minutes
    private static final int FISH_GROWTH = 6000; // each fish = 5 minutes

    private static final float BABY_SCALE = 0.45F;
    private static final float MAX_BOOST = 100.0F;

    // controls from the rider (set from the client, and from a network packet on the server)
    private boolean riderFire, riderBoost, riderUp;
    private float boostEnergy = MAX_BOOST;

    public RideableDragon(EntityType<? extends RideableDragon> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FLYING_SPEED, 0.9)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.2, 10.0F, 3.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        return nav;
    }

    // ---------------------------------------------------------------- data

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SADDLED, false);
        builder.define(GROWTH, 0);
        builder.define(BREATHING, false);
    }

    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(SADDLED, saddled);
    }

    public int getGrowth() {
        return this.entityData.get(GROWTH);
    }

    /** 0 = baby, 1 = young, 2 = adult */
    public int getStage() {
        int g = getGrowth();
        return g >= ADULT_AT ? 2 : (g >= YOUNG_AT ? 1 : 0);
    }

    public boolean isBreathing() {
        return this.entityData.get(BREATHING);
    }

    /** Server only: sets growth and updates size / health to match. */
    public void setGrowth(int growth) {
        growth = Mth.clamp(growth, 0, ADULT_AT);
        int oldStage = getStage();
        this.entityData.set(GROWTH, growth);

        float progress = growth / (float) ADULT_AT;
        float scale = Math.round((BABY_SCALE + (1.0F - BABY_SCALE) * progress) * 50.0F) / 50.0F;
        AttributeInstance scaleAttr = this.getAttribute(Attributes.SCALE);
        if (scaleAttr != null && Math.abs(scaleAttr.getBaseValue() - scale) > 0.001) {
            scaleAttr.setBaseValue(scale);
        }
        if (getStage() != oldStage || growth == 0) {
            AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
            if (health != null) {
                double max = getStage() == 0 ? 20.0 : (getStage() == 1 ? 40.0 : 80.0);
                health.setBaseValue(max);
                this.setHealth((float) max);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Saddled", isSaddled());
        output.putInt("Growth", getGrowth());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setSaddled(input.getBooleanOr("Saddled", false));
        this.entityData.set(GROWTH, input.getIntOr("Growth", 0));
    }

    /** Wild dragons that spawn naturally are adults; eggs / summons start as babies. */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        EntitySpawnReason reason, SpawnGroupData groupData) {
        if (reason == EntitySpawnReason.NATURAL || reason == EntitySpawnReason.CHUNK_GENERATION) {
            setGrowth(ADULT_AT);
        } else {
            setGrowth(0);
        }
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    // ---------------------------------------------------------------- taming / saddle / mount

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FISHES);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean client = this.level().isClientSide();

        if (!this.isTame()) {
            if (isFood(stack)) {
                if (!client) {
                    stack.consume(1, player);
                    if (this.random.nextInt(3) == 0) {
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (!this.isOwnedBy(player)) {
            return InteractionResult.PASS;
        }

        // feed fish to grow faster
        if (isFood(stack) && getStage() < 2) {
            if (!client) {
                stack.consume(1, player);
                setGrowth(getGrowth() + FISH_GROWTH);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.SUCCESS;
        }

        if (!isSaddled() && stack.is(Items.SADDLE)) {
            if (!client) {
                setSaddled(true);
                stack.consume(1, player);
            }
            return InteractionResult.SUCCESS;
        }

        if (player.isSecondaryUseActive()) {
            if (!client) {
                this.setOrderedToSit(!this.isOrderedToSit());
            }
            return InteractionResult.SUCCESS;
        }

        if (isSaddled() && !this.isVehicle()) {
            if (getStage() == 0) {
                if (!client) {
                    player.sendSystemMessage(
                            Component.literal("This dragon is still a baby. Feed it fish to help it grow!"));
                }
                return InteractionResult.SUCCESS;
            }
            if (!client) {
                player.startRiding(this);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        if (isSaddled() && getStage() >= 1 && this.getFirstPassenger() instanceof Player player) {
            return player;
        }
        return null;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        this.riderFire = false;
        this.riderBoost = false;
        this.riderUp = false;
        if (passenger instanceof Player player && !this.level().isClientSide()) {
            player.resetFallDistance();
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0));
        }
    }

    // ---------------------------------------------------------------- rider controls

    public void setRiderInput(boolean fire, boolean boost, boolean up) {
        this.riderFire = fire;
        this.riderBoost = boost;
        this.riderUp = up;
    }

    private double cruiseSpeed() {
        return getStage() >= 2 ? 0.8 : 0.6;   // blocks per tick
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.getControllingPassenger() instanceof Player rider) {
            // face where the rider looks
            this.setYRot(rider.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(rider.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

            double base = cruiseSpeed();
            double mult = 1.0;

            // boost (uses energy, refills when not boosting)
            boolean boosting = false;
            if (riderBoost && boostEnergy > 0.0F) {
                boosting = true;
                boostEnergy -= 1.5F;
                mult *= 1.6;
            } else if (boostEnergy < MAX_BOOST) {
                boostEnergy = Math.min(MAX_BOOST, boostEnergy + 0.6F);
            }

            // dive: looking steeply downward adds speed; climbing steeply costs a little
            float pitch = rider.getXRot();   // + = looking down
            if (pitch > 30.0F) {
                mult *= 1.0 + Math.min(1.0, (pitch - 30.0F) / 45.0F) * 0.6;
            } else if (pitch < -30.0F) {
                mult *= 0.85;
            }

            float yawRad = rider.getYRot() * Mth.DEG_TO_RAD;
            Vec3 look = rider.getLookAngle();
            Vec3 left = new Vec3(Mth.cos(yawRad), 0.0, Mth.sin(yawRad));

            Vec3 desired = look.scale(rider.zza * base * mult)
                    .add(left.scale(rider.xxa * base * 0.5));
            if (riderUp) {
                desired = desired.add(0.0, base * 0.5, 0.0);
            }

            double response = boosting ? 0.2 : 0.12;
            Vec3 vel = this.getDeltaMovement();
            this.setDeltaMovement(vel.scale(1.0 - response).add(desired.scale(response)));
            this.move(MoverType.SELF, this.getDeltaMovement());

            this.resetFallDistance();
            rider.resetFallDistance();
            this.calculateEntityAnimation(false);
        } else {
            super.travel(travelVector);
        }
    }

    // ---------------------------------------------------------------- server tick: growth + fire

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel server) {
            if (this.tickCount % 20 == 0 && getGrowth() < ADULT_AT) {
                setGrowth(getGrowth() + 20);
            }

            LivingEntity controller = this.getControllingPassenger();
            boolean firing = controller instanceof Player && riderFire && getStage() >= 1;
            if (this.entityData.get(BREATHING) != firing) {
                this.entityData.set(BREATHING, firing);
            }
            if (firing) {
                breatheFire(server, (Player) controller);
            }
        }
    }

    private void breatheFire(ServerLevel server, Player rider) {
        int stage = getStage();
        double range = stage >= 2 ? 14.0 : 8.0;
        float damage = stage >= 2 ? 5.0F : 2.5F;
        float scale = this.getScale();

        Vec3 look = rider.getLookAngle();
        float yaw = this.getYRot() * Mth.DEG_TO_RAD;
        Vec3 mouth = this.position().add(
                -Mth.sin(yaw) * 3.0 * scale,
                2.7 * scale,
                Mth.cos(yaw) * 3.0 * scale);

        // flames
        for (double d = 0.5; d <= range; d += 0.9) {
            Vec3 p = mouth.add(look.scale(d));
            double spread = 0.08 + d * 0.035;
            server.sendParticles(ParticleTypes.FLAME, p.x, p.y, p.z, 2, spread, spread, spread, 0.01);
        }
        server.sendParticles(ParticleTypes.SMOKE, mouth.x, mouth.y, mouth.z, 1, 0.1, 0.1, 0.1, 0.01);

        if (this.tickCount % 8 == 0) {
            server.playSound(null, mouth.x, mouth.y, mouth.z, SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1.2F, 0.7F);
        }

        // damage + ignite everything in a cone (every 5 ticks)
        if (this.tickCount % 5 == 0) {
            AABB box = new AABB(mouth, mouth.add(look.scale(range))).inflate(2.5);
            List<LivingEntity> targets = server.getEntitiesOfClass(LivingEntity.class, box, e ->
                    e != this && e != rider && !(e instanceof Player)
                            && !(e instanceof TamableAnimal pet && pet.isOwnedBy(rider)));
            for (LivingEntity target : targets) {
                Vec3 to = target.getEyePosition().subtract(mouth);
                double dist = to.length();
                if (dist > range) continue;
                if (dist > 2.5 && to.normalize().dot(look) < 0.82) continue;
                target.igniteForSeconds(5.0F);
                target.hurtServer(server, server.damageSources().playerAttack(rider), damage);
            }
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }
}
