package dev.onethecrazy.config.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class JuxServer implements IJuxWorld{
    public String ip, name, desc;
    public ServerData.Type serverType;

    @Override
    public void join() {
        var mc = Minecraft.getInstance();
        var screen = mc.screen;
        // Can safely assert this
        assert screen != null;

        ConnectScreen.startConnecting(screen, mc, ServerAddress.parseString(ip), new ServerData(name, ip, serverType), false, null);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    public JuxServer(String ip, String name, String desc, ServerData.Type type){
        this.ip = ip;
        this.desc = desc;
        this.name = name;
        this.serverType = type;
    }
}
