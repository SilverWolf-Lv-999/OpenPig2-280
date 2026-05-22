package kakiku.pig2mod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MyModel extends EntityModel {
   private final ModelPart root;

   public MyModel(ModelPart root) {
      this.root = root;
   }

   public void setupAnim(Entity t, float v, float v1, float v2, float v3, float v4) {
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, float v, float v1, float v2, float v3) {
      this.root.render(poseStack, vertexConsumer, i, i1, v, v1, v2, v3);
   }
}

