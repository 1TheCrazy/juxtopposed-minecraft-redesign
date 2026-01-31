package dev.onethecrazy.ui.rendering;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;

public class SmallLogoRenderer{
    // Textures
    public static final Identifier MINECRAFT_LOGO =
            Identifier.withDefaultNamespace("textures/gui/title/minecraft.png");
    public static final Identifier EASTER_EGG_LOGO =
            Identifier.withDefaultNamespace("textures/gui/title/minceraft.png");
    public static final Identifier MINECRAFT_EDITION =
            Identifier.withDefaultNamespace("textures/gui/title/edition.png");
    // Texture sizes
    private static final int LOGO_TEX_WIDTH  = 256;
    private static final int LOGO_TEX_HEIGHT = 64;
    private static final int EDITION_TEX_WIDTH  = 128;
    private static final int EDITION_TEX_HEIGHT = 16;
    // Sprite sizes
    private static final int LOGO_WIDTH  = 256;
    private static final int LOGO_HEIGHT = 44;
    private static final int EDITION_WIDTH  = 128;
    private static final int EDITION_HEIGHT = 14;
    // Scale
    private static final float SCALE = 0.75F;

    public static final int DEFAULT_Y_OFFSET = 30;
    private static final int EDITION_OVERLAP = 7;

    private final boolean showEasterEgg = RandomSource.create().nextFloat() < 1.0E-4F;

    private final boolean keepLogoThroughFade;

    public SmallLogoRenderer(boolean keepLogoThroughFade) {
        this.keepLogoThroughFade = keepLogoThroughFade;
    }

    public void renderLogo(GuiGraphics graphics, int screenWidth, float fade) {
        renderLogo(graphics, screenWidth, fade, DEFAULT_Y_OFFSET);
    }

    public void renderLogo(GuiGraphics graphics, int screenWidth, float fade, int yOffset) {
        float alpha = keepLogoThroughFade ? 1.0F : fade;
        int color = ARGB.white(alpha);

        int scaledLogoWidth  = Math.round(LOGO_WIDTH * SCALE);
        int scaledLogoHeight = Math.round(LOGO_HEIGHT * SCALE);
        int scaledEditionWidth  = Math.round(EDITION_WIDTH * SCALE);
        int scaledEditionHeight = Math.round(EDITION_HEIGHT * SCALE);

        int logoX = screenWidth / 2 - scaledLogoWidth / 2;
        int logoY = yOffset;

        Identifier logoTexture = showEasterEgg ? EASTER_EGG_LOGO : MINECRAFT_LOGO;

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                logoTexture,
                logoX, logoY,
                0.0F, 0.0F,
                scaledLogoWidth, scaledLogoHeight,
                LOGO_WIDTH, LOGO_HEIGHT,
                LOGO_TEX_WIDTH, LOGO_TEX_HEIGHT,
                color
        );

        int editionX = screenWidth / 2 - scaledEditionWidth / 2;
        int editionY = logoY + scaledLogoHeight - Math.round(EDITION_OVERLAP * SCALE);

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                MINECRAFT_EDITION,
                editionX, editionY,
                0.0F, 0.0F,
                scaledEditionWidth, scaledEditionHeight,
                EDITION_WIDTH, EDITION_HEIGHT,
                EDITION_TEX_WIDTH, EDITION_TEX_HEIGHT,
                color
        );
    }

    public boolean keepLogoThroughFade() {
        return keepLogoThroughFade;
    }
}
