package kakiku.pig2mod.xform;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import sun.misc.Unsafe;

public class MyCheckB {
   private static String gDec = MyCheck.C.dec("");
   private final String mm01 = "Pig2.hukkatsu";
   private final String mm02 = "Pig2.chienhukkatsu";
   private String mm03;
   private String mm04;
   private String[] mm05;
   private String mm06;

   public MyCheckB() {
      String var10001 = this.f12("java.home");
      this.mm03 = var10001 + "/bin/java";
      this.mm04 = this.f12("libraryDirectory");
      this.mm05 = null;
      this.mm06 = "";
   }

   public boolean check1() {
      try {
         Method m01 = this.getClass().getDeclaredMethod("f10");
         m01.setAccessible(true);
         Method m02 = this.getClass().getDeclaredMethod("f03");
         m02.setAccessible(true);
         if ((Boolean)this.f01(m01, this)) {
            this.f01(m02, this);
         }

         Method m03 = this.getClass().getDeclaredMethod("f90");
         m03.setAccessible(true);
         return (Boolean)this.f01(m03, (Object)null);
      } catch (Throwable var4) {
         return false;
      }
   }

   public boolean check2() {
      try {
         if (!"true".equals(this.f12(this.mm01)) && "true".equals(this.f12(this.mm02))) {
            Method m01 = this.getClass().getDeclaredMethod("f04");
            m01.setAccessible(true);
            Method m02 = this.getClass().getDeclaredMethod("f03");
            m02.setAccessible(true);
            if ((Boolean)this.f01(m01, this)) {
               this.f01(m02, this);
            }
         }

         Method m03 = this.getClass().getDeclaredMethod("f90");
         m03.setAccessible(true);
         return (Boolean)this.f01(m03, (Object)null);
      } catch (Throwable var3) {
         return false;
      }
   }

   private String f12(String string) {
      try {
         Class<?> c01 = Class.forName("java.lang.System");
         Method m01 = c01.getMethod("getProperty", String.class);
         Object x01 = this.f01(m01, (Object)null, string);
         return (String)x01;
      } catch (Exception var5) {
         return "";
      }
   }

   private boolean f10() {
      try {
         Class<?> c01 = Class.forName("java.lang.System");
         Method m91 = c01.getMethod("setProperty", String.class, String.class);
         this.f01(m91, (Object)null, "Pig2.paths", "paths");
         Class<?> c = Class.forName("kakiku.pig2mod.xform.MyXformService");
         Field f = c.getField("isDebugging");
         f.set((Object)null, true);
         if ("true".equals(this.f12(this.mm01))) {
            return false;
         } else if (!this.f07()) {
            return false;
         } else if (!this.f04()) {
            this.f01(m91, (Object)null, this.mm02, "true");
            return false;
         } else {
            return true;
         }
      } catch (Exception var5) {
         return false;
      }
   }

   private void f03() {
      try {
         this.mm05 = this.f06();
         if (this.mm05 == null) {
            throw new Exception("マイクラ起動時の引数の取得失敗");
         }

         this.mm06 = this.f08().toString();
         String x51 = this.f12("minecraft.launcher.brand");
         new ArrayList();
         List x52;
         if (!x51.contains("minecraft") && !x51.contains("CurseForge") && !x51.contains("Prism") && !x51.contains("HMCL") && !x51.contains("theseus") && !x51.contains("PCL")) {
            x52 = this.f02();
         } else {
            x52 = this.f02();
         }

         Class<?> c01 = Class.forName("java.lang.ProcessBuilder");
         Constructor<?> x53 = c01.getConstructor(List.class);
         Object x72 = x53.newInstance(x52);
         Class<?> c02 = Class.forName("net.minecraftforge.fml.loading.FMLPaths");
         Field fd37 = c02.getField("GAMEDIR");
         Object x37 = fd37.get((Object)null);
         Method m38 = c02.getMethod("get");
         Object x38 = this.f01(m38, x37);
         Class<?> c03 = Class.forName("java.nio.file.Path");
         Method m39 = c03.getMethod("toUri");
         Object x39 = this.f01(m39, x38);
         Class<?> c04 = Class.forName("java.io.File");
         Class<?> c05 = Class.forName("java.net.URI");
         Constructor<?> m40 = c04.getConstructor(c05);
         Object x40 = m40.newInstance(x39);
         Method m41 = x72.getClass().getMethod("directory", c04);
         this.f01(m41, x72, x40);
         Constructor<?> m91 = c04.getConstructor(String.class);
         Object x91 = m91.newInstance("logs/system.out.log");
         Method m92 = c01.getMethod("redirectOutput", c04);
         this.f01(m92, x72, x91);
         Object x93 = m91.newInstance("logs/system.err.log");
         Method m94 = c01.getMethod("redirectError", c04);
         this.f01(m94, x72, x93);
         Method m12 = x72.getClass().getDeclaredMethod("start");
         m12.setAccessible(true);
         x91 = this.f01(m12, x72);

         try {
            Class<?> c10 = Class.forName("org.apache.logging.log4j.LogManager");
            Method m10 = c10.getMethod("getContext", Boolean.TYPE);
            Object x10 = this.f01(m10, (Object)null, false);
            Method m11 = x10.getClass().getMethod("stop");
            this.f01(m11, x10);
            Class<?> c11 = Class.forName("java.lang.Thread");
            Method m73 = c11.getMethod("sleep", Long.TYPE);
            this.f01(m73, (Object)null, Long.parseLong("2500"));
            this.f05();
            Class<?> c23 = Class.forName("java.lang.Process");
            Method m23 = c23.getDeclaredMethod("waitFor");
            m23.setAccessible(true);
            this.f01(m23, x91);
         } catch (Throwable var29) {
         }

         Class<?> c15 = Class.forName("java.lang.Runtime");
         Method m74 = c15.getMethod("getRuntime");
         Object x74 = this.f01(m74, (Object)null);
         Method m75 = x74.getClass().getMethod("halt", Integer.TYPE);
         this.f01(m75, x74, 0);
      } catch (Throwable var30) {
      }

   }

   private List f02() {
      String x61 = this.f12("jdk.module.path");
      if (x61 == null || !x61.contains("bootstraplauncher-")) {
         x61 = this.f09();
      }

      String x62 = this.f12("java.class.path");
      List<String> x51 = new ArrayList();
      x51.add(this.mm03);
      String var10001 = "-D";
      x51.add(var10001 + this.mm01 + "=true");
      var10001 = "-Djava.net.preferIPv6Addresses=";
      x51.add(var10001 + this.f12("java.net.preferIPv6Addresses"));
      var10001 = "-DignoreList=";
      x51.add(var10001 + this.f12("ignoreList"));
      var10001 = "-DmergeModules=";
      x51.add(var10001 + this.f12("mergeModules"));
      var10001 = "-DlibraryDirectory=";
      x51.add(var10001 + this.f12("libraryDirectory"));
      var10001 = "-Djava.library.path=";
      x51.add(var10001 + this.f12("java.library.path"));
      var10001 = "-Djna.tmpdir=";
      x51.add(var10001 + this.f12("jna.tmpdir"));
      var10001 = "-Dorg.lwjgl.system.SharedLibraryExtractPath=";
      x51.add(var10001 + this.f12("org.lwjgl.system.SharedLibraryExtractPath"));
      var10001 = "-Dio.netty.native.workdir=";
      x51.add(var10001 + this.f12("io.netty.native.workdir"));
      var10001 = "-Dminecraft.launcher.brand=";
      x51.add(var10001 + this.f12("minecraft.launcher.brand"));
      var10001 = "-Dminecraft.launcher.version=";
      x51.add(var10001 + this.f12("minecraft.launcher.version"));
      var10001 = "-javaagent:";
      x51.add(var10001 + this.mm06);
      x51.add("-cp");
      x51.add(x62);
      x51.add("--module-path");
      x51.add(x61);
      x51.add("--add-modules");
      x51.add("ALL-MODULE-PATH");
      x51.add("--add-opens");
      x51.add("java.base/java.util.jar=cpw.mods.securejarhandler");
      x51.add("--add-opens");
      x51.add("java.base/java.lang.invoke=cpw.mods.securejarhandler");
      x51.add("--add-exports");
      x51.add("java.base/sun.security.util=cpw.mods.securejarhandler");
      x51.add("--add-exports");
      x51.add("jdk.naming.dns/com.sun.jndi.dns=java.naming");
      x51.addAll(List.of(this.f16()));
      x51.add("cpw.mods.bootstraplauncher.BootstrapLauncher");
      x51.addAll(List.of(this.mm05));
      return x51;
   }

   private String[] f16() {
      List<String> x10;
      try {
         Class<?> c01 = Class.forName("java.lang.management.ManagementFactory");
         Method m01 = c01.getDeclaredMethod("getRuntimeMXBean");
         m01.setAccessible(true);
         Object x01 = this.f01(m01, (Object)null);
         Class<?> c02 = Class.forName("java.lang.management.RuntimeMXBean");
         Method m02 = c02.getDeclaredMethod("getInputArguments");
         m02.setAccessible(true);
         x10 = (List)this.f01(m02, x01);
      } catch (Exception var7) {
         return new String[0];
      }

      List<String> x20 = new ArrayList();

      for(String x11 : x10) {
         if (x11.startsWith("-Xmx") || x11.startsWith("-Xms")) {
            x20.add(x11);
         }
      }

      return (String[])x20.toArray(new String[0]);
   }

   private String[] f06() {
      String[] x01 = null;

      try {
         Field fd01 = Unsafe.class.getDeclaredField("theUnsafe");
         fd01.setAccessible(true);
         Unsafe x30 = (Unsafe)fd01.get((Object)null);
         Class<?> c01 = Class.forName("cpw.mods.modlauncher.Launcher");
         Field fd11 = c01.getField("INSTANCE");
         Object x53 = fd11.get((Object)null);
         Field fd02 = x53.getClass().getDeclaredField("argumentHandler");
         long ff01 = x30.objectFieldOffset(fd02);
         Object x54 = x30.getObject(x53, ff01);
         Field fd03 = x54.getClass().getDeclaredField("args");
         long ff02 = x30.objectFieldOffset(fd03);
         x01 = (String[])x30.getObject(x54, ff02);
      } catch (Exception var14) {
         x01 = null;
      }

      return x01;
   }

   private String f11(String[] p1, String p2) {
      String rtn = "";

      for(int i = 0; i < p1.length - 1; ++i) {
         if (p2.equals(p1[i])) {
            rtn = p1[i + 1];
            break;
         }
      }

      return rtn;
   }

   private String f09() {
      try {
         String x01 = this.mm04;
         return String.join(";", x01 + "/cpw/mods/bootstraplauncher/1.1.2/bootstraplauncher-1.1.2.jar", x01 + "/cpw/mods/securejarhandler/2.1.10/securejarhandler-2.1.10.jar", x01 + "/net/minecraftforge/JarJarFileSystems/0.3.19/JarJarFileSystems-0.3.19.jar", x01 + "/org/ow2/asm/asm/9.7.1/asm-9.7.1.jar", x01 + "/org/ow2/asm/asm-tree/9.7.1/asm-tree-9.7.1.jar", x01 + "/org/ow2/asm/asm-util/9.7.1/asm-util-9.7.1.jar", x01 + "/org/ow2/asm/asm-analysis/9.7.1/asm-analysis-9.7.1.jar", x01 + "/org/ow2/asm/asm-commons/9.7.1/asm-commons-9.7.1.jar").replace('\\', '/');
      } catch (Exception var2) {
         return "";
      }
   }

   private boolean f07() throws Exception {
      List<String> x01 = List.of("net.minecraftforge.fml.loading.FMLServiceProvider", "org.spongepowered.asm.launch.MixinTransformationService", "optifine.OptiFineTransformationService");

      try {
         Class<?> c01 = Class.forName("kakiku.pig2mod.xform.MyXformService");
         Method m02 = Class.class.getMethod("getName");
         String x02 = (String)this.f01(m02, c01);
         Class<?> c02 = Class.forName("cpw.mods.modlauncher.api.ITransformationService");
         Class<?> c03 = Class.forName("java.util.ServiceLoader");
         Class<?> c04 = Class.forName("java.lang.ModuleLayer");
         Class<?> c05 = Class.forName("java.util.ServiceLoader$Provider");
         Class<?> c06 = Class.forName("java.util.stream.Stream");
         Class<?> c07 = Class.forName("java.util.function.Function");
         Method m03 = Class.class.getMethod("getModule");
         Object x03 = this.f01(m03, c01);
         Method m04 = x03.getClass().getMethod("getLayer");
         Object x04 = this.f01(m04, x03);
         Method m05 = c03.getMethod("load", c04, Class.class);
         Object x05 = this.f01(m05, (Object)null, x04, c02);
         Method m06 = c03.getMethod("stream");
         Method m07 = c05.getMethod("type");
         Object functionProxy = Proxy.newProxyInstance(c07.getClassLoader(), new Class[]{c07}, (proxy, method, args) -> method.getName().equals("apply") ? this.f01(m07, args[0]) : null);
         Method m08 = c06.getMethod("map", c07);
         Object x06 = this.f01(m06, x05);
         Object x08 = this.f01(m08, x06, functionProxy);
         List<?> x10 = new ArrayList();
         if (x08 instanceof Stream stream) {
            x10 = stream.toList();
         }

         if (x10.size() >= 4) {
            for(Object object : x10) {
               if (object instanceof Class) {
                  Class<?> clazz = (Class)object;
                  String x79 = (String)this.f01(m02, clazz);
                  if (!x02.equals(x79) && !x01.contains(x79)) {
                     return true;
                  }
               }
            }
         }

         Class<?> c21 = Class.forName("java.lang.management.ManagementFactory");
         Method m01 = c21.getDeclaredMethod("getRuntimeMXBean");
         m01.setAccessible(true);
         Object x21 = this.f01(m01, (Object)null);
         Class<?> c22 = Class.forName("java.lang.management.RuntimeMXBean");
         Method m22 = c22.getDeclaredMethod("getInputArguments");
         m22.setAccessible(true);
         List<String> x22 = (List)this.f01(m22, x21);
         if (x22 != null) {
            for(int i = 0; i < x22.size(); ++i) {
               String x23 = (String)x22.get(i);
               if (x23.startsWith("-javaagent") && !x23.contains("pig2_agent") || x23.startsWith("-agentlib") || x23.startsWith("-agentpath") || x23.startsWith("-Xbootclasspath") || x23.startsWith("--patch-module") || x23.startsWith("-Djava.system.class.loader") || x23.startsWith("-Djava.security.manager")) {
                  return true;
               }

               if (x23.startsWith("--add-opens") || x23.startsWith("--add-exports")) {
                  if ((x23.equals("--add-opens") || x23.equals("--add-exports")) && i + 1 < x22.size()) {
                     x23 = x23 + "=" + (String)x22.get(i + 1);
                  }

                  String x24 = x23.substring(x23.indexOf(61) + 1, x23.length());
                  if (x24.startsWith("java.base/jdk.internal.misc") || x24.startsWith("java.base/jdk.internal.reflect") || x24.startsWith("java.base/sun.misc") || x24.startsWith("java.base/java.lang.reflect") || x24.startsWith("java.base/java.lang.invoke") && !x24.equals("java.base/java.lang.invoke=cpw.mods.securejarhandler")) {
                     return true;
                  }
               }
            }
         }
      } catch (Throwable var33) {
      }

      return false;
   }

   private boolean f04() {
      String x51 = "lwjgl.dll";
      String x52 = "OpenAL.dll";
      String x53 = this.f12("java.library.path");

      try {
         Class<?> c01 = Class.forName("java.io.File");
         Constructor<?> m01 = c01.getConstructor(String.class);
         Object x01 = m01.newInstance(x53);
         Method m02 = x01.getClass().getMethod("exists");
         if ((Boolean)this.f01(m02, x01)) {
            Constructor<?> m03 = c01.getConstructor(c01, String.class);
            Object x03 = m03.newInstance(x01, x51);
            Object x04 = m03.newInstance(x01, x52);
            if ((Boolean)this.f01(m02, x03) && (Boolean)this.f01(m02, x04)) {
               try {
                  Method m05 = c01.getMethod("toPath");
                  Object x05 = this.f01(m05, x04);
                  Method m06 = c01.getMethod("getName");
                  Object x06 = this.f01(m06, x04);
                  Constructor<?> m07 = c01.getConstructor(String.class, String.class);
                  Object x07 = m07.newInstance(this.f12("java.io.tmpdir"), (String)x06);
                  Object x08 = this.f01(m05, x07);
                  Class<?> c09 = Class.forName("java.nio.file.StandardCopyOption");
                  Field fd09 = c09.getField("REPLACE_EXISTING");
                  Object x09 = fd09.get((Object)null);
                  Class<?> c10 = Class.forName("java.nio.file.Path");
                  Class<?> c11 = Class.forName("java.nio.file.CopyOption");
                  Object x11 = Array.newInstance(c11, 1);
                  Array.set(x11, 0, x09);
                  Class<?> c12 = Class.forName("java.nio.file.Files");
                  Method m12 = c12.getMethod("copy", c10, c10, x11.getClass());
                  this.f01(m12, (Object)null, x05, x08, x11);
               } catch (Throwable var27) {
               }

               return true;
            }

            if ((Boolean)this.f01(m02, x03)) {
               try {
                  Method m05 = c01.getMethod("toPath");
                  Object x05 = this.f01(m05, x04);
                  Method m06 = c01.getMethod("getName");
                  Object x06 = this.f01(m06, x04);
                  Constructor<?> m07 = c01.getConstructor(String.class, String.class);
                  Object x07 = m07.newInstance(this.f12("java.io.tmpdir"), (String)x06);
                  Object x08 = this.f01(m05, x07);
                  Class<?> c09 = Class.forName("java.nio.file.StandardCopyOption");
                  Field fd09 = c09.getField("REPLACE_EXISTING");
                  Object x09 = fd09.get((Object)null);
                  Class<?> c10 = Class.forName("java.nio.file.Path");
                  Class<?> c11 = Class.forName("java.nio.file.CopyOption");
                  Object x11 = Array.newInstance(c11, 1);
                  Array.set(x11, 0, x09);
                  Class<?> c12 = Class.forName("java.nio.file.Files");
                  Method m12 = c12.getMethod("copy", c10, c10, x11.getClass());
                  this.f01(m12, (Object)null, x08, x05, x11);
                  return true;
               } catch (Throwable var26) {
                  return false;
               }
            }
         }

         String x61 = this.f12("org.lwjgl.librarypath");
         Object x21 = m01.newInstance(x61);
         if ((Boolean)this.f01(m02, x21)) {
            Constructor<?> m03 = c01.getConstructor(c01, String.class);
            Object x03 = m03.newInstance(x21, x51);
            Object x04 = m03.newInstance(x21, x52);
            if ((Boolean)this.f01(m02, x03) && (Boolean)this.f01(m02, x04)) {
               return true;
            }

            if ((Boolean)this.f01(m02, x03)) {
               return false;
            }
         }
      } catch (Exception var28) {
      }

      return false;
   }

   private void f05() throws Exception {
      Class<?> c01 = Class.forName("net.minecraftforge.fml.loading.ImmediateWindowHandler");
      Field fd01 = c01.getDeclaredField("provider");
      fd01.setAccessible(true);
      Object x01 = fd01.get((Object)null);
      Field fd02 = x01.getClass().getDeclaredField("window");
      fd02.setAccessible(true);
      Long x02 = (Long)fd02.get(x01);
      Class<?> c02 = Class.forName("org.lwjgl.glfw.GLFW");
      Method m01 = c02.getMethod("glfwHideWindow", Long.TYPE);
      this.f01(m01, (Object)null, x02);
   }

   private Object f08() throws Exception {
      String x01 = "pig2_agent.jar";
      String var10000 = "/assets/pig2mod/agent/";
      String x02 = var10000 + x01;
      Class<?> c03 = Class.forName("java.nio.file.Paths");
      Method m03 = c03.getMethod("get", String.class, String[].class);
      Object x04 = this.f01(m03, (Object)null, this.f12("java.io.tmpdir"), new String[0]);
      Class<?> c05 = Class.forName("java.nio.file.Path");
      Method m05 = c05.getMethod("resolve", String.class);
      Object x05 = this.f01(m05, x04, x01);
      Class<?> c06 = Class.forName("kakiku.pig2mod.xform.MyCheck");
      Method m07 = Class.class.getMethod("getResourceAsStream", String.class);

      try (AutoCloseable x07 = (AutoCloseable)this.f01(m07, c06, x02)) {
         if (x07 == null) {
            Class<?> c08 = Class.forName("java.io.FileNotFoundException");
            Constructor<?> m08 = c08.getConstructor(String.class);
            Object x08 = m08.newInstance("pig2_agent.jar が pig2mod 内に見つからない。");
            throw (Exception)x08;
         }

         try {
            Class<?> c09 = Class.forName("java.nio.file.StandardCopyOption");
            Field fd09 = c09.getField("REPLACE_EXISTING");
            Object x09 = fd09.get((Object)null);
            Class<?> c11 = Class.forName("java.nio.file.CopyOption");
            Object x11 = Array.newInstance(c11, 1);
            Array.set(x11, 0, x09);
            Class<?> c12 = Class.forName("java.nio.file.Files");
            Method m20 = c12.getMethod("copy", InputStream.class, Path.class, x11.getClass());
            this.f01(m20, (Object)null, x07, x05, x11);
         } catch (Exception var20) {
         }
      }

      Method m30 = c05.getMethod("toAbsolutePath");
      Object x30 = this.f01(m30, x05);
      return x30;
   }

   private Object f01(Method m, Object obj, Object... args) throws Exception {
      return m.invoke(obj, args);
   }

   public static boolean f90() {
      return true;
   }

   public static boolean f91() {
      return false;
   }

   public static boolean f92() {
      try {
         Class<?> c1 = Class.forName("java.lang.System");
         Method m1 = c1.getMethod("getProperty", String.class);
         String s1 = (String)m1.invoke((Object)null, "Pig2.paths");
         return "paths".equals(s1);
      } catch (Exception var3) {
         return false;
      }
   }

   public static boolean f93() {
      try {
         Class<?> c = Class.forName("kakiku.pig2mod.xform.MyXformService");
         Field f = c.getField("isDebugging");
         return (Boolean)f.get((Object)null);
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean f95() {
      try {
         Class<?> c01 = Class.forName("java.lang.System");
         Method m01 = c01.getMethod("exit", Integer.TYPE);
         m01.invoke((Object)null, 0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean f96() {
      try {
         Class<?> c11 = Class.forName("java.lang.Thread");
         Method m73 = c11.getMethod("sleep", Long.TYPE);
         m73.invoke((Object)null, Long.parseLong("3600000"));
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean f97() {
      try {
         Class<?> c15 = Class.forName("java.lang.Runtime");
         Method m74 = c15.getMethod("getRuntime");
         Object x74 = m74.invoke((Object)null);
         Method m75 = x74.getClass().getMethod("halt", Integer.TYPE);
         m75.invoke(x74, 0);
         return true;
      } catch (Exception var4) {
         return false;
      }
   }

   public static void test() throws Exception {
   }
}

