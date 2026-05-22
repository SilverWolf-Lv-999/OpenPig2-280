package kakiku.pig2mod.mixin;

import net.minecraft.world.item.crafting.RecipeManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({RecipeManager.class})
public abstract class MxRecipeManager {
   @Redirect(
      method = {"m_5787_"},
      at = @At(
   value = "INVOKE",
   target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"
),
      remap = false
   )
   private void skipErrorPrint(Logger logger, String msg, Object arg1, Object arg2) {
   }
}

