package me.fengming.renderjs.events.level;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

@Info("""
        Invoked on rendering a living entity.
        """)
public class RenderEntityEventJS extends RenderEventJS {
    public PoseStack poseStack;
    public MultiBufferSource multiBufferSource;
    public LivingEntity entity;
    public LivingEntityRenderer<?, ?> renderer;
    public float partialTick;
    public int packedLight;

    protected RenderEntityEventJS(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        this.entity = entity;
        this.renderer = renderer;
        this.partialTick = partialTick;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.packedLight = packedLight;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public LivingEntityRenderer<?, ?> getRenderer() {
        return renderer;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public int getPackedLight() {
        return packedLight;
    }

    @Info("""
            Invoked after rendering a entity.
            """)
    public static class After extends RenderEntityEventJS {
        public After(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
            super(entity, renderer, partialTick, poseStack, multiBufferSource, packedLight);
        }
    }

    @Info("""
            Invoked before rendering a entity.
            """)
    public static class Before extends RenderEntityEventJS {
        public Before(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
            super(entity, renderer, partialTick, poseStack, multiBufferSource, packedLight);
        }
    }
}
