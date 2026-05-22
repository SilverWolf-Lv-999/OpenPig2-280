package kakiku.pig2mod.xform;

import cpw.mods.modlauncher.api.NamedPath;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService.Phase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MyPlugin implements ILaunchPluginService {
   private static String gDec = MyCheck.C.dec("");
   private static final Map gMixinAccessorSetters = new HashMap();
   private static final Set gClassNamesFromEntityToPig = Set.of("net.minecraft.world.entity.Entity", "net.minecraft.world.entity.LivingEntity", "net.minecraft.world.entity.Mob", "net.minecraft.world.entity.PathfinderMob", "net.minecraft.world.entity.AgeableMob", "net.minecraft.world.entity.animal.Animal", "net.minecraft.world.entity.animal.Pig");

   public void initializeLaunch(ILaunchPluginService.ITransformerLoader transformerLoader, NamedPath[] specialPaths) {
      MyLib2.killOtherXformFromMainThread();
      super.initializeLaunch(transformerLoader, specialPaths);
   }

   public EnumSet handlesClass(Type type, boolean b) {
      return MyLib2.isThisNameOtherBadMOD(type.getClassName()) ? EnumSet.of(Phase.AFTER) : EnumSet.noneOf(ILaunchPluginService.Phase.class);
   }

   public String name() {
      return "pig2xform";
   }

   public boolean processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type classType, String reason) {
      boolean bChanged = false;
      if (reason.equals("classloading")) {
         bChanged = this.processClass(classNode);
      } else if (reason.equals("mixin")) {
         bChanged = this.processClass(classNode);
      } else if (reason.equals("computing_frames")) {
      }

      if (bChanged) {
         MyXformer2.doNotResetThisClass(classType.getClassName());
      }

      return bChanged;
   }

   public boolean processClass(ClassNode classNode) {
      if (!MyLib2.isThisNameOtherBadMOD(classNode.name.replace('/', '.'))) {
         return false;
      } else if (classNode.name.contains("/__")) {
         return false;
      } else {
         boolean bChanged = false;
         bChanged |= this.transform_BadEventBus(classNode);
         bChanged |= this.transform_methodWithRunnableArg(classNode);
         bChanged |= this.transform_level_Entity(classNode);
         bChanged |= this.transform_BadThreads(classNode);
         bChanged |= this.transform_mFlashfur(classNode);
         bChanged |= this.transform_crashMethod(classNode);
         bChanged |= this.transform_BadEntityRemover(classNode);
         bChanged |= this.transform_disconnect(classNode);
         bChanged |= this.transform_BadDeleteFiles(classNode);
         bChanged |= this.transform_fakeEntityID(classNode);
         bChanged |= this.transform_BadRender(classNode);
         bChanged |= this.transform_mixinPlugin(classNode);
         bChanged |= this.transform_mixinMethods(classNode);
         bChanged |= this.transform_mixinAccessor(classNode);
         bChanged |= this.transform_playerJammer(classNode);
         bChanged |= this.transform_badDainyuu(classNode);
         bChanged |= this.transform_badCall(classNode);
         bChanged |= this.transform_fixOtherModBug(classNode);
         return bChanged;
      }
   }

   private boolean transform_tempXXXX(ClassNode classNode) {
      boolean bChanged = false;
      Map<String, List<String>> map = new HashMap();

      for(Map.Entry entry : map.entrySet()) {
         String targetClass = (String)entry.getKey();
         List<String> targetMethods = (List)entry.getValue();
         if (classNode.name.contains(targetClass)) {
            for(String targetMethod : targetMethods) {
               for(MethodNode method : classNode.methods) {
                  if (method.name.equals(targetMethod)) {
                  }
               }
            }
         }
      }

      return bChanged;
   }

   private boolean transform_fixOtherModBug(ClassNode classNode) {
      boolean bChanged = false;
      if (classNode.name.equals("com/mega/uom/event/eventhandler/client/screen/CreativeBoneHandler")) {
         for(MethodNode method : classNode.methods) {
            if (method.name.equals("renderAbilityTooltip")) {
               LabelNode labelContinue = new LabelNode();
               InsnList insnList = new InsnList();
               insnList.add(new FieldInsnNode(178, classNode.name, "selected", "Lnet/minecraft/world/item/ItemStack;"));
               insnList.add(new JumpInsnNode(199, labelContinue));
               insnList.add(new InsnNode(177));
               insnList.add(labelContinue);
               method.instructions.insert(insnList);
               bChanged = true;
            } else if (method.name.equals("onKeyPress")) {
               this.makeMethodEmpty(classNode, method);
               bChanged = true;
            }
         }
      }

      return bChanged;
   }

   private boolean transform_playerJammer(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         if (!classNode.name.contains("DragionnTickUpdateProcedure") || !method.name.equals("onEntityTick")) {
            boolean hasSubscribeAnnotation = false;
            if (method.visibleAnnotations != null) {
               for(AnnotationNode aNode : method.visibleAnnotations) {
                  if (aNode.desc.equals("Lnet/minecraftforge/eventbus/api/SubscribeEvent;")) {
                     hasSubscribeAnnotation = true;
                     break;
                  }
               }

               if (hasSubscribeAnnotation) {
                  if (!method.desc.equals("(Lnet/minecraftforge/event/TickEvent$PlayerTickEvent;)V") && (!method.desc.startsWith("(Lnet/minecraftforge/client/event/ScreenEvent") || !method.desc.endsWith(")V")) && !method.desc.equals("(Lnet/minecraftforge/client/event/ViewportEvent$ComputeFov;)V")) {
                     if (method.desc.equals("(Lnet/minecraftforge/event/entity/EntityJoinLevelEvent;)V") || method.desc.equals("(Lnet/minecraftforge/event/entity/living/LivingEvent$LivingTickEvent;)V")) {
                        int eventVar = (method.access & 8) != 0 ? 0 : 1;
                        int entityVar = eventVar + 1;
                        InsnList insnList = new InsnList();
                        insnList.add(new VarInsnNode(25, eventVar));
                        insnList.add(new MethodInsnNode(182, "net/minecraftforge/event/entity/EntityEvent", "getEntity", "()Lnet/minecraft/world/entity/Entity;", false));
                        insnList.add(new VarInsnNode(58, entityVar));
                        LabelNode notPlayer = new LabelNode();
                        insnList.add(new VarInsnNode(25, entityVar));
                        insnList.add(new TypeInsnNode(193, "net/minecraft/world/entity/player/Player"));
                        insnList.add(new JumpInsnNode(153, notPlayer));
                        insnList.add(new InsnNode(177));
                        insnList.add(notPlayer);
                        LabelNode notPig2 = new LabelNode();
                        insnList.add(new VarInsnNode(25, entityVar));
                        insnList.add(new MethodInsnNode(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
                        insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                        insnList.add(new LdcInsnNode("kakiku.pig2mod.entity.Pig2"));
                        insnList.add(new MethodInsnNode(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                        insnList.add(new JumpInsnNode(153, notPig2));
                        insnList.add(new InsnNode(177));
                        insnList.add(notPig2);
                        method.instructions.insert(insnList);
                        bChanged = true;
                     }
                  } else if (this.makeMethodEmpty(classNode, method)) {
                     bChanged = true;
                  }
               }
            }
         }
      }

      return bChanged;
   }

   private boolean transform_mixinAccessor(ClassNode classNode) {
      boolean bChanged = false;
      String ownerClassNameOfMixin = this.getOwnerClassNameFromMixinClass(classNode);
      if (ownerClassNameOfMixin != null) {
         for(MethodNode method : classNode.methods) {
            if (this.isThisMixinAccessorSet(classNode, method)) {
               String targetFieldName = null;
               if (method.visibleAnnotations != null) {
                  for(AnnotationNode annotation : method.visibleAnnotations) {
                     if (annotation.values != null) {
                        for(int i = 0; i < annotation.values.size(); i += 2) {
                           String key = (String)annotation.values.get(i);
                           Object value = annotation.values.get(i + 1);
                           if ("value".equals(key) && value instanceof String) {
                              String stringValue = (String)value;
                              targetFieldName = stringValue;
                           }
                        }
                     }
                  }
               }

               gMixinAccessorSetters.put(classNode.name + "." + method.name, new ArrayList(Arrays.asList(ownerClassNameOfMixin, targetFieldName)));
            }
         }
      }

      for(MethodNode method : classNode.methods) {
         for(AbstractInsnNode insn : method.instructions.toArray()) {
            if (insn instanceof MethodInsnNode mInsn) {
               String var10001 = mInsn.owner;
               List<String> target = (List)gMixinAccessorSetters.get(var10001 + "." + mInsn.name);
               if (target != null) {
                  String targetClassName = (String)target.get(0);
                  String targetFieldName = (String)target.get(1);
                  if (this.isBadDainyuu(targetClassName, targetFieldName)) {
                     this.makeMethodCallEmpty(method, mInsn);
                     bChanged = true;
                  }
               }
            }
         }
      }

      return bChanged;
   }

   private boolean transform_mixinMethods(ClassNode classNode) {
      boolean bChanged = false;
      String ownerClassNameOfMixin = this.getOwnerClassNameFromMixinClass(classNode);
      if (ownerClassNameOfMixin != null && (this.isThisClassNameFromEntityToPig(ownerClassNameOfMixin) || ownerClassNameOfMixin.equals("kakiku.pig2mod.entity.Pig2"))) {
         for(MethodNode method : classNode.methods) {
            if (this.isThisMixinAddedMethod(classNode, method) && (!method.name.startsWith("get") || !method.desc.startsWith("()")) && MyXformer2.getInstance().canMakeMethodEmpty(method) && ((method.access & 8) == 0 || method.desc.startsWith("(Lnet/minecraft/world/entity/Entity;") || method.desc.startsWith("(Lnet/minecraft/world/entity/LivingEntity;"))) {
               InsnList insnList = new InsnList();
               LabelNode labelReturn = new LabelNode();
               LabelNode labelContinue = new LabelNode();
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new JumpInsnNode(198, labelContinue));
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new MethodInsnNode(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
               insnList.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
               insnList.add(new LdcInsnNode("kakiku.pig2mod.entity.Pig2"));
               insnList.add(new MethodInsnNode(182, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
               insnList.add(new JumpInsnNode(154, labelReturn));
               insnList.add(new JumpInsnNode(167, labelContinue));
               insnList.add(labelReturn);
               MyXformer2.getInstance().insertReturn(method, insnList);
               insnList.add(labelContinue);
               method.instructions.insert(insnList);
               bChanged = true;
            }
         }
      }

      return bChanged;
   }

   private boolean transform_mixinPlugin(ClassNode classNode) {
      boolean bChanged = false;
      if (classNode.interfaces.contains("org/spongepowered/asm/mixin/extensibility/IMixinConfigPlugin")) {
         for(MethodNode method : classNode.methods) {
            if ((method.name.equals("getMixins") || method.name.equals("preApply") || method.name.equals("postApply")) && this.makeMethodEmpty(classNode, method)) {
               bChanged = true;
            }
         }
      }

      for(MethodNode method : classNode.methods) {
         boolean needChange = false;

         for(AbstractInsnNode insn : method.instructions.toArray()) {
            if (insn instanceof MethodInsnNode mInsn) {
               if (mInsn.owner.equals("org/spongepowered/asm/mixin/MixinEnvironment") && mInsn.name.equals("getCurrentEnvironment")) {
                  needChange = true;
                  break;
               }
            } else if (insn instanceof FieldInsnNode fInsn) {
               if (insn.getOpcode() == 178 && fInsn.owner.equals("org/spongepowered/asm/mixin/MixinEnvironment") && fInsn.name.equals("currentEnvironment")) {
                  needChange = true;
                  break;
               }
            }
         }

         if (needChange && this.makeMethodEmpty(classNode, method)) {
            bChanged = true;
         }
      }

      return bChanged;
   }

   private boolean transform_BadRender(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         boolean usingSetOrtho = false;
         boolean usingRenderClear = false;
         boolean usingTranslateBig = false;
         boolean usingJNIinvoke = false;
         boolean usingBuilderAssign = false;
         boolean usingGLFWMakeContextCurrent = false;

         for(AbstractInsnNode insn : method.instructions.toArray()) {
            if (insn instanceof MethodInsnNode mInsn) {
               if (mInsn.owner.equals("org/joml/Matrix4f") && mInsn.name.equals("setOrtho")) {
                  usingSetOrtho = true;
               }

               if (mInsn.owner.equals("com/mojang/blaze3d/systems/RenderSystem") && mInsn.name.equals("clear")) {
                  usingRenderClear = true;
               }

               if (mInsn.name.equals("translate") && (mInsn.desc.contains("DDD)") || mInsn.desc.contains("FFF)"))) {
                  AbstractInsnNode var18 = mInsn.getPrevious();
                  if (var18 instanceof LdcInsnNode) {
                     LdcInsnNode ldc = (LdcInsnNode)var18;
                     Object var19 = ldc.cst;
                     if (var19 instanceof Float) {
                        Float f = (Float)var19;
                        if (Math.abs(f) >= 10000.0F) {
                           usingTranslateBig = true;
                        }
                     }

                     var19 = ldc.cst;
                     if (var19 instanceof Double) {
                        Double d = (Double)var19;
                        if (Math.abs(d) >= (double)10000.0F) {
                           usingTranslateBig = true;
                        }
                     }
                  }
               }

               if (mInsn.owner.equals("org/lwjgl/system/JNI") && mInsn.name.startsWith("invoke")) {
                  usingJNIinvoke = true;
               }

               if (mInsn.owner.equals("org/lwjgl/glfw/GLFW") && mInsn.name.equals("glfwMakeContextCurrent")) {
                  usingGLFWMakeContextCurrent = true;
               }
            } else if (insn instanceof FieldInsnNode fInsn) {
               if (fInsn.getOpcode() == 181 && fInsn.owner.equals("com/mojang/blaze3d/vertex/Tesselator") && fInsn.name.equals("f_85907_")) {
                  usingBuilderAssign = true;
               }
            }
         }

         if ((usingSetOrtho && usingRenderClear || usingTranslateBig || usingJNIinvoke || usingBuilderAssign || usingGLFWMakeContextCurrent) && this.makeMethodEmpty(classNode, method)) {
            bChanged = true;
         }
      }

      return bChanged;
   }

   private boolean transform_fakeEntityID(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         if ((method.access & 1280) == 0) {
            if (method.name.equals("m_20148_") && method.desc.equals("()Ljava/util/UUID;")) {
               method.instructions.clear();
               method.tryCatchBlocks.clear();
               method.localVariables = null;
               InsnList insnList = new InsnList();
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new MethodInsnNode(183, "net/minecraft/world/entity/Entity", "m_20148_", "()Ljava/util/UUID;", false));
               insnList.add(new InsnNode(176));
               method.instructions.insert(insnList);
               bChanged = true;
            } else if (method.name.equals("m_19879_") && method.desc.equals("()I")) {
               method.instructions.clear();
               method.tryCatchBlocks.clear();
               method.localVariables = null;
               InsnList insnList = new InsnList();
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new MethodInsnNode(183, "net/minecraft/world/entity/Entity", "m_19879_", "()I", false));
               insnList.add(new InsnNode(172));
               method.instructions.insert(insnList);
               bChanged = true;
            } else if (method.name.equals("m_20084_") && method.desc.equals("(Ljava/util/UUID;)V")) {
               method.instructions.clear();
               method.tryCatchBlocks.clear();
               method.localVariables = null;
               InsnList insnList = new InsnList();
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new VarInsnNode(25, 1));
               insnList.add(new MethodInsnNode(183, "net/minecraft/world/entity/Entity", "m_20084_", "(Ljava/util/UUID;)V", false));
               insnList.add(new InsnNode(177));
               method.instructions.insert(insnList);
               bChanged = true;
            } else if (method.name.equals("m_20234_") && method.desc.equals("(I)V")) {
               method.instructions.clear();
               method.tryCatchBlocks.clear();
               method.localVariables = null;
               InsnList insnList = new InsnList();
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new VarInsnNode(21, 1));
               insnList.add(new MethodInsnNode(183, "net/minecraft/world/entity/Entity", "m_20234_", "(I)V", false));
               insnList.add(new InsnNode(177));
               method.instructions.insert(insnList);
               bChanged = true;
            }
         }
      }

      return bChanged;
   }

   private boolean transform_BadDeleteFiles(ClassNode classNode) {
      boolean bChanged = false;
      Set<MethodNode> fileDeleteMethods = new HashSet();
      Set<MethodNode> entitiesMethods = new HashSet();

      for(MethodNode method0 : classNode.methods) {
         boolean usesEntities = false;
         boolean callsDelete = false;
         ListIterator var9 = method0.instructions.iterator();

         while(var9.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)var9.next();
            if (insn instanceof LdcInsnNode ldc) {
               if ("entities".equals(ldc.cst)) {
                  usesEntities = true;
               }
            }

            if (insn instanceof MethodInsnNode minsn) {
               if (minsn.getOpcode() == 182 && minsn.owner.equals("java/io/File") && minsn.name.equals("delete") && minsn.desc.equals("()Z")) {
                  callsDelete = true;
               }
            }
         }

         if (usesEntities) {
            entitiesMethods.add(method0);
         }

         if (callsDelete) {
            fileDeleteMethods.add(method0);
         }
      }

      for(MethodNode method : fileDeleteMethods) {
         boolean isThisTargetMethod = entitiesMethods.contains(method);
         if (!isThisTargetMethod) {
            for(MethodNode caller : entitiesMethods) {
               if (method.name.contains(caller.name)) {
                  isThisTargetMethod = true;
                  break;
               }

               ListIterator var18 = caller.instructions.iterator();

               while(var18.hasNext()) {
                  AbstractInsnNode insn = (AbstractInsnNode)var18.next();
                  if (insn instanceof MethodInsnNode) {
                     MethodInsnNode minsn = (MethodInsnNode)insn;
                     if (minsn.name.equals(method.name) && minsn.desc.equals(method.desc)) {
                        isThisTargetMethod = true;
                        break;
                     }
                  }
               }

               if (isThisTargetMethod) {
                  break;
               }
            }
         }

         if (isThisTargetMethod && this.makeMethodEmpty(classNode, method)) {
            bChanged = true;
         }
      }

      return bChanged;
   }

   private boolean transform_disconnect(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         ListIterator var5 = method.instructions.iterator();

         while(var5.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)var5.next();
            if (insn instanceof MethodInsnNode methodInsn) {
               if (insn.getOpcode() == 182 && methodInsn.name.equals("m_9942_") && methodInsn.desc.equals("(Lnet/minecraft/network/chat/Component;)V")) {
                  MethodInsnNode staticCall = new MethodInsnNode(184, "kakiku/pig2mod/MyXformData", "disconnect", "(Lnet/minecraft/server/network/ServerGamePacketListenerImpl;Lnet/minecraft/network/chat/Component;)V", false);
                  method.instructions.set(methodInsn, staticCall);
                  bChanged = true;
               }
            }
         }
      }

      return bChanged;
   }

   private boolean transform_badDainyuu(ClassNode classNode) {
      boolean bChanged = false;
      String ownerClassNameOfMixin = this.getOwnerClassNameFromMixinClass(classNode);

      for(MethodNode method : classNode.methods) {
         ListIterator<AbstractInsnNode> it = method.instructions.iterator();

         while(it.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)it.next();
            if ((insn.getOpcode() == 181 || insn.getOpcode() == 179) && insn instanceof FieldInsnNode fieldInsn) {
               String owner = fieldInsn.owner;
               if (ownerClassNameOfMixin != null && owner.equals(classNode.name)) {
                  owner = ownerClassNameOfMixin.replace('.', '/');
                  if (!this.isThisMixinShadowField(classNode, fieldInsn)) {
                     continue;
                  }
               }

               if (this.isBadDainyuu(owner, fieldInsn.name)) {
                  InsnList insnList = new InsnList();
                  if (!fieldInsn.desc.equals("J") && !fieldInsn.desc.equals("D")) {
                     insnList.add(new InsnNode(87));
                  } else {
                     insnList.add(new InsnNode(88));
                  }

                  if (insn.getOpcode() == 181) {
                     insnList.add(new InsnNode(87));
                  }

                  method.instructions.insert(insn, insnList);
                  method.instructions.remove(insn);
                  bChanged = true;
               }
            }
         }
      }

      return bChanged;
   }

   private boolean isBadDainyuu(String className, String fieldName) {
      if (!className.equals("net/minecraft/client/Minecraft") && !className.equals("net/minecraftforge/common/MinecraftForge") && !className.equals("net/minecraft/server/level/ServerLevel") && !className.equals("net/minecraft/world/level/entity/EntitySection") && !className.equals("net/minecraft/world/level/entity/PersistentEntitySectionManager") && !className.equals("net/minecraft/client/multiplayer/ClientLevel") && !className.equals("net/minecraft/world/level/entity/TransientEntitySectionManager") && !className.equals("net/minecraft/world/level/entity/EntityTickList") && !className.equals("net/minecraft/client/renderer/entity/EntityRenderDispatcher") && !className.equals("net/minecraft/world/entity/player/Player") && !className.equals("com/mojang/blaze3d/vertex/BufferBuilder") && !className.equals("com/mojang/blaze3d/systems/RenderSystem") && !className.equals("com/mojang/blaze3d/platform/Window") && !className.equals("net/minecraft/client/renderer/GameRenderer") && !className.equals("net/minecraft/network/Connection") && !className.equals("net/minecraft/client/multiplayer/ClientPacketListener") && !className.equals("net/minecraft/client/player/LocalPlayer") && !className.equals("net/minecraft/client/renderer/LevelRenderer") && !className.equals("net/minecraft/client/Options") && !className.equals("net/minecraft/server/MinecraftServer") && !className.equals("net/minecraftforge/common/capabilities/CapabilityProvider") && !className.equals("cpw/mods/modlauncher/Launcher") && !className.equals("cpw/mods/modlauncher/ClassTransformer") && !className.equals("cpw/mods/modlauncher/LaunchPluginHandler") && !className.equals("net/minecraft/util/ClassInstanceMultiMap") && !className.equals("net/minecraft/world/level/entity/EntitySectionStorage") && !className.equals("net/minecraft/server/level/ServerChunkCache") && !className.startsWith("net/minecraft/server/level/ChunkMap") && !className.equals("net/minecraft/world/level/entity/EntityLookup") && !className.equals("net/minecraft/world/level/entity/LevelEntityGetterAdapter") && !className.equals("net/minecraft/server/level/ServerEntity") && !className.equals("net/minecraft/server/level/DistanceManager") && !className.equals("net/minecraft/server/level/Ticket") && !className.equals("net/minecraft/server/level/TickingTracker") && !className.equals("net/minecraft/server/level/ChunkHolder") && !className.equals("net/minecraft/network/syncher/SynchedEntityData") && !className.equals("net/minecraft/world/entity/ai/Brain") && !className.equals("net/minecraft/world/entity/ai/goal/GoalSelector") && !className.equals("net/minecraft/world/entity/player/Abilities") && !className.equals("net/minecraft/client/gui/screens/DeathScreen")) {
         if (this.isThisClassNameFromEntityToPig(className)) {
            if (fieldName == null) {
               return true;
            }

            if (fieldName.equals("f_146795_") || fieldName.equals("removalReason") || fieldName.equals("canUpdate") || fieldName.equals("f_19853_") || fieldName.equals("level") || fieldName.equals("f_146801_") || fieldName.equals("levelCallback") || fieldName.equals("f_19840_") || fieldName.equals("invulnerable") || fieldName.equals("isAddedToWorld") || fieldName.equals("f_20919_") || fieldName.equals("deathTime") || fieldName.equals("f_20916_") || fieldName.equals("hurtTime") || fieldName.equals("f_20890_") || fieldName.equals("dead") || fieldName.equals("f_19794_") || fieldName.equals("noPhysics") || fieldName.equals("f_20939_") || fieldName.equals("brain") || fieldName.equals("f_21345_") || fieldName.equals("goalSelector") || fieldName.equals("f_21346_") || fieldName.equals("targetSelector") || fieldName.equals("f_19804_") || fieldName.equals("entityData")) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean isThisClassNameFromEntityToPig(String pSlashName) {
      String name = pSlashName.replace('/', '.');
      return gClassNamesFromEntityToPig.contains(name);
   }

   private String getOwnerClassNameFromMixinClass(ClassNode classNode) {
      if (classNode.invisibleAnnotations != null) {
         for(AnnotationNode annotation : classNode.invisibleAnnotations) {
            if (annotation.desc.equals("Lorg/spongepowered/asm/mixin/Mixin;")) {
               List<?> aValues = annotation.values;

               for(int i = 0; i < aValues.size(); i += 2) {
                  Object var7 = aValues.get(i);
                  if (var7 instanceof String) {
                     String key = (String)var7;
                     if (key.equals("value")) {
                        Object var15 = aValues.get(i + 1);
                        if (var15 instanceof List) {
                           for(Object type0 : (List)var15) {
                              if (type0 instanceof Type) {
                                 Type type = (Type)type0;
                                 String originalClassName = type.getClassName();
                                 return originalClassName;
                              }
                           }
                        }
                     } else if (key.equals("targets")) {
                        Object var8 = aValues.get(i + 1);
                        if (var8 instanceof List) {
                           for(Object targetName0 : (List)var8) {
                              if (targetName0 instanceof String) {
                                 String targetName = (String)targetName0;
                                 return targetName;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   private boolean isThisMixinShadowField(ClassNode classNode, FieldInsnNode fieldInsn) {
      for(FieldNode field : classNode.fields) {
         if (field.name.equals(fieldInsn.name) && field.visibleAnnotations != null) {
            for(AnnotationNode annotationNode : field.visibleAnnotations) {
               if (annotationNode.desc.equals("Lorg/spongepowered/asm/mixin/Shadow;")) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean isThisMixinAddedMethod(ClassNode classNode, MethodNode method) {
      if (!method.name.endsWith("init>") && !method.name.startsWith("lambda$")) {
         List<AnnotationNode> annotations = new ArrayList();
         if (method.visibleAnnotations != null) {
            annotations.addAll(method.visibleAnnotations);
         }

         if (method.invisibleAnnotations != null) {
            annotations.addAll(method.invisibleAnnotations);
         }

         if (annotations.size() == 0) {
            return true;
         } else {
            for(AnnotationNode annotationNode : annotations) {
               if (annotationNode.desc != null && annotationNode.desc.contains("/mixin/Unique;")) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean isChangeAnnotation(String annotation) {
      return annotation.contains("mixin/injection/Inject;") || annotation.contains("mixin/injection/Modify") || annotation.contains("mixin/injection/Redirect") || annotation.contains("mixin/Overwrite") || annotation.contains("mixinextras/injector/wrap") || annotation.contains("mixinextras/injector/v2/Wrap") || annotation.contains("mixinextras/injector/modify") || annotation.contains("/Override;");
   }

   private boolean isThisMixinAccessorSet(ClassNode classNode, MethodNode method) {
      List<AnnotationNode> annotations = new ArrayList();
      if (method.visibleAnnotations != null) {
         annotations.addAll(method.visibleAnnotations);
      }

      if (method.invisibleAnnotations != null) {
         annotations.addAll(method.invisibleAnnotations);
      }

      for(AnnotationNode annotationNode : annotations) {
         if (annotationNode.desc != null && annotationNode.desc.contains("/mixin/gen/Accessor;") && !method.desc.startsWith("()")) {
            return true;
         }
      }

      return false;
   }

   private boolean transform_badCall(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         for(AbstractInsnNode insn : method.instructions.toArray()) {
            if (insn instanceof MethodInsnNode mInsn) {
               boolean needChange = false;
               String var10000 = mInsn.owner.replace("/", ".");
               String calleeName = var10000 + "." + mInsn.name + "()";

               for(String badCalleeName : MyXformer2.getInstance().mBadCalleeNames) {
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

      return bChanged;
   }

   private boolean transform_BadEntityRemover(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         if (MyXformer2.getInstance().canMakeMethodEmpty(method)) {
            boolean hasCallUnsafeGetPut = false;
            boolean hasNewRemoveEntitiesPacket = false;
            boolean hasRemovalReasonAssignment = false;
            boolean hasCanUpdateAssignment = false;
            boolean hasEntityLevel = false;
            boolean hasCallbackMove = false;
            boolean hasCallSetRemoved = false;
            InsnList insns = method.instructions;
            ListIterator var13 = insns.iterator();

            while(var13.hasNext()) {
               AbstractInsnNode insn = (AbstractInsnNode)var13.next();
               if (insn.getOpcode() == 182 || insn.getOpcode() == 184) {
                  MethodInsnNode mInsn = (MethodInsnNode)insn;
                  if (mInsn.owner.equals("sun/misc/Unsafe") && (mInsn.name.equals("putObject") || mInsn.name.equals("getObject"))) {
                     hasCallUnsafeGetPut = true;
                     break;
                  }

                  if ((mInsn.owner.equals("net/minecraft/world/entity/Entity") || mInsn.owner.equals("net/minecraft/world/entity/LivingEntity")) && mInsn.name.equals("m_142467_")) {
                     hasCallSetRemoved = true;
                     break;
                  }
               } else if (insn.getOpcode() == 187) {
                  TypeInsnNode tInsn = (TypeInsnNode)insn;
                  if (tInsn.desc.equals("net/minecraft/network/protocol/game/ClientboundRemoveEntitiesPacket")) {
                     hasNewRemoveEntitiesPacket = true;
                     break;
                  }
               } else if (insn.getOpcode() != 181) {
                  if (insn.getOpcode() == 185) {
                     MethodInsnNode mInsn = (MethodInsnNode)insn;
                     if (mInsn.owner.equals("net/minecraft/world/level/entity/EntityInLevelCallback") && mInsn.name.equals("m_142044_")) {
                        hasCallbackMove = true;
                        break;
                     }
                  }
               } else {
                  FieldInsnNode fieldInsn = (FieldInsnNode)insn;
                  if ((fieldInsn.owner.equals("net/minecraft/world/entity/Entity") || fieldInsn.owner.equals("net/minecraft/world/entity/LivingEntity")) && fieldInsn.name.equals("f_146795_") && fieldInsn.desc.equals("Lnet/minecraft/world/entity/Entity$RemovalReason;")) {
                     hasRemovalReasonAssignment = true;
                     break;
                  }

                  if ((fieldInsn.owner.equals("net/minecraft/world/entity/Entity") || fieldInsn.owner.equals("net/minecraft/world/entity/LivingEntity")) && fieldInsn.name.equals("canUpdate") && fieldInsn.desc.equals("Z")) {
                     hasCanUpdateAssignment = true;
                     break;
                  }

                  if ((fieldInsn.owner.equals("net/minecraft/world/entity/Entity") || fieldInsn.owner.equals("net/minecraft/world/entity/LivingEntity")) && fieldInsn.name.equals("f_19853_") && fieldInsn.desc.equals("Lnet/minecraft/world/level/Level;")) {
                     hasEntityLevel = true;
                     break;
                  }

                  if ((fieldInsn.owner.equals("net/minecraft/world/entity/Entity") || fieldInsn.owner.equals("net/minecraft/world/entity/LivingEntity")) && fieldInsn.name.equals("f_146801_") && fieldInsn.desc.equals("Lnet/minecraft/world/level/entity/EntityInLevelCallback;")) {
                     hasEntityLevel = true;
                     break;
                  }
               }
            }

            boolean hasArgEntity = method.desc.contains("Lnet/minecraft/world/entity/Entity;");
            boolean hasArgLivingEntity = method.desc.contains("Lnet/minecraft/world/entity/LivingEntity;");
            if (!hasArgEntity && !hasArgLivingEntity) {
               if (hasRemovalReasonAssignment && this.makeMethodEmpty(classNode, method)) {
                  bChanged = true;
               }
            } else if (hasCallUnsafeGetPut || hasNewRemoveEntitiesPacket || hasRemovalReasonAssignment || hasCanUpdateAssignment || hasEntityLevel || hasCallbackMove || hasCallSetRemoved) {
               int entityIndex = this.getEntityArgumentIndex(method);
               if (entityIndex >= 0) {
                  InsnList patch = new InsnList();
                  LabelNode labelReturn = new LabelNode();
                  LabelNode labelContinue = new LabelNode();
                  patch.add(new VarInsnNode(25, entityIndex));
                  patch.add(new JumpInsnNode(198, labelContinue));
                  boolean bProtectPig2 = true;
                  if (classNode.name.equals("net/mcreator/dragionnsstuff/procedures/DragionnTickUpdateProcedure") && method.name.equals("execute")) {
                     bProtectPig2 = false;
                  }

                  if (bProtectPig2) {
                     patch.add(new VarInsnNode(25, entityIndex));
                     patch.add(new MethodInsnNode(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
                     patch.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                     patch.add(new LdcInsnNode("pig2mod"));
                     patch.add(new MethodInsnNode(182, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false));
                     patch.add(new JumpInsnNode(154, labelReturn));
                  }

                  patch.add(new VarInsnNode(25, entityIndex));
                  patch.add(new MethodInsnNode(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
                  patch.add(new MethodInsnNode(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                  patch.add(new LdcInsnNode("Player"));
                  patch.add(new MethodInsnNode(182, "java/lang/String", "endsWith", "(Ljava/lang/String;)Z", false));
                  patch.add(new JumpInsnNode(154, labelReturn));
                  patch.add(new JumpInsnNode(167, labelContinue));
                  patch.add(labelReturn);
                  MyXformer2.getInstance().insertReturn(method, patch);
                  patch.add(labelContinue);
                  method.instructions.insert(patch);
                  bChanged = true;
               }
            }
         }
      }

      return bChanged;
   }

   private int getEntityArgumentIndex(MethodNode method) {
      int currentSlot = (method.access & 8) != 0 ? 0 : 1;

      for(Type type : Type.getArgumentTypes(method.desc)) {
         String d = type.getDescriptor();
         if (d.equals("Lnet/minecraft/world/entity/Entity;") || d.equals("Lnet/minecraft/world/entity/LivingEntity;")) {
            return currentSlot;
         }

         currentSlot += type.getSize();
      }

      return -1;
   }

   private boolean transform_crashMethod(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         boolean bFound = false;
         ListIterator var6 = method.instructions.iterator();

         while(var6.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)var6.next();
            if (insn instanceof MethodInsnNode methodInsn) {
               if (insn.getOpcode() == 184 && methodInsn.owner.equals("net/minecraft/client/Minecraft") && methodInsn.name.equals("m_91332_")) {
                  bFound = true;
                  break;
               }
            }
         }

         if (bFound && this.makeMethodEmpty(classNode, method)) {
            bChanged = true;
         }
      }

      return bChanged;
   }

   private boolean transform_mFlashfur(ClassNode classNode) {
      boolean bChanged = false;
      if (classNode.name.contains("MetapotentFlashfurRenderer")) {
         for(MethodNode method : classNode.methods) {
            if (method.name.equals("render") && method.desc.endsWith(";FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")) {
               LabelNode skipLabel = new LabelNode();
               InsnList insnList = new InsnList();
               insnList.add(new VarInsnNode(25, 1));
               insnList.add(new MethodInsnNode(184, "kakiku/pig2mod/MyXformData", "shouldRender", "(Lnet/minecraft/world/entity/Entity;)Z", false));
               insnList.add(new JumpInsnNode(154, skipLabel));
               insnList.add(new InsnNode(177));
               insnList.add(skipLabel);
               method.instructions.insert(insnList);
               bChanged = true;
            }
         }
      }

      return bChanged;
   }

   private boolean transform_BadThreads(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         ListIterator var5 = method.instructions.iterator();

         while(var5.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)var5.next();
            if (insn instanceof MethodInsnNode methodInsn) {
               if (insn.getOpcode() == 182) {
                  if (methodInsn.name.equals("start") && methodInsn.desc.equals("()Ljava/lang/Process;")) {
                     method.instructions.insertBefore(methodInsn, new InsnNode(87));
                     methodInsn.owner = "kakiku/pig2mod/MyXformData";
                     methodInsn.name = "start";
                     methodInsn.desc = "()Ljava/lang/Process;";
                     methodInsn.setOpcode(184);
                     bChanged = true;
                  } else if (methodInsn.owner.equals("java/lang/Thread") && methodInsn.name.equals("start") && methodInsn.desc.equals("()V")) {
                     method.instructions.set(methodInsn, new InsnNode(87));
                     bChanged = true;
                  } else if (methodInsn.owner.equals("java/util/concurrent/ForkJoinPool") && methodInsn.name.equals("execute") && methodInsn.desc.equals("(Ljava/lang/Runnable;)V")) {
                     method.instructions.insertBefore(methodInsn, new InsnNode(87));
                     method.instructions.set(methodInsn, new InsnNode(87));
                     bChanged = true;
                  } else if (methodInsn.owner.equals("java/util/Timer") && (methodInsn.name.equals("scheduleAtFixedRate") || methodInsn.name.equals("schedule"))) {
                     if (methodInsn.desc.equals("(Ljava/util/TimerTask;JJ)V")) {
                        method.instructions.insertBefore(methodInsn, new InsnNode(88));
                        method.instructions.insertBefore(methodInsn, new InsnNode(88));
                     } else if (methodInsn.desc.equals("(Ljava/util/TimerTask;J)V")) {
                        method.instructions.insertBefore(methodInsn, new InsnNode(88));
                     } else if (methodInsn.desc.equals("(Ljava/util/TimerTask;Ljava/util/Date;)V")) {
                        method.instructions.insertBefore(methodInsn, new InsnNode(87));
                     } else if (methodInsn.desc.equals("(Ljava/util/TimerTask;Ljava/util/Date;J)V")) {
                        method.instructions.insertBefore(methodInsn, new InsnNode(88));
                        method.instructions.insertBefore(methodInsn, new InsnNode(87));
                     }

                     method.instructions.insertBefore(methodInsn, new InsnNode(87));
                     method.instructions.set(methodInsn, new InsnNode(87));
                     bChanged = true;
                  }
               }
            }

            if (insn instanceof MethodInsnNode methodInsn) {
               if (insn.getOpcode() == 184 && methodInsn.owner.equals("java/util/concurrent/Executors") && (methodInsn.name.equals("newScheduledThreadPool") || methodInsn.name.equals("newSingleThreadScheduledExecutor") || methodInsn.name.equals("newFixedThreadPool")) && (methodInsn.desc.endsWith(")Ljava/util/concurrent/ScheduledExecutorService;") || methodInsn.desc.endsWith(")Ljava/util/concurrent/ExecutorService;")) && !classNode.name.contains("com/mega/uom/")) {
                  methodInsn.owner = "kakiku/pig2mod/MyXformData";
                  methodInsn.name = methodInsn.name;
                  bChanged = true;
               }
            }

            if (insn instanceof MethodInsnNode methodInsn) {
               if (insn.getOpcode() == 185) {
                  if (methodInsn.owner.equals("java/util/concurrent/ScheduledExecutorService") && methodInsn.name.equals("scheduleAtFixedRate") && methodInsn.desc.equals("(Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;") && !classNode.name.replace('/', '.').startsWith("com.mega.uom.")) {
                     MethodInsnNode newInsn = new MethodInsnNode(184, "kakiku/pig2mod/MyXformData", "scheduleAtFixedRate", "(Ljava/util/concurrent/ScheduledExecutorService;Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;", false);
                     method.instructions.set(methodInsn, newInsn);
                     bChanged = true;
                  } else if (methodInsn.owner.equals("java/util/concurrent/ExecutorService") && methodInsn.name.equals("execute") && methodInsn.desc.equals("(Ljava/lang/Runnable;)V")) {
                     method.instructions.insertBefore(methodInsn, new InsnNode(87));
                     method.instructions.set(methodInsn, new InsnNode(87));
                     bChanged = true;
                  }
               }
            }
         }

         if (classNode.superName.equals("java/lang/Thread") && method.name.equals("run") && method.desc.equals("()V") || classNode.interfaces.contains("java/lang/Runnable") && method.name.equals("run") && method.desc.equals("()V") || classNode.interfaces.contains("java/util/concurrent/Callable") && method.name.equals("call")) {
            this.makeMethodEmpty(classNode, method);
            bChanged = true;
         }
      }

      return bChanged;
   }

   private boolean transform_level_Entity(ClassNode classNode) {
      boolean bChanged = false;
      if (this.isBadClassForLevel(classNode) || "net/minecraft/world/item/SwordItem".equals(classNode.superName) || "net/minecraft/world/item/TieredItem".equals(classNode.superName) || "net/minecraft/world/item/Item".equals(classNode.superName) && !classNode.name.toLowerCase().contains("egg") && !classNode.name.toLowerCase().contains("spawn")) {
         for(MethodNode method : classNode.methods) {
            if (this.isBadMethodForLevel(classNode, method)) {
               System.setProperty("Pig2.needMaxFight", "true");
               if (this.makeMethodEmpty(classNode, method)) {
                  bChanged = true;
                  continue;
               }
            }

            for(int i = 0; i < method.instructions.size(); ++i) {
               AbstractInsnNode insn = method.instructions.get(i);
               if (insn.getOpcode() == 180 && insn instanceof FieldInsnNode) {
                  FieldInsnNode fieldInsn = (FieldInsnNode)insn;
                  if (fieldInsn.name.equals("f_19853_") && fieldInsn.desc.equals("Lnet/minecraft/world/level/Level;")) {
                     MethodInsnNode staticCall = new MethodInsnNode(184, "kakiku/pig2mod/MyXformData", "level_Entity", "(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/Level;", false);
                     method.instructions.set(fieldInsn, staticCall);
                     bChanged = true;
                  }
               }

               if (insn.getOpcode() == 182 && insn instanceof MethodInsnNode) {
                  MethodInsnNode methodInsn = (MethodInsnNode)insn;
                  if (methodInsn.name.equals("m_9236_") && methodInsn.desc.equals("()Lnet/minecraft/world/level/Level;")) {
                     MethodInsnNode staticCall = new MethodInsnNode(184, "kakiku/pig2mod/MyXformData", "level_Entity", "(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/Level;", false);
                     method.instructions.set(methodInsn, staticCall);
                     bChanged = true;
                  } else if (methodInsn.name.equals("m_284548_") && methodInsn.desc.equals("()Lnet/minecraft/server/level/ServerLevel;")) {
                     MethodInsnNode staticCall = new MethodInsnNode(184, "kakiku/pig2mod/MyXformData", "serverLevel_ServerPlayer", "(Lnet/minecraft/server/level/ServerPlayer;)Lnet/minecraft/server/level/ServerLevel;", false);
                     method.instructions.set(methodInsn, staticCall);
                     bChanged = true;
                  }
               }
            }

            String name = method.name;
            String desc = method.desc;
            if (name.equals("m_6883_") && desc.equals("(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;IZ)V") || name.equals("m_7203_") && desc.equals("(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;") || name.equals("m_5551_") && desc.equals("(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V") || name.equals("m_5929_") && desc.equals("(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;I)V")) {
               int levelArgIndex = this.getLevelArgumentIndex(desc);
               if (levelArgIndex >= 1) {
                  InsnList insert = new InsnList();
                  insert.add(new VarInsnNode(25, levelArgIndex));
                  insert.add(new MethodInsnNode(184, "kakiku/pig2mod/MyXformData", "levelArg", "(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/Level;", false));
                  insert.add(new VarInsnNode(58, levelArgIndex));
                  method.instructions.insert(insert);
                  bChanged = true;
               }
            }
         }
      }

      return bChanged;
   }

   private int getLevelArgumentIndex(String desc) {
      Type[] argTypes = Type.getArgumentTypes(desc);
      String levelDesc = "Lnet/minecraft/world/level/Level;";
      int index = 1;

      for(Type type : argTypes) {
         if (type.getDescriptor().equals(levelDesc)) {
            return index;
         }

         index += type.getSize();
      }

      return 0;
   }

   private boolean isBadClassForLevel(ClassNode classNode) {
      for(MethodNode method : classNode.methods) {
         if (this.isBadMethodForLevel(classNode, method)) {
            return true;
         }
      }

      return false;
   }

   private boolean isBadMethodForLevel(ClassNode classNode, MethodNode method) {
      if (method.name.toLowerCase().contains("addentity")) {
         return false;
      } else {
         for(int i = 0; i < method.instructions.size(); ++i) {
            AbstractInsnNode insn = method.instructions.get(i);
            if (insn.getOpcode() == 180 && insn instanceof FieldInsnNode) {
               FieldInsnNode fieldInsn = (FieldInsnNode)insn;
               if (fieldInsn.name.equals("f_143244_") && fieldInsn.desc.equals("Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;")) {
                  return true;
               }

               if (fieldInsn.name.equals("f_171631_") && fieldInsn.desc.equals("Lnet/minecraft/world/level/entity/TransientEntitySectionManager;")) {
                  return true;
               }
            }

            if (insn.getOpcode() == 187 && insn instanceof TypeInsnNode) {
               TypeInsnNode newInsn = (TypeInsnNode)insn;
               if ("net/minecraft/server/level/ServerLevel".equals(newInsn.desc) || "net/minecraft/client/multiplayer/ClientLevel".equals(newInsn.desc) || "net/minecraft/world/level/Level".equals(newInsn.desc)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean transform_methodWithRunnableArg(ClassNode classNode) {
      boolean bChanged = false;

      for(MethodNode method : classNode.methods) {
         Type[] argTypes = Type.getArgumentTypes(method.desc);
         boolean hasRunnableArg = Arrays.stream(argTypes).anyMatch((t) -> t.getSort() == 10 && t.getInternalName().equals("java/lang/Runnable"));
         if (hasRunnableArg && !classNode.superName.equals("net/minecraft/client/KeyMapping") && (!classNode.name.contains("AccessChecker") || !method.name.contains("performPrivilegedAction")) && this.makeMethodEmpty(classNode, method)) {
            bChanged = true;
         }
      }

      return bChanged;
   }

   private boolean transform_BadEventBus(ClassNode classNode) {
      boolean bChanged = false;
      if (classNode.superName.equals("net/minecraftforge/eventbus/EventBus") && !classNode.methods.isEmpty()) {
         for(MethodNode methodNode : classNode.methods) {
            if ((methodNode.name + methodNode.desc).equals("register(Ljava/lang/Object;)V")) {
               methodNode.instructions.clear();
               methodNode.tryCatchBlocks.clear();
               methodNode.localVariables = null;
               InsnList insnList = new InsnList();
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new VarInsnNode(25, 1));
               insnList.add(new MethodInsnNode(183, "net/minecraftforge/eventbus/EventBus", "register", "(Ljava/lang/Object;)V", false));
               insnList.add(new InsnNode(177));
               methodNode.instructions.add(insnList);
               bChanged = true;
            } else if ((methodNode.name + methodNode.desc).equals("unregister(Ljava/lang/Object;)V")) {
               methodNode.instructions.clear();
               methodNode.tryCatchBlocks.clear();
               methodNode.localVariables = null;
               InsnList insnList = new InsnList();
               insnList.add(new InsnNode(177));
               methodNode.instructions.add(insnList);
               bChanged = true;
            } else if ((methodNode.name + methodNode.desc).equals("post(Lnet/minecraftforge/eventbus/api/Event;Lnet/minecraftforge/eventbus/api/IEventBusInvokeDispatcher;)Z")) {
               methodNode.instructions.clear();
               methodNode.tryCatchBlocks.clear();
               methodNode.localVariables = null;
               InsnList insnList = new InsnList();
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new VarInsnNode(25, 1));
               insnList.add(new VarInsnNode(25, 2));
               insnList.add(new MethodInsnNode(183, "net/minecraftforge/eventbus/EventBus", "post", "(Lnet/minecraftforge/eventbus/api/Event;Lnet/minecraftforge/eventbus/api/IEventBusInvokeDispatcher;)Z", false));
               insnList.add(new InsnNode(172));
               methodNode.instructions.add(insnList);
               bChanged = true;
            } else if ((methodNode.name + methodNode.desc).equals("post(Lnet/minecraftforge/eventbus/api/Event;)Z")) {
               methodNode.instructions.clear();
               methodNode.tryCatchBlocks.clear();
               methodNode.localVariables = null;
               InsnList insnList = new InsnList();
               insnList.add(new VarInsnNode(25, 0));
               insnList.add(new VarInsnNode(25, 1));
               insnList.add(new MethodInsnNode(183, "net/minecraftforge/eventbus/EventBus", "post", "(Lnet/minecraftforge/eventbus/api/Event;)Z", false));
               insnList.add(new InsnNode(172));
               methodNode.instructions.add(insnList);
               bChanged = true;
            }

            if (Type.getReturnType(methodNode.desc).equals(Type.VOID_TYPE)) {
               Type[] args = Type.getArgumentTypes(methodNode.desc);
               if (args.length == 1) {
                  String arg1Name = args[0].getInternalName();
                  if (arg1Name.equals("net/minecraftforge/eventbus/api/Event") || arg1Name.equals("net/minecraftforge/eventbus/api/GenericEvent")) {
                     methodNode.instructions.clear();
                     methodNode.tryCatchBlocks.clear();
                     methodNode.localVariables = null;
                     InsnList insnList = new InsnList();
                     insnList.add(new InsnNode(177));
                     methodNode.instructions.add(insnList);
                     bChanged = true;
                  }
               }
            }
         }
      }

      return bChanged;
   }

   private boolean makeMethodEmpty(ClassNode classNode, MethodNode method) {
      if (!MyXformer2.getInstance().canMakeMethodEmpty(method)) {
         return false;
      } else if (method.name.equals("queueServerWork") && method.desc.equals("(ILjava/lang/Runnable;)V")) {
         return false;
      } else if (classNode.name.toLowerCase().contains("shader")) {
         return false;
      } else {
         MyXformer2.getInstance().makeMethodEmpty(classNode, method);
         return true;
      }
   }

   public void makeMethodCallEmpty(MethodNode method, MethodInsnNode methodInsn) {
      MyXformer2.getInstance().makeMethodCallEmpty(method, methodInsn);
   }
}

