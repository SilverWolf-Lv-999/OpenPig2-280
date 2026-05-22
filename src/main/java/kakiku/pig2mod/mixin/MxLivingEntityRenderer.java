package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntityRenderer.class})
public class MxLivingEntityRenderer {
   @Inject(
      method = {"getOverlayCoords"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void getOverlayCoords(LivingEntity pLivingEntity, float pU, CallbackInfoReturnable cir) {
      if (pLivingEntity instanceof Pig2 pig2) {
         cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(pU), OverlayTexture.v(false)));
         cir.cancel();
      }
   }
}

