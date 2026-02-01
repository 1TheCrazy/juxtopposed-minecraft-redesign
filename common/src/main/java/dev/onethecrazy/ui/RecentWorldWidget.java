package dev.onethecrazy.ui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.onethecrazy.Constants;
import dev.onethecrazy.config.world.IJuxWorld;
import dev.onethecrazy.config.world.JuxServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class RecentWorldWidget extends AbstractWidget {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final Identifier TEXTURE_GREEN = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/main_menu/button_green.png");
    private static final Identifier TEXTURE_PINK = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/main_menu/button_pink.png");
    private static final int TEX_WIDTH  = 816;
    private static final int TEX_HEIGHT = 408;
    private static final int STATE_COUNT = 3; // 0=disabled, 1=normal, 2=hovered
    private static final int STATE_HEIGHT = TEX_HEIGHT / STATE_COUNT;

    private final IJuxWorld entry;

    public RecentWorldWidget(int x, int y, IJuxWorld entry) {
        super(x, y, WIDTH, HEIGHT, Component.empty());

        this.entry = entry;
    }

    public IJuxWorld getEntry(){
        return entry;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        int stateIndex = this.isHovered() ? 2 : 1;
        var tex = entry instanceof JuxServer ? TEXTURE_PINK : TEXTURE_GREEN;

        gg.blit(
                RenderPipelines.GUI_TEXTURED, tex,
                this.getX(), this.getY(),
                (float) 0, (float)(stateIndex * STATE_HEIGHT), // normal or hovered texture
                this.width, this.height,
                TEX_WIDTH, STATE_HEIGHT,
                TEX_WIDTH, TEX_HEIGHT
        );

        var font = Minecraft.getInstance().font;
        int offset = Math.round(font.lineHeight / 2f);
        int margin = 5;
        int x = getX() + margin;
        int y = getY() + margin;

        gg.drawString(font, entry.getName(), x, y, 0xFFFFFFFF);
        gg.drawString(font, entry.getDescription(), x, y += margin * 2 + offset, 0x99FFFFFF);

        handleCursor(gg);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) { defaultButtonNarrationText(narrationElementOutput); }

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        this.entry.join();
    }

    @Override
    protected void handleCursor(@NotNull GuiGraphics gg) {
        if (this.isHovered()) {
            gg.requestCursor(CursorTypes.POINTING_HAND);
        }
    }

    public static class Builder {
        private int x;
        private int y;
        private int width = WIDTH;
        private int height = HEIGHT;
        private IJuxWorld entry;

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder entry(IJuxWorld entry) {
            this.entry = entry;
            return this;
        }

        public RecentWorldWidget build() {
            if (entry == null) {
                throw new IllegalStateException("RecentWorldWidget requires an IJuxWorld entry");
            }

            return new RecentWorldWidget(x, y, entry) {
                {
                    this.width = Builder.this.width;
                    this.height = Builder.this.height;
                }
            };
        }
    }
}
