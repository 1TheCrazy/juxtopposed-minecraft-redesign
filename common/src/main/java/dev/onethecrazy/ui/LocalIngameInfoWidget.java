package dev.onethecrazy.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class LocalIngameInfoWidget {
    private static final int TIME_OF_DAY_TIMELINE_HEIGHT = 60;
    private static final int TIME_OF_DAY_TIMELINE_WIDTH = 30;
    private static final int WIDGET_HEIGHT = TIME_OF_DAY_TIMELINE_HEIGHT;
    private static final int TEXT_MARGIN_BIG = 8;
    private static final int TEXT_MARGIN_SMALL = 2;
    private static final int WIDGET_MARGIN = 10;

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
        int x = width - WIDGET_MARGIN - TIME_OF_DAY_TIMELINE_WIDTH;

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

    }

    private static void renderText(String text, int x, int y, GuiGraphics gg){
        var font = Minecraft.getInstance().font;

        gg.drawString(font, text, x, y, 0xFFFFFFFF);
    }

    // Vibe coded, I'm tired as shit
    public static String formatMcTime(long dayTime) {
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

    public record Config(boolean showTimeOfDay, boolean showTimeOfDayTimeline, boolean showCoordinates, boolean showBiome){ }
}
