package kakiku.pig2mod.mixin;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.List;
import kakiku.pig2mod.MyLib;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundPingPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Connection.class})
public abstract class MxConnection {
   @Shadow
   public boolean disconnectionHandled;

   @Inject(
      method = {"send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void send(Packet pPacket, PacketSendListener pSendListener, CallbackInfo ci) {
      if (!this.disconnectionHandled && !(pPacket instanceof ClientboundPingPacket) && !Pig2.isOshimai() && Pig2.anyPig2sAlive()) {
         pPacket = this.fixPacket(pPacket);
         if (pPacket == null) {
            ci.cancel();
            return;
         }
      }

   }

   @Unique
   private Packet fixPacket(Packet pPacket) {
      if (pPacket instanceof ClientboundForgetLevelChunkPacket clientboundForgetLevelChunkPacket) {
         Level myLevel = this.myLevel();

         for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(myLevel)) {
            if (pig2ChunkPos.x == clientboundForgetLevelChunkPacket.x && pig2ChunkPos.z == clientboundForgetLevelChunkPacket.z && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerPlayer.m_9088_")) {
               return null;
            }
         }
      } else {
         if (pPacket instanceof ClientboundRemoveEntitiesPacket clientboundRemoveEntitiesPacket) {
            Level myLevel = this.myLevel();
            IntArrayList entityIds2 = new IntArrayList();

            for(int entityId : clientboundRemoveEntitiesPacket.getEntityIds().toIntArray()) {
               if (!Pig2.getAliveServerPigIDs(myLevel).contains(entityId) || MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerLevel.m_143261_") || MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerEntity.m_8534_")) {
                  entityIds2.add(entityId);
               }
            }

            clientboundRemoveEntitiesPacket.entityIds = entityIds2;
            return clientboundRemoveEntitiesPacket;
         }

         if (pPacket instanceof ClientboundBundlePacket clientboundBundlePacket) {
            List<Packet<ClientGamePacketListener>> packets2 = new ArrayList();

            for(Packet packet1 : clientboundBundlePacket.packets) {
               Packet<ClientGamePacketListener> packet2 = this.fixPacket(packet1);
               if (packet2 != null) {
                  packets2.add(packet2);
               }
            }

            clientboundBundlePacket.packets = packets2;
            return clientboundBundlePacket;
         }
      }

      return pPacket;
   }

   @Unique
   private Level myLevel() {
      return MyLib.getLevel();
   }
}

