package kakiku.pig2mod.xform;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.ModuleLayerHandler;
import sun.misc.Unsafe;

public class MyLaunchPluginHandler extends LaunchPluginHandler {
   public MyLaunchPluginHandler(ModuleLayerHandler layerHandler) {
      super(layerHandler);
   }

   public static MyLaunchPluginHandler myNew() {
      MyLaunchPluginHandler newInstance = null;

      try {
         Unsafe unsafe = MyLib2.getUnsafe();
         newInstance = (MyLaunchPluginHandler)unsafe.allocateInstance(MyLaunchPluginHandler.class);
         newInstance.restoreFields();
      } catch (Exception var2) {
      }

      return newInstance;
   }

   private void restoreFields() {
      try {
         MyLaunchPluginsMap.setMyLaunchPluginsMap(this);
      } catch (Exception var2) {
      }

   }
}

