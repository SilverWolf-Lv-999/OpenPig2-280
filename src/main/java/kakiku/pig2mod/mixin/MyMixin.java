package kakiku.pig2mod.mixin;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kakiku.pig2mod.xform.MyLib2;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MyMixin implements IMixinConfigPlugin {
   public void onLoad(String mixinPackage) {
   }

   public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
      return true;
   }

   public void acceptTargets(Set myTargets, Set otherTargets) {
      this.myMain("acceptTargets()");
   }

   public List getMixins() {
      this.myMain("getMixins()");
      return null;
   }

   public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
   }

   public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   private void MyLogMixin(String string) {
   }

   private boolean isThisNameMinecraftVanillaMixin(String name) {
      for(String blackName : MyLib2.gBlackListNames) {
         if (name.replace('/', '.').startsWith(blackName)) {
            return false;
         }
      }

      if (name.startsWith("net/minecraft")) {
         return true;
      } else if (name.startsWith("java/")) {
         return true;
      } else if (name.startsWith("com/mojang/")) {
         return true;
      } else if (name.startsWith("org/lwjgl/")) {
         return true;
      } else if (name.startsWith("jdk/")) {
         return true;
      } else if (name.startsWith("cpw/mods/")) {
         return true;
      } else if (name.startsWith("com/google/")) {
         return true;
      } else {
         return false;
      }
   }

   private void myMain(String phase) {
      this.editClassNodeMethods(phase);
   }

   private void editClassNodeMethods(String phase) {
      List<MethodNode> returnMethods = null;
      ArrayList<Object> configs = this.getPendingConfigs();
      if (configs != null) {
         for(Object config : configs) {
            String packageName = this.getPackageNameFromMixinConfig(config);
            if (!packageName.startsWith(MyMixin.class.getPackageName()) && !MyLib2.isThisNameWhiteListedMOD(packageName)) {
               ArrayList<Object> mixins = null;

               try {
                  Field mixinsField = config.getClass().getDeclaredField("mixins");
                  mixinsField.setAccessible(true);

                  for(Object mixinInfo : (ArrayList)mixinsField.get(config)) {
                     String classNameVanilla = "";

                     try {
                        Field targetClassNamesField = mixinInfo.getClass().getDeclaredField("targetClassNames");
                        targetClassNamesField.setAccessible(true);
                        ArrayList<String> targetClassNames = (ArrayList)targetClassNamesField.get(mixinInfo);
                        classNameVanilla = (String)targetClassNames.get(0);
                     } catch (Throwable var18) {
                        classNameVanilla = "";
                     }

                     if (this.isThisNameMinecraftVanillaMixin(classNameVanilla) || MyLib2.isThisNameMyMOD(classNameVanilla.replace('/', '.'))) {
                        Object pendingState = null;

                        try {
                           Field pendingStateField = mixinInfo.getClass().getDeclaredField("pendingState");
                           pendingStateField.setAccessible(true);
                           pendingState = pendingStateField.get(mixinInfo);
                           if (pendingState != null) {
                              ClassNode classNode = null;

                              try {
                                 Field classNodeField = pendingState.getClass().getDeclaredField("classNode");
                                 classNodeField.setAccessible(true);
                                 classNode = (ClassNode)classNodeField.get(pendingState);
                                 this.editMethods(classNode, classNameVanilla);
                              } catch (Throwable var16) {
                                 Object var27 = null;
                              }
                           } else if (!phase.equals("acceptTargets()") && !phase.equals("getMixins()")) {
                              MyLib2.SystemExitForDebug();
                           }
                        } catch (Throwable var17) {
                           pendingState = null;
                        }
                     }
                  }
               } catch (Throwable var19) {
                  Object var20 = null;
               }
            }
         }
      }

   }

   private void editMethods(ClassNode classNode, String classNameVanilla) {
      try {
         Iterator<MethodNode> iterator = classNode.methods.iterator();

         while(iterator.hasNext()) {
            MethodNode method = (MethodNode)iterator.next();
            List<AnnotationNode> visibleAnnotations = method.visibleAnnotations;
            String methodNameVanilla = "?";
            boolean needCheckForDebug = false;
            boolean needRemove = false;
            if (visibleAnnotations == null) {
               methodNameVanilla = method.name;
               if (this.isBadMixinMethod(classNameVanilla, methodNameVanilla, (String)null)) {
                  needRemove = true;
               }
            } else {
               for(AnnotationNode annotationNode : visibleAnnotations) {
                  if (isChangeAnnotation(annotationNode.desc)) {
                     needCheckForDebug = true;
                     methodNameVanilla = "?";
                     if (annotationNode.values != null) {
                        for(int i = 0; i < annotationNode.values.size() - 1; ++i) {
                           Object var14 = annotationNode.values.get(i);
                           if (var14 instanceof String) {
                              String str1 = (String)var14;
                              if (str1.equals("method")) {
                                 var14 = annotationNode.values.get(i + 1);
                                 if (var14 instanceof ArrayList) {
                                    ArrayList<?> list = (ArrayList)var14;
                                    if (!list.isEmpty()) {
                                       methodNameVanilla = (String)list.get(0);
                                       if (methodNameVanilla.contains("(")) {
                                          methodNameVanilla = methodNameVanilla.substring(0, methodNameVanilla.indexOf("("));
                                       }
                                       break;
                                    }
                                 }
                              }
                           }
                        }
                     }

                     if (methodNameVanilla.equals("?") && (annotationNode.desc.endsWith("/Overwrite;") || annotationNode.desc.endsWith("/Override;"))) {
                        methodNameVanilla = method.name;
                     }

                     if (this.isBadMixinMethod(classNameVanilla, methodNameVanilla, annotationNode.desc)) {
                        needRemove = true;
                        break;
                     }
                  }
               }
            }

            if (classNode.name.contains("canary/mixin/world/tick_scheduler/LevelChunkTicksMixin") && method.name.equals("reinit")) {
               needRemove = true;
            }

            if (needRemove) {
               iterator.remove();
            } else if (needCheckForDebug) {
            }
         }
      } catch (Exception var15) {
      }

   }

   private boolean isBadMixinMethod(String classNameVanilla, String methodNameVanilla, String annotation) {
      if (MyLib2.isThisNameMyMOD(classNameVanilla.replace('/', '.'))) {
         return true;
      } else if (methodNameVanilla.endsWith("init>")) {
         return false;
      } else if (!methodNameVanilla.contains("defineSynchedData") && !methodNameVanilla.equals("m_8097_")) {
         if (classNameVanilla.endsWith("/EntityRenderDispatcher") && methodNameVanilla.equals("reload")) {
            return false;
         } else if (this.isThisMethodOverride(classNameVanilla, methodNameVanilla)) {
            return true;
         } else {
            return isChangeAnnotation(annotation);
         }
      } else {
         return false;
      }
   }

   public static boolean isChangeAnnotation(String annotation) {
      if (annotation == null) {
         return false;
      } else {
         return annotation.contains("mixin/injection/Inject;") || annotation.contains("mixin/injection/Modify") || annotation.contains("mixin/injection/Redirect") || annotation.contains("mixin/Overwrite") || annotation.contains("mixinextras/injector/wrap") || annotation.contains("mixinextras/injector/v2/Wrap") || annotation.contains("mixinextras/injector/modify") || annotation.contains("/Override;");
      }
   }

   private boolean isThisMethodOverride(String classNameVanilla, String methodNameVanilla) {
      if (methodNameVanilla.startsWith("m_") && methodNameVanilla.endsWith("_")) {
         return true;
      } else {
         ClassNode cn = null;

         try {
            label80: {
               InputStream is = this.getClassBytes(classNameVanilla + ".class");

               boolean var13;
               label71: {
                  try {
                     if (is == null) {
                        var13 = false;
                        break label71;
                     }

                     ClassReader cr = new ClassReader(is);
                     cn = new ClassNode();
                     cr.accept(cn, 7);
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
                  break label80;
               }

               if (is != null) {
                  is.close();
               }

               return var13;
            }
         } catch (IOException var9) {
            cn = null;
         }

         if (cn != null && cn.methods != null) {
            for(MethodNode mn : cn.methods) {
               if (mn.name.equals(methodNameVanilla)) {
                  return true;
               }
            }

            if (cn.superName != null && !cn.superName.equals("java/lang/Object")) {
               return this.isThisMethodOverride(cn.superName, methodNameVanilla);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   private InputStream getClassBytes(String path) throws IOException {
      InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
      if (is != null) {
         return is;
      } else {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         if (cl != null) {
            is = cl.getResourceAsStream(path);
            if (is != null) {
               return is;
            }
         }

         return null;
      }
   }

   private ArrayList getPendingConfigs() {
      Object pendingConfigs = null;

      try {
         MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
         Field transformerField = environment.getClass().getDeclaredField("transformer");
         transformerField.setAccessible(true);
         Object transformer = transformerField.get(environment);
         Field processorField = transformer.getClass().getDeclaredField("processor");
         processorField.setAccessible(true);
         Object processor = processorField.get(transformer);
         Field pendingConfigsField = processor.getClass().getDeclaredField("pendingConfigs");
         pendingConfigsField.setAccessible(true);
         pendingConfigs = pendingConfigsField.get(processor);
      } catch (Exception var8) {
         pendingConfigs = null;
      }

      return (ArrayList)pendingConfigs;
   }

   private void setPendingConfigs(ArrayList configs) {
      try {
         MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
         Field transformerField = environment.getClass().getDeclaredField("transformer");
         transformerField.setAccessible(true);
         Object transformer = transformerField.get(environment);
         Field processorField = transformer.getClass().getDeclaredField("processor");
         processorField.setAccessible(true);
         Object processor = processorField.get(transformer);
         Field pendingConfigsField = processor.getClass().getDeclaredField("pendingConfigs");
         pendingConfigsField.setAccessible(true);
         pendingConfigsField.set(processor, configs);
      } catch (Exception var8) {
      }

   }

   private String getPackageNameFromMixinConfig(Object pMixinConfig) {
      Object packageName = null;

      try {
         Field packageNameField = pMixinConfig.getClass().getDeclaredField("mixinPackage");
         packageNameField.setAccessible(true);
         packageName = packageNameField.get(pMixinConfig);
      } catch (Exception var4) {
      }

      if (packageName == null) {
         packageName = "";
      }

      return (String)packageName;
   }
}

