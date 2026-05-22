package kakiku.pig2mod.event;

import kakiku.pig2mod.entity.MyEntities;
import kakiku.pig2mod.entity.Pig2;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "pig2mod",
   bus = Bus.MOD
)
public class MyEventBusEvent {
   @SubscribeEvent
   public static void registerAttributes(EntityAttributeCreationEvent event) {
      event.put((EntityType)MyEntities.PIG2.get(), Pig2.createAttributes().build());
   }
}

