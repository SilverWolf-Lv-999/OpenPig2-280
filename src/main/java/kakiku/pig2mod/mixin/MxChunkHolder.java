package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.level.ChunkHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChunkHolder.class})
public abstract class MxChunkHolder {
   @Inject(
      method = {"setTicketLevel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setTicketLevel(int pLevel, CallbackInfo ci) {
      if (Pig2.anyPig2sAlive() && !Pig2.isOshimai() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerChunkCache.m_8489_")) {
         ci.cancel();
      }
   }
}

