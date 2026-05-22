package kakiku.pig2mod.item;

import kakiku.pig2mod.entity.MyEntities;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MyItems {
   public static final DeferredRegister ITEMS;
   public static final RegistryObject PIG2_SPAWN_EGG;

   public static void register(IEventBus eventBus) {
      ITEMS.register(eventBus);
   }

   public static void addCreative(BuildCreativeModeTabContentsEvent event) {
      if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
         event.accept(PIG2_SPAWN_EGG);
      }

   }

   static {
      ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "pig2mod");
      PIG2_SPAWN_EGG = ITEMS.register("pig2_spawn_egg", () -> new ForgeSpawnEggItem(MyEntities.PIG2, 15771042, 14377823, new Item.Properties()));
   }
}

