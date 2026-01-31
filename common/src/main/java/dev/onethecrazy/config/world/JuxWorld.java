package dev.onethecrazy.config.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;

public class JuxWorld implements IJuxWorld{
    public String name, desc;

    @Override
    public void join() {
        var mc = Minecraft.getInstance();

        mc.createWorldOpenFlows().openWorld(this.name, () -> mc.setScreen(new TitleScreen()));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    public JuxWorld(String name, String desc){
        this.name = name;
        this.desc = desc;
    }
}
