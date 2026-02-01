package dev.onethecrazy.config.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;

import java.util.Objects;

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

    @Override
    public boolean sameAs(IJuxWorld other) {
        if(other instanceof JuxServer)
            return false;

        var wOther = (JuxWorld) other;

        return Objects.equals(wOther.name, name) && Objects.equals(wOther.desc, desc);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof JuxWorld w && sameAs(w);
    }

    public JuxWorld(String name, String desc){
        this.name = name;
        this.desc = desc;
    }
}
