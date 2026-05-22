package kakiku.pig2mod.xform;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.module.Configuration;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import joptsimple.OptionSpecBuilder;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

public class MyXformService implements ITransformationService {
   private static final String THIS_JAR_NAME = "pig2mod";
   private static final Class THIS_CLASS = MyXformService.class;
   public static final String THIS_XFORMSERVICE_NAME = "pig2xform";
   public static boolean isDebugging = false;
   private static final ITransformer gMyXformer = new MyXformer();
   public static final ILaunchPluginService gMyPlugin = new MyPlugin();
   private static MethodHandle gUnsafeGetUnsafeHandle;
   private static MethodHandle gUnsafePutReferenceHandle;

   public MyXformService() {
      test01();
   }

   public String name() {
      MyLib2.killOtherXformFromMainThread();
      return "pig2xform";
   }

   public void onLoad(IEnvironment env, Set otherServices) {
      MyLib2.killOtherXformFromMainThread();
   }

   public void arguments(BiFunction argumentBuilder) {
      MyLib2.killOtherXformFromMainThread();
   }

   public void argumentValues(ITransformationService.OptionResult option) {
      MyLib2.killOtherXformFromMainThread();
   }

   public void initialize(IEnvironment environment) {
      MyLib2.killOtherXformFromMainThread();
      test02();
   }

   public List beginScanning(IEnvironment environment) {
      MyLib2.killOtherXformFromMainThread();
      test03();
      return List.of();
   }

   public List completeScan(IModuleLayerManager layerManager) {
      MyLib2.killOtherXformFromMainThread();
      return List.of();
   }

   public @NotNull List transformers() {
      MyLib2.killOtherXformFromMainThread();

      try {
         List<ITransformer> list = new ArrayList();
         list.add(gMyXformer);
         return list;
      } catch (Throwable var2) {
         return new ArrayList();
      }
   }

   public Map.Entry additionalClassesLocator() {
      MyLib2.killOtherXformFromMainThread();
      return null;
   }

   public Map.Entry additionalResourcesLocator() {
      MyLib2.killOtherXformFromMainThread();
      return null;
   }

   public static void makeMyModLoadable() {
      test04();

      try {
         Class<?> modDirTransformerDiscoverer = Class.forName("net.minecraftforge.fml.loading.ModDirTransformerDiscoverer");
         VarHandle foundHandle = MethodHandles.privateLookupIn(modDirTransformerDiscoverer, MethodHandles.lookup()).findStaticVarHandle(modDirTransformerDiscoverer, "found", List.class);
         List<?> found = foundHandle.get();
         found.removeIf((namedPath) -> {
            Path[] paths = null;

            try {
               paths = (Path[])namedPath.getClass().getMethod(MyLib2.getStr("paths")).invoke(namedPath);
            } catch (Exception exception) {
               throw new RuntimeException(exception);
            }

            return paths[0].toString().contains("pig2mod");
         });
      } catch (Exception exception) {
         throw new RuntimeException(exception);
      }

      try {
         Class<?> launcher = Class.forName("cpw.mods.modlauncher.Launcher");
         Class<?> moduleLayerHandlerClass = Class.forName("cpw.mods.modlauncher.ModuleLayerHandler");
         VarHandle instanceHandle = MethodHandles.privateLookupIn(launcher, MethodHandles.lookup()).findStaticVarHandle(launcher, "INSTANCE", launcher);
         Object INSTANCE = instanceHandle.get();
         VarHandle moduleLayerHandlerHandle = MethodHandles.privateLookupIn(launcher, MethodHandles.lookup()).findVarHandle(launcher, "moduleLayerHandler", moduleLayerHandlerClass);
         Object moduleLayerHandler = moduleLayerHandlerHandle.get(INSTANCE);
         VarHandle completedLayersHandle = MethodHandles.privateLookupIn(moduleLayerHandlerClass, MethodHandles.lookup()).findVarHandle(moduleLayerHandlerClass, "completedLayers", EnumMap.class);
         EnumMap<?, ?> completedLayers = completedLayersHandle.get(moduleLayerHandler);
         Class<?> layerInfoClass = Class.forName("cpw.mods.modlauncher.ModuleLayerHandler.LayerInfo");
         VarHandle layerHandle = MethodHandles.privateLookupIn(layerInfoClass, MethodHandles.lookup()).findVarHandle(layerInfoClass, "layer", ModuleLayer.class);
         completedLayers.values().forEach((layerInfo) -> {
            ModuleLayer layer = layerHandle.get(layerInfo);
            Configuration config = layer.configuration();
            String moduleName = THIS_CLASS.getModule().getName();

            try {
               Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
               unsafeField.setAccessible(true);
               Unsafe unsafe = (Unsafe)unsafeField.get((Object)null);
               Field nameToModuleField = Configuration.class.getDeclaredField("nameToModule");
               long nameToModuleOffset = unsafe.objectFieldOffset(nameToModuleField);
               Field modulesField = Configuration.class.getDeclaredField("modules");
               long modulesOffset = unsafe.objectFieldOffset(modulesField);
               Map<String, Object> nameToModule1 = (Map)unsafe.getObject(config, nameToModuleOffset);
               Set<Object> modules1 = (Set)unsafe.getObject(config, modulesOffset);
               Map<String, Object> nameToModule2 = new HashMap(nameToModule1);
               Set<Object> modules2 = new HashSet(modules1);
               if (nameToModule1 != null && nameToModule1.containsKey(moduleName)) {
                  modules2.remove(nameToModule2.remove(moduleName));
               }

               Class<?> jdkUnsafeClass = Class.forName("jdk.internal.misc.Unsafe");
               if (gUnsafeGetUnsafeHandle == null) {
                  gUnsafeGetUnsafeHandle = MyLib2.getLookup().findStatic(jdkUnsafeClass, "getUnsafe", MethodType.methodType(jdkUnsafeClass));
               }

               Object internalUnsafe = gUnsafeGetUnsafeHandle.invoke();
               if (gUnsafePutReferenceHandle == null) {
                  gUnsafePutReferenceHandle = MyLib2.getLookup().findVirtual(internalUnsafe.getClass(), "putReference", MethodType.methodType(Void.TYPE, Object.class, Long.TYPE, Object.class));
               }

               gUnsafePutReferenceHandle.invoke(internalUnsafe, config, nameToModuleOffset, nameToModule2);
               gUnsafePutReferenceHandle.invoke(internalUnsafe, config, modulesOffset, modules2);
            } catch (Throwable t) {
               throw new RuntimeException(t);
            }
         });
      } catch (Exception var10) {
      }

   }

   public static void test01() {
      try {
         if (!(Boolean)Class.forName("kakiku.pig2mod.xform.MyCheckB").getMethod("f92").invoke((Object)null)) {
            Class.forName("kakiku.pig2mod.xform.MyCheckB").getMethod("f95").invoke((Object)null);
         }
      } catch (Exception var1) {
      }

   }

   public static void test02() {
      try {
         if (!(Boolean)Class.forName("kakiku.pig2mod.xform.MyCheckB").getMethod("f93").invoke((Object)null)) {
            Class.forName("kakiku.pig2mod.xform.MyCheck").getMethod("check3").invoke((Object)null);
         }
      } catch (Exception var1) {
      }

   }

   public static void test03() {
      try {
         if (!(Boolean)Class.forName("kakiku.pig2mod.xform.MyCheckB").getMethod("f91").invoke((Object)null)) {
            Class.forName("kakiku.pig2mod.xform.MyXformService").getDeclaredMethod("makeMyModLoadable").invoke((Object)null);
         }
      } catch (Exception var1) {
      }

   }

   public static void test04() {
      try {
         if (!(Boolean)Class.forName("kakiku.pig2mod.xform.MyCheckB").getMethod("f93").invoke((Object)null)) {
            Class.forName("kakiku.pig2mod.xform.MyCheckB").getMethod("f96").invoke((Object)null);
         }
      } catch (Exception var1) {
      }

   }

   static {
      MyLib2.getCaller1Name();
      (new MyCheck()).check1();
      MyXformer2.doIt();
      MyLib2.killOtherXformFromMainThread();
      gUnsafeGetUnsafeHandle = null;
      gUnsafePutReferenceHandle = null;
   }
}

