package kakiku.pig2mod.mixin;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Either;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyInt2ObjectOpenHashMap;
import kakiku.pig2mod.map.MyLong2ObjectLinkedOpenHashMap;
import kakiku.pig2mod.map.MyLongSet;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ChunkMap.class})
public abstract class MxChunkMap {
   @Unique
   ChunkMap thisChunkMap = (ChunkMap)this;
   @Shadow
   public int viewDistance;
   @Shadow
   public ServerLevel level;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void onInit(ServerLevel pLevel, LevelStorageSource.LevelStorageAccess pLevelStorageAccess, DataFixer pFixerUpper, StructureTemplateManager pStructureManager, Executor pDispatcher, BlockableEventLoop pMainThreadExecutor, LightChunkGetter pLightChunk, ChunkGenerator pGenerator, ChunkProgressListener pProgressListener, ChunkStatusUpdateListener pChunkStatusListener, Supplier pOverworldDataStorage, int pViewDistance, boolean pSync, CallbackInfo ci) {
      this.thisChunkMap.entityMap = new MyInt2ObjectOpenHashMap(this.thisChunkMap.entityMap, ChunkMap.class);
      this.thisChunkMap.updatingChunkMap = new MyLong2ObjectLinkedOpenHashMap(this.thisChunkMap.updatingChunkMap, ChunkMap.class);
      this.thisChunkMap.pendingUnloads = new MyLong2ObjectLinkedOpenHashMap(this.thisChunkMap.pendingUnloads, ChunkMap.class);
      this.thisChunkMap.entitiesInLevel = new MyLongSet(this.thisChunkMap.entitiesInLevel, ChunkMap.class);
   }

   @Inject(
      method = {"removeEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removeEntity(Entity pEntity, CallbackInfo ci) {
      if (pEntity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"scheduleUnload"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void scheduleUnload(long pChunkPos, ChunkHolder pChunkHolder, CallbackInfo ci) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.level)) {
         if (pig2ChunkPos.toLong() == pChunkPos && !Pig2.isOshimai()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"protoChunkToFullChunk"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void protoChunkToFullChunk(ChunkHolder pHolder, CallbackInfoReturnable cir) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.level)) {
         if (pig2ChunkPos.equals(pHolder.getPos()) && !Pig2.isOshimai()) {
            cir.setReturnValue(ChunkHolder.UNLOADED_CHUNK_FUTURE);
            return;
         }
      }

   }

   @Inject(
      method = {"prepareEntityTickingChunk"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void prepareEntityTickingChunk(ChunkHolder pChunk, CallbackInfoReturnable cir) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.level)) {
         if (pig2ChunkPos.equals(pChunk.getPos()) && !Pig2.isOshimai()) {
            cir.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"updateChunkScheduling"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void updateChunkScheduling(long pChunkPos, int pNewLevel, ChunkHolder pHolder, int pOldLevel, CallbackInfoReturnable cir) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.level)) {
         if (pig2ChunkPos.toLong() == pChunkPos && !Pig2.isOshimai()) {
            cir.setReturnValue(pHolder);
            return;
         }
      }

   }

   @Inject(
      method = {"broadcast"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void broadcast(Entity pEntity, Packet pPacket, CallbackInfo ci) {
      if (pEntity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"broadcastAndSend"},
      at = {@At("HEAD")},
      cancellable = true
   )
   protected void broadcastAndSend(Entity pEntity, Packet pPacket, CallbackInfo ci) {
      if (pEntity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"setViewDistance"},
      at = {@At("HEAD")},
      cancellable = true
   )
   protected void setViewDistance(int pViewDistance, CallbackInfo ci) {
      if (this.viewDistance > pViewDistance && Pig2.anyPig2sAlive() && !Pig2.isOshimai()) {
         ci.cancel();
      }
   }
}

