package kakiku.pig2mod.map;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.level.entity.EntityLookup;

public class MyHashMap extends HashMap {
   private Class mOwnerClass = null;
   private final Class OWNER_ClassInstanceMultiMap = ClassInstanceMultiMap.class;
   private final Class OWNER_EntityLookup = EntityLookup.class;

   public MyHashMap(Map map, Class ownerClass) {
      this.mOwnerClass = ownerClass;
      this.putAllSub(map);
   }

   private Object protect(Object v) {
      if (this.mOwnerClass == this.OWNER_ClassInstanceMultiMap) {
         if (v instanceof MyArrayList) {
            return v;
         }

         if (v instanceof List) {
            return new MyArrayList((List)v, MyHashMap.class);
         }
      }

      return v;
   }

   public Object remove(Object key) {
      return MyLib2.getCallerClass1() != this.mOwnerClass && !(this.get(key) instanceof LocalPlayer) ? null : super.remove(key);
   }

   public boolean remove(Object key, Object value) {
      return false;
   }

   public void clear() {
   }

   public Object put(Object key, Object value) {
      return this.get(key) != null && MyLib2.getCallerClass1() != this.mOwnerClass ? null : this.putSub(key, value);
   }

   private Object putSub(Object key, Object value) {
      return super.put(key, this.protect(value));
   }

   public Object putIfAbsent(Object key, Object value) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.putIfAbsent(key, this.protect(value)) : null;
   }

   public void putAll(Map map) {
      if (map.isEmpty() || MyLib2.getCallerClass1() == this.mOwnerClass) {
         this.putAllSub(map);
      }
   }

   private void putAllSub(Map map) {
      for(Map.Entry e : map.entrySet()) {
         this.putSub(e.getKey(), e.getValue());
      }

   }

   public Object replace(Object key, Object value) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.replace(key, this.protect(value)) : null;
   }

   public boolean replace(Object key, Object oldValue, Object newValue) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.replace(key, oldValue, this.protect(newValue)) : false;
   }

   public void replaceAll(BiFunction function) {
      if (MyLib2.getCallerClass1() == this.mOwnerClass) {
         super.replaceAll((k, v) -> this.protect(function.apply(k, v)));
      }
   }

   public Object compute(Object key, BiFunction remappingFunction) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.compute(key, (k, v) -> this.protect(remappingFunction.apply(k, v))) : super.get(key);
   }

   public Object computeIfPresent(Object key, BiFunction remappingFunction) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.computeIfPresent(key, (k, v) -> this.protect(remappingFunction.apply(k, v))) : super.get(key);
   }

   public Object computeIfAbsent(Object key, Function mappingFunction) {
      return this.mOwnerClass == this.OWNER_ClassInstanceMultiMap ? super.computeIfAbsent(key, (k) -> this.protect(mappingFunction.apply(k))) : super.computeIfAbsent(key, mappingFunction);
   }

   public Object merge(Object key, Object value, BiFunction remappingFunction) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.merge(key, this.protect(value), (oldV, newV) -> this.protect(remappingFunction.apply(oldV, newV))) : super.get(key);
   }

   public Set entrySet() {
      if (MyLib2.getCallerClass1() == this.mOwnerClass) {
         final Set<Map.Entry<K, V>> es = super.entrySet();
         return new AbstractSet() {
            public Iterator iterator() {
               final Iterator<Map.Entry<K, V>> it = es.iterator();
               return new Iterator() {
                  public boolean hasNext() {
                     return it.hasNext();
                  }

                  public Map.Entry next() {
                     final Map.Entry<K, V> e = (Map.Entry)it.next();
                     return new Map.Entry() {
                        public Object getKey() {
                           return e.getKey();
                        }

                        public Object getValue() {
                           return e.getValue();
                        }

                        public Object setValue(Object value) {
                           return e.setValue(MyHashMap.this.protect(value));
                        }
                     };
                  }

                  public void remove() {
                  }
               };
            }

            public int size() {
               return es.size();
            }
         };
      } else {
         return new HashSet(super.entrySet());
      }
   }

   public Set keySet() {
      return new HashSet(super.keySet());
   }

   public Collection values() {
      return new ArrayList(super.values());
   }
}

