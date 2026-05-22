package kakiku.pig2mod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import kakiku.pig2mod.Pig2Mod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Pig2Renderer extends PigRenderer {
   private static final ResourceLocation PIG2_LOCATION = new ResourceLocation("pig2mod", "json/settings.json");

   public Pig2Renderer(EntityRendererProvider.Context pContext) {
      super(pContext);
      Pig2Mod.gMyHelper.setContext(pContext);
   }

   public ResourceLocation getTextureLocation(Pig pEntity) {
      return PIG2_LOCATION;
   }

   protected void setupRotations(Pig pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
      if (pEntityLiving.deathTime > 0) {
         pEntityLiving.deathTime = 0;
         pEntityLiving.setHealth(10.0F);
      }

      super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
   }
}

