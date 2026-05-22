package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.world.level.entity.TransientEntitySectionManager.Callback"}
)
public abstract class MxTransientEntitySectionManager.Callback {
   @Shadow
   private EntityAccess entity;

   @Inject(
      method = {"m_142472_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void onRemove(Entity.RemovalReason pReason, CallbackInfo ci) {
      EntityAccess var4 = this.entity;
      if (var4 instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && Pig2.getAliveServerPigIDs(pig2.level).contains(pig2.id)) {
            ci.cancel();
            return;
         }
      }

   }
}

