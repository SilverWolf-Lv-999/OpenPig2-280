package kakiku.pig2mod.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.TickingTracker;
import net.minecraft.util.SortedArraySet;

public class MySortedArraySet extends SortedArraySet {
   private Class mOwnerClass = null;
   private final Class OWNER_MyLong2ObjectOpenHashMap = MyLong2ObjectOpenHashMap.class;

   public MySortedArraySet(SortedArraySet sortedArraySet, Class ownerClass) {
      super(4, sortedArraySet.comparator);
      this.mOwnerClass = ownerClass;
      this.addAll(sortedArraySet);
   }

   public boolean remove(Object pElement) {
      Class<?> caller1Class = MyLib2.getCallerClass1();
      return caller1Class != this.mOwnerClass && caller1Class != DistanceManager.class && caller1Class != TickingTracker.class ? false : super.remove(pElement);
   }

   public boolean removeIf(Predicate filter) {
      return false;
   }

   public boolean removeAll(Collection c) {
      return false;
   }

   public boolean retainAll(Collection c) {
      return false;
   }

   public void clear() {
   }

   public Iterator iterator() {
      final Iterator<T> it = super.iterator();
      return new Iterator() {
         public boolean hasNext() {
            return it.hasNext();
         }

         public Object next() {
            return it.next();
         }

         public void remove() {
            Class<?> caller1Class = MyLib2.getCallerClass1();
            if (caller1Class == MySortedArraySet.this.mOwnerClass || caller1Class == DistanceManager.class) {
               it.remove();
            }
         }
      };
   }

   public Spliterator spliterator() {
      return Spliterators.spliteratorUnknownSize(this.iterator(), 0);
   }
}

