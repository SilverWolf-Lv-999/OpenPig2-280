package kakiku.pig2mod.entity;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MyEntities {
   public static final DeferredRegister ENTITY_TYPES;
   public static final RegistryObject PIG2;
   public static final RegistryObject SNOWBALL2;

   public static void register(IEventBus eventBus) {
      ENTITY_TYPES.register(eventBus);
   }

   static {
      ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "pig2mod");
      PIG2 = ENTITY_TYPES.register("pig2", () -> Builder.of(Pig2::new, MobCategory.CREATURE).build("pig2"));
      SNOWBALL2 = ENTITY_TYPES.register("snowball2", () -> Builder.of(Snowball2::new, MobCategory.MISC).build("snowball2"));
   }
}

