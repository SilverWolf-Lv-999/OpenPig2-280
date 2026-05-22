package kakiku.pig2mod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MyHelperRender0 extends EntityRenderer {
   public MyHelperRender0() {
      super(new EntityRendererProvider.Context(Minecraft.instance.getEntityRenderDispatcher(), Minecraft.instance.getItemRenderer(), Minecraft.instance.getBlockRenderer(), Minecraft.instance.getEntityRenderDispatcher().getItemInHandRenderer(), Minecraft.instance.getResourceManager(), Minecraft.instance.getEntityModels(), Minecraft.instance.font));
   }

   public boolean shouldRender(Entity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
      return false;
   }

   public void render(Entity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
   }

   public @NotNull ResourceLocation getTextureLocation(Entity pEntity) {
      return new ResourceLocation("pig2mod", "");
   }
}

