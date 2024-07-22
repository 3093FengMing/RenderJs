package me.fengming.renderjs.events.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;

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

    public RenderEntityEventJS(RenderLivingEvent<?, ?> event) {
        this.entity = event.getEntity();
        this.renderer = event.getRenderer();
        this.partialTick = event.getPartialTick();
        this.poseStack = event.getPoseStack();
        this.multiBufferSource = event.getMultiBufferSource();
        this.packedLight = event.getPackedLight();
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
}
