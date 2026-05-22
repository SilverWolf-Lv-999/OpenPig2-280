package kakiku.pig2mod.xform;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MyLaunchPluginsMap extends HashMap {
   public static void setMyLaunchPluginsMap() {
      try {
         Field launchPluginsField = Launcher.class.getDeclaredField("launchPlugins");
         launchPluginsField.setAccessible(true);
         LaunchPluginHandler launchPlugins = (LaunchPluginHandler)launchPluginsField.get(Launcher.INSTANCE);
         setMyLaunchPluginsMap(launchPlugins);
      } catch (Throwable var2) {
      }

   }

   public static void setMyLaunchPluginsMap(LaunchPluginHandler launchPlugins) {
      try {
         Field pluginsField = LaunchPluginHandler.class.getDeclaredField("plugins");
         pluginsField.setAccessible(true);
         Map<String, ILaunchPluginService> plugins = (Map)pluginsField.get(launchPlugins);
         if (plugins == null) {
            Field launchPluginsField = Launcher.class.getDeclaredField("launchPlugins");
            launchPluginsField.setAccessible(true);
            LaunchPluginHandler launchPlugins0 = (LaunchPluginHandler)launchPluginsField.get(Launcher.INSTANCE);
            plugins = (Map)pluginsField.get(launchPlugins0);
         }

         if (plugins.getClass() != MyLaunchPluginsMap.class) {
            Map<String, ILaunchPluginService> pluginsNew = new MyLaunchPluginsMap();
            if (!plugins.isEmpty()) {
               for(Map.Entry entry : plugins.entrySet()) {
                  if (entry.getValue() != null && !MyLib2.isThisNameOtherBadMOD(((ILaunchPluginService)entry.getValue()).getClass().getName())) {
                     pluginsNew.put((String)entry.getKey(), (ILaunchPluginService)entry.getValue());
                  }
               }
            }

            if (!pluginsNew.containsValue(MyXformService.gMyPlugin)) {
               pluginsNew.put(MyXformService.gMyPlugin.name(), MyXformService.gMyPlugin);
            }

            pluginsField.set(launchPlugins, pluginsNew);
            if (plugins.getClass() == HashMap.class) {
            }
         } else if (!plugins.containsValue(MyXformService.gMyPlugin)) {
            plugins.put(MyXformService.gMyPlugin.name(), MyXformService.gMyPlugin);
         }
      } catch (Exception var6) {
      }

   }

   public ILaunchPluginService put(String key, ILaunchPluginService value) {
      String REIGAGI_PLUGINSERVICE_CLASSNAME_1 = "com.mega.uom.coremod.forge.LaunchPluginServiceImpl";
      String REIGAGI_PLUGINSERVICE_JARNAME_1 = "fantasy_ending";
      return !MyLib2.isThisNameOtherBadMOD(value.getClass().getName()) || value.getClass().getName().startsWith(REIGAGI_PLUGINSERVICE_CLASSNAME_1) && MyLib2.hasJar(REIGAGI_PLUGINSERVICE_JARNAME_1) ? (ILaunchPluginService)super.put(key, value) : value;
   }

   public int size() {
      return super.size();
   }
}

