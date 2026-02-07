package dev.onethecrazy.ui;

import dev.onethecrazy.JuxClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public class EffectIndicatorWidget {
    private static final Identifier TEXTURE = JuxClient.getInstance().config.getTextureManager().POTION_EFFECT_CONTAINER.get();
    private static final int TEX_WIDTH = 370;
    private static final int TEX_HEIGHT = 110;
    private static final int TEXT_OFFSET = 10;
    private static final int WIDGET_MARGIN = 5;
    private static final int WIDGET_WIDTH = 120;
    private static final int WIDGET_HEIGHT = 30;
    private static final int ICON_DIMENSIONS = 20;
    private static final int EFFECT_NAME_COLOR = JuxClient.getInstance().config.getTheme() == TextureManager.Theme.DARK ? 0xFFBDBDBD : 0xFF424242;
    private static final int EFFECT_DURATION_COLOR = JuxClient.getInstance().config.getTheme() == TextureManager.Theme.DARK ? 0xFFFFFFFF : 0xFF000000;

    public static void renderAll(GuiGraphics gg, Minecraft mc){
        var player = mc.player;
        // Can safely assert this
        assert player != null;

        int y = mc.getWindow().getGuiScaledHeight();
        int x = Math.round(WIDGET_MARGIN * 1.5f);

        for(var effect : player.getActiveEffects()){
            EffectIndicatorWidget.render(gg, x, y -= (WIDGET_MARGIN + WIDGET_HEIGHT), effect);
        }
    }

    public static void render(GuiGraphics gg, int x, int y, MobEffectInstance effect){
        renderEffect(gg, effect, x, y);
    }

    private static void renderEffect(
            GuiGraphics guiGraphics,
            MobEffectInstance effect,
            int x,
            int y
    ) {
        Minecraft mc = Minecraft.getInstance();

        Component name = getEffectName(effect);
        Component duration = MobEffectUtil.formatDuration(effect, 1.0F, mc.level.tickRateManager().tickrate());

        // Draw Background Container
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED, TEXTURE,
                x, y,
                0f, 0f,
                WIDGET_WIDTH, WIDGET_HEIGHT,
                TEX_WIDTH, TEX_HEIGHT,
                TEX_WIDTH, TEX_HEIGHT
        );

        // Draw Icon
        int iconX = x + TEXT_OFFSET / 2;
        int iconY = y + (WIDGET_HEIGHT - ICON_DIMENSIONS) / 2;
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                getEffectIdentifier(effect),
                iconX,
                iconY,
                ICON_DIMENSIONS,
                ICON_DIMENSIONS
        );

        // Draw Strings
        int stringX = iconX + TEXT_OFFSET + ICON_DIMENSIONS;
        int textYSpaceNeeded = mc.font.lineHeight + TEXT_OFFSET;
        int textYStart = y + (WIDGET_HEIGHT - textYSpaceNeeded) / 2;
        guiGraphics.drawString(mc.font, name, stringX, textYStart, EFFECT_NAME_COLOR, false);
        guiGraphics.drawString(mc.font, duration, stringX, textYStart + TEXT_OFFSET, EFFECT_DURATION_COLOR, false);
    }

    // Stolen from net.minecraft.client.gui.screens.inventory.EffectsInInventory#getEffectName
    private static Component getEffectName(MobEffectInstance effect){
        MutableComponent mutablecomponent = effect.getEffect().value().getDisplayName().copy();
        if (effect.getAmplifier() >= 1 && effect.getAmplifier() <= 9) {
            mutablecomponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + (effect.getAmplifier() + 1)));
        }

        return mutablecomponent;
    }

    private static Identifier getEffectIdentifier(MobEffectInstance effect){
        return Gui.getMobEffectSprite(effect.getEffect());
    }
}
