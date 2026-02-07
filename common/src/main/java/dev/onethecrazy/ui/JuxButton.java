package dev.onethecrazy.ui;

import dev.onethecrazy.JuxClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JuxButton extends Button {
    private static final Identifier DEFAULT_TEXTURE = JuxClient.getInstance().config.getTextureManager().BUTTON_GREY.get();
    private static final Identifier PINK_TEXTURE = JuxClient.getInstance().config.getTextureManager().BUTTON_PINK.get();
    private static final Identifier BLUE_TEXTURE = JuxClient.getInstance().config.getTextureManager().BUTTON_BLUE.get();
    private static final Identifier GREEN_TEXTURE = JuxClient.getInstance().config.getTextureManager().BUTTON_GREEN.get();
    public static final int TEX_WIDTH = 816;
    public static final int TEX_HEIGHT = 408;
    private static final int STATE_COUNT = 3; // 0=disabled, 1=normal, 2=hovered
    public static final int STATE_HEIGHT = TEX_HEIGHT / STATE_COUNT;

    private final Identifier texture;
    private final int u, v;
    private final int texWidth, texHeight;

    public JuxButton(
            int x, int y, int width, int height,
            Component message,
            Button.OnPress onPress,
            Button.CreateNarration createNarration,
            Identifier texture,
            int u, int v,
            int texWidth, int texHeight
    ) {
        super(x, y, width, height, message, onPress, createNarration);
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    @Override
    protected void renderContents(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        int stateIndex = getStateIndex();

        gg.blit(
                RenderPipelines.GUI_TEXTURED, texture,
                this.getX(), this.getY(),
                (float) u, (float) (v + stateIndex * STATE_HEIGHT),
                this.width, this.height,
                texWidth, STATE_HEIGHT,
                texWidth, texHeight
        );

        this.renderDefaultLabel(
                gg.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE)
        );
    }

    private int getStateIndex() {
        if (!this.active) return 0;
        return this.isHoveredOrFocused() ? 2 : 1;
    }

    public static Builder builderDefault(Component message, Button.OnPress onPress) {
        return new Builder(message, onPress)
                .texture(DEFAULT_TEXTURE, TEX_WIDTH, TEX_HEIGHT);
    }

    public static Builder builderPink(Component message, Button.OnPress onPress) {
        return new Builder(message, onPress)
                .texture(PINK_TEXTURE, TEX_WIDTH, TEX_HEIGHT);
    }

    public static Builder builderBlue(Component message, Button.OnPress onPress) {
        return new Builder(message, onPress)
                .texture(BLUE_TEXTURE, TEX_WIDTH, TEX_HEIGHT);
    }

    public static Builder builderGreen(Component message, Button.OnPress onPress) {
        return new Builder(message, onPress)
                .texture(GREEN_TEXTURE, TEX_WIDTH, TEX_HEIGHT);
    }

    public static class Builder extends Button.Builder{
        private final Component message;
        private final Button.OnPress onPress;

        private int x, y;
        private int width  = DEFAULT_WIDTH;
        private int height = DEFAULT_HEIGHT;

        private int u = 0, v = 0;
        private Identifier texture = DEFAULT_TEXTURE;
        private int texWidth = TEX_WIDTH;
        private int texHeight = TEX_HEIGHT;

        private @Nullable Tooltip tooltip;
        private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;

        public Builder(Component message, Button.OnPress onPress) {
            super(message, onPress);
            this.message = message;
            this.onPress = onPress;
        }

        public @NotNull Builder pos(int x, int y) { this.x = x; this.y = y; return this; }
        public @NotNull Builder size(int width, int height) { this.width = width; this.height = height; return this; }
        public @NotNull Builder bounds(int x, int y, int width, int height) { return pos(x, y).size(width, height); }

        public Builder uv(int u, int v) { this.u = u; this.v = v; return this; }

        public Builder texture(Identifier texture, int texWidth, int texHeight) {
            this.texture = texture;
            this.texWidth = texWidth;
            this.texHeight = texHeight;
            return this;
        }

        public @NotNull Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public @NotNull Builder createNarration(Button.@NotNull CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public @NotNull JuxButton build() {
            var button = new JuxButton(
                    x, y, width, height,
                    message, onPress, createNarration,
                    texture, u, v, texWidth, texHeight
            );

            button.setTooltip(tooltip);

            return button;
        }
    }
}
