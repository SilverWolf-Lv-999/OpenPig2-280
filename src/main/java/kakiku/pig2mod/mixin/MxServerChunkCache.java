package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerChunkCache.class})
public abstract class MxServerChunkCache {
   @Inject(
      method = {"removeEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removeEntity(Entity pEntity, CallbackInfo ci) {
      if (pEntity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }
}

