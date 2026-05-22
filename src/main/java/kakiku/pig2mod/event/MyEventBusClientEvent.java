package kakiku.pig2mod.event;

import kakiku.pig2mod.entity.MyEntities;
import kakiku.pig2mod.entity.Pig2Renderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "pig2mod",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class MyEventBusClientEvent {
   @SubscribeEvent
   public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
   }

   @SubscribeEvent
   public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
      event.registerEntityRenderer((EntityType)MyEntities.PIG2.get(), Pig2Renderer::new);
      event.registerEntityRenderer((EntityType)MyEntities.SNOWBALL2.get(), ThrownItemRenderer::new);
   }
}

