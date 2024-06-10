package me.fengming.renderjs.events;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;

@Info("""
	Invoked before rendering a block.
	""")
public class BeforeRenderBlockEventJS extends RenderEventJS {
	public LevelRenderer levelRenderer;
	public Camera camera;
	public PoseStack poseStack;
	public Matrix4f matrix4f;
	public RenderType renderType;

	public BeforeRenderBlockEventJS(LevelRenderer levelRenderer, Camera camera, PoseStack poseStack, Matrix4f matrix4f, RenderType renderType) {
		this.levelRenderer = levelRenderer;
		this.camera = camera;
		this.poseStack = poseStack;
		this.matrix4f = matrix4f;
		this.renderType = renderType;
	}

	@Override
	public Camera getCamera() {
		return this.camera;
	}

	@Override
	public PoseStack getPoseStack() {
		return this.poseStack;
	}

	@Override
	public Matrix4f getMatrix4f() {
		return this.matrix4f;
	}

	public LevelRenderer getLevelRenderer() {
		return this.levelRenderer;
	}

	public RenderType getRenderType() {
		return this.renderType;
	}
}
