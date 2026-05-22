package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.mixinsub.MyInterfaceOwnerLivingEntity;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class MxLivingEntity extends MxEntity {
   @Unique
   public LivingEntity thisLivingEntity = (LivingEntity)this;
   @Shadow
   public float oRun;
   @Shadow
   public float run;
   @Shadow
   public float animStep;
   @Shadow
   public int fallFlyTicks;
   @Shadow
   public static EntityDataAccessor DATA_HEALTH_ID;
   @Shadow
   @Final
   public static float EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT;
   @Shadow
   public int deathTime;
   @Shadow
   public AttributeMap attributes;
   @Shadow
   public Brain brain;

   @Shadow
   public abstract void baseTick();

   @Shadow
   public abstract void updatingUsingItem();

   @Shadow
   public abstract void updateSwimAmount();

   @Shadow
   public abstract void detectEquipmentUpdates();

   @Shadow
   public abstract boolean checkBedExists();

   @Shadow
   public abstract float tickHeadTurn(float var1, float var2);

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void livingEntityTick(CallbackInfo ci) {
      MyLib2.stopMyselfIfImBadThread("★★スレッド強制停止 MxLivingEntity.tick()先頭");

      try {
         if (ForgeHooks.onLivingTick(this.thisLivingEntity)) {
            ci.cancel();
            return;
         }

         this.baseTick();
         this.updatingUsingItem();
         this.updateSwimAmount();
         if (!this.thisLivingEntity.level().isClientSide) {
            int i = this.thisLivingEntity.getArrowCount();
            if (i > 0) {
               if (this.thisLivingEntity.removeArrowTime <= 0) {
                  this.thisLivingEntity.removeArrowTime = 20 * (30 - i);
               }

               --this.thisLivingEntity.removeArrowTime;
               if (this.thisLivingEntity.removeArrowTime <= 0) {
                  this.thisLivingEntity.setArrowCount(i - 1);
               }
            }

            int j = this.thisLivingEntity.getStingerCount();
            if (j > 0) {
               if (this.thisLivingEntity.removeStingerTime <= 0) {
                  this.thisLivingEntity.removeStingerTime = 20 * (30 - j);
               }

               --this.thisLivingEntity.removeStingerTime;
               if (this.thisLivingEntity.removeStingerTime <= 0) {
                  this.thisLivingEntity.setStingerCount(j - 1);
               }
            }

            this.detectEquipmentUpdates();
            if (this.thisLivingEntity.tickCount % 20 == 0) {
               this.thisLivingEntity.getCombatTracker().recheckStatus();
            }

            if (this.thisLivingEntity.isSleeping() && !this.checkBedExists()) {
               this.thisLivingEntity.stopSleeping();
            }
         }

         if (!this.thisLivingEntity.isRemoved()) {
            this.thisLivingEntity.aiStep();
         }

         double d1 = this.thisLivingEntity.getX() - this.thisLivingEntity.xo;
         double d0 = this.thisLivingEntity.getZ() - this.thisLivingEntity.zo;
         float f = (float)(d1 * d1 + d0 * d0);
         float f1 = this.thisLivingEntity.yBodyRot;
         float f2 = 0.0F;
         this.oRun = this.run;
         float f3 = 0.0F;
         if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float)Math.sqrt((double)f) * 3.0F;
            float f4 = (float)Mth.atan2(d0, d1) * (180F / (float)Math.PI) - 90.0F;
            float f5 = Mth.abs(Mth.wrapDegrees(this.thisLivingEntity.getYRot()) - f4);
            if (95.0F < f5 && f5 < 265.0F) {
               f1 = f4 - 180.0F;
            } else {
               f1 = f4;
            }
         }

         if (this.thisLivingEntity.attackAnim > 0.0F) {
            f1 = this.thisLivingEntity.getYRot();
         }

         if (!this.thisLivingEntity.onGround()) {
            f3 = 0.0F;
         }

         this.run += (f3 - this.run) * 0.3F;
         this.thisLivingEntity.level().getProfiler().push("headTurn");
         f2 = this.tickHeadTurn(f1, f2);
         this.thisLivingEntity.level().getProfiler().pop();
         this.thisLivingEntity.level().getProfiler().push("rangeChecks");

         while(this.thisLivingEntity.getYRot() - this.thisLivingEntity.yRotO < -180.0F) {
            LivingEntity var16 = this.thisLivingEntity;
            var16.yRotO -= 360.0F;
         }

         while(this.thisLivingEntity.getYRot() - this.thisLivingEntity.yRotO >= 180.0F) {
            LivingEntity var17 = this.thisLivingEntity;
            var17.yRotO += 360.0F;
         }

         while(this.thisLivingEntity.yBodyRot - this.thisLivingEntity.yBodyRotO < -180.0F) {
            LivingEntity var18 = this.thisLivingEntity;
            var18.yBodyRotO -= 360.0F;
         }

         while(this.thisLivingEntity.yBodyRot - this.thisLivingEntity.yBodyRotO >= 180.0F) {
            LivingEntity var19 = this.thisLivingEntity;
            var19.yBodyRotO += 360.0F;
         }

         while(this.thisLivingEntity.getXRot() - this.thisLivingEntity.xRotO < -180.0F) {
            LivingEntity var20 = this.thisLivingEntity;
            var20.xRotO -= 360.0F;
         }

         while(this.thisLivingEntity.getXRot() - this.thisLivingEntity.xRotO >= 180.0F) {
            LivingEntity var21 = this.thisLivingEntity;
            var21.xRotO += 360.0F;
         }

         while(this.thisLivingEntity.yHeadRot - this.thisLivingEntity.yHeadRotO < -180.0F) {
            LivingEntity var22 = this.thisLivingEntity;
            var22.yHeadRotO -= 360.0F;
         }

         while(this.thisLivingEntity.yHeadRot - this.thisLivingEntity.yHeadRotO >= 180.0F) {
            LivingEntity var23 = this.thisLivingEntity;
            var23.yHeadRotO += 360.0F;
         }

         this.thisLivingEntity.level().getProfiler().pop();
         this.animStep += f2;
         if (this.thisLivingEntity.isFallFlying()) {
            ++this.fallFlyTicks;
         } else {
            this.fallFlyTicks = 0;
         }

         if (this.thisLivingEntity.isSleeping()) {
            this.thisLivingEntity.setXRot(0.0F);
         }
      } catch (Exception var12) {
      }

      ci.cancel();
   }

   @Inject(
      method = {"getHealth"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getHealth(CallbackInfoReturnable cir) {
      if (Pig2.isMaxFighting() && MyLib2.isThisOtherMOD(this.thisLivingEntity)) {
         cir.setReturnValue(0.0F);
      } else {
         cir.setReturnValue((Float)this.entityData.get(DATA_HEALTH_ID));
      }
   }

   @Inject(
      method = {"setHealth"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setHealth(float pHealth, CallbackInfo ci) {
      if (this.thisLivingEntity instanceof Player && pHealth <= 0.0F && MyLib2.isCalledFromOtherModWithin5()) {
         ci.cancel();
      } else {
         this.entityData.set(DATA_HEALTH_ID, Mth.clamp(pHealth, 0.0F, this.thisLivingEntity.getMaxHealth()));
         ci.cancel();
      }
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void onInit(EntityType pEntityType, Level pLevel, CallbackInfo ci) {
      ((MyInterfaceOwnerLivingEntity)this.attributes).setOwner(this.thisLivingEntity);
      ((MyInterfaceOwnerLivingEntity)this.brain).setOwner(this.thisLivingEntity);

      for(LazyOptional lazyOptional : this.thisLivingEntity.handlers) {
         ((MyInterfaceOwnerLivingEntity)lazyOptional).setOwner(this.thisLivingEntity);
      }

   }

   @Inject(
      method = {"reviveCaps"},
      at = {@At("TAIL")},
      remap = false
   )
   public void reviveCaps(CallbackInfo ci) {
      for(LazyOptional lazyOptional : this.thisLivingEntity.handlers) {
         ((MyInterfaceOwnerLivingEntity)lazyOptional).setOwner(this.thisLivingEntity);
      }

   }

   @Inject(
      method = {"dropAllDeathLoot"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void dropAllDeathLoot(DamageSource pDamageSource, CallbackInfo ci) {
      if (this.thisLivingEntity instanceof Player && MyLib2.isCalledFromOtherModWithin1ExceptMixin()) {
         ci.cancel();
      }
   }
}

