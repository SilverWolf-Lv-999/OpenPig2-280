package kakiku.pig2mod.mixin;

import kakiku.pig2mod.entity.Pig2;
import kakiku.pig2mod.xform.MyLib2;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GuiGraphics.class})
public abstract class MxGuiGraphics {
   @Inject(
      method = {"blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void blit(ResourceLocation pAtlasLocation, int pX, int pY, float pUOffset, float pVOffset, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight, CallbackInfo ci) {
      if (Pig2.isMaxFighting() && MyLib2.isCalledFromOtherModWithin5()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   void innerBlit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV, CallbackInfo ci) {
      if (Pig2.isMaxFighting() && MyLib2.isCalledFromOtherModWithin5()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFFFFFF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   void innerBlit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV, float pRed, float pGreen, float pBlue, float pAlpha, CallbackInfo ci) {
      if (Pig2.isMaxFighting() && MyLib2.isCalledFromOtherModWithin5()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void drawString(Font pFont, Component pText, int pX, int pY, int pColor, boolean pDropShadow, CallbackInfoReturnable cir) {
      if (pFont == null) {
         cir.setReturnValue(0);
      } else if (Pig2.isMaxFighting() && MyLib2.isCalledFromOtherModWithin5()) {
         cir.setReturnValue(0);
      }
   }

   @Inject(
      method = {"drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;FFIZ)I"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void drawString(Font pFont, @Nullable String p_281896_, float p_283569_, float p_283418_, int p_281560_, boolean p_282130_, CallbackInfoReturnable cir) {
      if (pFont == null) {
         cir.setReturnValue(0);
      } else if (Pig2.isMaxFighting() && MyLib2.isCalledFromOtherModWithin5()) {
         cir.setReturnValue(0);
      }
   }

   @Inject(
      method = {"drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;FFIZ)I"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void drawString(Font pFont, FormattedCharSequence p_281596_, float p_281586_, float p_282816_, int p_281743_, boolean p_282394_, CallbackInfoReturnable cir) {
      if (pFont == null) {
         cir.setReturnValue(0);
      } else if (Pig2.isMaxFighting() && MyLib2.isCalledFromOtherModWithin5()) {
         cir.setReturnValue(0);
      }
   }

   @Inject(
      method = {"drawCenteredString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void drawCenteredString(Font pFont, String pText, int pX, int pY, int pColor, CallbackInfo ci) {
      if (pFont == null) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"drawCenteredString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void drawCenteredString(Font pFont, Component pText, int pX, int pY, int pColor, CallbackInfo ci) {
      if (pFont == null) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"drawCenteredString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void drawCenteredString(Font pFont, FormattedCharSequence pText, int pX, int pY, int pColor, CallbackInfo ci) {
      if (pFont == null) {
         ci.cancel();
      }
   }
}

