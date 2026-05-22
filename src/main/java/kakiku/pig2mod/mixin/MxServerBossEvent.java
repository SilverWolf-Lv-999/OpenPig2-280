package kakiku.pig2mod.mixin;

import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerBossEvent.class})
public abstract class MxServerBossEvent {
   @Unique
   public ServerBossEvent thisServerBossEvent = (ServerBossEvent)this;

   @Inject(
      method = {"addPlayer"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void addPlayer(ServerPlayer pPlayer, CallbackInfo ci) {
      if (!(this.thisServerBossEvent instanceof CustomBossEvent) && MyLib2.isCalledFromOtherModWithin5()) {
         ci.cancel();
      }
   }
}

