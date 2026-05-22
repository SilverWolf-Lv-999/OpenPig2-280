package kakiku.pig2mod.mixin;

import kakiku.pig2mod.MyLib;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public abstract class MxEntity {
   @Unique
   public Entity myEntity = (Entity)this;
   @Shadow
   public Entity.RemovalReason removalReason;
   @Shadow
   public EntityInLevelCallback levelCallback;
   @Shadow
   public SynchedEntityData entityData;
   @Shadow
   public Level level;

   @Shadow
   protected abstract boolean getSharedFlag(int var1);

   @Inject(
      method = {"isRemoved"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isRemoved(CallbackInfoReturnable cir) {
      if (Pig2.isMaxFighting() && MyLib2.isThisOtherMOD(this.myEntity)) {
         if (!MyLib.isCalledFromSpawnByServerPlayer()) {
            this.removalReason = RemovalReason.KILLED;
            cir.setReturnValue(true);
            return;
         }

         Pig2.resetMaxFight();
      }

   }

   @Inject(
      method = {"setLevelCallback"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setLevelCallback(EntityInLevelCallback pLevelCallback, CallbackInfo ci) {
      if (MyLib2.isThisOtherMOD(this.myEntity)) {
         if (MyLib2.isCalledFromOtherModWithin5()) {
            ci.cancel();
         } else {
            this.levelCallback = pLevelCallback;
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"setRemoved"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setRemoved(Entity.RemovalReason pRemovalReason, CallbackInfo ci) {
      Entity var5 = this.myEntity;
      if (var5 instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && MyLib2.getCaller2().getDeclaringClass() != Pig2.class) {
            ci.cancel();
            return;
         }
      } else {
         var5 = this.myEntity;
         if (var5 instanceof Player player) {
            if (!Pig2.isOshimai() && Pig2.anyPig2sAlive() && MyLib2.isCalledFromOtherModWithin5()) {
               ci.cancel();
               return;
            }
         }
      }

      if (this.removalReason == null) {
         this.removalReason = pRemovalReason;
      }

      if (this.removalReason.shouldDestroy()) {
         this.myEntity.stopRiding();
      }

      this.myEntity.getPassengers().forEach(Entity::m_8127_);
      this.levelCallback.onRemove(pRemovalReason);
      ci.cancel();
   }

   @Inject(
      method = {"isShiftKeyDown"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isShiftKeyDown(CallbackInfoReturnable cir) {
      cir.setReturnValue(this.getSharedFlag(1));
   }

   @Inject(
      method = {"save"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void save(CompoundTag pCompound, CallbackInfoReturnable cir) {
      if (MyLib2.isThisOtherMOD(this.myEntity)) {
         cir.setReturnValue(false);
      } else {
         cir.setReturnValue(this.myEntity.isPassenger() ? false : this.myEntity.saveAsPassenger(pCompound));
      }
   }

   @Inject(
      method = {"discard"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void discard(CallbackInfo ci) {
      if (!Pig2.isOshimai() && Pig2.anyPig2sAlive()) {
         Entity var3 = this.myEntity;
         if (var3 instanceof Player) {
            Player player = (Player)var3;
            if (MyLib2.isCalledFromOtherModWithin5()) {
               ci.cancel();
               return;
            }
         }
      }

      try {
         this.myEntity.remove(RemovalReason.DISCARDED);
      } catch (Exception var4) {
      }

      ci.cancel();
   }

   @Inject(
      method = {"setPos(DDD)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setPos(double pX, double pY, double pZ, CallbackInfo ci) {
      Entity var9 = this.myEntity;
      if (var9 instanceof Player player) {
         if (!Pig2.isOshimai() && Pig2.anyPig2sAlive() && MyLib2.isCalledFromOtherModWithin5()) {
            ci.cancel();
            return;
         }
      }

   }
}

