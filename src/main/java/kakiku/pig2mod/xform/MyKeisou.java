package kakiku.pig2mod.xform;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

public class MyKeisou implements Instrumentation {
   private final Object mTransformerManager = null;
   private Object mRetransfomableTransformerManager = null;
   private final long mNativeAgent = 0L;
   private final boolean mEnvironmentSupportsRedefineClasses = false;
   private volatile boolean mEnvironmentSupportsRetransformClassesKnown = false;
   private volatile boolean mEnvironmentSupportsRetransformClasses = false;
   private final boolean mEnvironmentSupportsNativeMethodPrefix = false;
   public static final Instrumentation gInstance = new MyKeisou();

   private MyKeisou() {
   }

   public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
   }

   public void addTransformer(ClassFileTransformer transformer) {
   }

   public boolean removeTransformer(ClassFileTransformer transformer) {
      return false;
   }

   public boolean isRetransformClassesSupported() {
      return false;
   }

   public void retransformClasses(Class... classes) throws UnmodifiableClassException {
   }

   public boolean isRedefineClassesSupported() {
      return false;
   }

   public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException {
   }

   public boolean isModifiableClass(Class theClass) {
      return false;
   }

   public Class[] getAllLoadedClasses() {
      return new Class[0];
   }

   public Class[] getInitiatedClasses(ClassLoader loader) {
      return new Class[0];
   }

   public long getObjectSize(Object objectToSize) {
      return 0L;
   }

   public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
   }

   public void appendToSystemClassLoaderSearch(JarFile jarfile) {
   }

   public boolean isNativeMethodPrefixSupported() {
      return false;
   }

   public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
   }

   public void redefineModule(Module module, Set extraReads, Map extraExports, Map extraOpens, Set extraUses, Map extraProvides) {
   }

   public boolean isModifiableModule(Module module) {
      return false;
   }
}

