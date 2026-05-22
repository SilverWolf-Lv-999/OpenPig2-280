package kakiku.pig2mod.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Resource.class})
public abstract class MxResource {
   @Unique
   Resource thisResource = (Resource)this;

   @Inject(
      method = {"open"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void open(CallbackInfoReturnable cir) {
      InputStream original = (InputStream)cir.getReturnValue();
      if (original != null) {
         if (this.thisResource.source() != null) {
            String packId = this.thisResource.source().packId();
            if (packId != null && (packId.equals("vanilla") || packId.startsWith("forge-1.20.1-") && packId.endsWith(".jar"))) {
               return;
            }
         }

         boolean shouldRemoveBuiltin = false;
         boolean shouldRemoveLoader = false;

         try {
            byte[] bytes = original.readAllBytes();
            if (bytes.length < 2 || bytes[0] != 123) {
               cir.setReturnValue(new ByteArrayInputStream(bytes));
               return;
            }

            String text = new String(bytes, StandardCharsets.UTF_8);
            if (!text.contains("\"parent\"") || !text.contains("item/")) {
               cir.setReturnValue(new ByteArrayInputStream(bytes));
               return;
            }

            JsonObject obj = JsonParser.parseString(text).getAsJsonObject();
            if (obj.has("parent")) {
               String parent = obj.get("parent").getAsString();
               if (parent != null && (parent.equals("builtin/entity") || parent.equals("minecraft:builtin/entity"))) {
                  shouldRemoveBuiltin = true;
               }
            }

            if (obj.has("loader")) {
               String loader = obj.get("loader").getAsString();
               if (!"forge:separate_transforms".equals(loader)) {
                  shouldRemoveLoader = true;
               }
            }

            if (!shouldRemoveBuiltin && !shouldRemoveLoader) {
               cir.setReturnValue(new ByteArrayInputStream(bytes));
               return;
            }

            if (shouldRemoveBuiltin) {
               obj.addProperty("parent", "minecraft:item/handheld");
            } else if (shouldRemoveLoader) {
               obj.remove("loader");
            }

            boolean hasTextures = obj.has("textures");
            boolean hasLayer0 = false;
            boolean hasParticle = false;
            String particleTexture = null;
            if (hasTextures) {
               JsonObject tex = obj.getAsJsonObject("textures");
               hasLayer0 = tex.has("layer0");
               hasParticle = tex.has("particle");
               if (hasParticle) {
                  particleTexture = tex.get("particle").getAsString();
               }
            }

            boolean broken = !hasTextures || !hasLayer0;
            if (broken) {
               obj.addProperty("parent", "minecraft:item/handheld");
               JsonObject tex = new JsonObject();
               if (hasParticle) {
                  tex.addProperty("layer0", particleTexture);
               } else {
                  tex.addProperty("layer0", "minecraft:item/stick");
               }

               obj.add("textures", tex);
            }

            String modID = null;
            if (obj.has("textures")) {
               JsonObject tex = obj.getAsJsonObject("textures");

               for(Map.Entry e : tex.entrySet()) {
                  String v = ((JsonElement)e.getValue()).getAsString();
                  int idx = v.indexOf(58);
                  if (idx > 0) {
                     String id = v.substring(0, idx);
                     if (!id.equals("minecraft")) {
                        break;
                     }
                  }
               }
            }

            byte[] modified = obj.toString().getBytes(StandardCharsets.UTF_8);
            cir.setReturnValue(new ByteArrayInputStream(modified));
         } catch (Throwable var15) {
            cir.setReturnValue(original);
         }

      }
   }
}

