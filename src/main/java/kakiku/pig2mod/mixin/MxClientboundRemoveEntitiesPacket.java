package kakiku.pig2mod.mixin;

import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ClientboundRemoveEntitiesPacket.class})
public abstract class MxClientboundRemoveEntitiesPacket {
}

