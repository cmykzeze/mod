package com.dragonriding.client;

import com.dragonriding.DragonRiding;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/** Large detailed dragon: long neck, horns, spikes, jointed wings, segmented tail. (Generated) */
public class DragonModel extends EntityModel<DragonRenderState> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(DragonRiding.MOD_ID, "rideable_dragon"), "main");

    private final ModelPart neck1;
    private final ModelPart neck2;
    private final ModelPart neck3;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart wing_l;
    private final ModelPart wing2_l;
    private final ModelPart wing_r;
    private final ModelPart wing2_r;
    private final ModelPart fthigh_l;
    private final ModelPart fthigh_r;
    private final ModelPart hthigh_l;
    private final ModelPart hthigh_r;

    public DragonModel(ModelPart root) {
        super(root);
        this.neck1 = root.getChild("neck1");
        this.neck2 = root.getChild("neck1").getChild("neck2");
        this.neck3 = root.getChild("neck1").getChild("neck2").getChild("neck3");
        this.head = root.getChild("neck1").getChild("neck2").getChild("neck3").getChild("head");
        this.jaw = root.getChild("neck1").getChild("neck2").getChild("neck3").getChild("head").getChild("jaw");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail1").getChild("tail2");
        this.tail3 = root.getChild("tail1").getChild("tail2").getChild("tail3");
        this.tail4 = root.getChild("tail1").getChild("tail2").getChild("tail3").getChild("tail4");
        this.wing_l = root.getChild("wing_l");
        this.wing2_l = root.getChild("wing_l").getChild("wing2_l");
        this.wing_r = root.getChild("wing_r");
        this.wing2_r = root.getChild("wing_r").getChild("wing2_r");
        this.fthigh_l = root.getChild("fthigh_l");
        this.fthigh_r = root.getChild("fthigh_r");
        this.hthigh_l = root.getChild("hthigh_l");
        this.hthigh_r = root.getChild("hthigh_r");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                    .texOffs(0, 0).addBox(-7F, -33F, -14F, 14F, 13F, 30F)
                    .texOffs(34, 44).addBox(-6.5F, -32F, -22F, 13F, 13F, 8F)
                    .texOffs(118, 44).addBox(-6F, -31F, 16F, 12F, 11F, 7F)
                    .texOffs(89, 0).addBox(-5F, -20.2F, -20F, 10F, 1.6F, 40F)
                    .texOffs(96, 119).addBox(-4.5F, -30F, -22.3F, 9F, 9F, 0.6F)
                    .texOffs(166, 132).addBox(-0.9F, -37.6F, -16F, 1.8F, 4.8F, 2.2F)
                    .texOffs(40, 142).addBox(-0.9F, -37.25F, -10F, 1.8F, 4.45F, 2.2F)
                    .texOffs(49, 142).addBox(-0.9F, -36.9F, -4F, 1.8F, 4.1F, 2.2F)
                    .texOffs(58, 142).addBox(-0.9F, -36.55F, 2F, 1.8F, 3.75F, 2.2F)
                    .texOffs(67, 142).addBox(-0.9F, -36.2F, 8F, 1.8F, 3.4F, 2.2F)
                    .texOffs(76, 142).addBox(-0.9F, -35.85F, 14F, 1.8F, 3.05F, 2.2F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));

        PartDefinition neck1 = root.addOrReplaceChild("neck1",
                CubeListBuilder.create()
                    .texOffs(84, 67).addBox(-3.6F, -3.6F, -8F, 7.2F, 7.2F, 9F)
                    .texOffs(152, 119).addBox(-2.5F, 3F, -7.5F, 5F, 0.9F, 7.5F)
                    .texOffs(85, 142).addBox(-0.8F, -6.4F, -6F, 1.6F, 3F, 2.2F),
                PartPose.offsetAndRotation(0F, -5F, -20F, -0.5F, 0F, 0F));

        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create()
                    .texOffs(207, 67).addBox(-3.2F, -3.2F, -8F, 6.4F, 6.4F, 9F)
                    .texOffs(178, 119).addBox(-2.2F, 2.7F, -7.5F, 4.4F, 0.8F, 7.5F)
                    .texOffs(94, 142).addBox(-0.8F, -6F, -6F, 1.6F, 3F, 2.2F),
                PartPose.offsetAndRotation(0F, 0F, -7.5F, -0.25F, 0F, 0F));

        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create()
                    .texOffs(0, 103).addBox(-2.9F, -2.9F, -8F, 5.8F, 5.8F, 9F)
                    .texOffs(203, 119).addBox(-2F, 2.4F, -7.5F, 4F, 0.8F, 7.5F)
                    .texOffs(103, 142).addBox(-0.8F, -5.7F, -6F, 1.6F, 3F, 2.2F),
                PartPose.offsetAndRotation(0F, 0F, -7.5F, 0.1F, 0F, 0F));

        PartDefinition head = neck3.addOrReplaceChild("head",
                CubeListBuilder.create()
                    .texOffs(157, 44).addBox(-4.5F, -4F, -9F, 9F, 8F, 9.5F)
                    .texOffs(99, 103).addBox(-3F, -1.5F, -16.5F, 6F, 4.3F, 8F)
                    .texOffs(122, 151).addBox(-2.2F, -2.3F, -16.7F, 1F, 1F, 1.3F)
                    .texOffs(128, 151).addBox(1.2F, -2.3F, -16.7F, 1F, 1F, 1.3F)
                    .texOffs(112, 142).addBox(-4.8F, -4.6F, -7.5F, 3.8F, 1.4F, 4F)
                    .texOffs(129, 142).addBox(1F, -4.6F, -7.5F, 3.8F, 1.4F, 4F)
                    .texOffs(0, 151).addBox(-4.7F, -2.2F, -6.4F, 0.3F, 1.8F, 1.6F)
                    .texOffs(5, 151).addBox(4.4F, -2.2F, -6.4F, 0.3F, 1.8F, 1.6F)
                    .texOffs(79, 156).addBox(-2.6F, 2.5F, -15.8F, 0.8F, 1.1F, 0.8F)
                    .texOffs(84, 156).addBox(1.8F, 2.5F, -15.8F, 0.8F, 1.1F, 0.8F),
                PartPose.offsetAndRotation(0F, 0F, -7.5F, 0.55F, 0F, 0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw",
                CubeListBuilder.create()
                    .texOffs(195, 44).addBox(-3.2F, -0.8F, -14.8F, 6.4F, 2.2F, 15.3F)
                    .texOffs(208, 103).addBox(-2.2F, 1F, -14F, 4.4F, 0.7F, 11F)
                    .texOffs(89, 156).addBox(-2.4F, -1.6F, -14.2F, 0.6F, 1F, 0.6F)
                    .texOffs(93, 156).addBox(1.8F, -1.6F, -14.2F, 0.6F, 1F, 0.6F),
                PartPose.offsetAndRotation(0F, 3.4F, -1.5F, 0.05F, 0F, 0F));

        PartDefinition horn_l = head.addOrReplaceChild("horn_l",
                CubeListBuilder.create()
                    .texOffs(0, 119).addBox(-1.3F, -1.3F, 0F, 2.6F, 2.6F, 9F),
                PartPose.offsetAndRotation(-3.5F, -3.5F, -1.5F, 0.35F, -0.35F, 0F));

        PartDefinition horn_r = head.addOrReplaceChild("horn_r",
                CubeListBuilder.create()
                    .texOffs(25, 119).addBox(-1.3F, -1.3F, 0F, 2.6F, 2.6F, 9F),
                PartPose.offsetAndRotation(3.5F, -3.5F, -1.5F, 0.35F, 0.35F, 0F));

        PartDefinition horntip_l = horn_l.addOrReplaceChild("horntip_l",
                CubeListBuilder.create()
                    .texOffs(175, 132).addBox(-0.8F, -0.8F, 0F, 1.6F, 1.6F, 6F),
                PartPose.offsetAndRotation(0F, 0F, 8.5F, 0.3F, 0F, 0F));

        PartDefinition horntip_r = horn_r.addOrReplaceChild("horntip_r",
                CubeListBuilder.create()
                    .texOffs(192, 132).addBox(-0.8F, -0.8F, 0F, 1.6F, 1.6F, 6F),
                PartPose.offsetAndRotation(0F, 0F, 8.5F, 0.3F, 0F, 0F));

        PartDefinition tail1 = root.addOrReplaceChild("tail1",
                CubeListBuilder.create()
                    .texOffs(77, 44).addBox(-4.4F, -4.2F, -1F, 8.8F, 8.4F, 11F)
                    .texOffs(146, 142).addBox(-0.8F, -7F, 1F, 1.6F, 3F, 2.2F),
                PartPose.offsetAndRotation(0F, -3F, 21F, -0.05F, 0F, 0F));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create()
                    .texOffs(0, 67).addBox(-3.6F, -3.4F, -1F, 7.2F, 6.8F, 11F)
                    .texOffs(155, 142).addBox(-0.8F, -6.2F, 1F, 1.6F, 3F, 2.2F),
                PartPose.offsetAndRotation(0F, 0F, 9.5F, -0.05F, 0F, 0F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create()
                    .texOffs(118, 67).addBox(-2.8F, -2.6F, -1F, 5.6F, 5.2F, 11F)
                    .texOffs(164, 142).addBox(-0.8F, -5.4F, 1F, 1.6F, 3F, 2.2F),
                PartPose.offsetAndRotation(0F, 0F, 9.5F, 0.05F, 0F, 0F));

        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create()
                    .texOffs(31, 103).addBox(-2F, -1.9F, -1F, 4F, 3.8F, 10.5F),
                PartPose.offsetAndRotation(0F, 0F, 9.5F, 0.1F, 0F, 0F));

        PartDefinition tailtip = tail4.addOrReplaceChild("tailtip",
                CubeListBuilder.create()
                    .texOffs(117, 119).addBox(-4.5F, -0.8F, 0F, 9F, 1.6F, 8F)
                    .texOffs(227, 119).addBox(-0.7F, -1.5F, 6F, 1.4F, 3F, 6F),
                PartPose.offsetAndRotation(0F, 0F, 9F, 0F, 0F, 0F));

        PartDefinition fthigh_l = root.addOrReplaceChild("fthigh_l",
                CubeListBuilder.create()
                    .texOffs(153, 67).addBox(-3F, -2F, -3.5F, 6F, 10F, 7F),
                PartPose.offsetAndRotation(-8F, -2F, -14F, -0.3F, 0F, 0F));

        PartDefinition fthigh_r = root.addOrReplaceChild("fthigh_r",
                CubeListBuilder.create()
                    .texOffs(180, 67).addBox(-3F, -2F, -3.5F, 6F, 10F, 7F),
                PartPose.offsetAndRotation(8F, -2F, -14F, -0.3F, 0F, 0F));

        PartDefinition fshin_l = fthigh_l.addOrReplaceChild("fshin_l",
                CubeListBuilder.create()
                    .texOffs(61, 103).addBox(-2.2F, -0.5F, -2.2F, 4.4F, 9.5F, 4.4F),
                PartPose.offsetAndRotation(0F, 8F, 0F, 0.7F, 0F, 0F));

        PartDefinition fshin_r = fthigh_r.addOrReplaceChild("fshin_r",
                CubeListBuilder.create()
                    .texOffs(80, 103).addBox(-2.2F, -0.5F, -2.2F, 4.4F, 9.5F, 4.4F),
                PartPose.offsetAndRotation(0F, 8F, 0F, 0.7F, 0F, 0F));

        PartDefinition ffoot_l = fshin_l.addOrReplaceChild("ffoot_l",
                CubeListBuilder.create()
                    .texOffs(0, 132).addBox(-2.5F, -0.5F, -5F, 5F, 2F, 6.5F)
                    .texOffs(10, 151).addBox(-2.2F, 0.2F, -7.2F, 1F, 1F, 2.2F)
                    .texOffs(18, 151).addBox(-0.5F, 0.2F, -7.2F, 1F, 1F, 2.2F)
                    .texOffs(26, 151).addBox(1.2F, 0.2F, -7.2F, 1F, 1F, 2.2F),
                PartPose.offsetAndRotation(0F, 8.5F, 0F, -0.4F, 0F, 0F));

        PartDefinition ffoot_r = fshin_r.addOrReplaceChild("ffoot_r",
                CubeListBuilder.create()
                    .texOffs(24, 132).addBox(-2.5F, -0.5F, -5F, 5F, 2F, 6.5F)
                    .texOffs(34, 151).addBox(1.2F, 0.2F, -7.2F, 1F, 1F, 2.2F)
                    .texOffs(42, 151).addBox(-0.5F, 0.2F, -7.2F, 1F, 1F, 2.2F)
                    .texOffs(50, 151).addBox(-2.2F, 0.2F, -7.2F, 1F, 1F, 2.2F),
                PartPose.offsetAndRotation(0F, 8.5F, 0F, -0.4F, 0F, 0F));

        PartDefinition hthigh_l = root.addOrReplaceChild("hthigh_l",
                CubeListBuilder.create()
                    .texOffs(190, 0).addBox(-3.75F, -2.5F, -4.375F, 7.5F, 12.5F, 8.75F),
                PartPose.offsetAndRotation(-7.5F, -1F, 15F, 0.2F, 0F, 0F));

        PartDefinition hthigh_r = root.addOrReplaceChild("hthigh_r",
                CubeListBuilder.create()
                    .texOffs(0, 44).addBox(-3.75F, -2.5F, -4.375F, 7.5F, 12.5F, 8.75F),
                PartPose.offsetAndRotation(7.5F, -1F, 15F, 0.2F, 0F, 0F));

        PartDefinition hshin_l = hthigh_l.addOrReplaceChild("hshin_l",
                CubeListBuilder.create()
                    .texOffs(38, 67).addBox(-2.75F, -0.625F, -2.75F, 5.5F, 11.875F, 5.5F),
                PartPose.offsetAndRotation(0F, 10F, 0F, -0.6F, 0F, 0F));

        PartDefinition hshin_r = hthigh_r.addOrReplaceChild("hshin_r",
                CubeListBuilder.create()
                    .texOffs(61, 67).addBox(-2.75F, -0.625F, -2.75F, 5.5F, 11.875F, 5.5F),
                PartPose.offsetAndRotation(0F, 10F, 0F, -0.6F, 0F, 0F));

        PartDefinition hfoot_l = hshin_l.addOrReplaceChild("hfoot_l",
                CubeListBuilder.create()
                    .texOffs(48, 132).addBox(-3.125F, -0.5F, -5F, 6.25F, 2F, 6.5F)
                    .texOffs(58, 151).addBox(-2.75F, 0.2F, -7.2F, 1.25F, 1F, 2.2F)
                    .texOffs(66, 151).addBox(-0.5F, 0.2F, -7.2F, 1F, 1F, 2.2F)
                    .texOffs(74, 151).addBox(1.5F, 0.2F, -7.2F, 1.25F, 1F, 2.2F),
                PartPose.offsetAndRotation(0F, 10.625F, 0F, -0.4F, 0F, 0F));

        PartDefinition hfoot_r = hshin_r.addOrReplaceChild("hfoot_r",
                CubeListBuilder.create()
                    .texOffs(75, 132).addBox(-3.125F, -0.5F, -5F, 6.25F, 2F, 6.5F)
                    .texOffs(82, 151).addBox(1.5F, 0.2F, -7.2F, 1.25F, 1F, 2.2F)
                    .texOffs(90, 151).addBox(-0.5F, 0.2F, -7.2F, 1F, 1F, 2.2F)
                    .texOffs(98, 151).addBox(-2.75F, 0.2F, -7.2F, 1.25F, 1F, 2.2F),
                PartPose.offsetAndRotation(0F, 10.625F, 0F, -0.4F, 0F, 0F));

        PartDefinition wing_l = root.addOrReplaceChild("wing_l",
                CubeListBuilder.create()
                    .texOffs(209, 132).addBox(-15F, -1.6F, -2F, 15.5F, 3.2F, 4F)
                    .texOffs(50, 119).addBox(-3F, -2.5F, -3F, 5F, 5F, 6F)
                    .texOffs(0, 86).addBox(-14.5F, -0.4F, 2F, 13.5F, 0.8F, 15F),
                PartPose.offsetAndRotation(-7F, -7F, -7F, 0F, 0F, 0.2F));

        PartDefinition wing_r = root.addOrReplaceChild("wing_r",
                CubeListBuilder.create()
                    .texOffs(0, 142).addBox(-0.5F, -1.6F, -2F, 15.5F, 3.2F, 4F)
                    .texOffs(73, 119).addBox(-2F, -2.5F, -3F, 5F, 5F, 6F)
                    .texOffs(58, 86).addBox(1F, -0.4F, 2F, 13.5F, 0.8F, 15F),
                PartPose.offsetAndRotation(7F, -7F, -7F, 0F, 0F, -0.2F));

        PartDefinition wing2_l = wing_l.addOrReplaceChild("wing2_l",
                CubeListBuilder.create()
                    .texOffs(173, 142).addBox(-16F, -1.3F, -1.6F, 16.5F, 2.6F, 3.2F)
                    .texOffs(106, 151).addBox(-17.5F, -0.8F, -2.6F, 1.7F, 1.6F, 1.8F)
                    .texOffs(102, 132).addBox(-28F, -0.4F, 1.6F, 8F, 0.8F, 7.4F)
                    .texOffs(128, 103).addBox(-20F, -0.4F, 1.6F, 8F, 0.8F, 11.4F)
                    .texOffs(116, 86).addBox(-12F, -0.4F, 1.6F, 12.5F, 0.8F, 14.4F),
                PartPose.offsetAndRotation(-15F, 0F, 0F, 0F, 0F, 0F));

        PartDefinition wing2_r = wing_r.addOrReplaceChild("wing2_r",
                CubeListBuilder.create()
                    .texOffs(214, 142).addBox(-0.5F, -1.3F, -1.6F, 16.5F, 2.6F, 3.2F)
                    .texOffs(114, 151).addBox(15.8F, -0.8F, -2.6F, 1.7F, 1.6F, 1.8F)
                    .texOffs(134, 132).addBox(20F, -0.4F, 1.6F, 8F, 0.8F, 7.4F)
                    .texOffs(168, 103).addBox(12F, -0.4F, 1.6F, 8F, 0.8F, 11.4F)
                    .texOffs(171, 86).addBox(-0.5F, -0.4F, 1.6F, 12.5F, 0.8F, 14.4F),
                PartPose.offsetAndRotation(15F, 0F, 0F, 0F, 0F, 0F));

        PartDefinition fing1_l = wing2_l.addOrReplaceChild("fing1_l",
                CubeListBuilder.create()
                    .texOffs(134, 151).addBox(-14F, -0.7F, -0.7F, 14.5F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(-16F, 0F, 0F, 0F, 0.35F, 0F));

        PartDefinition fing1_r = wing2_r.addOrReplaceChild("fing1_r",
                CubeListBuilder.create()
                    .texOffs(167, 151).addBox(-0.5F, -0.7F, -0.7F, 14.5F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(16F, 0F, 0F, 0F, -0.35F, 0F));

        PartDefinition fing2_l = wing2_l.addOrReplaceChild("fing2_l",
                CubeListBuilder.create()
                    .texOffs(200, 151).addBox(-12F, -0.7F, -0.7F, 12.5F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(-16F, 0F, 0F, 0F, 0.75F, 0F));

        PartDefinition fing2_r = wing2_r.addOrReplaceChild("fing2_r",
                CubeListBuilder.create()
                    .texOffs(0, 156).addBox(-0.5F, -0.7F, -0.7F, 12.5F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(16F, 0F, 0F, 0F, -0.75F, 0F));

        PartDefinition fing3_l = wing2_l.addOrReplaceChild("fing3_l",
                CubeListBuilder.create()
                    .texOffs(29, 156).addBox(-10F, -0.7F, -0.7F, 10.5F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(-16F, 0F, 0F, 0F, 1.15F, 0F));

        PartDefinition fing3_r = wing2_r.addOrReplaceChild("fing3_r",
                CubeListBuilder.create()
                    .texOffs(54, 156).addBox(-0.5F, -0.7F, -0.7F, 10.5F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(16F, 0F, 0F, 0F, -1.15F, 0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public void setupAnim(DragonRenderState state) {
        super.setupAnim(state);
        float t = state.ageInTicks;

        if (state.flying) {
            // wings: shoulder flap + lagging forearm; legs tucked
            float flap = Mth.sin(t * 0.33F) * 0.55F;
            this.wing_l.zRot = 0.2F + flap;
            this.wing_r.zRot = -(0.2F + flap);
            this.wing_l.yRot = 0.0F;
            this.wing_r.yRot = 0.0F;
            this.wing2_l.zRot = flap * 0.55F;
            this.wing2_r.zRot = -flap * 0.55F;
            this.wing2_l.yRot = 0.0F;
            this.wing2_r.yRot = 0.0F;
            this.fthigh_l.xRot = -0.3F;
            this.fthigh_r.xRot = -0.3F;
            this.hthigh_l.xRot = 0.2F;
            this.hthigh_r.xRot = 0.2F;
        } else {
            // on the ground: wings folded back along the body, legs walk
            this.wing_l.zRot = 0.35F;
            this.wing_r.zRot = -0.35F;
            this.wing_l.yRot = 1.2F;
            this.wing_r.yRot = -1.2F;
            this.wing2_l.zRot = 0.0F;
            this.wing2_r.zRot = 0.0F;
            this.wing2_l.yRot = 0.9F;
            this.wing2_r.yRot = -0.9F;
            float swing = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.1F * state.walkAnimationSpeed;
            this.fthigh_l.xRot = -0.3F + swing;
            this.fthigh_r.xRot = -0.3F - swing;
            this.hthigh_l.xRot = 0.2F - swing;
            this.hthigh_r.xRot = 0.2F + swing;
        }

        // neck + head follow where the dragon looks, with a gentle S-sway
        float headPitch = Mth.clamp(state.xRot, -40.0F, 40.0F) * Mth.DEG_TO_RAD * 0.6F;
        float headYaw = Mth.clamp(state.yRot, -50.0F, 50.0F) * Mth.DEG_TO_RAD;
        this.neck1.yRot = headYaw * 0.25F + Mth.sin(t * 0.07F) * 0.05F;
        this.neck2.yRot = headYaw * 0.3F + Mth.sin(t * 0.07F - 0.6F) * 0.05F;
        this.neck3.yRot = headYaw * 0.45F + Mth.sin(t * 0.07F - 1.2F) * 0.04F;
        this.head.xRot = 0.55F + headPitch;
        this.head.yRot = 0.0F;

        // mouth opens while breathing fire
        this.jaw.xRot = state.breathing ? 0.65F : 0.05F + Mth.sin(t * 0.09F) * 0.03F;

        // tail wave
        this.tail1.yRot = Mth.sin(t * 0.11F) * 0.18F;
        this.tail2.yRot = Mth.sin(t * 0.11F - 0.7F) * 0.22F;
        this.tail3.yRot = Mth.sin(t * 0.11F - 1.4F) * 0.26F;
        this.tail4.yRot = Mth.sin(t * 0.11F - 2.1F) * 0.30F;
    }
}
