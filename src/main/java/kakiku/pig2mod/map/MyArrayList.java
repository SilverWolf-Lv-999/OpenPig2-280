package kakiku.pig2mod.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ClassInstanceMultiMap;

public class MyArrayList extends ArrayList {
   private Class mOwnerClass = null;
   private final Class OWNER_ClassInstanceMultiMap = ClassInstanceMultiMap.class;
   private final Class OWNER_MyHashMap = MyHashMap.class;
   private final Class OWNER_ServerLevel = ServerLevel.class;
   private final Class OWNER_ClientLevel = ClientLevel.class;

   public MyArrayList(List list, Class ownerClass) {
      super(list);
      this.mOwnerClass = ownerClass;
   }

   public Object remove(int index) {
      return null;
   }

   public boolean remove(Object object) {
      Class<?> caller1Class = MyLib2.getCallerClass1();
      return caller1Class != this.mOwnerClass && (this.mOwnerClass != MyHashMap.class || caller1Class != ClassInstanceMultiMap.class) && (this.mOwnerClass != ClientLevel.class || !caller1Class.getName().equals("net.minecraft.client.multiplayer.ClientLevel.EntityCallbacks")) && (this.mOwnerClass != ServerLevel.class || !caller1Class.getName().equals("net.minecraft.server.level.ServerLevel.EntityCallbacks")) && (!(object instanceof LocalPlayer) || MyLib2.isThisOtherBadMOD(caller1Class)) ? false : super.remove(object);
   }

   public void clear() {
   }

   public Object set(int index, Object element) {
      return this.get(index);
   }

   public boolean removeAll(Collection c) {
      return false;
   }

   public boolean retainAll(Collection c) {
      return false;
   }

   public boolean removeIf(Predicate filter) {
      return false;
   }

   public void replaceAll(UnaryOperator operator) {
   }

   protected void removeRange(int fromIndex, int toIndex) {
   }

   public List subList(int fromIndex, int toIndex) {
      return new ArrayList(super.subList(fromIndex, toIndex));
   }

   public Iterator iterator() {
      final Iterator<E> it = super.iterator();
      return new Iterator() {
         Object last;

         public boolean hasNext() {
            return it.hasNext();
         }

         public Object next() {
            this.last = it.next();
            return this.last;
         }

         public void remove() {
         }
      };
   }

   public ListIterator listIterator() {
      final ListIterator<E> it = super.listIterator();
      return new ListIterator() {
         public boolean hasNext() {
            return it.hasNext();
         }

         public Object next() {
            return it.next();
         }

         public boolean hasPrevious() {
            return it.hasPrevious();
         }

         public Object previous() {
            return it.previous();
         }

         public int nextIndex() {
            return it.nextIndex();
         }

         public int previousIndex() {
            return it.previousIndex();
         }

         public void remove() {
            it.remove();
         }

         public void set(Object e) {
            it.set(e);
         }

         public void add(Object e) {
            it.add(e);
         }
      };
   }

   public Spliterator spliterator() {
      return Spliterators.spliteratorUnknownSize(this.iterator(), 0);
   }
}

