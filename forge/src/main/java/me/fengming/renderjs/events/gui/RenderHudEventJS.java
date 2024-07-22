package me.fengming.renderjs.events.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.event.RenderGuiEvent;

public class RenderHudEventJS extends RenderEventJS {
    public Window window;
    public GuiGraphics graphics;
    public float partialTick;

    public RenderHudEventJS(RenderGuiEvent event) {
        this.window = event.getWindow();
        this.graphics = event.getGuiGraphics();
        this.partialTick = event.getPartialTick();
    }

    @Override
    public PoseStack getPoseStack() {
        return graphics.pose();
    }

    public Window getWindow() {
        return this.window;
    }

    public GuiGraphics getGraphics() {
        return this.graphics;
    }

    public float getPartialTick() {
        return this.partialTick;
    }
}
