package dev.onethecrazy.mixin;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import dev.onethecrazy.Constants;
import dev.onethecrazy.ui.JuxButton;
import dev.onethecrazy.ui.rendering.SkinPreviewRenderer;
import dev.onethecrazy.ui.rendering.SmallLogoRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import org.joml.Vector2i;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MainMenuMixin extends Screen{
    @Unique private static final Component COPYRIGHT_TEXT = Component.translatable("title.credits");
    @Unique private static final int ROW_HEIGHT = 24;
    @Unique private static final String NEWS_LINK = "https://www.minecraft.net/en-us/articles"; // I don't know what the vision here was
    @Unique private static final SmallLogoRenderer juxtopposed_redesign$logoRenderer = new SmallLogoRenderer(false);
    @Unique private static final SkinPreviewRenderer juxtopposed_redesign$skinPreviewRenderer = new SkinPreviewRenderer(30f);;
    @Unique private static final Identifier juxtopposed_redesign$DECOR = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/main_menu/decor_blocks.png");
    @Unique private static final Identifier DECOR_TOP = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/main_menu/decor_blocks_top.png");
    @Unique private static final Vector2i DECOR_DIMENSIONS = new Vector2i(3600, 824);
    @Unique private static final int BUTTON_WIDTH = 120;
    @Unique private static final int BUTTON_HEIGHT = 20;
    @Unique private static final Identifier CHARACTER_CONTAINER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/main_menu/character_container.png");
    @Unique private static final Vector2i CONTAINER_DIMENSIONS = new Vector2i(818, 560);

    @Shadow private @Nullable SplashRenderer splash;
    @Shadow private @Nullable RealmsNotificationsScreen realmsNotificationsScreen;
    @Shadow protected abstract int createDemoMenuOptions(int y, int rowHeight);
    @Shadow protected abstract @Nullable Component getMultiplayerDisabledReason();
    @Shadow private long fadeInStart;
    @Shadow private boolean fading;

    protected MainMenuMixin(Component title) { super(title); }

    @Inject(method="init", at=@At("HEAD"), cancellable = true)
    protected void init(CallbackInfo ci) {
        // Don't render splash
        this.splash = null;
        this.realmsNotificationsScreen = null;

        int copyrightTextWidth = this.font.width(COPYRIGHT_TEXT);
        int copyrightX = this.width - copyrightTextWidth - 2;
        int buttonY = this.height / 4;

        if(this.minecraft.isDemo())
            this.createDemoMenuOptions(buttonY, ROW_HEIGHT);
        else
            juxtopposed_redesign$createNormalMenuOptions(buttonY, ROW_HEIGHT);

        this.addRenderableWidget(new PlainTextButton(copyrightX, this.height - 10, copyrightTextWidth, 10, COPYRIGHT_TEXT, (button) -> {
            this.minecraft.setScreen(new CreditsAndAttributionScreen(this));
        }, this.font));

        ci.cancel();
    }

    @Unique
    private void juxtopposed_redesign$createNormalMenuOptions(int y, int rowHeight){
        int buttonX = this.width / 2 - 130;

        // Worlds Button
        this.addRenderableWidget(JuxButton.builderDefault(Component.translatable("gui.juxtopposed_redesign.menu.world_button"), (button) -> {
            this.minecraft.setScreen(new SelectWorldScreen(this));
        }).bounds(buttonX, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        Component component = this.getMultiplayerDisabledReason();
        boolean multiplayerDisabled = component == null;
        Tooltip tooltip = component != null ? Tooltip.create(component) : null;

        // Servers
        this.addRenderableWidget(JuxButton.builderDefault(Component.translatable("gui.juxtopposed_redesign.menu.server_button"), (p_280833_) -> {
            Screen screen = this.minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this);
            this.minecraft.setScreen(screen);
        }).bounds(buttonX, y += rowHeight, BUTTON_WIDTH, BUTTON_HEIGHT).tooltip(tooltip).build()).active = multiplayerDisabled;

        // Realms
        this.addRenderableWidget(JuxButton.builderPink(Component.translatable("gui.juxtopposed_redesign.menu.realms_button"), (p_315821_) -> {
            this.minecraft.setScreen(new RealmsMainScreen(this));
        }).bounds(buttonX, y += rowHeight, BUTTON_WIDTH, BUTTON_HEIGHT).tooltip(tooltip).build()).active = multiplayerDisabled;

        // Settings
        this.addRenderableWidget(JuxButton.builderDefault(Component.translatable("gui.juxtopposed_redesign.menu.settings_button"), (p_315821_) -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }).bounds(buttonX, y += rowHeight, BUTTON_WIDTH, BUTTON_HEIGHT).tooltip(tooltip).build());

        // Access
        this.addRenderableWidget(JuxButton.builderDefault(Component.translatable("gui.juxtopposed_redesign.menu.access_button"), (p_315821_) -> {
            this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options));
        }).bounds(buttonX, y += rowHeight, BUTTON_WIDTH, BUTTON_HEIGHT).tooltip(tooltip).build());

        // News
        this.addRenderableWidget(JuxButton.builderBlue(Component.translatable("gui.juxtopposed_redesign.menu.news_button"), (p_315821_) -> {
            Util.getPlatform().openUri(NEWS_LINK);
        }).bounds(buttonX, y += rowHeight, BUTTON_WIDTH, BUTTON_HEIGHT).tooltip(tooltip).build());

        // Quit
        this.addRenderableWidget(JuxButton.builderDefault(Component.translatable("gui.juxtopposed_redesign.menu.quit_button"), (p_315821_) -> {
            this.minecraft.stop();
        }).bounds(buttonX, y += rowHeight, BUTTON_WIDTH, BUTTON_HEIGHT).tooltip(tooltip).build());
    }


    @Inject(method="render", at=@At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.fadeInStart == 0L && this.fading)
            this.fadeInStart = (int) Util.getMillis();

        float alpha = juxtopposed_redesign$getFadeAlpha();

        if (this.fading)
            fadeWidgets(alpha);

        this.renderPanorama(guiGraphics, partialTicks);

        // Math.round(width * 824f / 3600f) for preserved aspect ratio, but it looks bad
        int DECOR_HEIGHT = Math.round(width * 500f / 3600f);

        // Top decor
        // FUCK MOJANG MAPPINGS, WHY DON'T THEY HAVE FUCKING PARAMETER NAMES, THIS WAS PAINFUL
        int topDecorRenderY = - Math.round(DECOR_HEIGHT * 0.8f) / 2;
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED, // Pipeline
                DECOR_TOP, // Tex
                0, - Math.round(DECOR_HEIGHT * 0.8f) / 2, // x, y
                0, 0, // u, v
                width, DECOR_HEIGHT, // width, height
                DECOR_DIMENSIONS.x(), DECOR_DIMENSIONS.y(), // region size
                DECOR_DIMENSIONS.x(), DECOR_DIMENSIONS.y() // texture size
        );

        // Bottom decor
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED, // Pipeline
                juxtopposed_redesign$DECOR, // Tex
                0, height - DECOR_HEIGHT, // x, y
                0, 0, // u, v
                width, DECOR_HEIGHT, // width, height
                DECOR_DIMENSIONS.x(), DECOR_DIMENSIONS.y(), // region size
                DECOR_DIMENSIONS.x(), DECOR_DIMENSIONS.y() // texture size
        );

        // Continue Playing section
        int buttonX = this.width / 2 - 130;
        int buttonY = this.height / 4;
        int minX = buttonX + BUTTON_WIDTH + 10;
        int minY = buttonY;
        int continueHeight = Math.round(font.lineHeight * ( 3f / 2));
        int continueWidth = BUTTON_WIDTH;

        guiGraphics.fill(minX, minY, minX + continueWidth, minY + continueHeight, ARGB.color(0.5f, 0));
        guiGraphics.drawString(font, Component.translatable("gui.juxtopposed_redesign.menu.continue"), minX + 3, minY + Math.round(continueHeight * 0.25f), 0xFFFFFFFF);

        int worldSectionHeight = 100;

        // Skin Section
        int containerWidth = BUTTON_WIDTH;
        int containerHeight = Math.round(120f * 560f / 818f);
        int containerY = minY + continueHeight + worldSectionHeight;

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED, // Pipeline
                CHARACTER_CONTAINER, // Tex
                minX, containerY, // x, y
                0, 0, // u, v
                containerWidth, containerHeight, // width, height
                CONTAINER_DIMENSIONS.x(), CONTAINER_DIMENSIONS.y(), // region size
                CONTAINER_DIMENSIONS.x(), CONTAINER_DIMENSIONS.y() // texture size
        );

        String name = Minecraft.getInstance().getUser().getName();
        guiGraphics.drawCenteredString(font, name, minX + Math.round(BUTTON_WIDTH / 2f), containerY + Math.round(font.lineHeight / 2f), 0xFFFFFFFF);

        // Need to update in case of window resize
        juxtopposed_redesign$skinPreviewRenderer.updateDimensions(minX, containerY + BUTTON_HEIGHT - 3, containerWidth, containerHeight - BUTTON_HEIGHT);
        juxtopposed_redesign$skinPreviewRenderer.renderPreview(guiGraphics, partialTicks);

        // Render Text, Buttons, etc. at the end
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        juxtopposed_redesign$logoRenderer.renderLogo(guiGraphics, width, alpha, Math.round(topDecorRenderY + DECOR_HEIGHT / 1.5f));

        ci.cancel();
    }

    @Unique
    private float juxtopposed_redesign$getFadeAlpha() {
        if (!this.fading) {
            return 1.0F;
        }

        float elapsedMillis = Util.getMillis() - this.fadeInStart;
        float progress = elapsedMillis / 2000.0F;

        if (progress >= 1.0F) {
            this.fading = false;
            return 1.0F;
        }

        progress = Mth.clamp(progress, 0.0F, 1.0F);

        // Fade in during the second half of the animation
        return Mth.clampedMap(progress, 0.5F, 1.0F, 0.0F, 1.0F);
    }
}
