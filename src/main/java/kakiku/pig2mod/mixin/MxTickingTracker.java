package kakiku.pig2mod.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyLong2ObjectOpenHashMap;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TickingTracker;
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

@Mixin({TickingTracker.class})
public abstract class MxTickingTracker {
   @Unique
   private TickingTracker myTickingTracker = (TickingTracker)this;
   @Shadow
   public Long2ObjectOpenHashMap tickets;
   @Unique
   private ServerLevel myServerLevel = null;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void TickingTracker(CallbackInfo ci) {
      this.tickets = new MyLong2ObjectOpenHashMap(this.tickets, TickingTracker.class);
   }

   @Inject(
      method = {"removeTicket(JLnet/minecraft/server/level/Ticket;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removeTicket(long pChunkPos, Ticket pTicket, CallbackInfo ci) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.myServerLevel())) {
         if (pig2ChunkPos.toLong() == pChunkPos && !Pig2.isOshimai() && !MyLib2.isCalledFromTheClass("net.minecraft.server.level.DistanceManager") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerLevel.m_143261_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ChunkMap.m_140184_")) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"replacePlayerTicketsLevel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void replacePlayerTicketsLevel(int pTicketLevel, CallbackInfo ci) {
      if (!Pig2.isOshimai() && Pig2.anyPig2sAlive() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.DistanceManager.m_183911_")) {
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
         cir.setReturnValue((SortedArraySet)myTickets.myComputeIfAbsent(pChunkPos, (p_184180_) -> SortedArraySet.create(4)));
      }
   }

   @Unique
   private ServerLevel myServerLevel() {
      if (this.myServerLevel != null) {
         return this.myServerLevel;
      } else {
         for(ServerLevel sLevel : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
            if (sLevel.chunkSource.chunkMap.distanceManager.tickingTicketsTracker == this.myTickingTracker) {
               this.myServerLevel = sLevel;
            }
         }

         return this.myServerLevel;
      }
   }
}

