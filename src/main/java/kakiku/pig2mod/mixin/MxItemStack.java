package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ItemStack.class})
public abstract class MxItemStack implements IForgeItemStack {
   @Unique
   ItemStack thisItemStack = (ItemStack)this;

   @Shadow
   public abstract Item getItem();

   public boolean onEntitySwing(LivingEntity entity) {
      if (!Pig2.permitItemSwing(entity, this.getItem())) {
         return false;
      } else {
         try {
            return this.thisItemStack.getItem().onEntitySwing(this.thisItemStack, entity);
         } catch (Throwable var3) {
            return false;
         }
      }
   }

   @Inject(
      method = {"use"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void use(Level pLevel, Player pPlayer, InteractionHand pUsedHand, CallbackInfoReturnable cir) {
      try {
         cir.setReturnValue(this.getItem().use(pLevel, pPlayer, pUsedHand));
      } catch (Throwable var6) {
         cir.setReturnValue(InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand)));
      }
   }
}

