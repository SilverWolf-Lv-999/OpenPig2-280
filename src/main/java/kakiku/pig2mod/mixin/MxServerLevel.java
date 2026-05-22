package kakiku.pig2mod.mixin;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyArrayList;
import kakiku.pig2mod.map.MySet;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ServerLevel.class})
public abstract class MxServerLevel extends Level {
   @Unique
   public ServerLevel thisServerLevel = (ServerLevel)this;
   @Shadow
   public boolean handlingTick;
   @Shadow
   public SleepStatus sleepStatus;
   @Shadow
   public List players;
   @Shadow
   public LevelTicks blockTicks;
   @Shadow
   public LevelTicks fluidTicks;
   @Shadow
   public int emptyTime;
   @Shadow
   public EndDragonFight dragonFight;
   @Shadow
   public EntityTickList entityTickList;
   @Shadow
   public ServerChunkCache chunkSource;
   @Shadow
   public PersistentEntitySectionManager entityManager;
   @Shadow
   public Set navigatingMobs;

   @Shadow
   protected abstract void advanceWeatherCycle();

   @Shadow
   protected abstract void wakeUpAllPlayers();

   @Shadow
   protected abstract void resetWeatherCycle();

   @Shadow
   protected abstract void tickTime();

   @Shadow
   protected abstract void tickBlock(BlockPos var1, Block var2);

   @Shadow
   protected abstract void tickFluid(BlockPos var1, Fluid var2);

   @Shadow
   protected abstract void runBlockEvents();

   @Shadow
   protected abstract boolean shouldDiscardEntity(Entity var1);

   protected MxServerLevel(WritableLevelData pLevelData, ResourceKey pDimension, RegistryAccess pRegistryAccess, Holder pDimensionTypeRegistration, Supplier pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates) {
      super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
   }

   @Unique
   public void tickBlockEntities2() {
      super.tickBlockEntities();
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void onInit(MinecraftServer pServer, Executor pDispatcher, LevelStorageSource.LevelStorageAccess pLevelStorageAccess, ServerLevelData pServerLevelData, ResourceKey pDimension, LevelStem pLevelStem, ChunkProgressListener pProgressListener, boolean pIsDebug, long pBiomeZoomSeed, List pCustomSpawners, boolean pTickTime, @Nullable RandomSequences pRandomSequences, CallbackInfo ci) {
      this.players = new MyArrayList(this.players, ServerLevel.class);
      this.navigatingMobs = new MySet(this.navigatingMobs, ServerLevel.class);
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void tick(BooleanSupplier pHasTimeLeft, CallbackInfo ci) {
      ProfilerFiller profilerfiller = this.thisServerLevel.getProfiler();
      this.handlingTick = true;
      profilerfiller.push("world border");
      this.thisServerLevel.getWorldBorder().tick();
      profilerfiller.popPush("weather");
      this.advanceWeatherCycle();
      int i = this.thisServerLevel.getGameRules().getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE);
      if (this.sleepStatus.areEnoughSleeping(i) && this.sleepStatus.areEnoughDeepSleeping(i, this.players)) {
         if (this.thisServerLevel.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
            long j = this.thisServerLevel.getDayTime() + 24000L;
            this.thisServerLevel.setDayTime(ForgeEventFactory.onSleepFinished(this.thisServerLevel, j - j % 24000L, this.thisServerLevel.getDayTime()));
         }

         this.wakeUpAllPlayers();
         if (this.thisServerLevel.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE) && this.thisServerLevel.isRaining()) {
            this.resetWeatherCycle();
         }
      }

      this.thisServerLevel.updateSkyBrightness();
      this.tickTime();
      profilerfiller.popPush("tickPending");
      if (!this.thisServerLevel.isDebug()) {
         long k = this.thisServerLevel.getGameTime();
         profilerfiller.push("blockTicks");
         this.blockTicks.tick(k, 65536, this::m_184112_);
         profilerfiller.popPush("fluidTicks");
         this.fluidTicks.tick(k, 65536, this::m_184076_);
         profilerfiller.pop();
      }

      profilerfiller.popPush("raid");
      this.thisServerLevel.raids.tick();
      profilerfiller.popPush("chunkSource");
      this.thisServerLevel.getChunkSource().tick(pHasTimeLeft, true);
      profilerfiller.popPush("blockEvents");
      this.runBlockEvents();
      this.handlingTick = false;
      profilerfiller.pop();
      boolean flag = !this.players.isEmpty() || ForgeChunkManager.hasForcedChunks(this.thisServerLevel);
      if (flag) {
         this.thisServerLevel.resetEmptyTime();
      }

      if (flag || this.f_8551_++ < 300) {
         profilerfiller.push("entities");
         if (this.dragonFight != null) {
            profilerfiller.push("dragonFight");
            this.dragonFight.tick();
            profilerfiller.pop();
         }

         this.entityTickList.forEach((p_184065_) -> {
            if (p_184065_ != null && !p_184065_.isRemoved()) {
               if (this.shouldDiscardEntity(p_184065_)) {
                  p_184065_.discard();
               } else {
                  profilerfiller.push("checkDespawn");
                  p_184065_.checkDespawn();
                  profilerfiller.pop();
                  if (this.chunkSource.chunkMap.getDistanceManager().inEntityTickingRange(p_184065_.chunkPosition().toLong())) {
                     Entity entity = p_184065_.getVehicle();
                     if (entity != null) {
                        if (!entity.isRemoved() && entity.hasPassenger(p_184065_)) {
                           ci.cancel();
                           return;
                        }

                        p_184065_.stopRiding();
                     }

                     profilerfiller.push("tick");
                     if (p_184065_ != null && !p_184065_.isRemoved() && !(p_184065_ instanceof PartEntity)) {
                        ServerLevel var10000 = this.thisServerLevel;
                        ServerLevel var10001 = this.thisServerLevel;
                        Objects.requireNonNull(var10001);
                        var10000.guardEntityTick(var10001::m_8647_, p_184065_);
                     }

                     profilerfiller.pop();
                  }
               }
            }

         });
         profilerfiller.pop();
         this.tickBlockEntities2();
      }

      profilerfiller.push("entityManagement");
      this.entityManager.tick();
      profilerfiller.pop();
      ci.cancel();
   }

   @Inject(
      method = {"setChunkForced"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setChunkForced(int pChunkX, int pChunkZ, boolean pAdd, CallbackInfoReturnable cir) {
      if (!pAdd) {
         for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.thisServerLevel)) {
            if (pig2ChunkPos.equals(new ChunkPos(pChunkX, pChunkZ)) && !Pig2.isOshimai()) {
               cir.setReturnValue(false);
               return;
            }
         }
      }

   }

   @Inject(
      method = {"broadcastEntityEvent"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void broadcastEntityEvent(Entity pEntity, byte pState, CallbackInfo ci) {
      if (pEntity instanceof Pig2 pig2) {
         if (pState == 3 && !Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      } else if (pEntity instanceof Player player) {
         if (pState == 3 && MyLib2.isCalledFromOtherModWithin1ExceptMixin()) {
            ci.cancel();
            return;
         }
      }

   }

   @Redirect(
      method = {"tickNonPassenger"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/entity/Entity;tick()V"
)
   )
   public void wrapCall_Entity_tick(Entity pEntity) {
      try {
         pEntity.tick();
      } catch (Throwable var5) {
         try {
            pEntity.discard();
         } catch (Throwable var4) {
         }
      }

   }

   @ModifyVariable(
      method = {"addEntity"},
      at = @At("HEAD"),
      argsOnly = true,
      index = 1
   )
   public Entity addEntity_arg1(Entity pEntity) {
      if (pEntity.isRemoved()) {
         Pig2.seeFighting();
      }

      if (pEntity instanceof WitherSkull pWitherSkull) {
         if (pWitherSkull.getClass() != WitherSkull.class) {
            WitherSkull witherSkull = new WitherSkull(pWitherSkull.level, (LivingEntity)pWitherSkull.getOwner(), (double)0.0F, (double)0.0F, (double)0.0F);
            witherSkull.xPower = pWitherSkull.xPower;
            witherSkull.yPower = pWitherSkull.yPower;
            witherSkull.zPower = pWitherSkull.zPower;
            witherSkull.setOwner(pWitherSkull.getOwner());
            if (pWitherSkull.isDangerous()) {
               witherSkull.setDangerous(true);
            }

            witherSkull.setPosRaw(pWitherSkull.getX(), pWitherSkull.getY(), pWitherSkull.getZ());
            return witherSkull;
         }
      }

      return pEntity;
   }
}

