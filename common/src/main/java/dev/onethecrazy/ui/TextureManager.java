package dev.onethecrazy.ui;


import dev.onethecrazy.Constants;
import net.minecraft.resources.Identifier;

public class TextureManager {
    public final Theme theme;

    // Register Textures
    public final ITexture DEFAULT_BUTTON;
    public final ITexture MAIN_MENU_DECOR_BOTTOM;
    public final ITexture MAIN_MENU_DECOR_TOP;
    public final ITexture CHARACTER_CONTAINER;
    public final ITexture BUTTON_GREY;
    public final ITexture BUTTON_PINK;
    public final ITexture BUTTON_BLUE;
    public final ITexture BUTTON_GREEN;
    public final ITexture POTION_EFFECT_CONTAINER;

    public TextureManager(Theme theme) {
        this.theme = theme;

        // Initialize Textures
        DEFAULT_BUTTON = new ThemedTexture(theme, "gui/general/button_grey.png");
        MAIN_MENU_DECOR_BOTTOM = new AmbiguousTexture("gui/main_menu/decor_blocks.png");
        MAIN_MENU_DECOR_TOP = new AmbiguousTexture("gui/main_menu/decor_blocks_top.png");
        CHARACTER_CONTAINER = new AmbiguousTexture("gui/main_menu/character_container.png");
        BUTTON_GREY  = new ThemedTexture(theme, "gui/general/button_grey.png");
        BUTTON_PINK  = new AmbiguousTexture("gui/general/button_pink.png");
        BUTTON_BLUE  = new AmbiguousTexture("gui/general/button_blue.png");
        BUTTON_GREEN = new AmbiguousTexture("gui/general/button_green.png");
        POTION_EFFECT_CONTAINER = new ThemedTexture(theme, "gui/game/effect_indicator.png");
    }

    public static Identifier texture(ITexture texture){
        return texture.get();
    }

    public static boolean hasTheme(ITexture texture){
        return texture instanceof ThemedTexture;
    }

    // -- Texture Management Stuff --
    public interface ITexture{
        Identifier get();
    }

    private static class ThemedTexture implements ITexture{
        private final Theme theme;
        private final String path;

        @Override
        public Identifier get() {
            return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/" + theme.name().toLowerCase() + "/" + path);
        }

        public ThemedTexture(Theme theme, String path){ this.theme = theme; this.path = path; }
    }

    private static class AmbiguousTexture implements ITexture{
        private final String path;

        @Override
        public Identifier get() {
            return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/ambiguous" + "/" + path);
        }

        public AmbiguousTexture(String path) { this.path = path; }
    }

    public enum Theme {
        DARK,
        LIGHT
    }
}
