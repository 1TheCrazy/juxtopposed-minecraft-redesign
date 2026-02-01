package dev.onethecrazy.mixin;

import dev.onethecrazy.ui.JuxButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Button.Builder.class)
public abstract class ButtonBuilderMixin {
    @Accessor("y")
    abstract int jrd$getY();

    @Accessor("x")
    abstract int jrd$getX();

    @Accessor("width")
    abstract int jrd$getWidth();

    @Accessor("height")
    abstract int jrd$getHeight();

    @Accessor("message")
    abstract Component jrd$getMessage();

    @Accessor("onPress")
    abstract Button.OnPress jrd$getOnPress();

    @Inject(method="build", at=@At("TAIL"), cancellable = true)
    private void onReturnBuilder(CallbackInfoReturnable<Button> cir){
        var custom = JuxButton
                .builderDefault(jrd$getMessage(), jrd$getOnPress())
                        .size(jrd$getWidth(), jrd$getHeight());

        cir.setReturnValue(custom.build());
    }
}
