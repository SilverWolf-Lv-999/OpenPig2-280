package kakiku.pig2mod.mixin;

import java.util.Set;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MySet;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.server.level.ChunkMap.TrackedEntity"}
)
public abstract class MxChunkMap.TrackedEntity {
   @Shadow
   public Entity entity;
   @Shadow
   public Set seenBy;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void onInit(ChunkMap p_140477_, Entity pEntity, int pRange, int pUpdateInterval, boolean pTrackDelta, CallbackInfo ci) {
      this.seenBy = new MySet(this.seenBy, this.getClass());
   }

   @Inject(
      method = {"m_140489_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void broadcast(Packet p_140490_, CallbackInfo ci) {
      Entity var4 = this.entity;
      if (var4 instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerEntity.m_8533_")) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"m_140499_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void broadcastAndSend(Packet pPacket, CallbackInfo ci) {
      Entity var4 = this.entity;
      if (var4 instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"m_140482_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void broadcastRemoved(CallbackInfo ci) {
      Entity var3 = this.entity;
      if (var3 instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"m_140485_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void removePlayer(ServerPlayer pPlayer, CallbackInfo ci) {
      Entity var4 = this.entity;
      if (var4 instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerLevel.m_143261_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.world.entity.LivingEntity.m_6153_")) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"m_140497_"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void updatePlayer(ServerPlayer pPlayer, CallbackInfo ci) {
      Entity var4 = this.entity;
      if (var4 instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ChunkMap.m_140199_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.network.ServerGamePacketListenerImpl.m_7185_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ChunkMap.m_140421_")) {
            ci.cancel();
            return;
         }
      }

   }
}

