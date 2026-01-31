package dev.onethecrazy.config;

import dev.onethecrazy.config.world.IJuxWorld;
import dev.onethecrazy.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JuxConfig {
    private final List<IJuxWorld> recentWorlds = new ArrayList<>();

    public List<IJuxWorld> getRecentWorlds(){
        return recentWorlds;
    }

    public void addRecentWorld(@NotNull IJuxWorld world){
        if(recentWorlds.size() >= 2){
            recentWorlds.subList(0, recentWorlds.size() - 1).clear();
        }

        recentWorlds.add(world);

        save();
    }

    public void save(){
        FileUtil.writeConfig(this);
    }
}
