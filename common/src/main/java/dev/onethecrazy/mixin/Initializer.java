package dev.onethecrazy.mixin;

import dev.onethecrazy.Constants;
import dev.onethecrazy.JuxClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class Initializer {
    @Inject(method="<init>", at=@At("TAIL"))
    private void init(GameConfig gameConfig, CallbackInfo ci){
        JuxClient.init(gameConfig);
    }
}
