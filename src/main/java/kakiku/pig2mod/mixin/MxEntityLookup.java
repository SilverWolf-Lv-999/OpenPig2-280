package kakiku.pig2mod.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Map;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyHashMap;
import kakiku.pig2mod.map.MyInt2ObjectLinkedOpenHashMap;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityLookup.class})
public abstract class MxEntityLookup {
   @Shadow
   public Int2ObjectMap byId;
   @Shadow
   public Map byUuid;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void onInit(CallbackInfo ci) {
      this.byId = new MyInt2ObjectLinkedOpenHashMap(this.byId, EntityLookup.class);
      this.byUuid = new MyHashMap(this.byUuid, EntityLookup.class);
   }

   @Inject(
      method = {"remove"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void remove(EntityAccess pEntity, CallbackInfo ci) {
      if (pEntity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && Pig2.getAliveServerPigIDs(pig2.level).contains(pig2.id)) {
            ci.cancel();
            return;
         }
      }

   }
}

