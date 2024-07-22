package me.fengming.renderjs.core;

import com.google.common.collect.Maps;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.objects.gui.CustomOverlay;
import net.minecraft.nbt.CompoundTag;

import java.util.Map;
import java.util.function.Consumer;

@RemapPrefixForJS("rjs$")
public class RenderObjectManager {
    private static final Map<String, RenderObject> renderObjectMap = Maps.newHashMap();
    private static final Map<String, CustomOverlay> overlayMap = Maps.newHashMap();

    public static RenderObject rjs$get(String id) {
        return renderObjectMap.getOrDefault(id, null);
    }
    public static CustomOverlay rjs$getOverlay(String id) {
        return overlayMap.getOrDefault(id, null);
    }

    public static void rjs$remove(String id) {
        renderObjectMap.remove(id);
    }
    public static void rjs$removeOverlay(String id) {
        overlayMap.remove(id);
    }

    @Info("""
            Register new objects.
            Do not call this method in non RegisterCustomEvent.
            """)
    public static void rjs$registerObject(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            RenderObject object = RenderObject.loadFromNbt(tag.getCompound(key));
            if (object == null) return;
            renderObjectMap.put(key, object);
            ConsoleJS.CLIENT.log("Succeed register a render object: " + key + "(" + object.type + ")");
        }
    }

    @Info("""
            Register new overlays. Reload Client will not modify the content of the overlays.
            Do not call this method in non RegisterCustomEvent.
            """)
    public static void rjs$registerOverlay(String id, CompoundTag tag, CustomOverlay.RenderCallback callback, Consumer<CustomOverlay> overlayConsumer) {
        CustomOverlay overlay = CustomOverlay.loadFromNbt(id, tag, callback);
        if (overlay == null) return;
        overlayMap.put(id, overlay);
        overlayConsumer.accept(overlay);
        ConsoleJS.CLIENT.log("Succeed register a overlay: " + id);
    }
}
