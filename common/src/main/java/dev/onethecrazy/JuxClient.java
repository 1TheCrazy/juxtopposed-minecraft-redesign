package dev.onethecrazy;

import dev.onethecrazy.config.JuxConfig;
import dev.onethecrazy.util.FileUtil;
import net.minecraft.client.main.GameConfig;

import java.nio.file.Path;

public class JuxClient {
    // State
    public final Path gameDir;
    public JuxConfig config;

    // Singleton stuff
    private static JuxClient instance;

    public static JuxClient getInstance(){
        return instance;
    }

    public static void init(GameConfig config){
        instance = new JuxClient(config);

        FileUtil.createPaths();

        instance.config = FileUtil.loadConfig();
    }

    private JuxClient(GameConfig gameConfig){
        gameDir = gameConfig.location.gameDirectory.toPath();
        config = new JuxConfig();
    }
}
