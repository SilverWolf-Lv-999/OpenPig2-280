package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.mixinsub.MyInterfaceOwnerLivingEntity;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AttributeInstance.class})
public abstract class MxAttributeInstance implements MyInterfaceOwnerLivingEntity {
   @Shadow
   @Final
   private Attribute attribute;
   @Unique
   public LivingEntity myOwner;

   public LivingEntity getOwner() {
      return this.myOwner;
   }

   public void setOwner(LivingEntity pOwner) {
      this.myOwner = pOwner;
   }

   @Inject(
      method = {"setBaseValue"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setBaseValue(double pBaseValue, CallbackInfo ci) {
      LivingEntity var6 = this.myOwner;
      if (var6 instanceof Pig2 pig2) {
         if (MyLib2.isCalledFromOtherModWithin5()) {
            ci.cancel();
            return;
         }

         if (!Pig2.isOshimai() && !pig2.isEnding() && this.attribute.equals(Attributes.MAX_HEALTH) && pBaseValue <= (double)0.0F) {
            ci.cancel();
            return;
         }
      } else {
         var6 = this.myOwner;
         if (var6 instanceof Player player) {
            if (this.attribute.equals(Attributes.MAX_HEALTH) && pBaseValue <= (double)0.0F && MyLib2.isCalledFromOtherModWithin5()) {
               ci.cancel();
               return;
            }
         }
      }

   }
}

