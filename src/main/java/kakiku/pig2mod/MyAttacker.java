package kakiku.pig2mod;

import java.util.List;
import javax.annotation.Nullable;
import kakiku.pig2mod.entity.Pig2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "pig2mod",
   bus = Bus.MOD
)
public class MyAttacker {
   private static boolean mHasJustStartedServerTest = false;
   private static boolean mShiftKeyPressed = false;
   private static long mTestStartTime = 0L;

   public MyAttacker() {
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void onInputKey(InputEvent.Key event) {
   }

   @SubscribeEvent
   public void onServerTick(TickEvent.ServerTickEvent event) {
   }

   @Nullable
   private Pig2 findPig2() {
      Pig2 pig2 = null;
      Player player = MyLib.getPlayer();
      if (player == null) {
         return null;
      } else {
         Level level = player.level();
         if (level == null) {
            return null;
         } else {
            AABB levelBounds = new AABB(level.getWorldBorder().getMinX(), (double)level.getMinBuildHeight(), level.getWorldBorder().getMinZ(), level.getWorldBorder().getMaxX(), (double)level.getMaxBuildHeight(), level.getWorldBorder().getMaxZ());
            List<Entity> entities = level.getEntities((Entity)null, levelBounds, (entity) -> entity instanceof Pig2);
            if (entities.size() > 0) {
               pig2 = (Pig2)entities.get(0);
            }

            return pig2;
         }
      }
   }
}

