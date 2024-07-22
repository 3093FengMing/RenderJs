package me.fengming.renderjs.core.objects.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import me.fengming.renderjs.core.RenderObject;
import me.fengming.renderjs.core.RenderObjectManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record CustomOverlay(String id, Ordering ordering,
                            ResourceLocation other, List<RenderObject> objects,
                            RenderCallback callback) implements IGuiOverlay {
    public static CustomOverlay loadFromNbt(String id, CompoundTag overlay, RenderCallback callback) {
        Ordering ordering;
        if (!overlay.contains("ordering")) {
            ordering = Ordering.AFTER_ALL;
        } else {
            String sOrdering = overlay.getString("ordering");
            if (!Arrays.stream(Ordering.values()).map(Enum::toString).toList().contains(sOrdering.toUpperCase())) {
                ConsoleJS.CLIENT.error("Ordering " + sOrdering + " does not exist");
            }
            ordering = Ordering.valueOf(sOrdering.toUpperCase());
        }

        ResourceLocation otherOverlay = null;
        if (ordering != Ordering.AFTER_ALL && ordering != Ordering.BEFORE_ALL) {
            if (overlay.contains("other")) {
                otherOverlay = new ResourceLocation(overlay.getString("other"));
            } else {
                ConsoleJS.CLIENT.error("Missing a necessary key: other");
                return null;
            }
        }

        List<RenderObject> renderObjects = new ArrayList<>();
        if (overlay.contains("objects")) {
            for (Tag tag : overlay.getList("objects", 8)) {
                renderObjects.add(RenderObjectManager.rjs$get(tag.getAsString()));
            }
        }

        return new CustomOverlay(id, ordering, otherOverlay, renderObjects, callback);
    }

    public void addObjects(RenderObject... object) {
        this.objects.addAll(List.of(object));
    }

    public void removeObjects(RenderObject... object) {
        this.objects.removeAll(List.of(object));
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        PoseStack poseStack = graphics.pose();
        for (RenderObject object : objects) {
            object.rjs$setPoseStack(poseStack);
            object.rjs$render();
        }
        if (callback != null) callback.render(gui, graphics, partialTick, screenWidth, screenHeight);
        graphics.flush();
    }

    public interface RenderCallback {
        void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight);
    }

    public enum Ordering {
        BEFORE,
        AFTER,
        BEFORE_ALL,
        AFTER_ALL
    }
}
