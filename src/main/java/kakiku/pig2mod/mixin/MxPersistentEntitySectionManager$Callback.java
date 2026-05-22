package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.Visibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.world.level.entity.PersistentEntitySectionManager.Callback"}
)
public abstract class MxPersistentEntitySectionManager.Callback {
   @Unique
   private String thisPackeageName = this.getClass().getPackageName();
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
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"m_157620_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private void updateStatus(Visibility pOldVisibility, Visibility pNewVisibility, CallbackInfo ci) {
      if (pNewVisibility == Visibility.HIDDEN || pNewVisibility == Visibility.TRACKED && pOldVisibility == Visibility.TICKING) {
         EntityAccess var5 = this.entity;
         if (var5 instanceof Pig2) {
            Pig2 pig2 = (Pig2)var5;
            if (!Pig2.isOshimai() && !pig2.isEnding() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.world.level.entity.PersistentEntitySectionManager.Callback.m_142044_")) {
               ci.cancel();
               return;
            }
         }
      }

   }
}

