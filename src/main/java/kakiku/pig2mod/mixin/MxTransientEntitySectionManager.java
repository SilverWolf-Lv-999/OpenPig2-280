package kakiku.pig2mod.mixin;

import it.unimi.dsi.fastutil.longs.LongSet;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyLongSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.entity.LevelCallback;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TransientEntitySectionManager.class})
public abstract class MxTransientEntitySectionManager {
   @Unique
   TransientEntitySectionManager thisTESManager = (TransientEntitySectionManager)this;
   @Shadow
   public LongSet tickingChunks;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void onInit(Class pClazz, LevelCallback pCallbacks, CallbackInfo ci) {
      this.tickingChunks = new MyLongSet(this.tickingChunks, TransientEntitySectionManager.class);
   }

   @Inject(
      method = {"stopTicking"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void stopTicking(ChunkPos pPos, CallbackInfo ci) {
      for(ChunkPos pig2ChunkPos : Pig2.getAliveServerPigChunkPoss(this.myClientLevel())) {
         if (pig2ChunkPos.equals(pPos) && !Pig2.isOshimai()) {
            ci.cancel();
            return;
         }
      }

   }

   @Unique
   private ClientLevel myClientLevel() {
      return Minecraft.getInstance().level;
   }
}

