package dev.onethecrazy.mixin;

import dev.onethecrazy.JuxClient;
import dev.onethecrazy.ui.EffectIndicatorWidget;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class HudMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method="renderEffects", at=@At("HEAD"), cancellable = true)
    private void onVanillaRenderEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        if(!JuxClient.getInstance().config.showPotionEffectHud)
            return;

        EffectIndicatorWidget.renderAll(guiGraphics, minecraft);

        ci.cancel();
    }
}
