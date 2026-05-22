package kakiku.pig2mod.mixin;

import java.util.List;
import java.util.Map;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyArrayList;
import kakiku.pig2mod.map.MyHashMap;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.level.entity.EntitySection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClassInstanceMultiMap.class})
public abstract class MxClassInstanceMultiMap {
   @Unique
   private String thisPackeageName = this.getClass().getPackageName();
   @Unique
   private String safePackeageName1 = EntitySection.class.getPackageName();
   @Shadow
   public Map byClass;
   @Shadow
   public List allInstances;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void onInit(Class pBaseClass, CallbackInfo ci) {
      this.allInstances = new MyArrayList(this.allInstances, ClassInstanceMultiMap.class);
      this.byClass.put(pBaseClass, this.allInstances);
      this.byClass = new MyHashMap(this.byClass, ClassInstanceMultiMap.class);
   }

   @Inject(
      method = {"remove"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void remove(Object pKey, CallbackInfoReturnable cir) {
      if (pKey instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && Pig2.getAliveServerPigIDs(pig2.level).contains(pig2.id) && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.world.level.entity.PersistentEntitySectionManager.Callback.m_142044_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.world.level.entity.TransientEntitySectionManager.Callback.m_142044_")) {
            cir.setReturnValue(true);
            return;
         }
      }

   }
}

