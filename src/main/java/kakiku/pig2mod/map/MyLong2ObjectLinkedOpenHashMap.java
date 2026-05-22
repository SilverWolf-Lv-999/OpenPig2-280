package kakiku.pig2mod.map;

import it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap;
import it.unimi.dsi.fastutil.longs.AbstractLongSortedSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongComparator;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.level.ChunkMap;
import org.jetbrains.annotations.Nullable;

public class MyLong2ObjectLinkedOpenHashMap extends Long2ObjectLinkedOpenHashMap {
   private Class mOwnerClass = null;
   private final Class OWNER_ChunkMap = ChunkMap.class;

   public MyLong2ObjectLinkedOpenHashMap(Long2ObjectMap map, Class ownerClass) {
      this.mOwnerClass = ownerClass;
      this.defRetValue = map.defaultReturnValue();
      super.putAll(map);
   }

   public Object put(long k, Object v) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.put(k, v) : this.defRetValue;
   }

   public Object put(Long k, Object v) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.put(k, v) : this.defRetValue;
   }

   public void putAll(Map m) {
      if (MyLib2.getCallerClass1() == this.mOwnerClass) {
         super.putAll(m);
      }
   }

   public Object putIfAbsent(long key, Object value) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.putIfAbsent(key, value) : this.defRetValue;
   }

   public Object remove(long k) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.remove(k) : this.defRetValue;
   }

   public boolean remove(long k, Object v) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.remove(k, v) : false;
   }

   public Object remove(Object key) {
      return this.defRetValue;
   }

   public void clear() {
      if (MyLib2.getCallerClass1() == this.mOwnerClass) {
         super.clear();
      }
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
      return MyLib2.getCallerClass1() == this.mOwnerClass ? super.computeIfAbsent(key, mappingFunction) : super.get(key);
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
      return (ObjectCollection)(MyLib2.getCallerClass1() == this.mOwnerClass ? super.values() : new ObjectArrayList(super.values()));
   }

   public Long2ObjectSortedMap.FastSortedEntrySet long2ObjectEntrySet() {
      return new MySnapshotEntrySet(super.long2ObjectEntrySet());
   }

   public ObjectSortedSet entrySet() {
      return this.long2ObjectEntrySet();
   }

   public LongSortedSet keySet() {
      return new MySnapshotKeySet(super.keySet());
   }

   private final class MySnapshotEntrySet extends AbstractObjectSortedSet implements Long2ObjectSortedMap.FastSortedEntrySet {
      private final ObjectArrayList snapshot;

      MySnapshotEntrySet(Long2ObjectSortedMap.FastSortedEntrySet src) {
         this.snapshot = new ObjectArrayList(src.size());
         ObjectBidirectionalIterator var3 = src.iterator();

         while(var3.hasNext()) {
            Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry)var3.next();
            this.snapshot.add(new AbstractLong2ObjectMap.BasicEntry(e.getLongKey(), e.getValue()) {
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

      public ObjectBidirectionalIterator iterator(Long2ObjectMap.Entry fromElement) {
         return this.snapshot.iterator();
      }

      public ObjectBidirectionalIterator fastIterator() {
         return this.snapshot.iterator();
      }

      public ObjectBidirectionalIterator fastIterator(Long2ObjectMap.Entry from) {
         return this.snapshot.iterator();
      }

      public Long2ObjectMap.Entry first() {
         return this.snapshot.isEmpty() ? null : (Long2ObjectMap.Entry)this.snapshot.get(0);
      }

      public Long2ObjectMap.Entry last() {
         return this.snapshot.isEmpty() ? null : (Long2ObjectMap.Entry)this.snapshot.get(this.snapshot.size() - 1);
      }

      public @Nullable Comparator comparator() {
         return null;
      }

      public ObjectSortedSet subSet(Long2ObjectMap.Entry fromElement, Long2ObjectMap.Entry toElement) {
         return this;
      }

      public ObjectSortedSet headSet(Long2ObjectMap.Entry toElement) {
         return this;
      }

      public ObjectSortedSet tailSet(Long2ObjectMap.Entry fromElement) {
         return this;
      }
   }

   private final class MySnapshotKeySet extends AbstractLongSortedSet {
      private final LongArrayList snapshot;

      MySnapshotKeySet(LongSet src) {
         this.snapshot = new LongArrayList(src.size());
         LongIterator it = src.iterator();

         while(it.hasNext()) {
            this.snapshot.add(it.nextLong());
         }

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

      public LongBidirectionalIterator iterator() {
         return this.snapshot.iterator();
      }

      public LongBidirectionalIterator iterator(long fromElement) {
         return this.snapshot.iterator();
      }

      public LongSortedSet subSet(long fromElement, long toElement) {
         return this;
      }

      public LongSortedSet headSet(long toElement) {
         return this;
      }

      public LongSortedSet tailSet(long fromElement) {
         return this;
      }

      public LongComparator comparator() {
         return null;
      }

      public long firstLong() {
         return 0L;
      }

      public long lastLong() {
         return 0L;
      }
   }
}

