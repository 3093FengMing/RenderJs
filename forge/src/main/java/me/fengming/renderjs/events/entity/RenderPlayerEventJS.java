package me.fengming.renderjs.events.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class RenderPlayerEventJS extends RenderEventJS {
    public PoseStack poseStack;
    public MultiBufferSource multiBufferSource;
    public Player entity;
    public PlayerRenderer renderer;
    public float partialTick;
    public int packedLight;

    public RenderPlayerEventJS(RenderPlayerEvent event) {
        this.entity = event.getEntity();
        this.renderer = event.getRenderer();
        this.partialTick = event.getPartialTick();
        this.poseStack = event.getPoseStack();
        this.multiBufferSource = event.getMultiBufferSource();
        this.packedLight = event.getPackedLight();
    }

    @Override
    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public MultiBufferSource getMultiBufferSource() {
        return this.multiBufferSource;
    }

    public Player getEntity() {
        return this.entity;
    }

    public PlayerRenderer getRenderer() {
        return this.renderer;
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    public int getPackedLight() {
        return this.packedLight;
    }
}
