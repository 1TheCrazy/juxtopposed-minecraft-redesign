package dev.onethecrazy.ui;

import dev.onethecrazy.Constants;
import dev.onethecrazy.JuxClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.joml.Vector2i;

public class LocalIngameInfoWidget {
    private static final int TIME_OF_DAY_TIMELINE_HEIGHT = 70;
    private static final int TIME_OF_DAY_TIMELINE_WIDTH = 10;
    private static final int WIDGET_HEIGHT = TIME_OF_DAY_TIMELINE_HEIGHT;
    private static final int TEXT_MARGIN_BIG = 8;
    private static final int TEXT_MARGIN_SMALL = 2;
    private static final int WIDGET_MARGIN = 10;
    private static final Identifier WIDGET_TEXTURE = JuxClient.getInstance().config.getTextureManager().LOCAL_WIDGET_BACKGROUND.get();
    private static final Vector2i WIDGET_TEXTURE_SIZE = new Vector2i(32, 220);
    private static final Identifier SUN = JuxClient.getInstance().config.getTextureManager().SUN.get();
    private static final Identifier MOON = JuxClient.getInstance().config.getTextureManager().MOON.get();
    private static final int INDICATOR_DIMENSIONS = 44;

    public static void render(Config config, GuiGraphics gg){
        if(config.showTimeOfDayTimeline)
            renderTimeOfDayTimeline(gg);

        renderTextComponents(config, gg);
    }

    private static void renderTextComponents(Config config, GuiGraphics gg){
        var mc = Minecraft.getInstance();
        var level = mc.level;
        var player = mc.player;
        var font = mc.font;
        // Can safely assert this here
        assert level != null && player != null;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int y = height - WIDGET_MARGIN - WIDGET_HEIGHT;
        int x = width - WIDGET_MARGIN * 2 - TIME_OF_DAY_TIMELINE_WIDTH;

        // Render top-down

        y += TEXT_MARGIN_SMALL;
        if(config.showTimeOfDay){
            String time = formatMcTime(level.getDayTime());
            int renderX = x - font.width(time);

            renderText(time, renderX, y, gg);
        }
        y += font.lineHeight - 1;

        y += TEXT_MARGIN_BIG;
        if(config.showCoordinates){
            String playerXString = Math.round(player.getX()) + " X";
            String playerYString = Math.round(player.getY()) + " Y";
            String playerZString = Math.round(player.getZ()) + " Z";

            renderText(playerXString, x - font.width(playerXString), y, gg);
            y += font.lineHeight - 1 + TEXT_MARGIN_SMALL;
            renderText(playerYString, x - font.width(playerYString), y, gg);
            y += font.lineHeight - 1 + TEXT_MARGIN_SMALL;
            renderText(playerZString, x - font.width(playerZString), y, gg);
            y += font.lineHeight - 1 + TEXT_MARGIN_SMALL;
        }
        else
            y += font.lineHeight * 3 + TEXT_MARGIN_SMALL * 3;

        y += TEXT_MARGIN_BIG;
        if(config.showBiome){
            var biomeId = level.getBiome(player.blockPosition())
                    .unwrapKey()
                    .orElseThrow()
                    .identifier()
                    .toLanguageKey();

            String biomeString = Component.translatable("biome." + biomeId).getString();

            renderText(biomeString, x - font.width(biomeString), y, gg);
        }
    }

    private static void renderTimeOfDayTimeline(GuiGraphics gg){
        var mc = Minecraft.getInstance();
        var level = mc.level;

        // Can safely assert this here
        assert level != null;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int y = height - WIDGET_MARGIN - WIDGET_HEIGHT;
        int x = width - WIDGET_MARGIN - TIME_OF_DAY_TIMELINE_WIDTH;
        long time = level.getDayTime();

        // Draw the background
        gg.blit(
                RenderPipelines.GUI_TEXTURED,
                WIDGET_TEXTURE,
                x, y,
                (float) 0, 0,
                TIME_OF_DAY_TIMELINE_WIDTH, TIME_OF_DAY_TIMELINE_HEIGHT,
                WIDGET_TEXTURE_SIZE.x(), WIDGET_TEXTURE_SIZE.y(),
                WIDGET_TEXTURE_SIZE.x(), WIDGET_TEXTURE_SIZE.y(),
                timeOfDayTimelineColor(time)
        );

        // Draw indicator
        int indicatorDimensions = Math.round(TIME_OF_DAY_TIMELINE_WIDTH * 1.5f);
        int offset = (indicatorDimensions - TIME_OF_DAY_TIMELINE_WIDTH) / 2;
        int indicatorX = x - offset;

        gg.blit(
                RenderPipelines.GUI_TEXTURED,
                hasNightStarted(time) ? MOON : SUN,
                indicatorX, getTimeOfDayTimelineIndicatorHeight(time, y + TIME_OF_DAY_TIMELINE_HEIGHT - indicatorDimensions + offset, y - offset),
                (float) 0, 0,
                indicatorDimensions, indicatorDimensions,
                INDICATOR_DIMENSIONS, INDICATOR_DIMENSIONS,
                INDICATOR_DIMENSIONS, INDICATOR_DIMENSIONS
        );
    }

    private static void renderText(String text, int x, int y, GuiGraphics gg){
        var font = Minecraft.getInstance().font;

        gg.drawString(font, text, x, y, 0xFFFFFFFF);
    }

    // Vibe coded, I'm tired as shit
    private static String formatMcTime(long dayTime) {
        long timeOfDay = dayTime % 24000;

        // Convert ticks → total minutes
        int totalMinutes = (int) ((timeOfDay * 1440) / 24000);

        // Shift so 0 ticks = 6:00 AM
        totalMinutes = (totalMinutes + 360) % 1440;

        int hour24 = totalMinutes / 60;
        int minute = totalMinutes % 60;

        // Keep only tens of minutes
        minute = (minute / 10) * 10;

        boolean isPM = hour24 >= 12;
        int hour12 = hour24 % 12;
        if (hour12 == 0) hour12 = 12;

        return String.format(
                "%d:%02d %s",
                hour12,
                minute,
                isPM ? "PM" : "AM"
        );
    }

    // Vibe-coded as well
    private static int timeOfDayTimelineColor(long dayTime) {
        long t = Math.floorMod(dayTime, 24000); // safe wrap

        // Base cosine: 1 at noon (6000), 0 at midnight (18000)
        double base = 0.5 + 0.5 *
                Math.cos(2.0 * Math.PI * (t - 6000) / 24000.0);

        // Remap 0..1 → 0.5..1
        double brightness = 0.5 + 0.5 * base;

        int c = (int) Math.round(brightness * 255.0);
        return (0xFF << 24) | (c << 16) | (c << 8) | c;
    }

    // Vibe-coded again
    private static int getTimeOfDayTimelineIndicatorHeight(long dayTime, int base, int peak) {
        long t = Math.floorMod(dayTime, 24000); // 0..23999, safe even if dayTime is negative

        // 0 at 0 & 12000, 1 at 6000 & 18000
        double wave = 0.5 - 0.5 * Math.cos(2.0 * Math.PI * (t / 12000.0)); // 0..1

        double h = base + (peak - base) * wave;
        return (int) Math.round(h);
    }

    private static boolean hasNightStarted(long dayTime) {
        long timeOfDay = Math.floorMod(dayTime, 24000); // safe wrap

        return timeOfDay >= 12000;
    }

    public record Config(boolean showTimeOfDay, boolean showTimeOfDayTimeline, boolean showCoordinates, boolean showBiome){ }
}
