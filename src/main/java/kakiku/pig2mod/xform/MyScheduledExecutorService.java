package kakiku.pig2mod.xform;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MyScheduledExecutorService implements ScheduledExecutorService {
   public ScheduledFuture schedule(Runnable command, long delay, TimeUnit unit) {
      return null;
   }

   public ScheduledFuture schedule(Callable callable, long delay, TimeUnit unit) {
      return null;
   }

   public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
      return null;
   }

   public ScheduledFuture scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
      return null;
   }

   public void execute(Runnable command) {
   }

   public void shutdown() {
   }

   public List shutdownNow() {
      return List.of();
   }

   public boolean isShutdown() {
      return false;
   }

   public boolean isTerminated() {
      return false;
   }

   public boolean awaitTermination(long timeout, TimeUnit unit) {
      return false;
   }

   public Future submit(Callable task) {
      return null;
   }

   public Future submit(Runnable task, Object result) {
      return null;
   }

   public Future submit(Runnable task) {
      return null;
   }

   public List invokeAll(Collection tasks) {
      return List.of();
   }

   public List invokeAll(Collection tasks, long timeout, TimeUnit unit) {
      return List.of();
   }

   public Object invokeAny(Collection tasks) {
      return null;
   }

   public Object invokeAny(Collection tasks, long timeout, TimeUnit unit) {
      return null;
   }
}

