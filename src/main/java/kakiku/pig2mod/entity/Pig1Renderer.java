package kakiku.pig2mod.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class Pig1Renderer extends PigRenderer {
   private static final ResourceLocation PIG1_LOCATION = new ResourceLocation("pig2mod", "textures/entity/pig1.png");

   public Pig1Renderer(EntityRendererProvider.Context pContext) {
      super(pContext);
   }

   public @NotNull ResourceLocation getTextureLocation(Pig pEntity) {
      return PIG1_LOCATION;
   }
}

