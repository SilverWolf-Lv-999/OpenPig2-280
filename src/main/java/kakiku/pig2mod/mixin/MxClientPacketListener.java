package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPacketListener.class})
public abstract class MxClientPacketListener {
   @Unique
   private String thisPackeageName = this.getClass().getPackageName();
   @Unique
   private String safePackeageName1 = ClientboundTeleportEntityPacket.class.getPackageName();
   @Shadow
   public ClientLevel level;

   @Inject(
      method = {"handleTeleportEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void handleTeleportEntity(ClientboundTeleportEntityPacket pPacket, CallbackInfo ci) {
      if (Pig2.getAliveServerPigIDs(this.level).contains(pPacket.getId()) && !Pig2.isOshimai() && !MyLib2.isCalledFromTheClassAndMethod("io.netty.channel.SimpleChannelInboundHandler.channelRead") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.util.thread.BlockableEventLoop.m_7245_")) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"handleMoveEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void handleMoveEntity(ClientboundMoveEntityPacket pPacket, CallbackInfo ci) {
      if (Pig2.getAliveServerPigIDs(this.level).contains(pPacket.entityId) && !Pig2.isOshimai() && !MyLib2.isCalledFromTheClassAndMethod("io.netty.channel.SimpleChannelInboundHandler.channelRead") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.util.thread.BlockableEventLoop.m_7245_")) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"handleRemoveEntities"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void handleRemoveEntities(ClientboundRemoveEntitiesPacket pPacket, CallbackInfo ci) {
      for(int pigId : Pig2.getAliveServerPigIDs(this.level)) {
         if (pPacket.entityIds.contains(pigId) && !Pig2.isOshimai() && !MyLib2.isCalledFromTheClassAndMethod("io.netty.channel.SimpleChannelInboundHandler.channelRead")) {
            pPacket.entityIds.remove(pigId);
         }
      }

   }
}

