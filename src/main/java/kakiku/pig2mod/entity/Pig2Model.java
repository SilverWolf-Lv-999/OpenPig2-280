package kakiku.pig2mod.entity;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Pig2Model extends PigModel {
   public Pig2Model(ModelPart pRoot) {
      super(pRoot);
   }
}

