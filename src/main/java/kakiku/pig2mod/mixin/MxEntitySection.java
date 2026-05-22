package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.Visibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntitySection.class})
public abstract class MxEntitySection {
   @Unique
   private String thisPackeageName = this.getClass().getPackageName();
   @Shadow
   public ClassInstanceMultiMap storage;
   @Shadow
   public Visibility chunkStatus;

   @Inject(
      method = {"remove"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void remove(EntityAccess pEntity, CallbackInfoReturnable cir) {
      if (pEntity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && Pig2.getAliveServerPigIDs(pig2.level).contains(pig2.id) && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.world.level.entity.PersistentEntitySectionManager.Callback.m_142044_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.world.level.entity.TransientEntitySectionManager.Callback.m_142044_")) {
            cir.setReturnValue(true);
            return;
         }
      }

   }

   @Inject(
      method = {"updateChunkStatus"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void updateChunkStatus(Visibility pChunkStatus, CallbackInfoReturnable cir) {
      if (pChunkStatus == Visibility.HIDDEN || pChunkStatus == Visibility.TRACKED && this.chunkStatus == Visibility.TICKING) {
         for(EntityAccess entityAccess : this.storage.getAllInstances()) {
            if (entityAccess instanceof Pig2) {
               Pig2 pig2 = (Pig2)entityAccess;
               if (!Pig2.isOshimai() && !pig2.isEnding() && Pig2.getAliveServerPigIDs(pig2.level).contains(pig2.id) && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket.m_5797_")) {
                  cir.setReturnValue(this.chunkStatus);
                  return;
               }
            }
         }
      }

   }
}

