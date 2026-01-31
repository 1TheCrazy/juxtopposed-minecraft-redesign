package dev.onethecrazy.ui.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SkinPreviewRenderer {
    private final AvatarRenderState skinPreviewRenderState;
    private int x, y = 0;
    private int width, height = 0;
    private final float scale;
    private float yaw, pitch = 0;
    @Nullable private Supplier<net.minecraft.world.entity.player.PlayerSkin> skinLookup;

    public SkinPreviewRenderer(float scale){
        // Init render state
        skinPreviewRenderState = new AvatarRenderState();
        initRenderState();

        this.scale = scale;
    }

    public void renderPreview(GuiGraphics gg, float deltaTicks){
        // Tick the animation state
        skinPreviewRenderState.ageInTicks += deltaTicks;

        // Render the Preview of the player skin
        gg.submitEntityRenderState(
                skinPreviewRenderState,
                scale,
                new Vector3f(0f, 1.0f, 0f),
                new Quaternionf()
                        .rotateAxis((float) Math.toRadians(180f), 0f, 0f, 1f) // Z correction
                        .rotateAxis((float) Math.toRadians(180f + yaw), 0f, 1f, 0f) // Y correction
                        .rotateAxis((float) Math.toRadians(-pitch), 1f, 0f, 0f),
                null,
                x, y,
                x + width, y + height
        );

        if (skinLookup != null) {
            skinPreviewRenderState.skin = skinLookup.get();
        }
        // Render the border where the Mesh is placed inside
        // gg.renderOutline(x, y, width, height, 0xFFFFFFFF);
    }

    public void updateDimensions(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addRotation(float yaw, float pitch){
        this.yaw += yaw;
        this.pitch += pitch;
    }

    private void initRenderState(){
        Minecraft mc = Minecraft.getInstance();
        var profile = mc.getGameProfile();
        this.skinLookup = mc.getSkinManager().createLookup(profile, true);

        skinPreviewRenderState.entityType = EntityType.PLAYER;
        skinPreviewRenderState.distanceToCameraSq = 1;
        skinPreviewRenderState.x = skinPreviewRenderState.y = skinPreviewRenderState.z = 0.0;
    }
}