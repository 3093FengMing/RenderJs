package me.fengming.renderjs.core;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.objects.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@RemapPrefixForJS("rjs$")
public class RenderObjectManager {
    private static final Map<String, RenderObject> renderObjectMap = Maps.newHashMap();

    public static RenderObject rjs$get(String id) {
        return renderObjectMap.getOrDefault(id, null);
    }

    public static void rjs$remove(String id) {
        renderObjectMap.remove(id);
    }

    public static void rjs$register(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            RenderObject object = RenderObject.loadFromNbt(tag.getCompound(key));
            if (object == null) return;
            renderObjectMap.put(key, object);
            ConsoleJS.CLIENT.log("Succeed register render object: " + key + "(" + object.type + ")");
        }
    }
}
