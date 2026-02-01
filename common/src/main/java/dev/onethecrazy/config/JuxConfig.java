package dev.onethecrazy.config;

import com.google.common.base.Objects;
import dev.onethecrazy.config.world.IJuxWorld;
import dev.onethecrazy.util.FileUtil;
import dev.onethecrazy.util.RecentList;
import org.jetbrains.annotations.NotNull;

public class JuxConfig {
    private final RecentList<IJuxWorld> recentWorlds = new RecentList<>(2);

    public RecentList<IJuxWorld> getRecentWorlds(){
        return recentWorlds;
    }

    public void addRecentWorld(@NotNull IJuxWorld world){
        if(Objects.equal(recentWorlds.getNewest(), world))
            return;

        recentWorlds.add(world);

        save();
    }

    public void save(){
        FileUtil.writeConfig(this);
    }
}
