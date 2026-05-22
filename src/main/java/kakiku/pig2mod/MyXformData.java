package kakiku.pig2mod;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import kakiku.pig2mod.xform.MyLib2;
import kakiku.pig2mod.xform.MyScheduledExecutorService;
import kakiku.pig2mod.xform.MyScheduledFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class MyXformData {
   public static boolean gIsMFlashfur = false;

   public static void updateTitle_Minecraft() {
      String myTitle = "Minecraft* Forge 1.20.1_";
      Minecraft.instance.window.setTitle(myTitle);
   }

   public static void doNothing() {
   }

   public static boolean justReturnFalse() {
      return false;
   }

   public static Level level_Entity(Entity entity) {
      return MyHelper.getLevel1();
   }

   public static ServerLevel serverLevel_ServerPlayer(ServerPlayer serverPlayer) {
      return (ServerLevel)MyHelper.getLevel1();
   }

   public static Level levelArg(Level originalLevel) {
      return MyHelper.getLevel1();
   }

   public static Process start() throws IOException {
      return null;
   }

   public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
      return new MyScheduledExecutorService();
   }

   public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
      return new MyScheduledExecutorService();
   }

   public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
      return new MyScheduledExecutorService();
   }

   public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
      return new MyScheduledExecutorService();
   }

   public static ExecutorService newFixedThreadPool(int nThreads) {
      return new MyScheduledExecutorService();
   }

   public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
      return new MyScheduledExecutorService();
   }

   public static ScheduledFuture scheduleAtFixedRate(ScheduledExecutorService ignored, Runnable command, long initialDelay, long period, TimeUnit unit) {
      return new MyScheduledFuture();
   }

   public static boolean shouldRender(Entity enemy) {
      if (Minecraft.instance.level == null) {
         return false;
      } else {
         boolean isMFlashfur = enemy.getClass().getSimpleName().equals("MetapotentFlashfurEntity");
         if (!isMFlashfur) {
            return true;
         } else {
            HashSet<Entity> entities = Pig2Mod.gMyHelper.getEntitiesOfThisClassClient(Minecraft.instance.level, enemy.getClass());
            if (entities.isEmpty()) {
               gIsMFlashfur = false;

               try {
                  Object var9 = Class.forName("flashfur.omnimobs.entities.metapotent_flashfur.MetapotentFlashfurLevel").getField("metapotentFlashfurList").get((Object)null);
                  if (var9 instanceof List) {
                     List<?> list = (List)var9;
                     MethodHandles.Lookup lookup = MyLib2.getLookup();
                     MethodHandle mh = lookup.findSpecial(ArrayList.class, "clear", MethodType.methodType(Void.TYPE), list.getClass());
                     mh.invoke(list);
                  }
               } catch (Throwable var6) {
               }

               return false;
            } else if (entities.contains(enemy)) {
               gIsMFlashfur = true;
               return true;
            } else {
               try {
                  Object ura = enemy.getClass().getField("metapotentFlashfur").get(enemy);
                  Object mh = ura.getClass().getMethod("getMetapotentFlashfurProxy").invoke(ura);
                  if (mh instanceof Entity) {
                     Entity omote = (Entity)mh;
                     if (entities.contains(omote)) {
                        return true;
                     }
                  }
               } catch (Throwable var7) {
               }

               return false;
            }
         }
      }
   }

   public static InteractionResult useOn(Item item, UseOnContext pContext) {
      String nameToSpawn = "notch";
      if (item.toString().contains("popbob")) {
         nameToSpawn = "popbob";
      }

      EntityType<?> entitytype = (EntityType)ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("madness", nameToSpawn));
      if (entitytype == null) {
         return InteractionResult.SUCCESS;
      } else {
         Level level = pContext.getLevel();
         if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
         } else {
            ItemStack itemstack = pContext.getItemInHand();
            BlockPos blockpos = pContext.getClickedPos();
            Direction direction = pContext.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            BlockPos blockpos1;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
               blockpos1 = blockpos;
            } else {
               blockpos1 = blockpos.relative(direction);
            }

            Entity entity = entitytype.spawn((ServerLevel)level, itemstack, pContext.getPlayer(), blockpos1, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (entity != null) {
               if (item.toString().contains("herobrine")) {
                  entity.setCustomNameVisible(false);
               } else {
                  entity.setCustomNameVisible(true);
               }

               itemstack.shrink(1);
               level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
            }

            return InteractionResult.CONSUME;
         }
      }
   }

   public static void disconnect(ServerGamePacketListenerImpl serverGamePacketListener, Component pTextComponent) {
   }
}

