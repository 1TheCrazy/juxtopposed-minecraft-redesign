package dev.onethecrazy;

import dev.onethecrazy.config.JuxConfig;
import dev.onethecrazy.util.FileUtil;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class JuxClient {
    // State
    public final Path gameDir = Minecraft.getInstance().gameDirectory.toPath();
    public JuxConfig config;

    // Singleton stuff
    private static JuxClient instance;

    public static JuxClient getInstance(){
        return instance;
    }

    public static void init(){
        Constants.LOG.info("Initializing Juxtopposed's Redesign (by 1TheCrazy)");

        instance = new JuxClient();

        FileUtil.createPaths();

        instance.config = FileUtil.loadConfig();
    }

    private JuxClient(){
        config = new JuxConfig();
    }
}
