package kakiku.pig2mod.xform;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.jetbrains.annotations.NotNull;

public class MyScheduledFuture implements ScheduledFuture {
   public long getDelay(@NotNull TimeUnit unit) {
      return 0L;
   }

   public int compareTo(@NotNull Delayed o) {
      return 0;
   }

   public boolean cancel(boolean mayInterruptIfRunning) {
      return false;
   }

   public boolean isCancelled() {
      return false;
   }

   public boolean isDone() {
      return true;
   }

   public Object get() throws InterruptedException, ExecutionException {
      return null;
   }

   public Object get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return null;
   }
}

