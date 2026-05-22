package kakiku.pig2mod;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyCheck;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.EntityPersistentStorage;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.LevelCallback;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "pig2mod",
   bus = Bus.MOD
)
public class MyHelper {
   private static IEventBus gForgeEventBusBackup;
   private EntityRenderer gMyHelperRenderer = null;
   private Map gRenderersOriginal = null;
   private Set unregisteredObjects = Collections.synchronizedSet(new HashSet());
   private int mCorrectServerLevelEntityManagerCounter = 0;
   private static LevelCallback gEntityManagerCallbacks;
   private static EntityPersistentStorage gEntityManagerPermanentStorage;
   private int mFukkatsuCounter = 0;
   private boolean mIsWaitingFukkatsu = false;
   private long mFukkatsuWaitingTime = 0L;
   private int mCheckAndRemoveEnemiesServerSide = 0;
   private final ConcurrentHashMap mDyingEnemiesAndLimit = new ConcurrentHashMap();
   private static final long DYING_ANIMATION_PERMIT_TIME = 4000L;
   private int mCheckAndRemoveEnemiesClientSide = 0;
   private int mCorrectServerLevelEntitiesCounter = 0;
   private long mServerTickTime = 0L;
   private long mServerTickSpan = 0L;
   private Set mAbnormalConditionClientPig2IDs = new HashSet();
   private long mAbnormalConditionClientPig2StartTime = 0L;
   private long mClientTickTime = 0L;
   private long mClientTickSpan = 0L;
   private static int gRenderGuiOverlayEventFailureCounter;
   private static ServerLevel gServerLevel1;
   private static ClientLevel gClientLevel1;
   private static boolean gIsFirstStartScreen;
   private static boolean gbLoggingIn;
   private int mDebugVal1 = this.setDebugVal1();
   private boolean mIsDebugPig2StatusServer = false;
   private static boolean gbIsHomeKeyPressed;

   public MyHelper() {
      MyLib2.keepKillingAfterMod();
      this.registerForgeEventBus(this);
      this.preventForgeEventAttack();
   }

   private void preventForgeEventAttack() {
      Thread thread = new Thread(() -> {
         int msecWait = 100;
         int counter = 0;

         while(true) {
            if (MyLib2.isThisOtherMOD(MinecraftForge.EVENT_BUS)) {
               assert !MyLib2.isThisOtherMOD(gForgeEventBusBackup);

               this.unregisterEventSubscriptionByOtherBadMOD();
               Pig2.needMaxFight();
               ++counter;
            }

            MinecraftForge.EVENT_BUS.register(this);
         }
      });
      thread.setPriority(10);
      thread.start();
   }

   public void registerForgeEventBus(Object pObject) {
      try {
         MinecraftForge.EVENT_BUS.register(pObject);
      } catch (Exception var3) {
      }

   }

   public void unregisterForgeEventBus(Object pObject) {
      MinecraftForge.EVENT_BUS.unregister(pObject);
      this.unregisteredObjects.add(pObject);
   }

   public boolean unregisterEventSubscriptionByOtherBadMOD() {
      ConcurrentHashMap<Object, List<IEventListener>> listeners = null;

      try {
         Field field = EventBus.class.getDeclaredField("listeners");
         field.setAccessible(true);
         listeners = (ConcurrentHashMap)field.get(MinecraftForge.EVENT_BUS);

         for(Object key : listeners.keySet()) {
            if (MyLib2.isThisOtherBadMOD(key)) {
               this.unregisterForgeEventBus(key);
            }
         }

         return true;
      } catch (Exception var5) {
         return false;
      }
   }

   public void resetEventSubscriptionByOtherMOD() {
      if (!this.unregisteredObjects.isEmpty()) {
         for(Object object : this.unregisteredObjects) {
            if (object instanceof Entity) {
               Entity entity = (Entity)object;
               if (entity instanceof LivingEntity) {
                  LivingEntity livingEntity = (LivingEntity)entity;
                  if (livingEntity.isDeadOrDying() || !livingEntity.isAlive()) {
                     continue;
                  }
               }
            }

            this.registerForgeEventBus(object);
         }

      }
   }

   private void correctServerLevelEntityManager() {
      if (this.mCorrectServerLevelEntityManagerCounter++ % 5 == 0) {
         if (MyLib2.isServerThread()) {
            Player var2 = MyLib.getPlayer();
            if (var2 instanceof ServerPlayer) {
               ServerPlayer player = (ServerPlayer)var2;
               Level var3 = player.level();
               if (var3 instanceof ServerLevel) {
                  ServerLevel level = (ServerLevel)var3;
                  if (MyLib2.isThisOtherMOD(level.entityManager.callbacks) && gEntityManagerCallbacks != null) {
                     level.entityManager.callbacks = gEntityManagerCallbacks;
                     Pig2.needMaxFight();
                  } else {
                     gEntityManagerCallbacks = level.entityManager.callbacks;
                  }

                  if (MyLib2.isThisOtherMOD(level.entityManager.permanentStorage) && gEntityManagerPermanentStorage != null) {
                     level.entityManager.permanentStorage = gEntityManagerPermanentStorage;
                     Pig2.needMaxFight();
                  } else {
                     gEntityManagerPermanentStorage = level.entityManager.permanentStorage;
                  }
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void onEntityJoinWorld(EntityJoinLevelEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof Pig2 pig2) {
         if (MyLib2.isClientThread()) {
            pig2.checkAndAvoidClientGhost();
            if (pig2.isEnding()) {
               event.setCanceled(true);
            }
         }
      } else if (MyLib2.isThisOtherMOD(entity)) {
         if (Pig2.isKilledEnemyType(entity.getType())) {
            if (MyLib.isCalledFromSpawnByServerPlayer()) {
               Pig2.permitEntity(entity);
               return;
            }

            event.setCanceled(true);
            this.unregisterEventSubscriptionByOtherBadMOD();
            Pig2.seeFighting();
         } else {
            if (MyLib2.isServerThread() && MyLib.isCalledFromSpawnByServerPlayer()) {
               Pig2.permitEntity(entity);
               return;
            }

            if (MyLib2.isClientThread() && MyLib2.isCalledFromTheClass("ClientboundAddEntityPacket") || MyLib2.isCalledFromTheClass("ClientboundCustomPayloadPacket")) {
               if (!(Minecraft.instance.screen instanceof ReceivingLevelScreen)) {
                  Pig2.permitEntity(entity);
               }

               return;
            }

            double radius = (double)100.0F;
            AABB area = new AABB(entity.getX() - radius, entity.getY() - radius, entity.getZ() - radius, entity.getX() + radius, entity.getY() + radius, entity.getZ() + radius);
            int howmany = entity.level().getEntitiesOfClass(entity.getClass(), area).size();
            if (howmany > 10) {
               event.setCanceled(true);
               Pig2.addKilledEnemy(entity);
               Pig2.seeFighting();
            }
         }
      }

   }

   private void fukkatsu() {
      if (this.mFukkatsuCounter++ % 2 == 0) {
         if (MyLib2.isServerThread()) {
            boolean foundAbnormalCondition = false;
            Level var3 = MyLib.getLevel();
            if (var3 instanceof ServerLevel) {
               ServerLevel level = (ServerLevel)var3;
               Set<Pig2> fukkatsuPig2s = new HashSet();

               for(Pig2 pig2 : Pig2.getAliveServerPigsFromMyHelper(level)) {
                  if (!(level.getEntity(pig2.id) instanceof Pig2)) {
                     foundAbnormalCondition = true;
                     fukkatsuPig2s.add(pig2);
                  }

                  if (pig2.jisatsuzumi()) {
                     foundAbnormalCondition = true;
                     fukkatsuPig2s.add(pig2);
                  }

                  if (foundAbnormalCondition && !this.mIsWaitingFukkatsu) {
                     this.mIsWaitingFukkatsu = true;
                     this.mFukkatsuWaitingTime = System.currentTimeMillis();
                  }
               }

               if (this.mIsWaitingFukkatsu && System.currentTimeMillis() - this.mFukkatsuWaitingTime > 200L) {
                  this.mIsWaitingFukkatsu = false;

                  for(Pig2 fukkatsuPig2 : fukkatsuPig2s) {
                     fukkatsuPig2.onRemovedFromWorld();
                  }
               }
            }

            if (!foundAbnormalCondition) {
               this.mIsWaitingFukkatsu = false;
            }

         }
      }
   }

   public void removeAllServerSide() {
      if (MyLib2.isServerThread()) {
         this.unregisterEventSubscriptionByOtherBadMOD();
         Player var3 = MyLib.getPlayer();
         if (var3 instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)var3;
            Level var10 = player.level();
            if (var10 instanceof ServerLevel) {
               ServerLevel levelNow = (ServerLevel)var10;

               for(ServerLevel level : levelNow.getServer().getAllLevels()) {
                  Set<Entity> enemies = this.getEnemiesServer(level);
                  this.removeTheEnemiesInTheServerLevel(enemies, level);
                  Set<Entity> enemies2 = this.getEnemiesServer(level);
                  if (!enemies2.isEmpty()) {
                     for(Entity var9 : enemies2) {
                        ;
                     }
                  }
               }
            }
         }

      }
   }

   public void removeAllClientSide() {
      if (MyLib2.isClientThread()) {
         Minecraft minecraft = Minecraft.instance;
         ClientLevel level = minecraft.level;
         if (level != null) {
            HashSet<Entity> enemies = this.getEnemiesClient(level);
            this.unregisterEventSubscriptionByOtherBadMOD();
            this.removeTheEnemiesInTheClientLevel(enemies, level);
            this.replaceRendererOfOtherMOD();
         }
      }
   }

   private void checkAndRemoveEnemiesServerSide() {
      if (this.mCheckAndRemoveEnemiesServerSide++ % 5 == 0) {
         if (Pig2.isMaxFighting()) {
            this.removeAllServerSide();
         } else {
            Player player = MyLib.getPlayer();
            if (player == null) {
               return;
            }

            if (!(player.level() instanceof ServerLevel)) {
               return;
            }

            ServerLevel level = (ServerLevel)player.level();
            Set<Entity> enemies = this.getEnemiesServer(level);
            Iterator<Entity> iterator = enemies.iterator();

            while(iterator.hasNext()) {
               Entity enemy = (Entity)iterator.next();
               boolean bKillThisEnemy = true;
               if (Pig2.isKilledEnemyUUID(enemy.uuid)) {
                  if (enemy instanceof LivingEntity) {
                     LivingEntity livingEntity = (LivingEntity)enemy;
                     if (livingEntity.isDeadOrDying()) {
                        if (this.mDyingEnemiesAndLimit.containsKey(livingEntity)) {
                           if ((Long)this.mDyingEnemiesAndLimit.get(livingEntity) >= System.currentTimeMillis()) {
                              bKillThisEnemy = false;
                           }
                        } else {
                           bKillThisEnemy = false;
                           this.mDyingEnemiesAndLimit.put(livingEntity, System.currentTimeMillis() + 4000L);
                        }
                     }
                  }
               } else {
                  bKillThisEnemy = false;
               }

               if (!bKillThisEnemy) {
                  iterator.remove();
               }
            }

            if (!enemies.isEmpty()) {
               this.removeTheEnemiesInTheServerLevel(enemies, level);
            }
         }

      }
   }

   private void checkAndRemoveEnemiesClientSide() {
      if (this.mCheckAndRemoveEnemiesClientSide++ % 10 == 0) {
         Minecraft minecraft = Minecraft.instance;
         ClientLevel level = minecraft.level;
         if (level != null) {
            HashSet<Entity> enemies = this.getEnemiesClient(level);
            if (!Pig2.isMaxFighting()) {
               enemies.removeIf((entity) -> {
                  boolean var10000;
                  if (Pig2.isKilledEnemyUUID(entity.uuid)) {
                     label26: {
                        if (entity instanceof LivingEntity) {
                           LivingEntity livingEntity = (LivingEntity)entity;
                           if (livingEntity.isDeadOrDying()) {
                              break label26;
                           }
                        }

                        var10000 = false;
                        return var10000;
                     }
                  }

                  var10000 = true;
                  return var10000;
               });
               this.removeTheEnemiesInTheClientLevel(enemies, level);
               if (Pig2.hasKilledAllServerEnemies()) {
                  Set<Entity> enemies2 = this.getEnemiesClient(level);
                  if (!enemies2.isEmpty()) {
                     this.removeTheEnemiesInTheClientLevel(enemies2, level);
                     Pig2.needMaxFight();
                  }
               }
            }

            if (Pig2.isMaxFighting()) {
               this.unregisterEventSubscriptionByOtherBadMOD();
               this.removeTheEnemiesInTheClientLevel(enemies, level);
               this.replaceRendererOfOtherMOD();
            }

            if (this.getEnemiesClient(level).isEmpty()) {
               SoundEngine engine = Minecraft.instance.soundManager.soundEngine;

               for(SoundInstance instance : engine.instanceToChannel.keySet()) {
                  if (MyLib2.isThisOtherMOD(instance)) {
                     engine.stop(instance);
                  }
               }
            }

         }
      }
   }

   private HashSet getEnemiesServer(ServerLevel level) {
      HashSet<Entity> enemies = new HashSet();
      ObjectIterator var3 = level.entityManager.sectionStorage.sections.values().iterator();

      while(var3.hasNext()) {
         EntitySection<Entity> entitySection = (EntitySection)var3.next();
         entitySection.storage.getAllInstances().forEach((entityx) -> {
            if (MyLib2.isThisOtherMOD(entityx)) {
               enemies.add(entityx);
            }

         });
      }

      level.entityManager.visibleEntityStorage.getAllEntities().forEach((entityx) -> {
         if (MyLib2.isThisOtherMOD(entityx)) {
            enemies.add(entityx);
         }

      });
      level.entityTickList.active.values().forEach((entityx) -> {
         if (MyLib2.isThisOtherMOD(entityx)) {
            enemies.add(entityx);
         }

      });

      for(Entity entity : ((LevelEntityGetterAdapter)level.entityManager.entityGetter).visibleEntities.getAllEntities()) {
         if (MyLib2.isThisOtherMOD(entity)) {
            enemies.add(entity);
         }
      }

      return enemies;
   }

   private HashSet getEntitiesOfThisClassServer(ServerLevel level, Class entityClass) {
      HashSet<Entity> enemies = new HashSet();
      ObjectIterator var4 = level.entityManager.sectionStorage.sections.values().iterator();

      while(var4.hasNext()) {
         EntitySection<Entity> entitySection = (EntitySection)var4.next();
         entitySection.storage.getAllInstances().forEach((entityx) -> {
            if (entityClass.isInstance(entityx)) {
               enemies.add(entityx);
            }

         });
      }

      level.entityManager.visibleEntityStorage.getAllEntities().forEach((entityx) -> {
         if (entityClass.isInstance(entityx)) {
            enemies.add(entityx);
         }

      });
      level.entityTickList.active.values().forEach((entityx) -> {
         if (entityClass.isInstance(entityx)) {
            enemies.add(entityx);
         }

      });

      for(Entity entity : ((LevelEntityGetterAdapter)level.entityManager.entityGetter).visibleEntities.getAllEntities()) {
         if (entityClass.isInstance(entity)) {
            enemies.add(entity);
         }
      }

      return enemies;
   }

   private HashSet getEnemiesClient(ClientLevel level) {
      HashSet<Entity> enemies = new HashSet();
      ObjectIterator var3 = level.entityStorage.sectionStorage.sections.values().iterator();

      while(var3.hasNext()) {
         EntitySection<Entity> entitySection = (EntitySection)var3.next();
         entitySection.storage.getAllInstances().forEach((entity) -> {
            if (MyLib2.isThisOtherMOD(entity)) {
               enemies.add(entity);
            }

         });
      }

      level.entityStorage.entityStorage.getAllEntities().forEach((entity) -> {
         if (MyLib2.isThisOtherMOD(entity)) {
            enemies.add(entity);
         }

      });
      level.tickingEntities.active.values().forEach((entity) -> {
         if (MyLib2.isThisOtherMOD(entity)) {
            enemies.add(entity);
         }

      });
      return enemies;
   }

   public HashSet getEntitiesOfThisClassClient(ClientLevel level, Class entityClass) {
      HashSet<Entity> enemies = new HashSet();
      ObjectIterator var4 = level.entityStorage.sectionStorage.sections.values().iterator();

      while(var4.hasNext()) {
         EntitySection<Entity> entitySection = (EntitySection)var4.next();
         entitySection.storage.getAllInstances().forEach((entity) -> {
            if (entityClass.isInstance(entity)) {
               enemies.add(entity);
            }

         });
      }

      level.entityStorage.entityStorage.getAllEntities().forEach((entity) -> {
         if (entityClass.isInstance(entity)) {
            enemies.add(entity);
         }

      });
      level.tickingEntities.active.values().forEach((entity) -> {
         if (entityClass.isInstance(entity)) {
            enemies.add(entity);
         }

      });
      return enemies;
   }

   public void removeTheEnemiesInTheServerLevel(Set enemies, ServerLevel level) {
      if (MyLib2.isServerThread()) {
         Pig2.stopNavigationOfAllPig2();
         Iterator<Entity> it = enemies.iterator();

         while(it.hasNext()) {
            Entity enemy = (Entity)it.next();
            if (enemy instanceof Player) {
               it.remove();
            } else {
               Pig2.addKilledEnemy(enemy);
               if (enemy instanceof LivingEntity) {
                  LivingEntity livingEntity = (LivingEntity)enemy;
                  livingEntity.removeAllEffects();
               }

               enemy.setSharedFlag(5, true);
               enemy.entityData.set(Entity.DATA_SILENT, true);
               enemy.invulnerable = false;
               enemy.canUpdate = false;
               enemy.setPosRaw((double)10000.0F, (double)-10000.0F, (double)10000.0F);
               enemy.setRemoved(RemovalReason.KILLED);
               enemy.removalReason = RemovalReason.KILLED;
               this.invokeMethodOfEnemy(enemy);
               level.getChunkSource().removeEntity(enemy);
            }
         }

         ClassInstanceMultiMap<Entity> newStorage;
         EntitySection<Entity> entitySection;
         for(ObjectIterator newVisibleEntities = level.entityManager.sectionStorage.sections.values().iterator(); newVisibleEntities.hasNext(); entitySection.storage = newStorage) {
            entitySection = (EntitySection)newVisibleEntities.next();
            newStorage = new ClassInstanceMultiMap(Entity.class);

            for(Entity entity2 : entitySection.storage.getAllInstances()) {
               if (!enemies.contains(entity2)) {
                  newStorage.add(entity2);
               }
            }
         }

         for(Entity enemy : enemies) {
            level.entityManager.visibleEntityStorage.remove(enemy);
            level.entityManager.knownUuids.remove(enemy.uuid);
            level.entityTickList.active.remove(enemy.id, enemy);
         }

         EntityLookup<Entity> newVisibleEntities = new EntityLookup();

         for(Entity entity2 : ((LevelEntityGetterAdapter)level.entityManager.entityGetter).visibleEntities.getAllEntities()) {
            if (!enemies.contains(entity2)) {
               newVisibleEntities.add(entity2);
            }
         }

         level.entityManager.visibleEntityStorage = newVisibleEntities;
         level.entityManager.entityGetter = new LevelEntityGetterAdapter(level.entityManager.visibleEntityStorage, level.entityManager.sectionStorage);
      }
   }

   public void removeTheEnemiesInTheClientLevel(Set enemies, ClientLevel level) {
      if (MyLib2.isClientThread()) {
         Iterator<Entity> it = enemies.iterator();

         while(it.hasNext()) {
            Entity enemy = (Entity)it.next();
            if (enemy instanceof Player) {
               it.remove();
            } else {
               Pig2.addKilledEnemy(enemy);
               this.unregisterForgeEventBus(enemy);
               this.replaceRendererOfThisOtherMOD(enemy);
               enemy.setSharedFlag(5, true);
               enemy.entityData.set(Entity.DATA_SILENT, true);
               enemy.invulnerable = false;
               enemy.canUpdate = false;
               enemy.setPosRaw((double)10000.0F, (double)-10000.0F, (double)10000.0F);
               enemy.setRemoved(RemovalReason.KILLED);
               enemy.removalReason = RemovalReason.KILLED;
               this.invokeMethodOfEnemy(enemy);
            }
         }

         EntitySection<Entity> entitySection;
         ClassInstanceMultiMap<Entity> newStorage;
         for(ObjectIterator var9 = level.entityStorage.sectionStorage.sections.values().iterator(); var9.hasNext(); entitySection.storage = newStorage) {
            entitySection = (EntitySection)var9.next();
            newStorage = new ClassInstanceMultiMap(Entity.class);

            for(Entity entity2 : entitySection.storage.getAllInstances()) {
               if (!enemies.contains(entity2)) {
                  newStorage.add(entity2);
               }
            }
         }

         for(Entity enemy : enemies) {
            level.entityStorage.entityStorage.remove(enemy);
            level.tickingEntities.active.remove(enemy.id, enemy);
         }

      }
   }

   private void invokeMethodOfEnemy(Entity entity) {
      try {
         Method onLevelUnloadMethod = entity.getClass().getDeclaredMethod("onLevelUnload", LevelEvent.Unload.class);
         onLevelUnloadMethod.setAccessible(true);
         onLevelUnloadMethod.invoke(entity, new LevelEvent.Unload(entity.level));
      } catch (Exception var3) {
      }

   }

   public void correctServerLevelEntities(Set pPig2s) {
      if (this.mCorrectServerLevelEntitiesCounter++ % 5 == 0) {
         this.correctServerLevelEntitiesNow(pPig2s);
      }
   }

   private void correctServerLevelEntitiesNow(Set pPig2s) {
      if (pPig2s != null && !pPig2s.isEmpty()) {
         for(Pig2 pig2 : pPig2s) {
            if (!pig2.isEnding() && pig2.removalReason == null) {
               Level var5 = pig2.level;
               if (var5 instanceof ServerLevel) {
                  ServerLevel level = (ServerLevel)var5;
                  if (this.getEntitiesOfThisClassServer((ServerLevel)pig2.level, Pig2.class).contains(pig2)) {
                     if (level.entityManager.entityGetter.get(pig2.uuid) == null) {
                        EntityLookup<Entity> newVisibleEntities = new EntityLookup();
                        newVisibleEntities.add(pig2);

                        for(Entity entity : ((LevelEntityGetterAdapter)level.entityManager.entityGetter).visibleEntities.getAllEntities()) {
                           newVisibleEntities.add(entity);
                        }

                        level.entityManager.visibleEntityStorage = newVisibleEntities;
                        level.entityManager.entityGetter = new LevelEntityGetterAdapter(level.entityManager.visibleEntityStorage, level.entityManager.sectionStorage);
                     }

                     if (!level.entityManager.knownUuids.contains(pig2.uuid)) {
                        level.entityManager.knownUuids.add(pig2.uuid);
                     }

                     if (!level.entityTickList.contains(pig2)) {
                        EntityTickList entityTickList2 = new EntityTickList();
                        EntityTickList var10000 = level.entityTickList;
                        Objects.requireNonNull(entityTickList2);
                        var10000.forEach(entityTickList2::m_156908_);
                        entityTickList2.add(pig2);
                        level.entityTickList = entityTickList2;
                     }
                  }
               }
            }
         }

      }
   }

   public void replaceRendererOfOtherMOD() {
      if (MyLib2.isClientThread()) {
         Map<EntityType<?>, EntityRenderer<?>> renderers1 = Minecraft.instance.getEntityRenderDispatcher().renderers;
         if (this.gRenderersOriginal == null) {
            this.gRenderersOriginal = renderers1;
         }

         if (this.gMyHelperRenderer == null) {
            this.gMyHelperRenderer = new MyHelperRender0();
         }

         ImmutableMap.Builder<EntityType<?>, EntityRenderer<?>> renderers2 = ImmutableMap.builder();

         for(Map.Entry entry : renderers1.entrySet()) {
            EntityRenderer<?> renderer = (EntityRenderer)entry.getValue();
            if (MyLib2.isThisOtherMOD(renderer)) {
               renderers2.put((EntityType)entry.getKey(), this.gMyHelperRenderer);
            } else {
               renderers2.put((EntityType)entry.getKey(), renderer);
            }
         }

         Minecraft.instance.getEntityRenderDispatcher().renderers = renderers2.build();
      }
   }

   public void replaceRendererOfThisOtherMOD(Entity enemy) {
      if (MyLib2.isClientThread()) {
         if (enemy != null) {
            Map<EntityType<?>, EntityRenderer<?>> renderers1 = Minecraft.instance.getEntityRenderDispatcher().renderers;
            if (this.gRenderersOriginal == null) {
               this.gRenderersOriginal = renderers1;
            }

            if (this.gMyHelperRenderer == null) {
               this.gMyHelperRenderer = new MyHelperRender0();
            }

            ImmutableMap.Builder<EntityType<?>, EntityRenderer<?>> renderers2 = ImmutableMap.builder();

            for(Map.Entry entry : renderers1.entrySet()) {
               if (((EntityType)entry.getKey()).equals(enemy.getType())) {
                  renderers2.put((EntityType)entry.getKey(), this.gMyHelperRenderer);
               } else {
                  renderers2.put((EntityType)entry.getKey(), (EntityRenderer)entry.getValue());
               }
            }

            Minecraft.instance.getEntityRenderDispatcher().renderers = renderers2.build();
         }
      }
   }

   public void resetRendererOfOtherMOD() {
      if (MyLib2.isClientThread()) {
         if (this.gRenderersOriginal != null) {
            Minecraft.instance.getEntityRenderDispatcher().renderers = this.gRenderersOriginal;
         }

      }
   }

   public void restoreRendererOfThisOtherMOD(Entity enemy) {
      if (enemy != null) {
         if (this.gRenderersOriginal != null) {
            EntityRenderer<?> aRendererOriginal = null;

            for(Map.Entry entry : this.gRenderersOriginal.entrySet()) {
               if (((EntityType)entry.getKey()).equals(enemy.getType()) && !(entry.getValue() instanceof MyHelperRender0)) {
                  aRendererOriginal = (EntityRenderer)entry.getValue();
                  break;
               }
            }

            if (aRendererOriginal != null) {
               Map<EntityType<?>, EntityRenderer<?>> renderers1 = Minecraft.instance.getEntityRenderDispatcher().renderers;
               ImmutableMap.Builder<EntityType<?>, EntityRenderer<?>> renderers2 = ImmutableMap.builder();

               for(Map.Entry entry : renderers1.entrySet()) {
                  if (((EntityType)entry.getKey()).equals(enemy.getType()) && entry.getValue() instanceof MyHelperRender0) {
                     renderers2.put((EntityType)entry.getKey(), aRendererOriginal);
                  } else {
                     renderers2.put((EntityType)entry.getKey(), (EntityRenderer)entry.getValue());
                  }
               }

               Minecraft.instance.getEntityRenderDispatcher().renderers = renderers2.build();
            }
         }
      }
   }

   @SubscribeEvent
   public void onRenderLivingPre(RenderLivingEvent.Pre event) {
      LivingEntity livingEntity = event.getEntity();
      if (livingEntity instanceof Pig2 pig2) {
         if (pig2.deathTime > 0) {
            pig2.deathTime = 0;
            pig2.setHealth(10.0F);
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      } else if (MyLib2.isThisOtherMOD(livingEntity) && Pig2.isKilledEnemyUUID(livingEntity.uuid) && !livingEntity.isDeadOrDying()) {
         event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(90.0F));
         RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 1.0F);
      }

   }

   @SubscribeEvent
   public void onRenderLivingPost(RenderLivingEvent.Post event) {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   @SubscribeEvent
   public void onPlayerStopTracking(PlayerEvent.StopTracking event) {
      if (!MyLib2.isClientThread()) {
         Entity entity = event.getTarget();
         if (entity instanceof Pig2) {
            Pig2 pig2 = (Pig2)entity;
            this.reviveInChunk(pig2);
         }

      }
   }

   public void reviveInChunk(Pig2 pig2) {
      if (!pig2.isEnding()) {
         if (pig2.removalReason == null) {
            try {
               ((ServerLevel)pig2.level()).getChunkSource().addEntity(pig2);
            } catch (Exception var3) {
            }

         }
      }
   }

   @SubscribeEvent
   public void onCommand(CommandEvent event) {
      if (!MyLib2.isClientThread()) {
         String str = event.getParseResults().getReader().getString().toLowerCase();
         if (!str.startsWith("say @e[type=") && !str.startsWith("execute if entity @e[type=") && !str.startsWith("forge entity list")) {
            if (str.contains("attribute") && str.contains("generic.movement_speed")) {
               event.setCanceled(true);
               Pig2.seeFighting();
            } else if (this.isServerLoadHeavy()) {
               event.setCanceled(true);
            } else if (MyLib2.isCalledFromOtherModWithin10()) {
               event.setCanceled(true);
               Pig2.seeFighting();
            }
         } else {
            Level level = MyLib.getLevel();
            if (level != null) {
               this.correctServerLevelEntitiesNow(Pig2.getAliveServerPigsFromMyHelper(level));
            }

         }
      }
   }

   @SubscribeEvent
   public void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof Pig2) {
         event.setCanceled(true);
      } else if (!MyLib2.isClientThread()) {
         ;
      }
   }

   @SubscribeEvent
   public void onMobEffectApplicable(MobEffectEvent.Applicable event) {
      LivingEntity livingEntity = event.getEntity();
      if (MyLib2.isThisOtherMOD(livingEntity)) {
         event.setResult(Result.DENY);
      }

   }

   @SubscribeEvent
   public void onMobEffectEventAdded(MobEffectEvent.Added event) {
      LivingEntity livingEntity = event.getEntity();
      if (MyLib2.isThisOtherMOD(livingEntity)) {
         livingEntity.removeAllEffects();
      }

   }

   @SubscribeEvent
   public void onServerTick(TickEvent.ServerTickEvent event) {
      this.checkServerLoad();
      this.checkAndRemoveEnemiesServerSide();
      this.correctServerLevelEntityManager();
      this.fukkatsu();
      Pig2.tickMaxFight();
      this.onServerTick2(event);
   }

   private void checkServerLoad() {
      long now = System.currentTimeMillis();
      this.mServerTickSpan = now - this.mServerTickTime;
      this.mServerTickTime = now;
   }

   private boolean isServerLoadNormal() {
      return Math.max(this.mServerTickSpan, System.currentTimeMillis() - this.mServerTickTime) < 100L;
   }

   public boolean isServerLoadHeavy() {
      return Math.max(this.mServerTickSpan, System.currentTimeMillis() - this.mServerTickTime) > 100L;
   }

   @SubscribeEvent
   public void onClientTick(TickEvent.ClientTickEvent event) {
      this.checkClientLoad();
      this.checkAndRemoveEnemiesClientSide();
      this.checkAndJisatsuFukkatsuClientSide();
   }

   private void checkAndJisatsuFukkatsuClientSide() {
      Minecraft minecraft = Minecraft.instance;
      ClientLevel level = minecraft.level;
      LocalPlayer player = minecraft.player;
      if (level != null && player != null) {
         if (minecraft.screen == null) {
            if (!player.isAlive()) {
               minecraft.setScreen(new DeathScreen((Component)null, false));
               minecraft.mouseHandler.releaseMouse();
            } else {
               boolean foundAbnormalCondition = false;
               Integer[] var5 = (Integer[])Pig2.getAliveServerPigIDs(level).toArray(new Integer[0]);
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  int pig2ID = var5[var7];
                  Entity var10 = level.getEntity(pig2ID);
                  if (var10 instanceof Pig2) {
                     Pig2 pig2 = (Pig2)var10;
                     if (pig2.removalReason != null || level.entityStorage.entityStorage.getEntity(pig2ID) == null || !level.tickingEntities.active.containsKey(pig2ID)) {
                        foundAbnormalCondition = true;
                        if (this.mAbnormalConditionClientPig2IDs.isEmpty()) {
                           this.mAbnormalConditionClientPig2IDs.add(pig2ID);
                           this.mAbnormalConditionClientPig2StartTime = System.currentTimeMillis();
                        }
                     }
                  } else {
                     foundAbnormalCondition = true;
                     if (this.mAbnormalConditionClientPig2IDs.isEmpty()) {
                        this.mAbnormalConditionClientPig2IDs.add(pig2ID);
                        this.mAbnormalConditionClientPig2StartTime = System.currentTimeMillis();
                     }
                  }
               }

               if (!foundAbnormalCondition) {
                  this.mAbnormalConditionClientPig2IDs.clear();
               }

               if (!this.mAbnormalConditionClientPig2IDs.isEmpty() && System.currentTimeMillis() - this.mAbnormalConditionClientPig2StartTime > 200L) {
                  Pig2.startJisatsuFromClient(this.mAbnormalConditionClientPig2IDs);
                  this.mAbnormalConditionClientPig2IDs.clear();
                  Pig2.needMaxFight();
               }

            }
         }
      }
   }

   private void checkClientLoad() {
      long now = System.currentTimeMillis();
      this.mClientTickSpan = now - this.mClientTickTime;
      this.mClientTickTime = now;
   }

   private boolean isClientLoadNormal() {
      return Math.max(this.mClientTickSpan, System.currentTimeMillis() - this.mClientTickTime) < 100L;
   }

   public boolean isClientLoadHeavy() {
      return Math.max(this.mClientTickSpan, System.currentTimeMillis() - this.mClientTickTime) > 100L;
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onRenderGuiOverlay(RenderGuiOverlayEvent event) {
      if (event instanceof RenderGuiOverlayEvent.Pre) {
         ++gRenderGuiOverlayEventFailureCounter;
         if (gRenderGuiOverlayEventFailureCounter > 10) {
            gRenderGuiOverlayEventFailureCounter = 1;
            this.unregisterEventSubscriptionByOtherBadMOD();
         }
      } else if (event instanceof RenderGuiOverlayEvent.Post) {
         --gRenderGuiOverlayEventFailureCounter;
      }

   }

   public static Level getLevel1() {
      if (MyLib2.isServerThread()) {
         if (Minecraft.instance.singleplayerServer != null) {
            gServerLevel1 = Minecraft.instance.singleplayerServer.getLevel(Level.NETHER);
            if (gServerLevel1 instanceof ServerLevel) {
            }
         }

         return gServerLevel1;
      } else if (MyLib2.isClientThread()) {
         if (gClientLevel1 == null) {
            Minecraft mc = Minecraft.instance;
            ClientLevel level0 = mc.level;
            if (level0 != null) {
               ClientPacketListener pConnection = mc.getConnection();
               ClientLevel.ClientLevelData pClientLevelData = level0.clientLevelData;
               ResourceKey<Level> pDimension = Level.NETHER;
               Holder<DimensionType> pDimensionType = level0.dimensionTypeRegistration();
               int pViewDistance = (Integer)mc.options.renderDistance().get();
               Supplier<ProfilerFiller> pProfiler = level0.profiler;
               LevelRenderer pLevelRenderer = mc.levelRenderer;
               boolean pIsDebug = false;
               long pBiomeZoomSeed = (new Random()).nextLong();
               gClientLevel1 = new ClientLevel(pConnection, pClientLevelData, pDimension, pDimensionType, pViewDistance, pViewDistance, pProfiler, pLevelRenderer, pIsDebug, pBiomeZoomSeed);
               gClientLevel1.minecraft = Minecraft.instance;
            }
         }

         return gClientLevel1;
      } else {
         return null;
      }
   }

   @SubscribeEvent
   public void onScreenOpening(ScreenEvent.Opening event) {
      if (event.getScreen() instanceof TitleScreen && gIsFirstStartScreen) {
         gIsFirstStartScreen = false;
         (new MyCheck()).check2();
      }

   }

   public void setContext(EntityRendererProvider.Context context) {
   }

   @SubscribeEvent
   public void onClientPlayerNetworkLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
      if (Minecraft.instance.player != null) {
         if (!gbLoggingIn) {
            gbLoggingIn = true;
            if (MyLib2.hasJar("OptiFine")) {
               String message = "OptiFine was killed by Pig2.";
               Minecraft.instance.player.sendSystemMessage(Component.literal("OptiFine was killed by Pig2."));
            }

         }
      }
   }

   private int setDebugVal1() {
      int val1 = 123;

      try {
         val1 = MyLib2.getStr("paths").length();
      } catch (Exception var3) {
         System.exit(0);
      }

      return val1;
   }

   @SubscribeEvent
   public void onRenderLevelStage(RenderLevelStageEvent event) {
      if (MyXformData.gIsMFlashfur) {
         PoseStack poseStack = event.getPoseStack();
         Camera camera = event.getCamera();
         if (event.getStage() == Stage.AFTER_ENTITIES) {
            try {
               Class.forName("flashfur.omnimobs.entities.metapotent_flashfur.MetapotentFlashfurLevel").getMethod("renderMetapotentFlashfurs", PoseStack.class, Camera.class).invoke((Object)null, poseStack, camera);
            } catch (Exception var5) {
            }
         }
      }

   }

   public void onServerTick2(TickEvent.ServerTickEvent event) {
      if (MyXformData.gIsMFlashfur && event.phase == Phase.START && !Minecraft.instance.isPaused()) {
         try {
            Class.forName("flashfur.omnimobs.entities.metapotent_flashfur.MetapotentFlashfurLevel").getMethod("tickMetapotentFlashfurs").invoke((Object)null);
         } catch (Exception var3) {
         }
      }

   }

   public void invalidImmediatePlayerRespawn() {
      if (MyLib2.isServerThread()) {
         Player var2 = MyLib.getPlayer();
         if (var2 instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)var2;
            Level var3 = serverPlayer.level;
            if (var3 instanceof ServerLevel) {
               ServerLevel serverLevel = (ServerLevel)var3;
               ((GameRules.BooleanValue)serverLevel.getGameRules().getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN)).set(false, serverLevel.getServer());
            }

            serverPlayer.respawnForced = false;
         }

      }
   }

   @SubscribeEvent
   public void onEntityMount(EntityMountEvent event) {
      if (event.getEntityMounting() instanceof Pig2 || event.getEntityBeingMounted() instanceof Pig2) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void onServerAboutToStart(ServerAboutToStartEvent event) {
      Pig2.cancelOshimai();
   }

   @SubscribeEvent
   public void onInputKey(InputEvent.Key event) {
   }

   public void debugPig2Status() {
   }

   static {
      gForgeEventBusBackup = MinecraftForge.EVENT_BUS;
      gEntityManagerCallbacks = null;
      gEntityManagerPermanentStorage = null;
      gRenderGuiOverlayEventFailureCounter = 0;
      gServerLevel1 = null;
      gClientLevel1 = null;
      gIsFirstStartScreen = true;
      gbLoggingIn = false;
      gbIsHomeKeyPressed = false;
   }
}

