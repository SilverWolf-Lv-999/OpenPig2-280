package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Player.class})
public abstract class MxPlayer {
   @Unique
   private Player thisPlayer = (Player)this;

   @Inject(
      method = {"attack"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void attack(Entity pTarget, CallbackInfo ci) {
      if (pTarget instanceof Pig2 pig2) {
         if (MyLib2.isThisOtherMOD(this.thisPlayer.getMainHandItem().getItem())) {
            Pig2.seeFighting();
            ci.cancel();
            return;
         }
      }

   }
}

