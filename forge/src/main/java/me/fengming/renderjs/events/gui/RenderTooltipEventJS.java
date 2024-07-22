package me.fengming.renderjs.events.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.events.RenderEventJS;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;

import java.util.List;

public class RenderTooltipEventJS extends RenderEventJS {
    public ItemStack itemStack;
    public List<ClientTooltipComponent> components;
    public Font font;
    public GuiGraphics graphics;
    public int x;
    public int y;

    public RenderTooltipEventJS(RenderTooltipEvent event) {
        this.itemStack = event.getItemStack();
        this.x = event.getX();
        this.y = event.getY();
        this.font = event.getFont();
        this.graphics = event.getGraphics();
        this.components = event.getComponents();
    }

    @Override
    public PoseStack getPoseStack() {
        return graphics.pose();
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public List<ClientTooltipComponent> getComponents() {
        return this.components;
    }

    public Font getFont() {
        return this.font;
    }

    public GuiGraphics getGraphics() {
        return this.graphics;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public static class Before extends RenderTooltipEventJS {
        public int screenWidth;
        public int screenHeight;
        public ClientTooltipPositioner positioner;

        public Before(RenderTooltipEvent.Pre event) {
            super(event);
            this.screenWidth = event.getScreenWidth();
            this.screenHeight = event.getScreenHeight();
            this.positioner = event.getTooltipPositioner();
        }

        public int getScreenWidth() {
            return this.screenWidth;
        }

        public int getScreenHeight() {
            return this.screenHeight;
        }

        public ClientTooltipPositioner getPositioner() {
            return this.positioner;
        }
    }

    public static class Color extends RenderTooltipEventJS {
        public final int originalBackground;
        public final int originalBorderStart;
        public final int originalBorderEnd;
        public int backgroundStart;
        public int backgroundEnd;
        public int borderStart;
        public int borderEnd;
        public Color(RenderTooltipEvent.Color event) {
            super(event);
            this.originalBackground = event.getOriginalBackgroundStart();
            this.originalBorderStart = event.getOriginalBorderStart();
            this.originalBorderEnd = event.getOriginalBorderEnd();
            this.backgroundStart = event.getBackgroundStart();
            this.backgroundEnd = event.getBackgroundEnd();
            this.borderStart = event.getBorderStart();
            this.borderEnd = event.getBorderEnd();
        }

        public int getOriginalBackground() {
            return this.originalBackground;
        }

        public int getOriginalBorderStart() {
            return this.originalBorderStart;
        }

        public int getOriginalBorderEnd() {
            return this.originalBorderEnd;
        }

        public int getBackgroundStart() {
            return this.backgroundStart;
        }

        public int getBackgroundEnd() {
            return this.backgroundEnd;
        }

        public int getBorderStart() {
            return this.borderStart;
        }

        public int getBorderEnd() {
            return this.borderEnd;
        }

        public void setBackgroundStart(int backgroundStart) {
            this.backgroundStart = backgroundStart;
        }

        public void setBackgroundEnd(int backgroundEnd) {
            this.backgroundEnd = backgroundEnd;
        }

        public void setBorderStart(int borderStart) {
            this.borderStart = borderStart;
        }

        public void setBorderEnd(int borderEnd) {
            this.borderEnd = borderEnd;
        }
    }

    @Info("""
            No objects can be rendered here, it should only be able to modify tooltips.
            """)
    public static class GatherComponents extends RenderEventJS {
        public ItemStack itemStack;
        public int screenWidth;
        public int screenHeight;
        public List<Either<FormattedText, TooltipComponent>> elements;
        public int maxWidth;

        public GatherComponents(RenderTooltipEvent.GatherComponents event) {
            this.itemStack = event.getItemStack();
            this.screenWidth = event.getScreenWidth();
            this.screenHeight = event.getScreenHeight();
            this.elements = event.getTooltipElements();
            this.maxWidth = event.getMaxWidth();
        }

        @Override
        public PoseStack getPoseStack() {
            throw new IllegalStateException();
        }

        public ItemStack getItemStack() {
            return this.itemStack;
        }

        public int getScreenWidth() {
            return this.screenWidth;
        }

        public int getScreenHeight() {
            return this.screenHeight;
        }

        public List<Either<FormattedText, TooltipComponent>> getElements() {
            return this.elements;
        }

        public int getMaxWidth() {
            return this.maxWidth;
        }
    }
}
