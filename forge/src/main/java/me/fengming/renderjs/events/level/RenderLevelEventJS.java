package me.fengming.renderjs.events.level;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@Info("""
        Invoked on rendering the world.
        """)
public class RenderLevelEventJS extends RenderEventJS {
    private final LevelRenderer levelRenderer;
    private final PoseStack poseStack;
    private final Matrix4f projectionMatrix;
    private final int renderTick;
    private final float partialTick;
    private final Camera camera;
    private final Frustum frustum;

    public RenderLevelEventJS(RenderLevelStageEvent event) {
        this.levelRenderer = event.getLevelRenderer();
        this.poseStack = event.getPoseStack();
        this.projectionMatrix = event.getProjectionMatrix();
        this.renderTick = event.getRenderTick();
        this.partialTick = event.getPartialTick();
        this.camera = event.getCamera();
        this.frustum = event.getFrustum();
    }

    public LevelRenderer getLevelRenderer() {
        return this.levelRenderer;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public int getRenderTick() {
        return this.renderTick;
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Frustum getFrustum() {
        return this.frustum;
    }

    public void renderInWorldXyz(String id, float x, float y, float z) {
        super.renderInWorldCameraXYZ(id, this.camera, x, y, z);
    }
}
