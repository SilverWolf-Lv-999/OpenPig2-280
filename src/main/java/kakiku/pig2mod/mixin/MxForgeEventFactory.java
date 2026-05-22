package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ForgeEventFactory.class})
public abstract class MxForgeEventFactory {
   @Inject(
      method = {"onStopEntityTracking"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void onStopEntityTracking(Entity entity, Player player, CallbackInfo ci) {
      if (entity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding() && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerLevel.m_143261_") && !MyLib2.isCalledFromTheClassAndMethod("net.minecraft.server.level.ServerEntity.m_8534_")) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"onLivingConvert"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void onLivingConvert(LivingEntity entity, LivingEntity outcome, CallbackInfo ci) {
      if (entity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            ci.cancel();
            return;
         }
      }

   }

   @Inject(
      method = {"onEntityTeleportCommand"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void onEntityTeleportCommand(Entity entity, double targetX, double targetY, double targetZ, CallbackInfoReturnable cir) {
      if (entity instanceof Pig2 pig2) {
         if (!Pig2.isOshimai() && !pig2.isEnding()) {
            EntityTeleportEvent.TeleportCommand event = new EntityTeleportEvent.TeleportCommand(entity, targetX, targetY, targetZ);
            event.setCanceled(true);
            cir.setReturnValue(event);
            return;
         }
      }

   }
}

