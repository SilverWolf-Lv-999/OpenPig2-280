package kakiku.pig2mod.map;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.AbstractIntSortedSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import kakiku.pig2mod.MyHelper;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.EntityTickList;
import org.jetbrains.annotations.Nullable;

public class MyInt2ObjectLinkedOpenHashMap extends Int2ObjectLinkedOpenHashMap {
   private Class mOwnerClass = null;
   private final Class OWNER_EntityLookup = EntityLookup.class;
   private final Class OWNER_EntityTickList = EntityTickList.class;

   public MyInt2ObjectLinkedOpenHashMap(Int2ObjectMap map, Class ownerClass) {
      this.mOwnerClass = ownerClass;
      this.defRetValue = map.defaultReturnValue();
      super.putAll(map);
   }

   public Object put(int k, Object v) {
      return this.get(k) != this.defRetValue && MyLib2.getCallerClass1() != this.mOwnerClass ? this.defRetValue : super.put(k, v);
   }

   public Object put(Integer k, Object v) {
      return (MyLib2.getCallerClass1() == this.mOwnerClass || !(this.get(k) instanceof Pig2)) && this.mOwnerClass == this.OWNER_EntityLookup ? super.put(k, v) : this.defRetValue;
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
      return MyLib2.getCallerClass1() != this.mOwnerClass && (this.get(k) instanceof Pig2 || this.get(k) instanceof Player) ? this.defRetValue : super.remove(k);
   }

   public boolean remove(int k, Object v) {
      return MyLib2.getCallerClass1() != this.mOwnerClass && MyLib2.getCallerClass1() != MyHelper.class ? false : super.remove(k, v);
   }

   public Object remove(Object key) {
      return this.defRetValue;
   }

   public void clear() {
      if (MyLib2.getCallerClass1() == this.mOwnerClass) {
         super.clear();
      }
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

   public Int2ObjectSortedMap.FastSortedEntrySet int2ObjectEntrySet() {
      return new MySnapshotEntrySet(super.int2ObjectEntrySet());
   }

   public ObjectSortedSet entrySet() {
      return this.int2ObjectEntrySet();
   }

   public IntSortedSet keySet() {
      return new MySnapshotKeySet(super.keySet());
   }

   private final class MySnapshotEntrySet extends AbstractObjectSortedSet implements Int2ObjectSortedMap.FastSortedEntrySet {
      private final ObjectArrayList snapshot;

      MySnapshotEntrySet(Int2ObjectSortedMap.FastSortedEntrySet src) {
         this.snapshot = new ObjectArrayList(src.size());
         ObjectBidirectionalIterator var3 = src.iterator();

         while(var3.hasNext()) {
            Int2ObjectMap.Entry<V> e = (Int2ObjectMap.Entry)var3.next();
            this.snapshot.add(new AbstractInt2ObjectMap.BasicEntry(e.getIntKey(), e.getValue()) {
               public Object setValue(Object v) {
                  return this.getValue();
               }
            });
         }

      }

      public int size() {
         return this.snapshot.size();
      }

      public ObjectBidirectionalIterator iterator() {
         return this.snapshot.iterator();
      }

      public ObjectBidirectionalIterator iterator(Int2ObjectMap.Entry fromElement) {
         return this.snapshot.iterator();
      }

      public ObjectBidirectionalIterator fastIterator() {
         return this.snapshot.iterator();
      }

      public ObjectBidirectionalIterator fastIterator(Int2ObjectMap.Entry from) {
         return this.snapshot.iterator();
      }

      public Int2ObjectMap.Entry first() {
         return this.snapshot.isEmpty() ? null : (Int2ObjectMap.Entry)this.snapshot.get(0);
      }

      public Int2ObjectMap.Entry last() {
         return this.snapshot.isEmpty() ? null : (Int2ObjectMap.Entry)this.snapshot.get(this.snapshot.size() - 1);
      }

      public @Nullable Comparator comparator() {
         return null;
      }

      public ObjectSortedSet subSet(Int2ObjectMap.Entry fromElement, Int2ObjectMap.Entry toElement) {
         return this;
      }

      public ObjectSortedSet headSet(Int2ObjectMap.Entry toElement) {
         return this;
      }

      public ObjectSortedSet tailSet(Int2ObjectMap.Entry fromElement) {
         return this;
      }
   }

   private final class MySnapshotKeySet extends AbstractIntSortedSet {
      private final IntArrayList snapshot;

      MySnapshotKeySet(IntSet src) {
         this.snapshot = new IntArrayList(src.size());
         IntIterator it = src.iterator();

         while(it.hasNext()) {
            this.snapshot.add(it.nextInt());
         }

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

      public IntBidirectionalIterator iterator() {
         return this.snapshot.iterator();
      }

      public IntBidirectionalIterator iterator(int fromElement) {
         return this.snapshot.iterator();
      }

      public IntSortedSet subSet(int fromElement, int toElement) {
         return this;
      }

      public IntSortedSet headSet(int toElement) {
         return this;
      }

      public IntSortedSet tailSet(int fromElement) {
         return this;
      }

      public IntComparator comparator() {
         return null;
      }

      public int firstInt() {
         return 0;
      }

      public int lastInt() {
         return 0;
      }
   }
}

