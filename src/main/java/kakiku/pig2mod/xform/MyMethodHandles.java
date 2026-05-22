package kakiku.pig2mod.xform;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import sun.misc.Unsafe;

public class MyMethodHandles {
   private static MethodHandle myDefineClass0Method = null;
   private static final Set gPassedSet = ConcurrentHashMap.newKeySet();
   private static final Set gChangedSet = ConcurrentHashMap.newKeySet();

   public static MethodHandle myMethodHandleWrap(MethodHandle pMethodHandle, String myInfo) {
      if (!gPassedSet.contains(pMethodHandle) && !gChangedSet.contains(pMethodHandle)) {
         try {
            List<String> memberInfo = getMemberInfo(pMethodHandle, (List)null);
            MethodType type = pMethodHandle.type();
            if (memberContains(memberInfo, "ClassLoader.defineClass0(")) {
               MethodHandle wrapped = getMyDefineClass0Method(type);
               gChangedSet.add(wrapped);
               return wrapped;
            }

            if (isBadCall(memberInfo)) {
               boolean isOK = false;
               String caller1ClassName = MyLib2.getCaller1().getClassName();
               if (caller1ClassName.equals("kakiku.MyLib0")) {
                  String callerName = MyLib2.getCallerName_withoutVanilla(2);
                  if (callerName.startsWith("kakiku.pig2mod.")) {
                     if (memberContains(memberInfo, "kakiku.pig2mod.")) {
                        isOK = true;
                     } else if (memberContains(memberInfo, "Unsafe.putReference(")) {
                        isOK = true;
                     }
                  }
               }

               if (isOK) {
                  gPassedSet.add(pMethodHandle);
                  return pMethodHandle;
               }

               MethodHandle wrapped = MethodHandles.empty(type);
               if (type.returnType() == Boolean.TYPE && memberContains(memberInfo, "Unsafe.compareAnd")) {
                  wrapped = MethodHandles.dropArguments(MethodHandles.constant(Boolean.TYPE, true), 0, type.parameterArray());
               }

               gChangedSet.add(wrapped);
               return wrapped;
            }

            if (isBadDainyuu(pMethodHandle, memberInfo)) {
               MethodHandle wrapped = MethodHandles.empty(type);
               gChangedSet.add(wrapped);
               return wrapped;
            }
         } catch (Throwable var7) {
         }

         gPassedSet.add(pMethodHandle);
         return pMethodHandle;
      } else {
         return pMethodHandle;
      }
   }

   private static boolean isBadCall(List memberIndo) {
      for(String desc : memberIndo) {
         if (!desc.contains("/put") && !desc.contains("/get") && !desc.startsWith("kakiku.pig2mod.xform.MyMethodHandles.myDefineClass0(")) {
            for(String badCalleeName : MyXformer2.getInstance().mBadCalleeNames) {
               if (desc.startsWith(badCalleeName)) {
                  return true;
               }
            }

            if (desc.startsWith("java.lang.Thread.start0(") || desc.startsWith("java.lang.ThreadGroup.add(") || desc.startsWith("jdk.internal.loader.NativeLibraries.findBuiltinLib(") || desc.startsWith("jdk.internal.loader.NativeLibraries.NativeLibraryImpl.open(") || desc.contains("Unsafe.putInt") || desc.contains("Unsafe.putLong") || desc.contains("Unsafe.compareAnd") || desc.contains("Unsafe.putObject") || desc.contains("Unsafe.putReference") || desc.contains("Unsafe.putBoolean") || desc.contains("Unsafe.putByte") || desc.contains("Unsafe.putShort") || desc.contains("Unsafe.putChar") || desc.contains("Unsafe.putFloat") || desc.contains("Unsafe.putDouble") || desc.startsWith("java.lang.reflect.Method.") || desc.startsWith("java.lang.invoke.VarHandle.") || desc.startsWith("java.lang.invoke.MethodHandle.") || desc.startsWith("java.lang.reflect.Field.set") && !desc.startsWith("java.lang.reflect.Field.setAccessible(") || desc.startsWith("java.lang.Thread.start(") || desc.startsWith("kakiku.")) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean isBadDainyuu(MethodHandle pMethodHandle, List memberInfo) {
      for(String desc : memberInfo) {
         if (isBadDainyuu(pMethodHandle, desc)) {
            return true;
         }
      }

      return false;
   }

   private static boolean isBadDainyuu(MethodHandle pMethodHandle, String desc) {
      if (!isThisFieldPutter(pMethodHandle, desc)) {
         return false;
      } else {
         String targetClassAndFieldNameWithPeriod = desc.contains("/") ? desc.substring(0, desc.indexOf("/")) : desc;
         String targetClassNameWithPeriod = targetClassAndFieldNameWithPeriod.substring(0, targetClassAndFieldNameWithPeriod.lastIndexOf("."));
         if (MyXformer2.getInstance().mFullClassNamesOfProtectedFields.contains(targetClassNameWithPeriod)) {
            return true;
         } else {
            for(String className : MyXformer2.getInstance().mClassNameStartsOfProtectedFields) {
               if (targetClassNameWithPeriod.startsWith(className)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private static boolean isThisFieldPutter(MethodHandle pMethodHandle, String desc) {
      if (!desc.endsWith("/putField") && !desc.endsWith("/putStatic")) {
         return pMethodHandle.getClass().getSimpleName().contains(".Accessor") && pMethodHandle.type().toString().endsWith(")void");
      } else {
         return true;
      }
   }

   public static Class myDefineClass0(ClassLoader loader, Class lookupClass, String name, byte[] bytes, int off, int len, ProtectionDomain pd, boolean initialize, int flags, Object classData) {
      try {
         Class<?> cls = loader.loadClass(name);
         if (classData != null) {
            MyXformer2.gMapClassClassData.put(cls, classData);
         }

         return cls;
      } catch (Throwable var11) {
         return Object.class;
      }
   }

   private static MethodHandle getMyDefineClass0Method(MethodType type) {
      try {
         if (myDefineClass0Method == null) {
            myDefineClass0Method = MethodHandles.lookup().findStatic(MyMethodHandles.class, "myDefineClass0", type).asType(type.generic());
         }
      } catch (Throwable var2) {
      }

      return myDefineClass0Method;
   }

   private static List getMemberInfo(MethodHandle methodHandle, List memberInfo) {
      if (memberInfo == null) {
         memberInfo = new ArrayList();
      }

      try {
         Unsafe unsafe = MyLib2.getUnsafe();
         Field memberField = getDeclaredFieldOrNull(methodHandle.getClass(), "member");
         if (memberField == null) {
            memberField = getDeclaredFieldOrNull(methodHandle.getClass().getSuperclass(), "member");
         }

         if (memberField != null) {
            Object member = unsafe.getObject(methodHandle, unsafe.objectFieldOffset(memberField));
            memberInfo.add(member.toString());
            return memberInfo;
         }

         memberField = getDeclaredFieldOrNull(methodHandle.getClass(), "target");
         if (memberField == null) {
            memberField = getDeclaredFieldOrNull(methodHandle.getClass().getSuperclass(), "target");
         }

         if (memberField != null) {
            Object var5 = unsafe.getObject(methodHandle, unsafe.objectFieldOffset(memberField));
            if (var5 instanceof MethodHandle) {
               MethodHandle childMethodHandle = (MethodHandle)var5;
               return getMemberInfo(childMethodHandle, memberInfo);
            }
         }

         if (methodHandle.getClass().getName().contains("BoundMethodHandle.Species_")) {
            for(Field field : getAllFieldsOrEmpty(methodHandle.getClass())) {
               if (!Modifier.isStatic(field.getModifiers()) && field.getType() != Integer.TYPE && field.getType() != Long.TYPE && field.getName().contains("argL")) {
                  Object fieldObject = unsafe.getObject(methodHandle, unsafe.objectFieldOffset(field));
                  if (fieldObject instanceof MethodHandle) {
                     MethodHandle childMethodHandle = (MethodHandle)fieldObject;
                     memberInfo = getMemberInfo(childMethodHandle, memberInfo);
                  }
               }
            }

            return memberInfo;
         }
      } catch (Throwable var7) {
      }

      memberInfo.add("不明");
      return memberInfo;
   }

   private static Field getDeclaredFieldOrNull(Class clazz, String name) {
      try {
         return clazz.getDeclaredField(name);
      } catch (NoSuchFieldException var3) {
         return null;
      }
   }

   private static List getAllFieldsOrEmpty(Class clazz) {
      List<Field> fields = new ArrayList();

      try {
         fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      } catch (Throwable var3) {
      }

      return fields;
   }

   private static boolean memberContains(List list, String str) {
      if (list == null) {
         return false;
      } else {
         for(String s : list) {
            if (s.contains(str)) {
               return true;
            }
         }

         return false;
      }
   }
}

