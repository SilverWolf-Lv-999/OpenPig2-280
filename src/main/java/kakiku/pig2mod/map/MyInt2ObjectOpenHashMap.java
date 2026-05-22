package kakiku.pig2mod.map;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.AbstractIntSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.mixin.MxAccessorTrackedEntity;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;

public class MyInt2ObjectOpenHashMap extends Int2ObjectOpenHashMap {
   private Class mOwnerClass = null;
   private final Class OWNER_ChunkMap = ChunkMap.class;

   public MyInt2ObjectOpenHashMap(Int2ObjectMap map, Class ownerClass) {
      this.mOwnerClass = ownerClass;
      this.defRetValue = map.defaultReturnValue();
      super.putAll(map);
   }

   private Object protect(Object v) {
      if (this.mOwnerClass != this.OWNER_ChunkMap) {
         return v;
      } else {
         if (v instanceof MxAccessorTrackedEntity) {
            MxAccessorTrackedEntity aTrackedEntity = (MxAccessorTrackedEntity)v;
            Set<ServerPlayerConnection> seenBy1 = aTrackedEntity.getSeenBy();
            if (!(seenBy1 instanceof MySet)) {
               aTrackedEntity.setSeenBy(new MySet(seenBy1, MyInt2ObjectOpenHashMap.class));
            }
         }

         return v;
      }
   }

   public Object put(int k, Object v) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.put(k, this.protect(v)) : this.defRetValue;
   }

   public Object put(Integer k, Object v) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.put(k, this.protect(v)) : this.defRetValue;
   }

   public void putAll(Map m) {
      if (MyLib2.getCallerClass1() == this.mOwnerClass) {
         super.putAll(m);
      }
   }

   public Object putIfAbsent(int key, Object value) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.putIfAbsent(key, value) : this.defRetValue;
   }

   public Object remove(int k) {
      if (MyLib2.getCallerClass1() == this.mOwnerClass) {
         return super.remove(k);
      } else {
         V v = (V)this.get(k);
         if (this.mOwnerClass == this.OWNER_ChunkMap && v instanceof MxAccessorTrackedEntity) {
            MxAccessorTrackedEntity aTrackedEntity = (MxAccessorTrackedEntity)v;
            Entity entity = aTrackedEntity.getEntity();
            if (!(entity instanceof Pig2)) {
               return super.remove(k);
            }
         }

         return this.defRetValue;
      }
   }

   public boolean remove(int k, Object v) {
      return false;
   }

   public Object remove(Object key) {
      return this.defRetValue;
   }

   public void clear() {
   }

   public Object compute(int k, BiFunction remappingFunction) {
      return super.get(k);
   }

   public Object computeIfPresent(int k, BiFunction remappingFunction) {
      return super.get(k);
   }

   public Object computeIfAbsent(int k, IntFunction mappingFunction) {
      return super.get(k);
   }

   public Object computeIfAbsent(int key, Int2ObjectFunction mappingFunction) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.computeIfAbsent(key, mappingFunction) : super.get(key);
   }

   public Object merge(int k, Object v, BiFunction remappingFunction) {
      return super.get(k);
   }

   public boolean replace(int k, Object oldValue, Object v) {
      return false;
   }

   public Object replace(int k, Object v) {
      return super.get(k);
   }

   public void replaceAll(BiFunction function) {
   }

   public ObjectCollection values() {
      return new ObjectArrayList(super.values());
   }

   public Int2ObjectMap.FastEntrySet int2ObjectEntrySet() {
      return new MySnapshotEntrySet(super.int2ObjectEntrySet());
   }

   public ObjectSet entrySet() {
      return this.int2ObjectEntrySet();
   }

   public IntSet keySet() {
      return new MySnapshotKeySet(super.keySet());
   }

   private final class MySnapshotEntrySet extends AbstractObjectSet implements Int2ObjectMap.FastEntrySet {
      private final ObjectArrayList snapshot;

      MySnapshotEntrySet(Int2ObjectMap.FastEntrySet src) {
         this.snapshot = new ObjectArrayList(src.size());
         ObjectIterator var3 = src.iterator();

         while(var3.hasNext()) {
            Int2ObjectMap.Entry<V> e = (Int2ObjectMap.Entry)var3.next();
            this.snapshot.add(new AbstractInt2ObjectMap.BasicEntry(e.getIntKey(), e.getValue()) {
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

   private final class MySnapshotKeySet extends AbstractIntSet {
      private final IntArrayList snapshot;

      MySnapshotKeySet(IntSet src) {
         this.snapshot = new IntArrayList(src.size());
         IntIterator it = src.iterator();

         while(it.hasNext()) {
            this.snapshot.add(it.nextInt());
         }

      }

      public IntIterator iterator() {
         return this.snapshot.iterator();
      }

      public int size() {
         return this.snapshot.size();
      }

      public void clear() {
         this.snapshot.clear();
      }

      public boolean remove(int k) {
         return this.snapshot.rem(k);
      }

      public boolean contains(int k) {
         return this.snapshot.contains(k);
      }
   }
}

