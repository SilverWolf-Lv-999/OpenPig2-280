package kakiku.pig2mod.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class MyLevelsMap extends LinkedHashMap {
   public MyLevelsMap(Map serverLevelsMap) {
      super(serverLevelsMap);
   }

   public Object put(Object key, Object value) {
      StackWalker.StackFrame caller = MyLib2.getCaller1();
      return caller.getDeclaringClass() == MinecraftServer.class && caller.getMethodName().equals("m_129815_") && value.getClass() == ServerLevel.class ? super.put(key, value) : null;
   }

   public void putAll(Map m) {
   }

   public boolean replace(Object key, Object oldValue, Object newValue) {
      return false;
   }

   public Object replace(Object key, Object value) {
      return null;
   }

   public void replaceAll(BiFunction function) {
   }

   public Object remove(Object key) {
      return null;
   }

   public boolean remove(Object key, Object value) {
      return false;
   }

   public void clear() {
   }

   public Object compute(Object key, BiFunction remappingFunction) {
      return super.get(key);
   }

   public Object computeIfPresent(Object key, BiFunction remappingFunction) {
      return super.get(key);
   }

   public Object computeIfAbsent(Object key, Function mappingFunction) {
      return super.get(key);
   }

   public Object merge(Object key, Object value, BiFunction remappingFunction) {
      return super.get(key);
   }

   public Set entrySet() {
      return new HashSet(super.entrySet());
   }

   public Set keySet() {
      return new HashSet(super.keySet());
   }

   public Collection values() {
      return new ArrayList(super.values());
   }
}

