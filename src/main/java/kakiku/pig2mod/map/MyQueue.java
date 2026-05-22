package kakiku.pig2mod.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.jetbrains.annotations.NotNull;

public class MyQueue implements Queue {
   private final Queue myQueue;
   private Class mOwnerClass = null;
   private final Class OWNER_PersistentEntitySectionManager = PersistentEntitySectionManager.class;

   public MyQueue(Queue queue, Class ownerClass) {
      this.myQueue = queue;
      this.mOwnerClass = ownerClass;
   }

   public Object remove() {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.myQueue.remove() : null;
   }

   public Object poll() {
      return MyLib2.getCallerClass1() == this.mOwnerClass ? this.myQueue.poll() : null;
   }

   public boolean remove(Object o) {
      return false;
   }

   public boolean removeAll(@NotNull Collection c) {
      return false;
   }

   public boolean removeIf(@NotNull Predicate filter) {
      return false;
   }

   public boolean retainAll(@NotNull Collection c) {
      return false;
   }

   public void clear() {
   }

   public Iterator iterator() {
      final Iterator<E> it = this.myQueue.iterator();
      return new Iterator() {
         public boolean hasNext() {
            return it.hasNext();
         }

         public Object next() {
            return it.next();
         }

         public void remove() {
         }
      };
   }

   public @NotNull Spliterator spliterator() {
      return Spliterators.spliterator(this.iterator(), (long)this.size(), 0);
   }

   public boolean add(Object e) {
      return this.myQueue.add(e);
   }

   public boolean offer(Object e) {
      return this.myQueue.offer(e);
   }

   public boolean addAll(@NotNull Collection c) {
      return this.myQueue.addAll(c);
   }

   public Object element() {
      return this.myQueue.element();
   }

   public Object peek() {
      return this.myQueue.peek();
   }

   public int size() {
      return this.myQueue.size();
   }

   public boolean isEmpty() {
      return this.myQueue.isEmpty();
   }

   public boolean contains(Object o) {
      return this.myQueue.contains(o);
   }

   public boolean containsAll(@NotNull Collection c) {
      return this.myQueue.containsAll(c);
   }

   public @NotNull Object[] toArray() {
      return this.myQueue.toArray();
   }

   public @NotNull Object[] toArray(@NotNull Object[] a) {
      return this.myQueue.toArray(a);
   }
}

