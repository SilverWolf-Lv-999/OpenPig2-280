package kakiku.pig2mod;

import kakiku.pig2mod.entity.MyEntities;
import kakiku.pig2mod.item.MyItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("pig2mod")
public class Pig2Mod {
   public static final String MOD_ID = "pig2mod";
   public static MyHelper gMyHelper = null;
   public static MyAttacker gMyAttacker = null;

   public Pig2Mod() {
      IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
      modEventBus.addListener(this::commonSetup);
      MyItems.register(modEventBus);
      MyEntities.register(modEventBus);
      modEventBus.addListener(this::addCreative);
      gMyHelper = new MyHelper();
      gMyHelper.registerForgeEventBus(this);
      gMyAttacker = new MyAttacker();
   }

   private void commonSetup(FMLCommonSetupEvent event) {
   }

   private void addCreative(BuildCreativeModeTabContentsEvent event) {
      MyItems.addCreative(event);
   }

   @SubscribeEvent
   public void onServerStarting(ServerStartingEvent event) {
   }

   @EventBusSubscriber(
      modid = "pig2mod",
      bus = Bus.MOD,
      value = {Dist.CLIENT}
   )
   public static class ClientModEvents {
      @SubscribeEvent
      public static void onFMLClientSetup(FMLClientSetupEvent event) {
      }
   }
}

