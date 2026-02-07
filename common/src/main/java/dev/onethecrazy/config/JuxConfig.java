package dev.onethecrazy.config;

import com.google.common.base.Objects;
import dev.onethecrazy.config.world.IJuxWorld;
import dev.onethecrazy.ui.TextureManager;
import dev.onethecrazy.util.FileUtil;
import dev.onethecrazy.util.RecentList;
import org.jetbrains.annotations.NotNull;

public class JuxConfig {
    // Config
    public boolean showPotionEffectHud = true;
    private TextureManager.Theme theme = TextureManager.Theme.DARK;

    private final RecentList<IJuxWorld> recentWorlds = new RecentList<>(2);
    private transient TextureManager textureManager;

    public void save(){
        FileUtil.writeConfig(this);
    }

    // Accessors & Mutators
    public RecentList<IJuxWorld> getRecentWorlds(){
        return recentWorlds;
    }

    public void addRecentWorld(@NotNull IJuxWorld world){
        if(Objects.equal(recentWorlds.getNewest(), world))
            return;

        recentWorlds.add(world);

        save();
    }

    public TextureManager getTextureManager(){
        if (this.textureManager == null)
            this.textureManager = new TextureManager(theme);

        return this.textureManager;
    }

    public void setTheme(TextureManager.Theme theme){
        this.theme = theme;
        this.textureManager = new TextureManager(theme);
    }

    public TextureManager.Theme getTheme(){
        return this.theme;
    }
}
