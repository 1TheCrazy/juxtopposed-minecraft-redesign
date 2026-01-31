package dev.onethecrazy.mixin;

import dev.onethecrazy.ui.JuxButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Button.class)
public class ButtonTextureOverwrite {
    @Inject(method="builder", at=@At("RETURN"), cancellable = true)
    private static void onReturnBuilder(Component message, Button.OnPress onPress, CallbackInfoReturnable<Button.Builder> cir){
        // Inject Jux Texture
        cir.setReturnValue(JuxButton.builderDefault(message, onPress));
    }
}
