package kakiku.pig2mod.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SynchedEntityData.class})
public abstract class MxSynchedEntityData {
   @Shadow
   public Entity entity;
   @Shadow
   public Int2ObjectMap itemsById;

   @Shadow
   abstract SynchedEntityData.DataItem getItem(EntityDataAccessor var1);

   @Inject(
      method = {"set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;Z)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void set(EntityDataAccessor pKey, Object pValue, boolean pForce, CallbackInfo ci) {
      if (this.entity instanceof Pig2) {
         if (pKey.equals(LivingEntity.DATA_HEALTH_ID) && pValue instanceof Float) {
            Float f = (Float)pValue;
            if (f <= 0.0F && MyLib2.isCalledFromOtherModWithin5()) {
               ci.cancel();
               return;
            }
         }
      } else if (this.entity instanceof Player && pKey.equals(LivingEntity.DATA_HEALTH_ID) && pValue instanceof Float) {
         Float f = (Float)pValue;
         if (f <= 0.0F && MyLib2.isCalledFromOtherModWithin1ExceptMixin()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void set(EntityDataAccessor pKey, Object pValue, CallbackInfo ci) {
      if (this.entity instanceof Player && pKey.equals(LivingEntity.DATA_HEALTH_ID) && pValue instanceof Float) {
         Float f = (Float)pValue;
         if (f <= 0.0F && MyLib2.isCalledFromOtherModWithin1ExceptMixin()) {
            ci.cancel();
            return;
         }
      }

   }
}

