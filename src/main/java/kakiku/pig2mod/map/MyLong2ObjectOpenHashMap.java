package kakiku.pig2mod.map;

import it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap;
import it.unimi.dsi.fastutil.longs.AbstractLongSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.TickingTracker;
import net.minecraft.util.SortedArraySet;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;

public class MyLong2ObjectOpenHashMap extends Long2ObjectOpenHashMap {
   private Class mOwnerClass = null;
   private final Class OWNER_EntitySectionStorage = EntitySectionStorage.class;
   private final Class OWNER_PersistentEntitySectionManager = PersistentEntitySectionManager.class;
   private final Class OWNER_DistanceManager = DistanceManager.class;
   private final Class OWNER_TickingTracker = TickingTracker.class;

   public MyLong2ObjectOpenHashMap(Long2ObjectMap map, Class ownerClass) {
      this.mOwnerClass = ownerClass;
      this.defRetValue = map.defaultReturnValue();
      super.putAll(map);
   }

   private Object protect(Object v) {
      if (this.mOwnerClass == this.OWNER_DistanceManager || this.mOwnerClass == this.OWNER_TickingTracker) {
         if (v instanceof MySortedArraySet) {
            return v;
         }

         if (v instanceof SortedArraySet) {
            SortedArraySet<?> sortedArraySet = (SortedArraySet)v;
            return new MySortedArraySet(sortedArraySet, MyLong2ObjectOpenHashMap.class);
         }
      }

      return v;
   }

   public void putAll(Map m) {
      if (MyLib2.getCallerClass1() == this.mOwnerClass) {
         super.putAll(m);
      }
   }

   public Object put(long k, Object v) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.put(k, this.protect(v)) : this.defRetValue;
   }

   public Object myPut(long k, Object v) {
      return super.put(k, this.protect(v));
   }

   public Object put(Long k, Object v) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.put(k, this.protect(v)) : this.defRetValue;
   }

   public Object putIfAbsent(long key, Object value) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.putIfAbsent(key, this.protect(value)) : this.defRetValue;
   }

   public Object remove(long k) {
      if (MyLib2.getCallerClass1() != this.mOwnerClass && MyLib2.getCaller2().getDeclaringClass() != this.mOwnerClass) {
         Object var4 = this.get(k);
         if (var4 instanceof EntitySection) {
            EntitySection<?> entitySection = (EntitySection)var4;
            if (entitySection.isEmpty()) {
               return super.remove(k);
            }
         }

         return this.defRetValue;
      } else {
         return super.remove(k);
      }
   }

   public boolean remove(long k, Object v) {
      return false;
   }

   public Object remove(Object key) {
      return this.defRetValue;
   }

   public void clear() {
   }

   public Object compute(long k, BiFunction remappingFunction) {
      return super.get(k);
   }

   public Object computeIfPresent(long k, BiFunction remappingFunction) {
      return super.get(k);
   }

   public Object computeIfAbsent(long k, LongFunction mappingFunction) {
      return super.get(k);
   }

   public Object computeIfAbsent(long key, Long2ObjectFunction mappingFunction) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.myComputeIfAbsent(key, mappingFunction) : super.get(key);
   }

   public Object myComputeIfAbsent(long key, Long2ObjectFunction mappingFunction) {
      V v = (V)super.computeIfAbsent(key, mappingFunction);
      if (v instanceof MySortedArraySet) {
         return v;
      } else if (v instanceof SortedArraySet) {
         V v2 = (V)this.protect(v);
         super.put(key, v2);
         return v2;
      } else {
         return v;
      }
   }

   public Object merge(long k, Object v, BiFunction remappingFunction) {
      return super.get(k);
   }

   public boolean replace(long k, Object oldValue, Object v) {
      return false;
   }

   public Object replace(long k, Object v) {
      return super.get(k);
   }

   public void replaceAll(BiFunction function) {
   }

   public ObjectCollection values() {
      return new ObjectArrayList(super.values());
   }

   public Long2ObjectMap.FastEntrySet long2ObjectEntrySet() {
      Class<?> caller1Class = MyLib2.getCallerClass1();
      return (Long2ObjectMap.FastEntrySet)(caller1Class != this.mOwnerClass && caller1Class != MyLong2ObjectOpenHashMap.class ? new MySnapshotEntrySet(super.long2ObjectEntrySet()) : super.long2ObjectEntrySet());
   }

   public ObjectSet entrySet() {
      return this.long2ObjectEntrySet();
   }

   public LongSet keySet() {
      return new MySnapshotKeySet(super.keySet());
   }

   private final class MySnapshotEntrySet extends AbstractObjectSet implements Long2ObjectMap.FastEntrySet {
      private final ObjectArrayList snapshot;

      MySnapshotEntrySet(Long2ObjectMap.FastEntrySet src) {
         this.snapshot = new ObjectArrayList(src.size());
         ObjectIterator var3 = src.iterator();

         while(var3.hasNext()) {
            Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry)var3.next();
            this.snapshot.add(new AbstractLong2ObjectMap.BasicEntry(e.getLongKey(), e.getValue()) {
               public Object setValue(Object v) {
                  return this.getValue();
               }
            });
         }

      }

      public ObjectIterator iterator() {
         return this.snapshot.iterator();
      }

      public ObjectIterator fastIterator() {
         return this.snapshot.iterator();
      }

      public int size() {
         return this.snapshot.size();
      }
   }

   private final class MySnapshotKeySet extends AbstractLongSet {
      private final LongArrayList snapshot;

      MySnapshotKeySet(LongSet src) {
         this.snapshot = new LongArrayList(src.size());
         LongIterator it = src.iterator();

         while(it.hasNext()) {
            this.snapshot.add(it.nextLong());
         }

      }

      public LongIterator iterator() {
         return this.snapshot.iterator();
      }

      public int size() {
         return this.snapshot.size();
      }

      public void clear() {
         this.snapshot.clear();
      }

      public boolean remove(long k) {
         return this.snapshot.rem(k);
      }

      public boolean contains(long k) {
         return this.snapshot.contains(k);
      }
   }
}

