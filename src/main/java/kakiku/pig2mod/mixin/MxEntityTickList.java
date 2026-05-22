package kakiku.pig2mod.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyInt2ObjectLinkedOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityTickList.class})
public abstract class MxEntityTickList {
   @Shadow
   public Int2ObjectMap active;
   @Shadow
   public Int2ObjectMap passive;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void onInit(CallbackInfo ci) {
      this.active = new MyInt2ObjectLinkedOpenHashMap(this.active, EntityTickList.class);
      this.passive = new MyInt2ObjectLinkedOpenHashMap(this.passive, EntityTickList.class);
   }

   @Inject(
      method = {"remove"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void remove(Entity pEntity, CallbackInfo ci) {
      if (pEntity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && Pig2.getAliveServerPigIDs(pig2.level).contains(pig2.id)) {
            ci.cancel();
            return;
         }
      }

   }
}

