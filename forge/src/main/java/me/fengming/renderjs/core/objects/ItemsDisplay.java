package me.fengming.renderjs.core.objects;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@RemapPrefixForJS("rjs$")
public class ItemsDisplay extends RenderObject {
    protected ItemStack item;
    protected ItemDisplayContext context = ItemDisplayContext.GUI;
    protected boolean leftHand = false;
    protected int light = 15728880;

    public ItemsDisplay(float[] vertices, float r, float g, float b, float a, String texLoc, ObjectType type) {
        super(vertices, r, g, b, a, texLoc, type);
    }

    public void rjs$setItem(ItemStack item) {
        this.item = item;
    }

    public void rjs$setItemDisplayContext(ItemDisplayContext context) {
        this.context = context;
    }

    public void rjs$setLeftHand(boolean leftHand) {
        this.leftHand = leftHand;
    }

    public void rjs$setLight(int light) {
        this.light = light;
    }

    @Override
    public void rjs$render() {
        prepare();

        for (int i = 0; i < vertices.length; i += 3) {
            poseStack.translate(vertices[i], vertices[i + 1], vertices[i + 2]);
            mc.getItemRenderer().renderStatic(mc.player, item, context, leftHand, poseStack, mc.renderBuffers().bufferSource(), mc.level, light, OverlayTexture.NO_OVERLAY, i);
        }
        poseStack.popPose();
    }
}
