package kakiku.pig2mod.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.concurrent.Executor;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyLong2ObjectOpenHashMap;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.SortedArraySet;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DistanceManager.class})
public abstract class MxDistanceManager {
   @Unique
   public DistanceManager myDistanceManager = (DistanceManager)this;
   @Unique
   private String thisPackeageName = this.getClass().getPackageName();
   @Shadow
   public Long2ObjectOpenHashMap tickets;
   @Shadow
   public Long2ObjectMap playersPerChunk;
   @Shadow
   public int simulationDistance;
   @Unique
   private ServerLevel myServerLevel = null;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void onInit(Executor pDispatcher, Executor pMainThreadExecutor, CallbackInfo ci) {
      this.tickets = new MyLong2ObjectOpenHashMap(this.tickets, MxDistanceManager.class);
      this.myDistanceManager.forcedTickets = new MyLong2ObjectOpenHashMap(this.myDistanceManager.forcedTickets, MxDistanceManager.class);
   }

   @Inject(
      method = {"removeTicket(JLnet/minecraft/server/level/Ticket;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removeTicket(long pChunkPos, Ticket pTicket, CallbackInfo ci) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.myServerLevel())) {
         if (pig2ChunkPos.toLong() == pChunkPos && !Pig2.isOshimai() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.DistanceManager.PlayerTicketTracker.m_143214_")) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"removeTicketsOnClosing"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removeTicketsOnClosing(CallbackInfo ci) {
      if (!Pig2.isOshimai() && Pig2.anyPig2sAlive()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"removeRegionTicket(Lnet/minecraft/server/level/TicketType;Lnet/minecraft/world/level/ChunkPos;ILjava/lang/Object;Z)V"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void removeRegionTicket(TicketType pType, ChunkPos pPos, int pDistance, Object pValue, boolean forceTicks, CallbackInfo ci) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.myServerLevel())) {
         if (pig2ChunkPos.equals(pPos) && !Pig2.isOshimai()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"updateChunkForced"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void updateChunkForced(ChunkPos pPos, boolean pAdd, CallbackInfo ci) {
      if (!pAdd) {
         for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.myServerLevel())) {
            if (pig2ChunkPos.equals(pPos) && !Pig2.isOshimai()) {
               ci.cancel();
               return;
            }
         }
      }

   }

   @Inject(
      method = {"removePlayer"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removePlayer(SectionPos pSectionPos, ServerPlayer pPlayer, CallbackInfo ci) {
      if (this.playersPerChunk.get(pSectionPos.chunk().toLong()) == null) {
         ci.cancel();
      } else if (!Pig2.isOshimai() && Pig2.anyPig2sAlive() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerLevel.m_143261_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ChunkMap.m_140184_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.world.entity.LivingEntity.m_6153_")) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"updateSimulationDistance"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void updateSimulationDistance(int pSimulationDistance, CallbackInfo ci) {
      if ((pSimulationDistance < this.simulationDistance || pSimulationDistance < 5) && !Pig2.isOshimai() && Pig2.anyPig2sAlive()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"getTickets"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getTickets(long pChunkPos, CallbackInfoReturnable cir) {
      Long2ObjectOpenHashMap var5 = this.tickets;
      if (var5 instanceof MyLong2ObjectOpenHashMap myTickets) {
         cir.setReturnValue((SortedArraySet)myTickets.myComputeIfAbsent(pChunkPos, (p_183923_) -> SortedArraySet.create(4)));
      }
   }

   @Unique
   private ServerLevel myServerLevel() {
      if (this.myServerLevel != null) {
         return this.myServerLevel;
      } else {
         for(ServerLevel sLevel : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
            if (sLevel.chunkSource.chunkMap.distanceManager == this.myDistanceManager) {
               this.myServerLevel = sLevel;
            }
         }

         return this.myServerLevel;
      }
   }
}

