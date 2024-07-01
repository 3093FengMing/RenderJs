package me.fengming.renderjs.core.objects;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

@RemapPrefixForJS("rjs$")
public class BlocksDisplay extends RenderObject {
    protected BlockState blockState;
    protected int worldLight = 15;
    protected int blockLight = 15;
    protected RenderType renderType = null;

    public BlocksDisplay(float[] vertices, float r, float g, float b, float a, String texLoc, ObjectType type) {
        super(vertices, r, g, b, a, texLoc, type);
    }

    public void rjs$setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public void rjs$setWorldLight(int worldLight) {
        this.worldLight = worldLight;
    }

    public void rjs$setBlockLight(int blockLight) {
        this.blockLight = blockLight;
    }

    public void rjs$setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }

    @Override
    public void rjs$render() {
        prepare();

        for (int i = 0; i < vertices.length; i += 3) {
            poseStack.translate(vertices[i], vertices[i + 1], vertices[i + 2]);
            mc.getBlockRenderer().renderSingleBlock(blockState, poseStack, mc.renderBuffers().bufferSource(), LightTexture.pack(worldLight, blockLight), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
        }
        poseStack.popPose();
    }
}
