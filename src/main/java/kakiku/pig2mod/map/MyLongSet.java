package kakiku.pig2mod.map;

import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSpliterator;
import it.unimi.dsi.fastutil.longs.LongSpliterators;
import java.util.Collection;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import org.jetbrains.annotations.NotNull;

public class MyLongSet implements LongSet {
   private LongSet mySet;
   private Class mOwnerClass = null;
   private final Class OWNER_PersistentEntitySectionManager = PersistentEntitySectionManager.class;
   private final Class OWNER_TransientEntitySectionManager = TransientEntitySectionManager.class;
   private final Class OWNER_ChunkMap = ChunkMap.class;

   public MyLongSet(LongSet set, Class ownerClass) {
      this.mySet = set;
      this.mOwnerClass = ownerClass;
   }

   public boolean remove(long k) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.mySet.remove(k) : false;
   }

   public boolean rem(long k) {
      return false;
   }

   public boolean remove(Object o) {
      return false;
   }

   public boolean removeIf(Predicate filter) {
      return false;
   }

   public boolean removeIf(LongPredicate filter) {
      return false;
   }

   public boolean removeIf(it.unimi.dsi.fastutil.longs.LongPredicate filter) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.mySet.removeIf(filter) : false;
   }

   public boolean removeAll(@NotNull Collection c) {
      return false;
   }

   public boolean removeAll(LongCollection c) {
      return false;
   }

   public boolean retainAll(@NotNull Collection c) {
      return false;
   }

   public boolean retainAll(LongCollection c) {
      return false;
   }

   public void clear() {
   }

   public boolean add(long key) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.mySet.add(key) : false;
   }

   public boolean add(Long o) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.mySet.add(o) : false;
   }

   public boolean addAll(LongCollection c) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.mySet.addAll(c) : false;
   }

   public boolean addAll(@NotNull Collection c) {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.mySet.addAll(c) : false;
   }

   public LongIterator iterator() {
      final long[] snapshot = this.mySet.toLongArray();
      return new LongIterator() {
         int idx = 0;

         public boolean hasNext() {
            return this.idx < snapshot.length;
         }

         public long nextLong() {
            return snapshot[this.idx++];
         }

         public void remove() {
         }
      };
   }

   public LongIterator longIterator() {
      return this.iterator();
   }

   public LongSpliterator spliterator() {
      return LongSpliterators.asSpliteratorUnknownSize(this.iterator(), 0);
   }

   public LongSpliterator longSpliterator() {
      return LongSpliterators.asSpliteratorUnknownSize(this.iterator(), 0);
   }

   public int size() {
      return this.mySet.size();
   }

   public boolean isEmpty() {
      return this.mySet.isEmpty();
   }

   public boolean contains(long key) {
      return this.mySet.contains(key);
   }

   public boolean containsAll(LongCollection c) {
      return this.mySet.containsAll(c);
   }

   public boolean containsAll(@NotNull Collection c) {
      return this.mySet.containsAll(c);
   }

   public @NotNull Object[] toArray() {
      return this.mySet.toArray();
   }

   public @NotNull Object[] toArray(@NotNull Object[] a) {
      return this.mySet.toArray(a);
   }

   public long[] toArray(long[] a) {
      return this.mySet.toArray(a);
   }

   public long[] toLongArray() {
      return this.mySet.toLongArray();
   }
}

