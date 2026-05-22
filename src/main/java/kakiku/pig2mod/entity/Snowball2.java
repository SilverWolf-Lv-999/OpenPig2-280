package kakiku.pig2mod.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class Snowball2 extends Snowball {
   public Snowball2(EntityType pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   public Snowball2(Level pLevel, LivingEntity pShooter) {
      super(pLevel, pShooter);
   }

   protected void onHitEntity(EntityHitResult pResult) {
      super.onHitEntity(pResult);
      Entity entity = pResult.getEntity();
      float amount = 3.0F;
      entity.hurt(this.damageSources().thrown(this, this.getOwner()), amount);
   }
}

