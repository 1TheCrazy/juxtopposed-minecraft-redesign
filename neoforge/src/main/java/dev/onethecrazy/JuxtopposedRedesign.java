package dev.onethecrazy;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class JuxtopposedRedesign {

    public JuxtopposedRedesign(IEventBus eventBus) {
        JuxClient.init();
    }
}