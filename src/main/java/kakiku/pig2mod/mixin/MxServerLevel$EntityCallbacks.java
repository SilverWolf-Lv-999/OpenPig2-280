package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.server.level.ServerLevel.EntityCallbacks"}
)
public abstract class MxServerLevel.EntityCallbacks {
   @Inject(
      method = {"m_141981_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void onTrackingEnd(Entity entity, CallbackInfo ci) {
      if (entity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"m_141983_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void onTickingEnd(Entity entity, CallbackInfo ci) {
      if (entity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }
}

