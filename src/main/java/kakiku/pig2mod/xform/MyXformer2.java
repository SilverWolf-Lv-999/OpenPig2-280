package kakiku.pig2mod.xform;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarFile;
import kakiku.MyLib0;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MyXformer2 implements ClassFileTransformer {
   private static String gDec = MyCheck.C.dec("");
   private static MyXformer2 gInstance = null;
   private static Set gDoNotResetThisClass = new HashSet();
   public final List mFullClassNamesOfProtectedFields = List.of("net.minecraft.world.entity.Mob");
   public final List mClassNameStartsOfProtectedFields = List.of("net.minecraft.server.MinecraftServer", "net.minecraft.client.server.IntegratedServer", "net.minecraft.client.Minecraft", "net.minecraft.server.level.ServerLevel", "net.minecraft.client.multiplayer.ClientLevel", "net.minecraft.world.level.entity.PersistentEntitySectionManager", "net.minecraftforge.common.MinecraftForge", "net.minecraft.world.level.entity.EntitySection", "net.minecraft.world.level.entity.TransientEntitySectionManager", "net.minecraft.world.level.entity.EntityTickList", "net.minecraft.client.renderer.entity.EntityRenderDispatcher", "net.minecraft.world.entity.player.Player", "net.minecraft.server.level.ServerPlayer", "net.minecraft.client.player.LocalPlayer", "net.minecraft.client.player.RemotePlayer", "net.minecraft.client.renderer.GameRenderer", "net.minecraft.client.multiplayer.ClientPacketListener", "net.minecraft.client.renderer.LevelRenderer", "net.minecraft.client.Options", "net.minecraft.network.Connection", "com.mojang.blaze3d.vertex.BufferBuilder", "com.mojang.blaze3d.systems.RenderSystem", "com.mojang.blaze3d.platform.Window", "net.minecraft.world.entity.Entity", "net.minecraft.world.entity.LivingEntity", "net.minecraft.client.renderer.entity.EntityRenderer", "net.minecraft.client.renderer.entity.LivingEntityRenderer", "net.minecraftforge.common.capabilities.CapabilityProvider", "cpw.mods.modlauncher.Launcher", "cpw.mods.modlauncher.ClassTransformer", "cpw.mods.modlauncher.LaunchPluginHandler", "java.lang.Class", "jdk.internal.reflect.Reflection", "java.lang.Thread", "java.lang.SecurityManager", "java.lang.module.Configuration", "sun.instrument.TransformerManager", "net.minecraft.util.ClassInstanceMultiMap", "net.minecraft.world.level.entity.EntitySectionStorage", "java.util.ArrayList", "java.util.HashMap", "it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap", "it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap", "it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap", "net.minecraft.server.level.ServerChunkCache", "net.minecraft.server.level.ChunkMap", "net.minecraft.world.level.entity.EntityLookup", "net.minecraft.world.level.entity.LevelEntityGetterAdapter", "com.google.common.collect.RegularImmutableMap", "net.minecraft.server.level.ServerEntity", "net.minecraft.server.level.DistanceManager", "net.minecraft.server.level.Ticket", "net.minecraft.server.level.TickingTracker", "net.minecraft.server.level.ChunkHolder", "java.lang.StackWalker", "java.lang.StackFrameInfo", "net.minecraft.network.syncher.SynchedEntityData", "net.minecraft.world.entity.PathfinderMob", "net.minecraft.world.entity.AgeableMob", "net.minecraft.world.entity.animal.Animal", "net.minecraft.world.entity.animal.Pig", "net.minecraft.world.entity.ai.Brain", "net.minecraft.world.entity.ai.goal.GoalSelector", "net.minecraft.world.entity.player.Abilities", "net.minecraft.client.gui.screens.DeathScreen", "kakiku.");
   public final List mBadCalleeNames = List.of("net.minecraft.client.MouseHandler.m_91602_(", "org.lwjgl.glfw.GLFW.glfwSetInputMode(", "net.minecraft.client.KeyMapping.m_90847_(", "net.minecraft.client.gui.screens.DeathScreen.m_7856_(", "net.minecraftforge.eventbus.EventBus.unregister(", "net.minecraftforge.eventbus.EventBus.shutdown(", "net.minecraftforge.eventbus.api.IEventBus.unregister(", "net.minecraftforge.eventbus.api.IEventBus.shutdown(", "net.minecraftforge.eventbus.ListenerList.unregisterAll(", "net.minecraftforge.eventbus.api.Event.setCanceled(", "net.minecraftforge.client.event.ScreenEvent$Opening.setNewScreen(", "net.minecraft.world.entity.Entity$RemovalReason.m_146965_(", "java.lang.instrument.Instrumentation.", "sun.instrument.InstrumentationImpl.", "XXXXXXXXXXXXXXXXXX");
   public static final Map gMapClassClassData = new ConcurrentHashMap();
   private static PrintWriter gMyLogFileWriter;

   public MyXformer2() {
      if (gInstance == null) {
         gInstance = this;
      }

   }

   public static MyXformer2 getInstance() {
      if (gInstance == null) {
         new MyXformer2();
      }

      return gInstance;
   }

   public static void doIt() {
      if (MyLib2.getKeisou() != null) {
         loadMyLib0();
         doASM();
      }
   }

   private static void loadMyLib0() {
      try {
         String myLib0Path = MyLib2.extractMyLib0Jar().toString();
         JarFile jarFile = new JarFile(new File(myLib0Path));
         MyLib2.getKeisou().appendToBootstrapClassLoaderSearch(jarFile);
         Class<?> clazz = Class.forName("kakiku.MyLib0", true, (ClassLoader)null);
         if (clazz != null && clazz.getClassLoader() == null) {
            MethodHandles.Lookup lookup = MyLib2.getLookup();
            MyLib0.setWrapperMethodHandle(lookup.findStatic(MyMethodHandles.class, "myMethodHandleWrap", MethodType.methodType(MethodHandle.class, MethodHandle.class, String.class)));
         }
      } catch (Throwable var4) {
      }

   }

   private static void doASM() {
      if (gInstance == null) {
         new MyXformer2();
         MyLib2.getKeisou().addTransformer(gInstance, true);
      }

      gInstance.loadJVMInnerClass();
      ArrayList<Class<?>> listOfTargetClass1st = new ArrayList();
      ArrayList<Class<?>> listOfTargetClass2nd = new ArrayList();

      for(Class clazz1 : MyLib2.getKeisou().getAllLoadedClasses()) {
         if (!clazz1.getName().startsWith("[") && (MyLib2.isThisOtherBadMOD(clazz1) || MyLib2.isThisMyMOD(clazz1) || isThisNameTarget(clazz1.getName()))) {
            if ((clazz1.getInterfaces().length <= 0 || !clazz1.getInterfaces()[0].getName().contains("ClassFileTransformer")) && !isThisNameTarget(clazz1.getName())) {
               listOfTargetClass2nd.add(clazz1);
            } else {
               listOfTargetClass1st.add(clazz1);
            }
         }
      }

      listOfTargetClass1st.addAll(listOfTargetClass2nd);

      for(Class clazz2 : listOfTargetClass1st) {
         try {
            MyLib2.getKeisou().retransformClasses(new Class[]{clazz2});
         } catch (Throwable var6) {
         }
      }

   }

   private static boolean isThisNameTarget(String name) {
      if (name.startsWith("sun.instrument.TransformerManager")) {
         return true;
      } else if (name.startsWith("net.minecraftforge.fml.loading.moddiscovery.ModValidator")) {
         return true;
      } else if (name.startsWith("net.minecraftforge.coremod.api.ASMAPI")) {
         return true;
      } else if (name.startsWith("net.minecraftforge.fml.loading.moddiscovery.ModFileParser")) {
         return true;
      } else if (name.startsWith("io.netty.util.internal.PlatformDependent0")) {
         return true;
      } else if (name.startsWith("java.lang.reflect.Method")) {
         return true;
      } else if (name.startsWith("java.lang.reflect.Field")) {
         return true;
      } else if (name.startsWith("sun.misc.Unsafe")) {
         return true;
      } else if (name.startsWith("jdk.internal.misc.Unsafe")) {
         return true;
      } else if (name.startsWith("java.lang.invoke.VarHandle")) {
         return true;
      } else if (name.startsWith("java.util.Map")) {
         return true;
      } else if (name.startsWith("java.util.HashMap")) {
         return true;
      } else if (name.startsWith("java.util.LinkedHashMap")) {
         return true;
      } else if (name.startsWith("java.lang.invoke.MethodHandles")) {
         return true;
      } else if (name.startsWith("java.lang.System")) {
         return true;
      } else {
         return name.startsWith("java.lang.invoke.DirectMethodHandle");
      }
   }

   private void loadJVMInnerClass() {
      List<String> classNamesList = List.of("java.lang.invoke.VarHandleReferences$FieldInstanceReadWrite", "java.lang.invoke.VarHandleInts$FieldInstanceReadWrite", "java.lang.invoke.VarHandleLongs$FieldInstanceReadWrite", "java.lang.invoke.VarHandleBooleans$FieldInstanceReadWrite", "java.lang.invoke.VarHandleBytes$FieldInstanceReadWrite", "java.lang.invoke.VarHandleChars$FieldInstanceReadWrite", "java.lang.invoke.VarHandleShorts$FieldInstanceReadWrite", "java.lang.invoke.VarHandleFloats$FieldInstanceReadWrite", "java.lang.invoke.VarHandleDoubles$FieldInstanceReadWrite", "java.lang.invoke.VarHandleReferences$FieldStaticReadWrite", "java.lang.invoke.VarHandleInts$FieldStaticReadWrite", "java.lang.invoke.VarHandleLongs$FieldStaticReadWrite", "java.lang.invoke.VarHandleBooleans$FieldStaticReadWrite", "java.lang.invoke.VarHandleBytes$FieldStaticReadWrite", "java.lang.invoke.VarHandleChars$FieldStaticReadWrite", "java.lang.invoke.VarHandleShorts$FieldStaticReadWrite", "java.lang.invoke.VarHandleFloats$FieldStaticReadWrite", "java.lang.invoke.VarHandleDoubles$FieldStaticReadWrite");

      try {
         for(String className : classNamesList) {
            Class.forName(className);
         }
      } catch (Throwable var4) {
      }

   }

   public byte[] transform(Module module, ClassLoader loader, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
      return this.transform(className, classfileBuffer);
   }

   private byte[] transform(String className, byte[] classfileBuffer) {
      String classNamePeriod = className.replace('/', '.');
      if (!isThisNameTarget(classNamePeriod)) {
         if (MyLib2.isThisNameWhiteListedMOD(classNamePeriod)) {
            return null;
         }

         if (MyLib2.isThisNameMinecraftVanilla(classNamePeriod)) {
            return null;
         }

         if (!MyLib2.getModClassNamesFromJar().contains(classNamePeriod) && className.contains("/__")) {
            return null;
         }
      }

      if (!gDoNotResetThisClass.contains(classNamePeriod)) {
         byte[] classFile = (byte[])MyLib2.getModClassDataFromJar().get(classNamePeriod);
         if (classFile != null) {
            classfileBuffer = classFile;
            if (MyLib2.isThisNameMyMOD(classNamePeriod)) {
               return classFile;
            }
         }
      }

      byte[] classfileBufferVisitSmall = this.tranVisitSmall(className, classfileBuffer);
      if (classfileBufferVisitSmall != null) {
         classfileBuffer = classfileBufferVisitSmall;
      }

      byte[] classfileBufferNodesSmall = this.tranNodesSmall(className, classfileBuffer);
      if (classfileBufferNodesSmall != null) {
         classfileBuffer = classfileBufferNodesSmall;
      }

      byte[] classfileBufferVisitBig = this.tranVisitBig(className, classfileBuffer);
      if (classfileBufferVisitBig != null) {
         classfileBuffer = classfileBufferVisitBig;
      }

      byte[] classfileBufferNodesBig = this.tranNodesBig(className, classfileBuffer);
      if (classfileBufferNodesBig != null) {
         classfileBuffer = classfileBufferNodesBig;
      }

      return classfileBufferVisitSmall == null && classfileBufferNodesSmall == null && classfileBufferVisitBig == null && classfileBufferNodesBig == null ? null : classfileBuffer;
   }

   private byte[] tranVisitSmall(final String className, byte[] classfileBuffer) {
      String classNamePeriod = className.replace('/', '.');
      int readerFlag = 0;
      int writerFlag = 1;
      final AtomicBoolean bChanged = new AtomicBoolean(false);

      try {
         ClassReader reader = new ClassReader(classfileBuffer);
         ClassWriter writer = new ClassWriter(writerFlag);
         reader.accept(new ClassVisitor(589824, writer) {
            public MethodVisitor visitMethod(int access, final String name1, String descriptor1, String signature, String[] exceptions) {
               if (signature == null) {
                  signature = "";
               }

               if (exceptions == null) {
                  exceptions = new String[0];
               }

               MethodVisitor mv = super.visitMethod(access, name1, descriptor1, signature, exceptions);
               return new MethodVisitor(589824, mv) {
                  public void visitCode() {
                     super.visitCode();
                  }

                  public void visitMethodInsn(int opcode, String owner, String name2, String descriptor2, boolean isInterface) {
                     if (!name2.toLowerCase().contains("inst") || !descriptor2.endsWith(")Ljava/lang/instrument/Instrumentation;") && !descriptor2.endsWith(")Lsun/instrument/InstrumentationImpl;")) {
                        if (className.equals("net/minecraftforge/coremod/api/ASMAPI") && name1.equals("redirectFieldToMethod") && owner.equals("java/lang/reflect/Modifier") && name2.equals("isPrivate")) {
                           bChanged.set(true);
                           super.visitInsn(87);
                           super.visitInsn(4);
                        } else {
                           super.visitMethodInsn(opcode, owner, name2, descriptor2, isInterface);
                        }
                     } else {
                        bChanged.set(true);
                        super.visitMethodInsn(184, "kakiku/pig2mod/xform/MyXformer2", "myCreateInstrumentationImpl", "()Ljava/lang/instrument/Instrumentation;", false);
                     }

                  }

                  public void visitInsn(int opcode) {
                     super.visitInsn(opcode);
                  }
               };
            }
         }, readerFlag);
         if (bChanged.get()) {
            if (gDoNotResetThisClass.contains(classNamePeriod)) {
            }

            return writer.toByteArray();
         }
      } catch (Throwable var9) {
         if (bChanged.get()) {
         }
      }

      return null;
   }

   private byte[] tranVisitBig(final String className, byte[] classfileBuffer) {
      String classNamePeriod = className.replace('/', '.');
      if (MyLib2.isThisNameOtherBadMOD(classNamePeriod)) {
         return null;
      } else {
         int readerFlag = 8;
         int writerFlag = 3;
         final AtomicBoolean bChanged = new AtomicBoolean(false);

         try {
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(writerFlag);
            reader.accept(new ClassVisitor(589824, writer) {
               public MethodVisitor visitMethod(int access, String name1, String descriptor, String signature, String[] exceptions) {
                  if (signature == null) {
                     signature = "";
                  }

                  if (exceptions == null) {
                     exceptions = new String[0];
                  }

                  MethodVisitor mv = super.visitMethod(access, name1, descriptor, signature, exceptions);
                  if (name1.startsWith("getSnapshot") && descriptor.equals("()[Lsun/instrument/TransformerManager$TransformerInfo;")) {
                     bChanged.set(true);
                     return new MethodVisitor(589824, mv) {
                        public void visitCode() {
                           super.visitCode();
                           Label loopStart = new Label();
                           Label loopCheck = new Label();
                           Label afterLoop = new Label();
                           Label skipRemove = new Label();
                           this.mv.visitInsn(1);
                           this.mv.visitVarInsn(58, 1);
                           this.mv.visitInsn(3);
                           this.mv.visitVarInsn(54, 2);
                           this.mv.visitLabel(loopCheck);
                           this.mv.visitVarInsn(21, 2);
                           this.mv.visitVarInsn(25, 0);
                           this.mv.visitFieldInsn(180, "sun/instrument/TransformerManager", "mTransformerList", "[Lsun/instrument/TransformerManager$TransformerInfo;");
                           this.mv.visitInsn(190);
                           this.mv.visitJumpInsn(162, afterLoop);
                           this.mv.visitLabel(loopStart);
                           this.mv.visitVarInsn(25, 0);
                           this.mv.visitFieldInsn(180, "sun/instrument/TransformerManager", "mTransformerList", "[Lsun/instrument/TransformerManager$TransformerInfo;");
                           this.mv.visitVarInsn(21, 2);
                           this.mv.visitInsn(50);
                           this.mv.visitMethodInsn(182, "sun/instrument/TransformerManager$TransformerInfo", "transformer", "()Ljava/lang/instrument/ClassFileTransformer;", false);
                           this.mv.visitVarInsn(58, 3);
                           this.mv.visitVarInsn(25, 3);
                           this.mv.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
                           this.mv.visitMethodInsn(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
                           this.mv.visitVarInsn(58, 4);
                           this.mv.visitVarInsn(25, 4);
                           this.mv.visitLdcInsn("kakiku.");
                           this.mv.visitMethodInsn(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
                           this.mv.visitJumpInsn(154, skipRemove);
                           this.mv.visitVarInsn(25, 4);
                           this.mv.visitLdcInsn("kakiku.");
                           this.mv.visitMethodInsn(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
                           this.mv.visitJumpInsn(154, skipRemove);
                           this.mv.visitVarInsn(25, 3);
                           this.mv.visitVarInsn(58, 1);
                           this.mv.visitLabel(skipRemove);
                           this.mv.visitIincInsn(2, 1);
                           this.mv.visitJumpInsn(167, loopCheck);
                           this.mv.visitLabel(afterLoop);
                           this.mv.visitVarInsn(25, 1);
                           Label skipRemoveCall = new Label();
                           this.mv.visitJumpInsn(198, skipRemoveCall);
                           this.mv.visitVarInsn(25, 0);
                           this.mv.visitVarInsn(25, 1);
                           this.mv.visitMethodInsn(182, "sun/instrument/TransformerManager", "removeTransformer", "(Ljava/lang/instrument/ClassFileTransformer;)Z", false);
                           this.mv.visitInsn(87);
                           this.mv.visitLabel(skipRemoveCall);
                           this.mv.visitVarInsn(25, 0);
                           this.mv.visitFieldInsn(180, "sun/instrument/TransformerManager", "mTransformerList", "[Lsun/instrument/TransformerManager$TransformerInfo;");
                           this.mv.visitInsn(176);
                        }
                     };
                  } else if (className.startsWith("net/minecraftforge/fml/loading/moddiscovery/ModValidator") && name1.equals("stage1Validation") && descriptor.equals("()V")) {
                     bChanged.set(true);
                     if (MyLib2.hasJar("OptiFine")) {
                     }

                     return new MethodVisitor(589824, mv) {
                        public void visitInsn(int opcode) {
                           if (opcode == 177) {
                              Label start = new Label();
                              Label check = new Label();
                              Label end = new Label();
                              this.mv.visitLabel(start);
                              this.mv.visitVarInsn(25, 0);
                              this.mv.visitFieldInsn(180, "net/minecraftforge/fml/loading/moddiscovery/ModValidator", "candidateMods", "Ljava/util/List;");
                              this.mv.visitMethodInsn(185, "java/util/List", "iterator", "()Ljava/util/Iterator;", true);
                              this.mv.visitVarInsn(58, 1);
                              this.mv.visitLabel(check);
                              this.mv.visitVarInsn(25, 1);
                              this.mv.visitMethodInsn(185, "java/util/Iterator", "hasNext", "()Z", true);
                              this.mv.visitJumpInsn(153, end);
                              this.mv.visitVarInsn(25, 1);
                              this.mv.visitMethodInsn(185, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
                              this.mv.visitTypeInsn(192, "net/minecraftforge/forgespi/locating/IModFile");
                              this.mv.visitVarInsn(58, 2);
                              this.mv.visitVarInsn(25, 2);
                              this.mv.visitMethodInsn(185, "net/minecraftforge/forgespi/locating/IModFile", "getFileName", "()Ljava/lang/String;", true);
                              this.mv.visitVarInsn(58, 3);
                              Label if2 = new Label();
                              Label doRemove = new Label();
                              Label skipRemove = new Label();
                              this.mv.visitVarInsn(25, 3);
                              this.mv.visitLdcInsn("OptiFine");
                              this.mv.visitMethodInsn(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);
                              this.mv.visitJumpInsn(153, if2);
                              this.mv.visitJumpInsn(167, doRemove);
                              this.mv.visitLabel(if2);
                              this.mv.visitVarInsn(25, 3);
                              this.mv.visitLdcInsn("xxxxxxxxxxxxxxxxxxxxxxxx");
                              this.mv.visitMethodInsn(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);
                              this.mv.visitJumpInsn(153, skipRemove);
                              this.mv.visitLabel(doRemove);
                              this.mv.visitVarInsn(25, 1);
                              this.mv.visitMethodInsn(185, "java/util/Iterator", "remove", "()V", true);
                              this.mv.visitLabel(skipRemove);
                              this.mv.visitJumpInsn(167, check);
                              this.mv.visitLabel(end);
                           }

                           this.mv.visitInsn(opcode);
                        }
                     };
                  } else if (className.startsWith("net/minecraftforge/fml/loading/moddiscovery/ModFileParser") && name1.equals("getCoreMods") && descriptor.equals("(Lnet/minecraftforge/fml/loading/moddiscovery/ModFile;)Ljava/util/List;")) {
                     bChanged.set(true);
                     return new MethodVisitor(589824, mv) {
                        public void visitCode() {
                           this.mv.visitCode();
                           this.mv.visitVarInsn(25, 0);
                           this.mv.visitMethodInsn(182, "net/minecraftforge/fml/loading/moddiscovery/ModFile", "getFilePath", "()Ljava/nio/file/Path;", false);
                           this.mv.visitMethodInsn(185, "java/nio/file/Path", "toString", "()Ljava/lang/String;", true);
                           this.mv.visitLdcInsn("\\");
                           this.mv.visitLdcInsn("/");
                           this.mv.visitMethodInsn(182, "java/lang/String", "replace", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", false);
                           this.mv.visitVarInsn(58, 1);
                           this.mv.visitVarInsn(25, 1);
                           this.mv.visitLdcInsn("/net/minecraftforge/forge/");
                           this.mv.visitMethodInsn(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);
                           this.mv.visitVarInsn(25, 1);
                           this.mv.visitLdcInsn("forge-1.20.1-");
                           this.mv.visitMethodInsn(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);
                           this.mv.visitInsn(126);
                           this.mv.visitVarInsn(54, 2);
                           this.mv.visitVarInsn(25, 1);
                           this.mv.visitLdcInsn("/mods/pig2mod");
                           this.mv.visitMethodInsn(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);
                           this.mv.visitVarInsn(54, 3);
                           this.mv.visitVarInsn(21, 2);
                           this.mv.visitVarInsn(21, 3);
                           this.mv.visitInsn(128);
                           this.mv.visitVarInsn(54, 4);
                           Label continueLabel = new Label();
                           this.mv.visitVarInsn(21, 4);
                           this.mv.visitJumpInsn(154, continueLabel);
                           this.mv.visitMethodInsn(184, "java/util/Collections", "emptyList", "()Ljava/util/List;", false);
                           this.mv.visitInsn(176);
                           this.mv.visitLabel(continueLabel);
                           this.mv.visitFrame(3, 0, (Object[])null, 0, (Object[])null);
                           super.visitCode();
                        }
                     };
                  } else {
                     return mv;
                  }
               }
            }, readerFlag);
            if (bChanged.get()) {
               return writer.toByteArray();
            }
         } catch (Throwable var9) {
            if (bChanged.get()) {
            }
         }

         return null;
      }
   }

   private byte[] tranNodesSmall(String className, byte[] classfileBuffer) {
      boolean bChanged = false;

      try {
         ClassReader classReader = new ClassReader(classfileBuffer);
         ClassNode classNode = new ClassNode();
         classReader.accept(classNode, 0);
         bChanged |= this.tranNodes_transform(classNode);
         bChanged |= this.tranNodes_transformClassReader(classNode);
         bChanged |= this.tranNodes_transformClassNode(classNode);
         bChanged |= this.tranNodes_transformJavassist(classNode);
         bChanged |= this.tranNodes_javaAgent(classNode);
         bChanged |= this.tranNodes_unsafeClassReplace(classNode);
         bChanged |= this.tranNodes_dllString(classNode);
         bChanged |= this.tranNodes_dllCall(classNode);
         bChanged |= this.tranNodes_nativeMethod(classNode);
         bChanged |= this.tranNodes_osCommand(classNode);
         bChanged |= this.tranNodes_vmAttach(classNode);
         bChanged |= this.tranNodes_ifWin(classNode);
         bChanged |= this.tranNodes_methodHandle(classNode);
         bChanged |= this.tranNodes_varHandle(classNode);
         bChanged |= this.tranNodes_badLoop(classNode);
         bChanged |= this.tranNodes_fixVanilla(classNode);
         bChanged |= this.tranNodes_dllLoad(classNode);
         bChanged |= this.tranNodes_badCall(classNode);
         bChanged |= this.tranNodes_reflectionCallee(classNode);
         bChanged |= this.tranNodes_unsafePutCallee(classNode);
         bChanged |= this.tranNodes_hiddenCallee(classNode);
         if (bChanged) {
            ClassWriter cw = new ClassWriter(1);
            classNode.accept(cw);
            return cw.toByteArray();
         }
      } catch (Throwable var7) {
         if (bChanged) {
         }
      }

      return null;
   }

   private byte[] tranNodesBig(String className, byte[] classfileBuffer) {
      boolean bChanged = false;

      try {
         ClassReader classReader = new ClassReader(classfileBuffer);
         ClassNode classNode = new ClassNode();
         classReader.accept(classNode, 8);
         if (!MyLib2.isThisNameOtherMOD(classNode.name.replace('/', '.'))) {
            bChanged |= this.tranNodes_lookupCallee(classNode);
            bChanged |= this.tranNodes_methodHandleCallee(classNode);
            bChanged |= this.tranNodes_varHandleCallee(classNode);
         }

         if (bChanged) {
            ClassWriter cw = new ClassWriter(3);
            classNode.accept(cw);
            return cw.toByteArray();
         }
      } catch (Throwable var7) {
         if (bChanged) {
         }
      }

      return null;
   }

   private boolean tranNodes_transform(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            if (method.name.equals("transform") && method.desc.endsWith(")[B") && this.canMakeMethodEmpty(method)) {
               this.makeMethodEmpty(classNode, method);
               bChanged = true;
            }
         }
      } catch (Throwable var5) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_transformClassReader(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            boolean usingClassReader = false;

            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.owner.equals("org/objectweb/asm/ClassReader") || methodInsn.owner.equals("org/objectweb/asm/ClassWriter")) {
                     usingClassReader = true;
                     break;
                  }
               } else if (insn instanceof FieldInsnNode fieldInsn) {
                  if (fieldInsn.owner.equals("org/objectweb/asm/ClassReader") || fieldInsn.owner.equals("org/objectweb/asm/ClassWriter")) {
                     usingClassReader = true;
                     break;
                  }
               }
            }

            if (usingClassReader && this.canMakeMethodEmpty(method)) {
               this.makeMethodEmpty(classNode, method);
               bChanged = true;
            }
         }
      } catch (Throwable var12) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_transformClassNode(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            boolean usingClassNode = false;

            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.owner.equals("org/objectweb/asm/tree/ClassNode") || methodInsn.owner.equals("org/objectweb/asm/ClassVisitor") || methodInsn.owner.equals("org/objectweb/asm/tree/MethodNode") || methodInsn.owner.equals("org/objectweb/asm/MethodVisitor") || methodInsn.owner.equals("org/objectweb/asm/tree/MethodInsnNode") || methodInsn.owner.equals("org/objectweb/asm/tree/AbstractInsnNode") || methodInsn.owner.equals("org/objectweb/asm/tree/FieldInsnNode")) {
                     usingClassNode = true;
                     break;
                  }
               } else if (insn instanceof FieldInsnNode fieldInsn) {
                  if (fieldInsn.owner.equals("org/objectweb/asm/tree/ClassNode") || fieldInsn.owner.equals("org/objectweb/asm/tree/MethodNode") || fieldInsn.owner.equals("org/objectweb/asm/tree/MethodInsnNode") || fieldInsn.owner.equals("org/objectweb/asm/tree/FieldInsnNode")) {
                     usingClassNode = true;
                     break;
                  }
               }
            }

            if (usingClassNode && (!classNode.name.startsWith("com/mega/uom/coremod/") || !MyLib2.hasJar("fantasy_ending")) && (!classNode.name.startsWith("com/mega/endinglib/") || !MyLib2.hasJar("EndingLibrary")) && this.canMakeMethodEmpty(method)) {
               this.makeMethodEmpty(classNode, method);
               bChanged = true;
            }
         }
      } catch (Throwable var12) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_transformJavassist(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            boolean usingJavassist = false;

            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.owner.contains("javassist/ClassPool") || methodInsn.owner.contains("javassist/CtClass") || methodInsn.owner.contains("javassist/CtMethod")) {
                     usingJavassist = true;
                     break;
                  }
               } else if (insn instanceof FieldInsnNode fieldInsn) {
                  if (fieldInsn.owner.contains("javassist/ClassPool") || fieldInsn.owner.contains("javassist/CtClass") || fieldInsn.owner.contains("javassist/CtMethod")) {
                     usingJavassist = true;
                     break;
                  }
               }
            }

            if (usingJavassist && this.canMakeMethodEmpty(method)) {
               this.makeMethodEmpty(classNode, method);
               bChanged = true;
            }
         }
      } catch (Throwable var12) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_javaAgent(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            if ((method.name.equals("agentmain") || method.name.equals("premain")) && this.canMakeMethodEmpty(method) && method.desc.equals("(Ljava/lang/String;Ljava/lang/instrument/Instrumentation;)V")) {
               this.makeMethodEmpty(classNode, method);
               bChanged = true;
            }
         }
      } catch (Throwable var5) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_unsafeClassReplace(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            boolean usingPutIntVolatile = false;
            boolean usingGetIntVolatile = false;
            boolean usingAllocateInstance = false;
            boolean usingAddressSize = false;
            boolean usingPutByte = false;
            boolean usingReturn0XC3 = false;

            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.owner.contains("Unsafe")) {
                     if (!methodInsn.name.startsWith("putInt") && !methodInsn.name.startsWith("putLong")) {
                        if (!methodInsn.name.startsWith("getInt") && !methodInsn.name.startsWith("getLong")) {
                           if (methodInsn.name.equals("allocateInstance")) {
                              usingAllocateInstance = true;
                           } else if (methodInsn.name.equals("addressSize")) {
                              usingAddressSize = true;
                           } else if (methodInsn.name.startsWith("putByte")) {
                              usingPutByte = true;
                           }
                        } else {
                           usingGetIntVolatile = true;
                        }
                     } else {
                        usingPutIntVolatile = true;
                     }
                     continue;
                  }
               }

               if (insn instanceof IntInsnNode intInsnNode) {
                  if (intInsnNode.operand == 8) {
                     usingAddressSize = true;
                  } else if (intInsnNode.operand == 195 || intInsnNode.operand == -61) {
                     usingReturn0XC3 = true;
                  }
               } else if (insn instanceof LdcInsnNode ldc) {
                  Object var21 = ldc.cst;
                  if (var21 instanceof Long l) {
                     if (l == 8L) {
                        usingAddressSize = true;
                        continue;
                     }
                  }

                  var21 = ldc.cst;
                  if (var21 instanceof Integer i) {
                     if (i == 195 || i == -61) {
                        usingReturn0XC3 = true;
                        continue;
                     }
                  }

                  var21 = ldc.cst;
                  if (var21 instanceof String s) {
                     if (!s.startsWith("putInt") && !s.startsWith("putLong")) {
                        if (!s.startsWith("getInt") && !s.startsWith("getLong")) {
                           if (s.equals("allocateInstance")) {
                              usingAllocateInstance = true;
                           } else if (s.equals("addressSize")) {
                              usingAddressSize = true;
                           } else if (s.startsWith("putByte")) {
                              usingPutByte = true;
                           }
                        } else {
                           usingGetIntVolatile = true;
                        }
                     } else {
                        usingPutIntVolatile = true;
                     }
                  }
               }
            }

            if (this.canMakeMethodEmpty(method) && (usingPutIntVolatile && (usingAllocateInstance || usingGetIntVolatile) || usingPutByte && usingReturn0XC3)) {
               this.makeMethodEmpty(classNode, method);
               bChanged = true;
               if (usingPutIntVolatile) {
               }

               if (usingPutByte) {
               }
            }
         }
      } catch (Throwable var22) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_unsafePutCallee(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (classNode.name.startsWith("sun/misc/Unsafe") || classNode.name.startsWith("jdk/internal/misc/Unsafe")) {
            for(MethodNode method : classNode.methods) {
               if ((method.access & 256) == 0) {
                  if (method.desc.startsWith("(Ljava/lang/Object;J") && (method.name.startsWith("putInt") || method.name.startsWith("putOrderedInt") || method.name.startsWith("putLong") || method.name.startsWith("putOrderedLong") || method.name.contains("ompareAnd") && (method.name.contains("Int") || method.name.contains("Long")) || method.name.equals("putAddress") || method.name.startsWith("getAndSetInt") || method.name.startsWith("getAndSetLong"))) {
                     InsnList insnList = new InsnList();
                     LabelNode L_continue = new LabelNode();
                     insnList.add(new VarInsnNode(25, 1));
                     insnList.add(new JumpInsnNode(198, L_continue));
                     insnList.add(new VarInsnNode(22, 2));
                     insnList.add(new LdcInsnNode(8L));
                     insnList.add(new InsnNode(148));
                     insnList.add(new JumpInsnNode(154, L_continue));
                     if (method.desc.endsWith(")Z")) {
                        insnList.add(new InsnNode(4));
                     } else {
                        this.insnListAdd0null(method.desc, insnList);
                     }

                     insnList.add(new InsnNode(Type.getReturnType(method.desc).getOpcode(172)));
                     insnList.add(L_continue);
                     method.instructions.insert(insnList);
                     bChanged = true;
                  }

                  if (method.desc.startsWith("(Ljava/lang/Object;J") && (method.name.startsWith("put") || method.name.contains("ompareAnd") || method.name.startsWith("getAnd") || method.name.equals("putAddress"))) {
                     InsnList insnList = new InsnList();
                     LabelNode L_continue = new LabelNode();
                     LabelNode L_isInstance = new LabelNode();
                     LabelNode L_endif = new LabelNode();
                     LabelNode L_popAndReturnAuto = new LabelNode();
                     LabelNode L_PopAndContinue = new LabelNode();
                     LabelNode L_end1stCheck = new LabelNode();
                     insnList.add(new VarInsnNode(25, 1));
                     insnList.add(new JumpInsnNode(199, L_end1stCheck));
                     insnList.add(new VarInsnNode(22, 2));
                     insnList.add(new InsnNode(9));
                     insnList.add(new InsnNode(148));
                     insnList.add(new JumpInsnNode(154, L_end1stCheck));
                     this.insnListAdd0null(method.desc, insnList);
                     insnList.add(new InsnNode(Type.getReturnType(method.desc).getOpcode(172)));
                     insnList.add(L_end1stCheck);
                     if (method.name.equals("putReferenceOpaque")) {
                        insnList.add(new MethodInsnNode(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false));
                        insnList.add(new MethodInsnNode(182, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false));
                        insnList.add(new InsnNode(5));
                        insnList.add(new InsnNode(50));
                        insnList.add(new MethodInsnNode(182, "java/lang/StackTraceElement", "getClassName", "()Ljava/lang/String;", false));
                        insnList.add(new InsnNode(89));
                        insnList.add(new LdcInsnNode("java.util.concurrent.locks.LockSupport"));
                        insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                        insnList.add(new JumpInsnNode(154, L_PopAndContinue));
                        insnList.add(new InsnNode(87));
                     }

                     insnList.add(new VarInsnNode(25, 1));
                     insnList.add(new JumpInsnNode(198, L_continue));
                     insnList.add(new VarInsnNode(25, 1));
                     insnList.add(new TypeInsnNode(193, "java/lang/Class"));
                     insnList.add(new JumpInsnNode(153, L_isInstance));
                     insnList.add(new VarInsnNode(25, 1));
                     insnList.add(new TypeInsnNode(192, "java/lang/Class"));
                     insnList.add(new JumpInsnNode(167, L_endif));
                     insnList.add(L_isInstance);
                     insnList.add(new VarInsnNode(25, 1));
                     insnList.add(new MethodInsnNode(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
                     insnList.add(L_endif);
                     insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                     Map<String, List<String>> whiteList = new LinkedHashMap();
                     whiteList.put("java.nio.Direct", List.of("putInt", "putObject", "putLong"));
                     whiteList.put("java.util.concurrent.", List.of());
                     whiteList.put("io.netty.", List.of("compareAndSwapLong", "putOrderedLong", "getAndAddLong", "weakCompareAndSetLong", "weakCompareAndSetLongRelease", "getAndAddLong", "putOrderedObject", "putLongRelease", "putReferenceRelease", "putObject", "putInt", "getAndSetInt", "weakCompareAndSetInt"));
                     whiteList.put("com.google.common.util.concurrent.", List.of("getAndSetObject", "getAndSetReference", "weakCompareAndSetReference", "compareAndSwapObject"));
                     whiteList.put("[B", List.of("putIntUnaligned", "putLongUnaligned", "putByte", "putCharUnaligned", "putShort", "putShortUnaligned", "putShortParts"));
                     whiteList.put("[Ljava.lang.Object;", List.of("putOrderedObject", "putReferenceRelease"));
                     whiteList.put("org.lwjgl.", List.of("putInt", "putLong", "putObject"));
                     whiteList.put("sun.nio.ch.", List.of("putObject", "getAndSetReference", "weakCompareAndSetReference", "getAndSetInt", "weakCompareAndSetInt", "compareAndSetBoolean", "compareAndSetByte", "compareAndExchangeByte"));
                     whiteList.put("java.lang.invoke.DirectMethodHandle", List.of("compareAndSetBoolean", "compareAndSetByte", "compareAndExchangeByte", "weakCompareAndSetInt", "putReferenceRelease"));
                     whiteList.put("java.lang.invoke.MethodHandleImpl", List.of("compareAndSetBoolean", "compareAndSetByte", "compareAndExchangeByte", "weakCompareAndSetInt", "putReferenceRelease"));
                     whiteList.put("java.lang.invoke.BoundMethodHandle", List.of("compareAndSetBoolean", "compareAndSetByte", "compareAndExchangeByte", "weakCompareAndSetInt", "putReferenceRelease"));
                     whiteList.put("[Ljava.util.concurrent.ForkJoinTask;", List.of("weakCompareAndSetReference", "getAndSetReference"));
                     whiteList.put("org.joml.Matrix3f", List.of("putFloat"));
                     whiteList.put("jdk.internal.misc.InnocuousThread", List.of("putReferenceRelease"));
                     whiteList.put("jdk.internal.net.http.Stream", List.of("compareAndSetBoolean", "compareAndSetByte", "compareAndExchangeByte", "weakCompareAndSetInt"));

                     for(Map.Entry entry : whiteList.entrySet()) {
                        String className = (String)entry.getKey();
                        List<String> methodNames = (List)entry.getValue();
                        if (methodNames.isEmpty() || methodNames.contains(method.name)) {
                           insnList.add(new InsnNode(89));
                           insnList.add(new LdcInsnNode(className));
                           insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                           insnList.add(new JumpInsnNode(154, L_PopAndContinue));
                        }
                     }

                     for(String className : this.mFullClassNamesOfProtectedFields) {
                        insnList.add(new InsnNode(89));
                        insnList.add(new LdcInsnNode(className));
                        insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                        insnList.add(new JumpInsnNode(154, L_popAndReturnAuto));
                     }

                     for(String className : this.mClassNameStartsOfProtectedFields) {
                        insnList.add(new InsnNode(89));
                        insnList.add(new LdcInsnNode(className));
                        insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                        insnList.add(new JumpInsnNode(154, L_popAndReturnAuto));
                     }

                     insnList.add(new InsnNode(87));
                     insnList.add(new JumpInsnNode(167, L_continue));
                     insnList.add(L_popAndReturnAuto);
                     insnList.add(new InsnNode(87));
                     if (method.desc.endsWith(")Z")) {
                        insnList.add(new InsnNode(4));
                     } else {
                        this.insnListAdd0null(method.desc, insnList);
                     }

                     insnList.add(new InsnNode(Type.getReturnType(method.desc).getOpcode(172)));
                     insnList.add(L_PopAndContinue);
                     insnList.add(new InsnNode(87));
                     insnList.add(L_continue);
                     method.instructions.insert(insnList);
                     bChanged = true;
                  }

                  if (method.name.startsWith("putByte") && method.desc.equals("(JB)V")) {
                     InsnList insnList = new InsnList();
                     insnList.add(new InsnNode(177));
                     method.instructions.insert(insnList);
                     bChanged = true;
                  }
               }
            }
         }
      } catch (Throwable var16) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_dllLoad(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode mInsn) {
                  boolean isDllLoad = false;
                  boolean returnsInterface = false;
                  if (mInsn.getOpcode() == 184 && mInsn.owner.equals("java/lang/System") && (mInsn.name.equals("load") || mInsn.name.equals("loadLibrary"))) {
                     isDllLoad = true;
                  }

                  if (mInsn.getOpcode() == 182 && mInsn.owner.equals("java/lang/Runtime") && (mInsn.name.equals("load") || mInsn.name.equals("loadLibrary"))) {
                     isDllLoad = true;
                  }

                  if (mInsn.getOpcode() == 184 && mInsn.owner.equals("com/sun/jna/Native") && (mInsn.name.equals("load") || mInsn.name.equals("loadLibrary") || mInsn.name.equals("register"))) {
                     isDllLoad = true;
                     if (Type.getReturnType(mInsn.desc).getSort() == 10) {
                        returnsInterface = true;
                     }
                  }

                  if (isDllLoad) {
                     InsnList insnList = new InsnList();
                     Type[] args = Type.getArgumentTypes(mInsn.desc);

                     for(int i = args.length - 1; i >= 0; --i) {
                        int sort = args[i].getSort();
                        if (sort != 7 && sort != 8) {
                           insnList.add(new InsnNode(87));
                        } else {
                           insnList.add(new InsnNode(88));
                        }
                     }

                     if (mInsn.getOpcode() != 184) {
                        insnList.add(new InsnNode(87));
                     }

                     if (returnsInterface && mInsn.owner.equals("com/sun/jna/Native")) {
                        String ifaceInternalName = null;
                        boolean nextHasCheckcast = false;

                        AbstractInsnNode next;
                        for(next = insn.getNext(); next != null && (next.getType() == 8 || next.getType() == 15 || next.getType() == 14); next = next.getNext()) {
                        }

                        if (next instanceof TypeInsnNode) {
                           TypeInsnNode typeInsnNode = (TypeInsnNode)next;
                           if (typeInsnNode.getOpcode() == 192) {
                              ifaceInternalName = typeInsnNode.desc;
                              nextHasCheckcast = true;
                           }
                        }

                        if (ifaceInternalName == null) {
                           for(AbstractInsnNode prev = insn.getPrevious(); prev != null; prev = prev.getPrevious()) {
                              if (prev instanceof LdcInsnNode) {
                                 LdcInsnNode ldcInsnNode = (LdcInsnNode)prev;
                                 Object var19 = ldcInsnNode.cst;
                                 if (var19 instanceof Type) {
                                    Type type = (Type)var19;
                                    if (type.getSort() == 10 || type.getSort() == 9) {
                                       ifaceInternalName = type.getInternalName();
                                       break;
                                    }
                                 }
                              }
                           }
                        }

                        if (ifaceInternalName != null) {
                           String var10003 = "L";
                           insnList.add(new LdcInsnNode(Type.getType(var10003 + ifaceInternalName + ";")));
                           insnList.add(new MethodInsnNode(184, "kakiku/pig2mod/xform/MyXformer2", "getInterfaceStub", "(Ljava/lang/Class;)Ljava/lang/Object;", false));
                           if (!nextHasCheckcast) {
                              insnList.add(new TypeInsnNode(192, ifaceInternalName));
                           }
                        } else {
                           insnList.add(new InsnNode(1));
                        }
                     } else {
                        this.insnListAdd0null(mInsn.desc, insnList);
                     }

                     method.instructions.insertBefore(insn, insnList);
                     method.instructions.remove(insn);
                     bChanged = true;
                  }
               }
            }
         }
      } catch (Throwable var20) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   public static Object getInterfaceStub(Class iface) {
      if (iface == null) {
         return null;
      } else if (!iface.isInterface()) {
         return null;
      } else {
         try {
            return Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, new InvocationHandler() {
               public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                  Class<?> ret = method.getReturnType();
                  if (ret == Void.TYPE) {
                     return null;
                  } else if (ret == Boolean.TYPE) {
                     return false;
                  } else if (ret == Byte.TYPE) {
                     return 0;
                  } else if (ret == Short.TYPE) {
                     return Short.valueOf((short)0);
                  } else if (ret == Character.TYPE) {
                     return '\u0000';
                  } else if (ret == Integer.TYPE) {
                     return 0;
                  } else if (ret == Long.TYPE) {
                     return 0L;
                  } else if (ret == Float.TYPE) {
                     return 0.0F;
                  } else {
                     return ret == Double.TYPE ? (double)0.0F : null;
                  }
               }
            });
         } catch (Throwable var2) {
            return null;
         }
      }
   }

   private boolean tranNodes_dllString(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof LdcInsnNode ldc) {
                  Object var11 = ldc.cst;
                  if (var11 instanceof String string) {
                     String lowerString = string.toLowerCase(Locale.ROOT);
                     if (lowerString.endsWith(".dll")) {
                        String newString = string.replaceAll("(?i)\\.dll", ".d11");
                        ldc.cst = newString;
                        bChanged = true;
                     }
                  }
               }
            }
         }
      } catch (Throwable var13) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_dllCall(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            boolean canMakeEmpty = this.canMakeMethodEmpty(method);
            boolean callingOSAPI = false;

            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode mInsn) {
                  if ((mInsn.getOpcode() == 185 || mInsn.getOpcode() == 182) && mInsn.owner.startsWith("com/sun/jna/platform/")) {
                     callingOSAPI = true;
                     if (canMakeEmpty) {
                        break;
                     }
                  }
               }
            }

            if (callingOSAPI && this.canMakeMethodEmpty(method)) {
               this.makeMethodEmpty(classNode, method);
               bChanged = true;
            }
         }
      } catch (Throwable var12) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_nativeMethod(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            if ((method.access & 256) != 0) {
               method.access &= -257;
               method.instructions.clear();
               method.tryCatchBlocks.clear();
               method.localVariables = null;
               InsnList insnList = new InsnList();
               Type returnType = Type.getReturnType(method.desc);
               switch (returnType.getSort()) {
                  case 0:
                     insnList.add(new InsnNode(177));
                     break;
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                     insnList.add(new InsnNode(3));
                     insnList.add(new InsnNode(172));
                     break;
                  case 6:
                     insnList.add(new InsnNode(11));
                     insnList.add(new InsnNode(174));
                     break;
                  case 7:
                     insnList.add(new InsnNode(9));
                     insnList.add(new InsnNode(173));
                     break;
                  case 8:
                     insnList.add(new InsnNode(14));
                     insnList.add(new InsnNode(175));
                     break;
                  case 9:
                     insnList.add(new InsnNode(1));
                     insnList.add(new InsnNode(176));
                     break;
                  case 10:
                     String var10003 = "L";
                     insnList.add(new LdcInsnNode(Type.getType(var10003 + returnType.getInternalName() + ";")));
                     insnList.add(new MethodInsnNode(184, "kakiku/pig2mod/xform/MyXformer2", "getInterfaceStub", "(Ljava/lang/Class;)Ljava/lang/Object;", false));
                     insnList.add(new InsnNode(176));
               }

               method.instructions.insert(insnList);
               bChanged = true;
            }
         }
      } catch (Throwable var7) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_ifWin(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode mInsn) {
                  if (mInsn.getOpcode() == 184 && "java/lang/System".equals(mInsn.owner) && "getProperty".equals(mInsn.name) && ("(Ljava/lang/String;)Ljava/lang/String;".equals(mInsn.desc) || "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;".equals(mInsn.desc))) {
                     AbstractInsnNode var13 = mInsn.getPrevious();
                     if (var13 instanceof LdcInsnNode) {
                        LdcInsnNode ldc = (LdcInsnNode)var13;
                        if ("os.name".equals(ldc.cst)) {
                           boolean twoArgs = "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;".equals(mInsn.desc);
                           InsnList insnList = new InsnList();
                           if (twoArgs) {
                              insnList.add(new InsnNode(87));
                           }

                           insnList.add(new InsnNode(87));
                           insnList.add(new LdcInsnNode(""));
                           method.instructions.insert(mInsn, insnList);
                           method.instructions.remove(mInsn);
                           bChanged = true;
                        }
                     }
                  }
               } else if (insn instanceof LdcInsnNode ldc) {
                  Object insnList = ldc.cst;
                  if (insnList instanceof String cst) {
                     if (cst.equals("os.name")) {
                        InsnList insnList = new InsnList();
                        insnList.add(new LdcInsnNode("Pig2.os.name"));
                        method.instructions.insert(ldc, insnList);
                        method.instructions.remove(ldc);
                        System.setProperty("Pig2.os.name", "");
                        bChanged = true;
                     }
                  }
               }
            }
         }
      } catch (Throwable var15) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_methodHandle(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            InsnList insns = method.instructions;

            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.getOpcode() == 182 && methodInsn.owner.equals("java/lang/invoke/MethodHandles$Lookup") && methodInsn.name.startsWith("find") && !methodInsn.name.equals("findConstructor") && methodInsn.desc.endsWith(")Ljava/lang/invoke/MethodHandle;")) {
                     InsnList inject = new InsnList();
                     String var10003 = "他MODのLookup.";
                     inject.add(new LdcInsnNode(var10003 + method.name + "()呼出しから"));
                     inject.add(new MethodInsnNode(184, "kakiku/pig2mod/xform/MyMethodHandles", "myMethodHandleWrap", "(Ljava/lang/invoke/MethodHandle;Ljava/lang/String;)Ljava/lang/invoke/MethodHandle;", false));
                     insns.insert(insn, inject);
                     bChanged = true;
                  }
               }
            }
         }
      } catch (Throwable var12) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_varHandle(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            InsnList insns = method.instructions;

            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.owner.equals("java/lang/invoke/VarHandle") && (methodInsn.name.startsWith("set") || methodInsn.name.contains("AndSet"))) {
                     if (!methodInsn.name.equals("set") || !methodInsn.desc.equals("(Z)V")) {
                        this.makeMethodCallEmpty(method, methodInsn);
                        bChanged = true;
                     }
                  } else if (methodInsn.getOpcode() == 182 && "java/lang/invoke/VarHandle".equals(methodInsn.owner) && "get".equals(methodInsn.name) && (methodInsn.desc.equals("(Ljava/lang/Class;)Ljava/lang/Object;") || methodInsn.desc.equals("(Ljava/lang/Object;)Ljava/lang/Object;"))) {
                     insns.set(methodInsn, new MethodInsnNode(184, "kakiku/pig2mod/xform/MyXformer2", "myVarHandleGet", "(Ljava/lang/invoke/VarHandle;Ljava/lang/Object;)Ljava/lang/Object;", false));
                     bChanged = true;
                  }
               }
            }
         }
      } catch (Throwable var11) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   public static Object myVarHandleGet(VarHandle varHandle, Object arg1) {
      if (varHandle.toString().contains("java.lang.Class") && arg1 instanceof Class clazz) {
         Object classData = gMapClassClassData.getOrDefault(clazz, (Object)null);
         return classData;
      } else {
         return varHandle.get(arg1);
      }
   }

   private boolean tranNodes_badLoop(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            ListIterator<AbstractInsnNode> it = method.instructions.iterator();

            while(it.hasNext()) {
               AbstractInsnNode insn = (AbstractInsnNode)it.next();
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.owner.equals("net/minecraft/client/Minecraft") && methodInsn.name.equals("m_91396_") && methodInsn.desc.equals("()Z")) {
                     AbstractInsnNode next = methodInsn.getNext();
                     if (next instanceof JumpInsnNode) {
                        JumpInsnNode jump = (JumpInsnNode)next;
                        if ((jump.getOpcode() == 153 || jump.getOpcode() == 154) && this.hasBackJumpTo(methodInsn, jump)) {
                           AbstractInsnNode prev = methodInsn.getPrevious();
                           if (prev != null && (prev.getOpcode() == 25 || prev.getOpcode() == 178 || prev.getOpcode() == 184)) {
                              method.instructions.remove(prev);
                           }

                           InsnList insnList = new InsnList();
                           insnList.add(new InsnNode(3));
                           method.instructions.insert(methodInsn, insnList);
                           method.instructions.remove(methodInsn);
                           bChanged = true;
                        }
                     }
                  }
               }
            }
         }
      } catch (Throwable var12) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean hasBackJumpTo(MethodInsnNode condCall, JumpInsnNode condJump) {
      AbstractInsnNode cur = condCall.getPrevious();

      LabelNode startLabel;
      for(startLabel = null; cur != null; cur = cur.getPrevious()) {
         if (cur instanceof LabelNode startLabel) {
            break;
         }
      }

      if (startLabel == null) {
         return false;
      } else {
         for(AbstractInsnNode scan = condCall.getNext(); scan != null && scan != condJump.label; scan = scan.getNext()) {
            if (scan instanceof JumpInsnNode) {
               JumpInsnNode j = (JumpInsnNode)scan;
               if (j.getOpcode() == 167 && j.label == startLabel) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean tranNodes_osCommand(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.owner.equals("java/lang/Runtime") && methodInsn.name.equals("exec") || methodInsn.owner.equals("java/lang/ProcessBuilder") && methodInsn.name.equals("start") || methodInsn.owner.equals("java/awt/Desktop") && (methodInsn.name.equals("open") || methodInsn.name.equals("browse")) || methodInsn.owner.endsWith("/ProcessImpl") || methodInsn.owner.endsWith("/Win32Process")) {
                     if (this.canMakeMethodEmpty(method)) {
                        this.makeMethodEmpty(classNode, method);
                        bChanged = true;
                        break;
                     }

                     this.makeMethodCallEmpty(method, methodInsn);
                     bChanged = true;
                  }
               }
            }
         }
      } catch (Throwable var10) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_vmAttach(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
            return false;
         }

         for(MethodNode method : classNode.methods) {
            for(AbstractInsnNode insn : method.instructions.toArray()) {
               if (insn instanceof MethodInsnNode methodInsn) {
                  if (methodInsn.owner.equals("com/sun/tools/attach/VirtualMachine") && methodInsn.name.equals("attach")) {
                     if (this.canMakeMethodEmpty(method)) {
                        this.makeMethodEmpty(classNode, method);
                        bChanged = true;
                        break;
                     }

                     this.makeMethodCallEmpty(method, methodInsn);
                     bChanged = true;
                  }
               }
            }
         }
      } catch (Throwable var10) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_fixVanilla(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (classNode.name.equals("io/netty/util/internal/PlatformDependent0")) {
            for(MethodNode method : classNode.methods) {
               if (method.name.equals("<clinit>")) {
                  for(AbstractInsnNode insn : method.instructions.toArray()) {
                     if (insn instanceof MethodInsnNode) {
                        MethodInsnNode mInsn = (MethodInsnNode)insn;
                        if (mInsn.owner.equals("io/netty/util/internal/logging/InternalLogger") && mInsn.name.equals("debug") && mInsn.desc.equals("(Ljava/lang/String;Ljava/lang/Throwable;)V")) {
                           method.instructions.insertBefore(mInsn, new InsnNode(87));
                           mInsn.desc = "(Ljava/lang/String;)V";
                           bChanged = true;
                        }
                     }
                  }
               }
            }
         }
      } catch (Throwable var10) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_reflectionCallee(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (classNode.name.startsWith("java/lang/reflect/Method")) {
            for(MethodNode method : classNode.methods) {
               if (method.name.equals("invoke")) {
                  InsnList insnList = new InsnList();
                  LabelNode L_skipUnsafeByMethod = new LabelNode();
                  LabelNode L_skipFieldByMethod = new LabelNode();
                  LabelNode L_skipMethodByMethod = new LabelNode();
                  LabelNode L_skipVarHandleByMethod = new LabelNode();
                  LabelNode L_skipMethodHandleByMethod = new LabelNode();
                  LabelNode L_continue = new LabelNode();
                  LabelNode L_returnAuto = new LabelNode();
                  LabelNode L_returnVoidOrNull = new LabelNode();
                  LabelNode L_returnTrue = new LabelNode();
                  LabelNode L_returnInt = new LabelNode();
                  LabelNode L_returnLong = new LabelNode();
                  LabelNode L_returnByte = new LabelNode();
                  LabelNode L_returnShort = new LabelNode();
                  LabelNode L_returnChar = new LabelNode();
                  LabelNode L_returnFloat = new LabelNode();
                  LabelNode L_returnDouble = new LabelNode();
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "clazz", "Ljava/lang/Class;"));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                  insnList.add(new LdcInsnNode("Unsafe"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false));
                  insnList.add(new JumpInsnNode(153, L_skipUnsafeByMethod));

                  for(String listType : List.of("Int", "Object", "Reference", "Boolean", "Byte", "Short", "Char", "Long", "Float", "Double")) {
                     insnList.add(new VarInsnNode(25, 0));
                     insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                     String var10003 = "put";
                     insnList.add(new LdcInsnNode(var10003 + listType));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnAuto));
                     insnList.add(new VarInsnNode(25, 0));
                     insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                     var10003 = "putOrdered";
                     insnList.add(new LdcInsnNode(var10003 + listType));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnAuto));
                     insnList.add(new VarInsnNode(25, 0));
                     insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                     var10003 = "AndSet";
                     insnList.add(new LdcInsnNode(var10003 + listType));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnAuto));
                  }

                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_skipUnsafeByMethod);
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "clazz", "Ljava/lang/Class;"));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                  insnList.add(new LdcInsnNode("java.lang.reflect.Field"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(153, L_skipFieldByMethod));

                  for(String listType : List.of("", "Int", "Boolean", "Byte", "Short", "Char", "Long", "Float", "Double")) {
                     insnList.add(new VarInsnNode(25, 0));
                     insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                     String var43 = "set";
                     insnList.add(new LdcInsnNode(var43 + listType));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnAuto));
                  }

                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_skipFieldByMethod);
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "clazz", "Ljava/lang/Class;"));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                  insnList.add(new LdcInsnNode("java.lang.reflect.Method"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(153, L_skipMethodByMethod));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                  insnList.add(new LdcInsnNode("invoke"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_returnAuto));
                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_skipMethodByMethod);
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "clazz", "Ljava/lang/Class;"));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                  insnList.add(new LdcInsnNode("java.lang.invoke.VarHandle"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(153, L_skipVarHandleByMethod));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                  insnList.add(new LdcInsnNode("set"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_returnAuto));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                  insnList.add(new LdcInsnNode("AndSet"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_returnAuto));
                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_skipVarHandleByMethod);
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "clazz", "Ljava/lang/Class;"));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                  insnList.add(new LdcInsnNode("java.lang.invoke.MethodHandle"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(153, L_skipMethodHandleByMethod));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                  insnList.add(new LdcInsnNode("invoke"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_returnAuto));
                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_skipMethodHandleByMethod);
                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_returnAuto);
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "returnType", "Ljava/lang/Class;"));
                  insnList.add(new FieldInsnNode(178, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;"));
                  insnList.add(new JumpInsnNode(165, L_returnTrue));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "returnType", "Ljava/lang/Class;"));
                  insnList.add(new FieldInsnNode(178, "java/lang/Integer", "TYPE", "Ljava/lang/Class;"));
                  insnList.add(new JumpInsnNode(165, L_returnInt));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "returnType", "Ljava/lang/Class;"));
                  insnList.add(new FieldInsnNode(178, "java/lang/Long", "TYPE", "Ljava/lang/Class;"));
                  insnList.add(new JumpInsnNode(165, L_returnLong));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "returnType", "Ljava/lang/Class;"));
                  insnList.add(new FieldInsnNode(178, "java/lang/Byte", "TYPE", "Ljava/lang/Class;"));
                  insnList.add(new JumpInsnNode(165, L_returnByte));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "returnType", "Ljava/lang/Class;"));
                  insnList.add(new FieldInsnNode(178, "java/lang/Short", "TYPE", "Ljava/lang/Class;"));
                  insnList.add(new JumpInsnNode(165, L_returnShort));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "returnType", "Ljava/lang/Class;"));
                  insnList.add(new FieldInsnNode(178, "java/lang/Character", "TYPE", "Ljava/lang/Class;"));
                  insnList.add(new JumpInsnNode(165, L_returnChar));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "returnType", "Ljava/lang/Class;"));
                  insnList.add(new FieldInsnNode(178, "java/lang/Float", "TYPE", "Ljava/lang/Class;"));
                  insnList.add(new JumpInsnNode(165, L_returnFloat));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new FieldInsnNode(180, "java/lang/reflect/Method", "returnType", "Ljava/lang/Class;"));
                  insnList.add(new FieldInsnNode(178, "java/lang/Double", "TYPE", "Ljava/lang/Class;"));
                  insnList.add(new JumpInsnNode(165, L_returnDouble));
                  insnList.add(new JumpInsnNode(167, L_returnVoidOrNull));
                  insnList.add(L_returnVoidOrNull);
                  insnList.add(new InsnNode(1));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_returnTrue);
                  insnList.add(new FieldInsnNode(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;"));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_returnInt);
                  insnList.add(new InsnNode(3));
                  insnList.add(new MethodInsnNode(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_returnLong);
                  insnList.add(new InsnNode(9));
                  insnList.add(new MethodInsnNode(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_returnByte);
                  insnList.add(new InsnNode(3));
                  insnList.add(new MethodInsnNode(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_returnShort);
                  insnList.add(new InsnNode(3));
                  insnList.add(new MethodInsnNode(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_returnChar);
                  insnList.add(new InsnNode(3));
                  insnList.add(new MethodInsnNode(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_returnFloat);
                  insnList.add(new InsnNode(11));
                  insnList.add(new MethodInsnNode(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_returnDouble);
                  insnList.add(new InsnNode(14));
                  insnList.add(new MethodInsnNode(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_continue);
                  method.instructions.insert(insnList);
                  bChanged = true;
               }
            }
         } else if (classNode.name.startsWith("java/lang/reflect/Field")) {
            for(MethodNode method : classNode.methods) {
               if (method.name.startsWith("set") && method.desc.startsWith("(Ljava/lang/Object;") && method.desc.endsWith(")V")) {
                  InsnList insnList = new InsnList();
                  LabelNode L_returnVoid = new LabelNode();
                  LabelNode L_continue = new LabelNode();

                  for(String className : List.of("net.minecraftforge.registries.ObjectHolderRef", "com.sun.jna.Structure", "org.openjdk.nashorn.internal.runtime.Context$ContextCodeInstaller", "com.google.gson.internal.bind.ReflectiveTypeAdapterFactory", "kakiku.pig2mod.")) {
                     insnList.add(new MethodInsnNode(184, "jdk/internal/reflect/Reflection", "getCallerClass", "()Ljava/lang/Class;"));
                     insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                     insnList.add(new LdcInsnNode(className));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_continue));
                  }

                  for(String className : this.mFullClassNamesOfProtectedFields) {
                     insnList.add(new VarInsnNode(25, 0));
                     insnList.add(new FieldInsnNode(180, "java/lang/reflect/Field", "clazz", "Ljava/lang/Class;"));
                     insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                     insnList.add(new LdcInsnNode(className));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnVoid));
                  }

                  for(String className : this.mClassNameStartsOfProtectedFields) {
                     insnList.add(new VarInsnNode(25, 0));
                     insnList.add(new FieldInsnNode(180, "java/lang/reflect/Field", "clazz", "Ljava/lang/Class;"));
                     insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                     insnList.add(new LdcInsnNode(className));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnVoid));
                  }

                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_returnVoid);
                  insnList.add(new InsnNode(1));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_continue);
                  method.instructions.insert(insnList);
                  bChanged = true;
               }
            }
         }
      } catch (Throwable var26) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_varHandleCallee(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (classNode.name.startsWith("java/lang/invoke/VarHandle") && (classNode.name.contains("$FieldInstanceReadWrite") || classNode.name.contains("$FieldStaticReadWrite"))) {
            for(MethodNode method : classNode.methods) {
               boolean bIsInstance = classNode.name.contains("$FieldInstanceReadWrite");
               if ((bIsInstance && method.desc.startsWith("(Ljava/lang/invoke/VarHandle;Ljava/lang/Object;") || !bIsInstance && method.desc.startsWith("(Ljava/lang/invoke/VarHandle;")) && (method.name.startsWith("set") || method.name.contains("ompareAnd") || method.name.startsWith("getAnd"))) {
                  InsnList insnList = new InsnList();
                  LabelNode L_returnAuto = new LabelNode();
                  LabelNode L_continue = new LabelNode();
                  LabelNode L_PopAndContinue = new LabelNode();
                  if (bIsInstance) {
                     insnList.add(new VarInsnNode(25, 1));
                     insnList.add(new JumpInsnNode(198, L_continue));
                     insnList.add(new VarInsnNode(25, 1));
                     insnList.add(new MethodInsnNode(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
                  } else {
                     insnList.add(new VarInsnNode(25, 0));
                     insnList.add(new FieldInsnNode(180, classNode.name.replace("ReadWrite", "ReadOnly"), "base", "Ljava/lang/Object;"));
                     insnList.add(new TypeInsnNode(192, "java/lang/Class"));
                  }

                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));

                  for(String className : List.of("java.util.concurrent.", "jdk.dynalink.BiClassValue", "sun.nio.ch.", "io.netty.util.", "javax.net.ssl.SSLContext", "java.net.Socket", "jdk.internal.net.http.", "sun.security.ssl.SSLSocketImpl")) {
                     insnList.add(new InsnNode(89));
                     insnList.add(new LdcInsnNode(className));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_PopAndContinue));
                  }

                  if (bIsInstance && method.name.equals("set")) {
                     insnList.add(new MethodInsnNode(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false));
                     insnList.add(new MethodInsnNode(182, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false));
                     insnList.add(new InsnNode(5));
                     insnList.add(new InsnNode(50));
                     insnList.add(new MethodInsnNode(182, "java/lang/StackTraceElement", "getClassName", "()Ljava/lang/String;", false));
                     insnList.add(new LdcInsnNode("java.util.concurrent.atomic."));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_PopAndContinue));
                  }

                  for(String className : this.mFullClassNamesOfProtectedFields) {
                     insnList.add(new InsnNode(89));
                     insnList.add(new LdcInsnNode(className));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnAuto));
                  }

                  for(String className : this.mClassNameStartsOfProtectedFields) {
                     insnList.add(new InsnNode(89));
                     insnList.add(new LdcInsnNode(className));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnAuto));
                  }

                  insnList.add(new InsnNode(87));
                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_returnAuto);
                  insnList.add(new InsnNode(87));
                  if (method.desc.endsWith(")Z")) {
                     insnList.add(new InsnNode(4));
                  } else {
                     this.insnListAdd0null(method.desc, insnList);
                  }

                  insnList.add(new InsnNode(Type.getReturnType(method.desc).getOpcode(172)));
                  insnList.add(L_PopAndContinue);
                  insnList.add(new InsnNode(87));
                  insnList.add(L_continue);
                  method.instructions.insert(insnList);
                  bChanged = true;
               }
            }
         }
      } catch (Throwable var13) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_lookupCallee(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (classNode.name.startsWith("sun/misc/Unsafe")) {
            for(MethodNode method : classNode.methods) {
               if ((method.access & 256) == 0 && method.name.startsWith("getObject") && method.desc.equals("(Ljava/lang/Object;J)Ljava/lang/Object;")) {
                  InsnList insnList = new InsnList();
                  LabelNode L_continue = new LabelNode();
                  LabelNode L_popAndContinue = new LabelNode();
                  LabelNode L_tryStart = new LabelNode();
                  LabelNode L_tryEnd = new LabelNode();
                  LabelNode L_catch = new LabelNode();
                  insnList.add(L_tryStart);
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new LdcInsnNode(Type.getType("Ljava/lang/invoke/MethodHandles$Lookup;")));
                  insnList.add(new LdcInsnNode("IMPL_LOOKUP"));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", false));
                  insnList.add(new MethodInsnNode(182, "sun/misc/Unsafe", "staticFieldBase", "(Ljava/lang/reflect/Field;)Ljava/lang/Object;", false));
                  insnList.add(new VarInsnNode(25, 1));
                  insnList.add(new JumpInsnNode(166, L_continue));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new LdcInsnNode(Type.getType("Ljava/lang/invoke/MethodHandles$Lookup;")));
                  insnList.add(new LdcInsnNode("IMPL_LOOKUP"));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", false));
                  insnList.add(new MethodInsnNode(182, "sun/misc/Unsafe", "staticFieldOffset", "(Ljava/lang/reflect/Field;)J", false));
                  insnList.add(new VarInsnNode(22, 2));
                  insnList.add(new InsnNode(148));
                  insnList.add(new JumpInsnNode(154, L_continue));
                  List<String> CallerWhiteList = new ArrayList(List.of("kakiku.pig2mod.xform.MyLib2", "net.jodah.typetools.TypeResolver", "com.mega.uom.util.java.InstrumentationHelper"));
                  insnList.add(new MethodInsnNode(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false));
                  insnList.add(new MethodInsnNode(182, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false));
                  insnList.add(new InsnNode(5));
                  insnList.add(new InsnNode(50));
                  insnList.add(new MethodInsnNode(182, "java/lang/StackTraceElement", "getClassName", "()Ljava/lang/String;", false));

                  for(String callerWhite : CallerWhiteList) {
                     insnList.add(new InsnNode(89));
                     insnList.add(new LdcInsnNode(callerWhite));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_popAndContinue));
                  }

                  insnList.add(new InsnNode(87));
                  insnList.add(new MethodInsnNode(184, "java/lang/invoke/MethodHandles", "lookup", "()Ljava/lang/invoke/MethodHandles$Lookup;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_tryEnd);
                  insnList.add(L_catch);
                  insnList.add(new InsnNode(87));
                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_popAndContinue);
                  insnList.add(new InsnNode(87));
                  insnList.add(L_continue);
                  method.instructions.insertBefore(method.instructions.getFirst(), insnList);
                  method.tryCatchBlocks.add(new TryCatchBlockNode(L_tryStart, L_tryEnd, L_catch, "java/lang/Throwable"));
                  bChanged = true;
               }
            }
         } else if (classNode.name.equals("java/lang/invoke/MethodHandles$Lookup")) {
            for(MethodNode method : classNode.methods) {
               if (method.name.equals("findVirtual") && method.desc.equals("(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;") || method.name.equals("findStatic") && method.desc.equals("(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;") || method.name.equals("findConstructor") && method.desc.equals("(Ljava/lang/Class;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;")) {
                  boolean bConstructor = method.name.equals("findConstructor");
                  boolean bVirtual = method.name.contains("Virtual");
                  InsnList insnList = new InsnList();
                  LabelNode L_continue = new LabelNode();
                  LabelNode L_popAndContinue = new LabelNode();
                  LabelNode L_returnChangedMethod = new LabelNode();
                  LabelNode L_returnEmptyMethod = new LabelNode();
                  int varIndex1Class = 1;
                  int varIndex2String = 2;
                  int varIndex3MethodType = 3;
                  if (bConstructor) {
                     varIndex3MethodType = 2;
                     varIndex2String = -1;
                  }

                  insnList.add(new FieldInsnNode(178, "java/lang/invoke/MethodHandles$Lookup", "IMPL_LOOKUP", "Ljava/lang/invoke/MethodHandles$Lookup;"));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new JumpInsnNode(165, L_continue));
                  List<String> watchListClass = new ArrayList(List.of("java.lang.", "jdk.internal."));

                  for(String packageName : MyLib2.gModPackageNamesFromJar) {
                     if (!MyLib2.isThisNameMyMOD(packageName)) {
                        watchListClass.add(packageName);
                     }
                  }

                  for(String className : watchListClass) {
                     insnList.add(new VarInsnNode(25, varIndex1Class));
                     insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                     insnList.add(new LdcInsnNode(className));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_returnChangedMethod));
                  }

                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_returnChangedMethod);
                  insnList.add(new MethodInsnNode(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false));
                  insnList.add(new MethodInsnNode(182, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false));
                  insnList.add(new InsnNode(5));
                  insnList.add(new InsnNode(50));
                  insnList.add(new MethodInsnNode(182, "java/lang/StackTraceElement", "getClassName", "()Ljava/lang/String;", false));

                  for(String callerClass : List.of("java.lang.", "jdk.internal.", "kakiku.pig2mod.", "jdk.dynalink.", "org.openjdk.nashorn.internal.", "com.mojang.blaze3d.platform.")) {
                     insnList.add(new InsnNode(89));
                     insnList.add(new LdcInsnNode(callerClass));
                     insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                     insnList.add(new JumpInsnNode(154, L_popAndContinue));
                  }

                  insnList.add(new InsnNode(89));
                  insnList.add(new VarInsnNode(25, varIndex1Class));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_popAndContinue));
                  insnList.add(new InsnNode(87));
                  if (bConstructor) {
                     insnList.add(new FieldInsnNode(178, "java/lang/invoke/MethodHandles$Lookup", "IMPL_LOOKUP", "Ljava/lang/invoke/MethodHandles$Lookup;"));
                     insnList.add(new VarInsnNode(25, varIndex1Class));
                     insnList.add(new VarInsnNode(25, varIndex3MethodType));
                     insnList.add(new MethodInsnNode(182, classNode.name, method.name, method.desc, false));
                     insnList.add(new InsnNode(176));
                  } else {
                     LabelNode L_returnUsingStrongLookup = new LabelNode();

                     for(String methodName : List.of("defineClass0", "findLoadedClass")) {
                        insnList.add(new LdcInsnNode(methodName));
                        insnList.add(new VarInsnNode(25, varIndex2String));
                        insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                        insnList.add(new JumpInsnNode(154, L_returnUsingStrongLookup));
                     }

                     insnList.add(new JumpInsnNode(167, L_returnEmptyMethod));
                     insnList.add(L_returnUsingStrongLookup);
                     insnList.add(new FieldInsnNode(178, "java/lang/invoke/MethodHandles$Lookup", "IMPL_LOOKUP", "Ljava/lang/invoke/MethodHandles$Lookup;"));
                     insnList.add(new VarInsnNode(25, varIndex1Class));
                     insnList.add(new VarInsnNode(25, varIndex2String));
                     insnList.add(new VarInsnNode(25, varIndex3MethodType));
                     insnList.add(new MethodInsnNode(182, classNode.name, method.name, method.desc, false));
                     insnList.add(new InsnNode(176));
                  }

                  insnList.add(L_returnEmptyMethod);
                  insnList.add(new VarInsnNode(25, varIndex3MethodType));
                  if (bVirtual) {
                     insnList.add(new InsnNode(3));
                     insnList.add(new InsnNode(4));
                     insnList.add(new TypeInsnNode(189, "java/lang/Class"));
                     insnList.add(new InsnNode(89));
                     insnList.add(new InsnNode(3));
                     insnList.add(new VarInsnNode(25, varIndex1Class));
                     LabelNode L_NotJdkUnsafe = new LabelNode();
                     insnList.add(new InsnNode(89));
                     insnList.add(new LdcInsnNode(Type.getType("Ljdk/internal/misc/Unsafe;")));
                     insnList.add(new JumpInsnNode(166, L_NotJdkUnsafe));
                     insnList.add(new InsnNode(87));
                     insnList.add(new LdcInsnNode(Type.getType("Ljava/lang/Object;")));
                     insnList.add(L_NotJdkUnsafe);
                     insnList.add(new InsnNode(83));
                     insnList.add(new MethodInsnNode(182, "java/lang/invoke/MethodType", "insertParameterTypes", "(I[Ljava/lang/Class;)Ljava/lang/invoke/MethodType;", false));
                  }

                  insnList.add(new MethodInsnNode(184, "java/lang/invoke/MethodHandles", "empty", "(Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_popAndContinue);
                  insnList.add(new InsnNode(87));
                  insnList.add(L_continue);
                  method.instructions.insert(insnList);
                  bChanged = true;
               } else if ((method.name.equals("findStaticVarHandle") || method.name.equals("findVarHandle")) && method.desc.equals("(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/VarHandle;")) {
                  InsnList insnList = new InsnList();
                  LabelNode L_continue = new LabelNode();
                  LabelNode L_returnNormalUnsafe = new LabelNode();
                  LabelNode L_returnUsingImplLookup = new LabelNode();
                  insnList.add(new FieldInsnNode(178, "java/lang/invoke/MethodHandles$Lookup", "IMPL_LOOKUP", "Ljava/lang/invoke/MethodHandles$Lookup;"));
                  insnList.add(new VarInsnNode(25, 0));
                  insnList.add(new JumpInsnNode(165, L_continue));
                  insnList.add(new VarInsnNode(25, 1));
                  insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                  insnList.add(new LdcInsnNode("sun.misc.Unsafe"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                  insnList.add(new LdcInsnNode("theInternalUnsafe"));
                  insnList.add(new VarInsnNode(25, 2));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                  insnList.add(new InsnNode(126));
                  insnList.add(new JumpInsnNode(154, L_returnNormalUnsafe));
                  Map<String, List<String>> whiteList = new LinkedHashMap();
                  whiteList.put("jdk.internal.misc.UnsafeConstants", List.of("BIG_ENDIAN"));
                  whiteList.put("java.lang.SecurityManager", List.of("initialized"));
                  whiteList.put("java.lang.Thread", List.of("eetop", "group"));
                  whiteList.put("java.lang.ClassLoader", List.of("libraries", "packages", "parent"));
                  whiteList.put("jdk.internal.loader.NativeLibraries", List.of("loadLibraryOnlyIfPresent", "loadedLibraryNames", "loader", "libraries", "nativeLibraryContext", "isJNI"));
                  whiteList.put("jdk.internal.loader.NativeLibraries$NativeLibraryImpl", List.of("name", "fromClass"));
                  whiteList.put("jdk.internal.loader.ClassLoaders", List.of("APP_LOADER"));
                  whiteList.put("jdk.internal.ref.CleanerFactory", List.of("commonCleaner"));
                  whiteList.put("java.lang.Class", List.of("classData", "reflectionData"));
                  whiteList.put("java.lang.NamedPackage", List.of("module"));
                  whiteList.put("jdk.internal.access.SharedSecrets", List.of("javaLangAccess"));
                  whiteList.put("jdk.internal.loader.BootLoader", List.of("NATIVE_LIBS"));
                  whiteList.put("jdk.internal.reflect.Reflection", List.of("fieldFilterMap", "methodFilterMap"));
                  whiteList.put("net.minecraft.client.Minecraft", List.of("level", "f_91073_"));
                  whiteList.put("net.minecraft.world.entity.Entity", List.of("level", "f_19853_", "removalReason", "f_146795_"));
                  whiteList.put("net.minecraft.client.multiplayer.ClientLevel", List.of("entityStorage", "f_171631_"));
                  whiteList.put("net.minecraft.server.level.ServerLevel", List.of("entityManager", "f_143244_"));
                  whiteList.put("net.minecraftforge.eventbus.EventBus", List.of("exceptionHandler", "trackPhases", "shutdown", "baseType", "checkTypesOnDispatch", "factory"));

                  for(Map.Entry entry : whiteList.entrySet()) {
                     String className = (String)entry.getKey();

                     for(String fieldName : (List)entry.getValue()) {
                        insnList.add(new VarInsnNode(25, 1));
                        insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                        insnList.add(new LdcInsnNode(className));
                        insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                        insnList.add(new LdcInsnNode(fieldName));
                        insnList.add(new VarInsnNode(25, 2));
                        insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                        insnList.add(new InsnNode(126));
                        insnList.add(new JumpInsnNode(154, L_returnUsingImplLookup));
                     }
                  }

                  insnList.add(new JumpInsnNode(167, L_continue));
                  insnList.add(L_returnNormalUnsafe);
                  insnList.add(new LdcInsnNode("theUnsafe"));
                  insnList.add(new VarInsnNode(58, 2));
                  insnList.add(new VarInsnNode(25, 1));
                  insnList.add(new VarInsnNode(58, 3));
                  insnList.add(new JumpInsnNode(167, L_returnUsingImplLookup));
                  insnList.add(L_returnUsingImplLookup);
                  insnList.add(new FieldInsnNode(178, "java/lang/invoke/MethodHandles$Lookup", "IMPL_LOOKUP", "Ljava/lang/invoke/MethodHandles$Lookup;"));
                  insnList.add(new VarInsnNode(25, 1));
                  insnList.add(new VarInsnNode(25, 2));
                  insnList.add(new VarInsnNode(25, 3));
                  insnList.add(new MethodInsnNode(182, classNode.name, method.name, method.desc, false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_continue);
                  method.instructions.insert(insnList);
                  bChanged = true;
               }
            }
         }
      } catch (Throwable var19) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_methodHandleCallee(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (classNode.name.equals("java/lang/invoke/MethodHandles$Lookup")) {
            for(MethodNode method : classNode.methods) {
               if ((method.name.equals("getDirectMethodCommon") || method.name.equals("getDirectFieldCommon")) && method.desc.endsWith(")Ljava/lang/invoke/MethodHandle;")) {
                  for(AbstractInsnNode insn : method.instructions.toArray()) {
                     if (insn.getOpcode() == 176) {
                        InsnList insnList = new InsnList();
                        String var10003 = "Lookup.";
                        insnList.add(new LdcInsnNode(var10003 + method.name + "()本体から"));
                        insnList.add(new MethodInsnNode(184, "kakiku/MyLib0", "wrapMethodHandle", "(Ljava/lang/invoke/MethodHandle;Ljava/lang/String;)Ljava/lang/invoke/MethodHandle;", false));
                        method.instructions.insertBefore(insn, insnList);
                        bChanged = true;
                     }
                  }
               }
            }
         }
      } catch (Throwable var10) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_hiddenCallee(ClassNode classNode) {
      boolean bChanged = false;

      try {
         if (classNode.name.startsWith("java/lang/System")) {
            for(MethodNode method : classNode.methods) {
               if (method.name.equals("defineClass") && method.desc.equals("(Ljava/lang/ClassLoader;Ljava/lang/Class;Ljava/lang/String;[BLjava/security/ProtectionDomain;ZILjava/lang/Object;)Ljava/lang/Class;")) {
                  LabelNode L_continue = new LabelNode();
                  InsnList insnList = new InsnList();
                  insnList.add(new VarInsnNode(25, 3));
                  insnList.add(new LdcInsnNode("java/lang/"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_continue));
                  insnList.add(new VarInsnNode(25, 3));
                  insnList.add(new LdcInsnNode("jdk.jfr."));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_continue));
                  insnList.add(new VarInsnNode(25, 3));
                  insnList.add(new LdcInsnNode("$$InjectedInvoker"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "endsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_continue));
                  insnList.add(new VarInsnNode(25, 3));
                  insnList.add(new LdcInsnNode("$$Lambda$"));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_continue));
                  insnList.add(new VarInsnNode(25, 3));
                  insnList.add(new LdcInsnNode("com.llamalad7.mixinextras.sugar.impl."));
                  insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                  insnList.add(new JumpInsnNode(154, L_continue));
                  insnList.add(new VarInsnNode(25, 1));
                  insnList.add(new VarInsnNode(25, 3));
                  insnList.add(new MethodInsnNode(182, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", false));
                  insnList.add(new InsnNode(176));
                  insnList.add(L_continue);
                  method.instructions.insert(insnList);
                  bChanged = true;
               }
            }
         }
      } catch (Throwable var7) {
         if (bChanged) {
            bChanged = false;
         }
      }

      return bChanged;
   }

   private boolean tranNodes_badCall(ClassNode classNode) {
      if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
         return false;
      } else {
         boolean bChanged = false;

         try {
            for(MethodNode method : classNode.methods) {
               for(AbstractInsnNode insn : method.instructions.toArray()) {
                  if (insn instanceof MethodInsnNode) {
                     MethodInsnNode mInsn = (MethodInsnNode)insn;
                     boolean needChange = false;
                     String var10000 = mInsn.owner.replace("/", ".");
                     String calleeName = var10000 + "." + mInsn.name + "()";

                     for(String badCalleeName : this.mBadCalleeNames) {
                        if (calleeName.startsWith(badCalleeName)) {
                           needChange = true;
                           break;
                        }
                     }

                     if (needChange) {
                        this.makeMethodCallEmpty(method, mInsn);
                        bChanged = true;
                     }
                  }
               }
            }
         } catch (Throwable var14) {
            if (bChanged) {
               bChanged = false;
            }
         }

         return bChanged;
      }
   }

   private boolean tranNodes_tempXXXX(ClassNode classNode) {
      if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
         return false;
      } else {
         boolean bChanged = false;

         try {
            if (classNode.name.contains("xxxx/xxxx")) {
               for(MethodNode method : classNode.methods) {
                  if (method.name.equals("xxxx")) {
                  }
               }
            }
         } catch (Throwable var5) {
            if (bChanged) {
               bChanged = false;
            }
         }

         return bChanged;
      }
   }

   public static Instrumentation myCreateInstrumentationImpl() {
      return MyKeisou.gInstance;
   }

   public static void doNotResetThisClass(String className) {
      if (MyLib2.getCallerClass1() == MyPlugin.class) {
         gDoNotResetThisClass.add(className);
      }
   }

   public boolean canMakeMethodEmpty(MethodNode method) {
      return (method.access & 1280) == 0 && !method.name.equals("<init>") && !method.name.equals("<clinit>");
   }

   public void makeMethodEmpty(ClassNode classNode, MethodNode method) {
      boolean shouldInsertSuperCall = this.shouldInsertSuperCall(classNode, method);
      method.instructions.clear();
      method.tryCatchBlocks.clear();
      method.localVariables = null;
      InsnList insnList = new InsnList();
      Type returnType = Type.getReturnType(method.desc);
      if (shouldInsertSuperCall) {
         this.insnListAddSuperCall(classNode, method, insnList);
      } else {
         this.insnListAdd0null(method.desc, insnList);
      }

      insnList.add(new InsnNode(returnType.getOpcode(172)));
      method.instructions.insert(insnList);
   }

   private boolean shouldInsertSuperCall(ClassNode classNode, MethodNode method) {
      if (!MyLib2.isThisNameMinecraftVanilla(classNode.superName.replace('/', '.'))) {
         return false;
      } else {
         ListIterator var3 = method.instructions.iterator();

         while(var3.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)var3.next();
            if (insn instanceof MethodInsnNode) {
               MethodInsnNode mInsn = (MethodInsnNode)insn;
               if (mInsn.getOpcode() == 183 && mInsn.owner.equals(classNode.superName) && mInsn.name.equals(method.name) && mInsn.desc.equals(method.desc)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private void insnListAddSuperCall(ClassNode classNode, MethodNode method, InsnList insnList) {
      insnList.add(new VarInsnNode(25, 0));
      int index = 1;

      for(Type arg : Type.getArgumentTypes(method.desc)) {
         insnList.add(new VarInsnNode(arg.getOpcode(21), index));
         index += arg.getSize();
      }

      insnList.add(new MethodInsnNode(183, classNode.superName, method.name, method.desc, false));
   }

   private void insnListAdd0null(String desc, InsnList insnList) {
      Type returnType = Type.getReturnType(desc);
      switch (returnType.getSort()) {
         case 0:
         default:
            break;
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
            insnList.add(new InsnNode(3));
            break;
         case 6:
            insnList.add(new InsnNode(11));
            break;
         case 7:
            insnList.add(new InsnNode(9));
            break;
         case 8:
            insnList.add(new InsnNode(14));
            break;
         case 9:
            Type elem = returnType.getElementType();
            insnList.add(new InsnNode(3));
            if (elem.getSort() != 10 && elem.getSort() != 9) {
               byte var10000;
               switch (elem.getSort()) {
                  case 1 -> var10000 = 4;
                  case 2 -> var10000 = 5;
                  case 3 -> var10000 = 8;
                  case 4 -> var10000 = 9;
                  case 5 -> var10000 = 10;
                  case 6 -> var10000 = 6;
                  case 7 -> var10000 = 11;
                  case 8 -> var10000 = 7;
                  default -> var10000 = 10;
               }

               int atype = var10000;
               insnList.add(new IntInsnNode(188, atype));
            } else {
               insnList.add(new TypeInsnNode(189, elem.getInternalName()));
            }
            break;
         case 10:
            String internalName = returnType.getInternalName();
            if (internalName.equals("java/util/List")) {
               insnList.add(new TypeInsnNode(187, "java/util/ArrayList"));
               insnList.add(new InsnNode(89));
               insnList.add(new MethodInsnNode(183, "java/util/ArrayList", "<init>", "()V", false));
            } else if (internalName.equals("java/util/Set")) {
               insnList.add(new TypeInsnNode(187, "java/util/HashSet"));
               insnList.add(new InsnNode(89));
               insnList.add(new MethodInsnNode(183, "java/util/HashSet", "<init>", "()V", false));
            } else if (internalName.equals("java/util/Map")) {
               insnList.add(new TypeInsnNode(187, "java/util/HashMap"));
               insnList.add(new InsnNode(89));
               insnList.add(new MethodInsnNode(183, "java/util/HashMap", "<init>", "()V", false));
            } else if (internalName.equals("java/lang/String")) {
               insnList.add(new LdcInsnNode(""));
            } else if (internalName.equals("java/util/Optional")) {
               insnList.add(new MethodInsnNode(184, "java/util/Optional", "empty", "()Ljava/util/Optional;", false));
            } else if (this.isEnumClassName(internalName)) {
               if (internalName.equals("net/minecraft/world/entity/Entity$RemovalReason")) {
                  insnList.add(new InsnNode(1));
               } else {
                  insnList.add(new MethodInsnNode(184, internalName, "values", "()[L" + internalName + ";", false));
                  insnList.add(new InsnNode(3));
                  insnList.add(new InsnNode(50));
               }
            } else if (internalName.equals("net/minecraft/world/phys/Vec3")) {
               insnList.add(new TypeInsnNode(187, "net/minecraft/world/phys/Vec3"));
               insnList.add(new InsnNode(89));
               insnList.add(new InsnNode(14));
               insnList.add(new InsnNode(14));
               insnList.add(new InsnNode(14));
               insnList.add(new MethodInsnNode(183, "net/minecraft/world/phys/Vec3", "<init>", "(DDD)V", false));
            } else if (internalName.equals("net/minecraft/core/BlockPos")) {
               insnList.add(new TypeInsnNode(187, "net/minecraft/core/BlockPos"));
               insnList.add(new InsnNode(89));
               insnList.add(new InsnNode(3));
               insnList.add(new InsnNode(3));
               insnList.add(new InsnNode(3));
               insnList.add(new MethodInsnNode(183, "net/minecraft/core/BlockPos", "<init>", "(III)V", false));
            } else if (internalName.equals("net/minecraft/world/level/ChunkPos")) {
               insnList.add(new TypeInsnNode(187, "net/minecraft/world/level/ChunkPos"));
               insnList.add(new InsnNode(89));
               insnList.add(new InsnNode(3));
               insnList.add(new InsnNode(3));
               insnList.add(new MethodInsnNode(183, "net/minecraft/world/level/ChunkPos", "<init>", "(II)V", false));
            } else if (internalName.equals("net/minecraft/world/phys/AABB")) {
               insnList.add(new TypeInsnNode(187, "net/minecraft/world/phys/AABB"));
               insnList.add(new InsnNode(89));
               insnList.add(new InsnNode(14));
               insnList.add(new InsnNode(14));
               insnList.add(new InsnNode(14));
               insnList.add(new InsnNode(14));
               insnList.add(new InsnNode(14));
               insnList.add(new InsnNode(14));
               insnList.add(new MethodInsnNode(183, "net/minecraft/world/phys/AABB", "<init>", "(DDDDDD)V", false));
            } else if (internalName.equals("java/util/UUID")) {
               insnList.add(new TypeInsnNode(187, "java/util/UUID"));
               insnList.add(new InsnNode(89));
               insnList.add(new InsnNode(9));
               insnList.add(new InsnNode(9));
               insnList.add(new MethodInsnNode(183, "java/util/UUID", "<init>", "(JJ)V", false));
            } else {
               insnList.add(new InsnNode(1));
            }
      }

   }

   private boolean isEnumClassName(String className) {
      if (className.equals("java/lang/Object")) {
         return false;
      } else {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         boolean isEnum = false;

         try {
            InputStream is = cl != null ? cl.getResourceAsStream(className + ".class") : ClassLoader.getSystemResourceAsStream(className + ".class");

            try {
               if (is != null) {
                  ClassReader cr = new ClassReader(is);
                  isEnum = (cr.getAccess() & 16384) != 0;
               }
            } catch (Throwable var8) {
               if (is != null) {
                  try {
                     is.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (is != null) {
               is.close();
            }
         } catch (Throwable var9) {
         }

         return isEnum;
      }
   }

   public void insertReturn(MethodNode method, InsnList insnList) {
      this.insnListAdd0null(method.desc, insnList);
      Type returnType = Type.getReturnType(method.desc);
      insnList.add(new InsnNode(returnType.getOpcode(172)));
   }

   public void makeMethodCallEmpty(MethodNode method, MethodInsnNode methodInsn) {
      if (!methodInsn.name.equals("<init>") && !methodInsn.name.equals("<clinit>")) {
         InsnList insnList = new InsnList();
         Type[] args = Type.getArgumentTypes(methodInsn.desc);

         for(int i = args.length - 1; i >= 0; --i) {
            int sort = args[i].getSort();
            if (sort != 7 && sort != 8) {
               insnList.add(new InsnNode(87));
            } else {
               insnList.add(new InsnNode(88));
            }
         }

         if (methodInsn.getOpcode() != 184) {
            insnList.add(new InsnNode(87));
         }

         this.insnListAdd0null(methodInsn.desc, insnList);
         method.instructions.insertBefore(methodInsn, insnList);
         method.instructions.remove(methodInsn);
      }
   }

   private void printAllStackTraces(ClassNode classNode, MethodNode method, InsnList insnList) {
   }

   private void concatCaller1NameIntoInsnList(InsnList insnList) {
      insnList.add(new LdcInsnNode("←"));
      insnList.add(new MethodInsnNode(182, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false));
      this.pushCaller1NameIntoInsnList(insnList);
      insnList.add(new MethodInsnNode(182, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false));
   }

   private void pushCaller1NameIntoInsnList(InsnList insnList) {
      this.pushCallerNthNameIntoInsnList(insnList, 1);
   }

   private void pushCallerNthNameIntoInsnList(InsnList insnList, int nth) {
      insnList.add(new MethodInsnNode(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false));
      insnList.add(new MethodInsnNode(182, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false));
      insnList.add(new LdcInsnNode(1 + nth));
      insnList.add(new InsnNode(50));
      insnList.add(new InsnNode(89));
      insnList.add(new MethodInsnNode(182, "java/lang/StackTraceElement", "getClassName", "()Ljava/lang/String;", false));
      insnList.add(new LdcInsnNode("."));
      insnList.add(new MethodInsnNode(182, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false));
      insnList.add(new InsnNode(95));
      insnList.add(new MethodInsnNode(182, "java/lang/StackTraceElement", "getMethodName", "()Ljava/lang/String;", false));
      insnList.add(new MethodInsnNode(182, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false));
      insnList.add(new LdcInsnNode("()"));
      insnList.add(new MethodInsnNode(182, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false));
   }

   private void push1stCallerNameWithoutSpecifiedIntoInsnList(MethodNode method, InsnList insnList, List pSpecifiedList) {
      int localStackTraceElementArray = method.maxLocals++;
      int localArrayI = method.maxLocals++;
      int localStackTraceElement = method.maxLocals++;
      int localReturnString = method.maxLocals++;
      LabelNode L_loopStart = new LabelNode();
      LabelNode L_continue = new LabelNode();
      LabelNode L_returnEmpty = new LabelNode();
      LabelNode L_checkNextCaller = new LabelNode();
      insnList.add(new MethodInsnNode(184, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false));
      insnList.add(new MethodInsnNode(182, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false));
      insnList.add(new VarInsnNode(58, localStackTraceElementArray));
      insnList.add(new InsnNode(5));
      insnList.add(new VarInsnNode(54, localArrayI));
      insnList.add(L_loopStart);
      insnList.add(new VarInsnNode(21, localArrayI));
      insnList.add(new VarInsnNode(25, localStackTraceElementArray));
      insnList.add(new InsnNode(190));
      insnList.add(new JumpInsnNode(162, L_returnEmpty));
      insnList.add(new VarInsnNode(25, localStackTraceElementArray));
      insnList.add(new VarInsnNode(21, localArrayI));
      insnList.add(new InsnNode(50));
      insnList.add(new VarInsnNode(58, localStackTraceElement));
      insnList.add(new VarInsnNode(25, localStackTraceElement));
      insnList.add(new MethodInsnNode(182, "java/lang/StackTraceElement", "getClassName", "()Ljava/lang/String;", false));
      insnList.add(new LdcInsnNode("."));
      this.insnListAdd_String_concat(insnList);
      insnList.add(new VarInsnNode(25, localStackTraceElement));
      insnList.add(new MethodInsnNode(182, "java/lang/StackTraceElement", "getMethodName", "()Ljava/lang/String;", false));
      this.insnListAdd_String_concat(insnList);
      insnList.add(new LdcInsnNode("()"));
      this.insnListAdd_String_concat(insnList);
      insnList.add(new VarInsnNode(58, localReturnString));
      if (pSpecifiedList != null) {
         for(String specified : pSpecifiedList) {
            insnList.add(new VarInsnNode(25, localReturnString));
            insnList.add(new LdcInsnNode(specified));
            this.insnListAdd_String_contains_thenJump(insnList, L_checkNextCaller);
         }
      }

      insnList.add(new VarInsnNode(25, localReturnString));
      insnList.add(new JumpInsnNode(167, L_continue));
      insnList.add(L_checkNextCaller);
      insnList.add(new IincInsnNode(localArrayI, 1));
      insnList.add(new JumpInsnNode(167, L_loopStart));
      insnList.add(L_returnEmpty);
      insnList.add(new LdcInsnNode(""));
      insnList.add(L_continue);
   }

   public void printPushedString(InsnList insnList) {
      insnList.add(new FieldInsnNode(178, "java/lang/System", "out", "Ljava/io/PrintStream;"));
      insnList.add(new InsnNode(95));
      insnList.add(new MethodInsnNode(182, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
   }

   public void insnListAdd_String_concat(InsnList insnList) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false));
   }

   public void insnListAdd_String_contains_thenJump(InsnList insnList, LabelNode label) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false));
      insnList.add(new JumpInsnNode(154, label));
   }

   public void insnListAdd_String_contains_elseJump(InsnList insnList, LabelNode label) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false));
      insnList.add(new JumpInsnNode(153, label));
   }

   public void insnListAdd_String_startsWith_thenJump(InsnList insnList, LabelNode label) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
      insnList.add(new JumpInsnNode(154, label));
   }

   public void insnListAdd_String_startsWith_elseJump(InsnList insnList, LabelNode label) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
      insnList.add(new JumpInsnNode(153, label));
   }

   public void insnListAdd_String_endsWith_thenJump(InsnList insnList, LabelNode label) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "endsWith", "(Ljava/lang/String;)Z", false));
      insnList.add(new JumpInsnNode(154, label));
   }

   public void insnListAdd_String_endsWith_elseJump(InsnList insnList, LabelNode label) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "endsWith", "(Ljava/lang/String;)Z", false));
      insnList.add(new JumpInsnNode(153, label));
   }

   public void insnListAdd_String_equals_thenJump(InsnList insnList, LabelNode label) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
      insnList.add(new JumpInsnNode(154, label));
   }

   public void insnListAdd_String_equals_elseJump(InsnList insnList, LabelNode label) {
      insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
      insnList.add(new JumpInsnNode(153, label));
   }

   private void debugSaveClassInspectLogFile(String className, byte[] classfileBuffer1) {
   }

   private static void toMyLogFile(String string) {
   }

   private static String simpleName(String className) {
      int lastIndex = className.lastIndexOf("/");
      if (lastIndex == -1) {
         lastIndex = className.lastIndexOf(".");
      }

      return className.substring(lastIndex + 1);
   }
}

