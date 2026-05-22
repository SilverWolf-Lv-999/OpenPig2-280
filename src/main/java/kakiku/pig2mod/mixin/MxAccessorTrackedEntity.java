package kakiku.pig2mod.mixin;

import java.util.Set;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
   targets = {"net.minecraft.server.level.ChunkMap.TrackedEntity"}
)
public interface MxAccessorTrackedEntity {
   @Accessor("seenBy")
   Set getSeenBy();

   @Accessor("seenBy")
   void setSeenBy(Set var1);

   @Accessor("entity")
   Entity getEntity();
}

