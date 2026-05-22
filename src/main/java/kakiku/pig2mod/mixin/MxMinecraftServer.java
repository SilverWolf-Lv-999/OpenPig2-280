package kakiku.pig2mod.mixin;

import com.mojang.datafixers.DataFixer;
import java.net.Proxy;
import java.util.Map;
import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.map.MyLevelsMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftServer.class})
public abstract class MxMinecraftServer {
   @Shadow
   public Map levels;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void onInit(Thread pServerThread, LevelStorageSource.LevelStorageAccess pStorageSource, PackRepository pPackRepository, WorldStem pWorldStem, Proxy pProxy, DataFixer pFixerUpper, Services pServices, ChunkProgressListenerFactory pProgressListenerFactory, CallbackInfo ci) {
      this.levels = new MyLevelsMap(this.levels);
   }

   @Inject(
      method = {"createLevels"},
      at = {@At("TAIL")}
   )
   protected void createLevels(ChunkProgressListener pListener, CallbackInfo ci) {
      System.setProperty("Pig2.ServerLevelsMap", Integer.toHexString(System.identityHashCode(this.levels)));
   }

   @Inject(
      method = {"stopServer"},
      at = {@At("HEAD")}
   )
   public void stopServer(CallbackInfo ci) {
      Pig2.oshimai();
   }
}

