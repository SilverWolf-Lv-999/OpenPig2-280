package kakiku.pig2mod.mixin;

import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MouseHandler.class})
public abstract class MxMouseHandler {
   @Inject(
      method = {"grabMouse"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void grabMouse(CallbackInfo ci) {
      if (MyLib2.isCalledFromOtherModWithin5() && Minecraft.instance.screen != null) {
         ci.cancel();
      }
   }
}

