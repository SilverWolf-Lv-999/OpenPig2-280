package kakiku.pig2mod.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Inventory.class})
public abstract class MxInventory {
   @Inject(
      method = {"removeItem(Lnet/minecraft/world/item/ItemStack;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removeItem(ItemStack pStack, CallbackInfo ci) {
      String itemClassName = pStack.getItem().getClass().getName();
      StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
      if (caller.getClassName().equals(itemClassName) && "m_6883_".equals(caller.getMethodName())) {
         ci.cancel();
      }
   }
}

