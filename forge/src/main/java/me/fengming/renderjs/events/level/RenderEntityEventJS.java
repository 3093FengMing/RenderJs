package me.fengming.renderjs.events.level;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

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

    protected RenderEntityEventJS(RenderPlayerEvent event) {
        this.entity = event.getEntity();
        this.renderer = event.getRenderer();
        this.partialTick = event.getPartialTick();
        this.poseStack = event.getPoseStack();
        this.multiBufferSource = event.getMultiBufferSource();
        this.packedLight = event.getPackedLight();
    }

    protected RenderEntityEventJS(RenderLivingEvent<?, ?> event) {
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

    @Info("""
            Invoked after rendering a entity.
            """)
    public static class After extends RenderEntityEventJS {
        public After(RenderPlayerEvent event) {
            super(event);
        }

        public After(RenderLivingEvent<?, ?> event) {
            super(event);
        }
    }

    @Info("""
            Invoked before rendering a entity.
            """)
    public static class Before extends RenderEntityEventJS {
        public Before(RenderPlayerEvent event) {
            super(event);
        }

        public Before(RenderLivingEvent<?, ?> event) {
            super(event);
        }
    }
}
