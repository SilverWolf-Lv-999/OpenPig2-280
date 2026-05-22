package kakiku.pig2mod.mixin;

import kakiku.pig2mod.mixinsub.MyInterfaceOwnerLivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Mob.class})
public abstract class MxMob {
   @Unique
   Mob thisMob = (Mob)this;
   @Shadow
   public GoalSelector goalSelector;
   @Shadow
   public GoalSelector targetSelector;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void onInit(EntityType pEntityType, Level pLevel, CallbackInfo ci) {
      ((MyInterfaceOwnerLivingEntity)this.goalSelector).setOwner(this.thisMob);
      ((MyInterfaceOwnerLivingEntity)this.targetSelector).setOwner(this.thisMob);
   }
}

