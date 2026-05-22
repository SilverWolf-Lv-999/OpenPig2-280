package kakiku.pig2mod.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.jetbrains.annotations.NotNull;

public class MySet implements Set {
   private Set mySet;
   private Class mOwnerClass = null;
   private static final Class OWNER_PersistentEntitySectionManager = PersistentEntitySectionManager.class;
   private static final Class OWNER_ServerLevel = ServerLevel.class;
   private static final Class OWNER_MyInt2ObjectOpenHashMap = MyInt2ObjectOpenHashMap.class;
   private static Class OWNER_ChunkMap.TrackedEntity = null;

   public MySet(Set set, Class ownerClass) {
      this.mySet = set;
      this.mOwnerClass = ownerClass;
      if (ownerClass.getName().equals("net.minecraft.server.level.ChunkMap.TrackedEntity")) {
         OWNER_ChunkMap.TrackedEntity = ownerClass;
      }

   }

   public boolean remove(Object o) {
      if (MyLib2.getCallerClass1() != this.mOwnerClass && (this.mOwnerClass != OWNER_ServerLevel || !MyLib2.getCallerClass1().getName().equals("net.minecraft.server.level.ServerLevel.EntityCallbacks")) && (this.mOwnerClass != OWNER_ServerLevel || o instanceof Pig2 || o instanceof Player)) {
         if (this.mOwnerClass == OWNER_PersistentEntitySectionManager && o instanceof UUID) {
            UUID uuid = (UUID)o;
            if (!Pig2.getAliveServerPigUUIDs((Level)null).contains(uuid)) {
               return this.mySet.remove(o);
            }
         }

         return false;
      } else {
         return this.mySet.remove(o);
      }
   }

   public boolean removeIf(Predicate filter) {
      return false;
   }

   public boolean removeAll(@NotNull Collection c) {
      return false;
   }

   public boolean retainAll(@NotNull Collection c) {
      return false;
   }

   public void clear() {
   }

   public boolean add(Object e) {
      return this.mySet.add(e);
   }

   public boolean addAll(@NotNull Collection c) {
      return this.mySet.addAll(c);
   }

   public Iterator iterator() {
      final Iterator<E> it = this.mySet.iterator();
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

   public boolean equals(Object o) {
      return this.mySet.equals(o instanceof MySet ? ((MySet)o).mySet : o);
   }

   public Spliterator spliterator() {
      return Spliterators.spliteratorUnknownSize(this.iterator(), 0);
   }

   public void forEach(Consumer action) {
      this.mySet.forEach(action);
   }

   public int size() {
      return this.mySet.size();
   }

   public boolean isEmpty() {
      return this.mySet.isEmpty();
   }

   public boolean contains(Object o) {
      return this.mySet.contains(o);
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

   public int hashCode() {
      return this.mySet.hashCode();
   }
}

