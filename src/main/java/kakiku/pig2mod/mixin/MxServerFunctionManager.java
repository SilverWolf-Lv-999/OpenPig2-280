package kakiku.pig2mod.mixin;

import javax.annotation.Nullable;
import kakiku.pig2mod.entity.Pig2;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.ServerFunctionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ServerFunctionManager.class})
public abstract class MxServerFunctionManager {
   @Inject(
      method = {"execute(Lnet/minecraft/commands/CommandFunction;Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/server/ServerFunctionManager.TraceCallbacks;)I"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void execute(CommandFunction pFunctionObject, CommandSourceStack pSource, @Nullable ServerFunctionManager.TraceCallbacks pTracer, CallbackInfoReturnable cir) {
      if (Pig2.anyPig2sAlive()) {
         cir.setReturnValue(0);
      }
   }
}

