package kakiku.pig2mod.mixin;

import java.util.List;
import java.util.function.Supplier;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyArrayList;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientLevel.class})
public abstract class MxClientLevel {
   @Unique
   ClientLevel thisClientLevel = (ClientLevel)this;
   @Unique
   private String thisPackeageName = this.getClass().getPackageName();
   @Shadow
   public List players;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void onInit(ClientPacketListener pConnection, ClientLevel.ClientLevelData pClientLevelData, ResourceKey pDimension, Holder pDimensionType, int pViewDistance, int pServerSimulationDistance, Supplier pProfiler, LevelRenderer pLevelRenderer, boolean pIsDebug, long pBiomeZoomSeed, CallbackInfo ci) {
      this.players = new MyArrayList(this.players, ClientLevel.class);
   }

   @Inject(
      method = {"removeEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removeEntity(int pEntityId, Entity.RemovalReason pReason, CallbackInfo ci) {
      if (Pig2.getAliveServerPigIDs(this.thisClientLevel).contains(pEntityId) && !Pig2.isOshimai() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.client.multiplayer.ClientLevel.m_104739_")) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"unload"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void unload(LevelChunk pChunk, CallbackInfo ci) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.thisClientLevel)) {
         if (pig2ChunkPos.equals(pChunk.getPos()) && !Pig2.isOshimai()) {
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
      index = 2
   )
   public Entity addEntity_arg2(Entity pEntityToSpawn) {
      if (pEntityToSpawn instanceof WitherSkull pWitherSkull) {
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

      return pEntityToSpawn;
   }
}

