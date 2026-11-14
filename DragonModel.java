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

/** Simple blocky dragon: body, head, tail, two flapping wings, two legs. */
public class DragonModel extends EntityModel<DragonRenderState> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(DragonRiding.MOD_ID, "rideable_dragon"), "main");

    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;

    public DragonModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.wingLeft = root.getChild("wing_left");
        this.wingRight = root.getChild("wing_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-6, -5, -12, 12, 10, 24),
                PartPose.offset(0, 10, 0));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 40).addBox(-4, -4, -10, 8, 8, 10),
                PartPose.offset(0, 8, -12));
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(0, 60).addBox(-3, -3, 0, 6, 6, 20),
                PartPose.offset(0, 10, 12));
        root.addOrReplaceChild("wing_right",
                CubeListBuilder.create().texOffs(60, 0).addBox(0, -1, -8, 22, 2, 16),
                PartPose.offset(6, 6, -2));
        root.addOrReplaceChild("wing_left",
                CubeListBuilder.create().texOffs(60, 20).addBox(-22, -1, -8, 22, 2, 16),
                PartPose.offset(-6, 6, -2));
        root.addOrReplaceChild("leg_right",
                CubeListBuilder.create().texOffs(60, 40).addBox(-2, 0, -2, 4, 8, 4),
                PartPose.offset(4, 15, 6));
        root.addOrReplaceChild("leg_left",
                CubeListBuilder.create().texOffs(60, 55).addBox(-2, 0, -2, 4, 8, 4),
                PartPose.offset(-4, 15, 6));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(DragonRenderState state) {
        super.setupAnim(state);
        float flap = Mth.sin(state.ageInTicks * 0.4F) * 0.6F;
        this.wingRight.zRot = flap;
        this.wingLeft.zRot = -flap;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.tail.yRot = Mth.sin(state.ageInTicks * 0.1F) * 0.3F;
    }
}
