package dev.onethecrazy.mixin;

import dev.onethecrazy.JuxClient;
import dev.onethecrazy.config.world.IJuxWorld;
import dev.onethecrazy.config.world.JuxServer;
import dev.onethecrazy.config.world.JuxWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LocalPlayer.class)
public class WorldJoinMixin {

    @Unique private boolean juxtopposed_redesign$ticked;

    // Best Place to make sure everything is initialized
    @Inject(method="tick", at=@At("TAIL"))
    private void onJoinWorld(CallbackInfo ci){
        if(juxtopposed_redesign$ticked)
            return;

        var mc = Minecraft.getInstance();
        IJuxWorld world;

        // Singleplayer
        if(mc.isSingleplayer()){
            IntegratedServer is = mc.getSingleplayerServer();
            // Can safely assert this here
            assert is != null;

            String levelName = is.getWorldData().getLevelName();
            String desc = is.getWorldData().getGameType().getLongDisplayName().getString();

            world = new JuxWorld(levelName, desc);
        }
        // Multiplayer
        else {
            var info = mc.getCurrentServer();

            // Can safely assert this here
            assert info != null;

            String desc;
            if(info.isLan())
                desc = Component.translatable("gui.juxtopposed_redesign.menu.server_type.lan").getString();
            else if(info.isRealm())
                desc = Component.translatable("gui.juxtopposed_redesign.menu.server_type.realm").getString();
            else
                desc = Component.translatable("gui.juxtopposed_redesign.menu.server_type.server").getString();

            world= new JuxServer(info.ip, info.name, desc, info.type());
        }

        var config = JuxClient.getInstance().config;

        juxtopposed_redesign$ticked = true;

        config.addRecentWorld(world);
    }
}
