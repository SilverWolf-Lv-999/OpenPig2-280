package kakiku.pig2mod.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.List;
import java.util.Objects;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyLong2ObjectOpenHashMap;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.Visibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntitySectionStorage.class})
public abstract class MxEntitySectionStorage {
   @Unique
   EntitySectionStorage myEntitySectionStorage = (EntitySectionStorage)this;
   @Shadow
   public Long2ObjectMap sections;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void onInit(Class pEntityClass, Long2ObjectFunction pInitialSectionVisibility, CallbackInfo ci) {
      this.sections = new MyLong2ObjectOpenHashMap(this.sections, EntitySectionStorage.class);
   }

   @Inject(
      method = {"getOrCreateSection"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getOrCreateSection(long pSectionPos, CallbackInfoReturnable cir) {
      Long2ObjectMap var5 = this.sections;
      if (var5 instanceof MyLong2ObjectOpenHashMap mySections) {
         EntitySectionStorage var10003 = this.myEntitySectionStorage;
         Objects.requireNonNull(var10003);
         cir.setReturnValue((EntitySection)mySections.myComputeIfAbsent(pSectionPos, var10003::m_156901_));
      }
   }

   @Inject(
      method = {"remove"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void remove(long pSectionId, CallbackInfo ci) {
      EntitySection<T> section = (EntitySection)this.sections.get(pSectionId);
      if (section != null && !section.isEmpty()) {
         List<T> pig2List = (List)section.storage.byClass.get(Pig2.class);
         if (pig2List != null && !pig2List.isEmpty() && !Pig2.isOshimai()) {
            ci.cancel();
            return;
         }
      }

   }
}

