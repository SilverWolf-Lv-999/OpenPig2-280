package kakiku.pig2mod.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "pig2mod",
   bus = Bus.MOD
)
public class Pig1 extends Pig {
   public Pig1(EntityType pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      MinecraftForge.EVENT_BUS.register(this);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Pig.createAttributes();
   }

   public void onAddedToWorld() {
      super.onAddedToWorld();
      this.setBaby(true);
      this.setPersistenceRequired();
   }

   public boolean hurt(DamageSource pSource, float pAmount) {
      boolean bReturn = super.hurt(pSource, pAmount);
      return bReturn;
   }

   public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
   }
}

