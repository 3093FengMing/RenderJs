package me.fengming.renderjs.core;

import com.google.common.collect.Maps;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.Arrays;
import java.util.Map;

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
            CompoundTag object = tag.getCompound(key);
            if (!object.contains("type")) {
                throw new IllegalArgumentException("type is null");
            }
            String type = object.getString("type");

            if (!Arrays.stream(RenderObject.RenderType.values()).map(Enum::toString).toList().contains(type.toUpperCase())) {
                throw new IllegalArgumentException("Type " + type + " does not exist");
            }

            String textureLocation = null;
            float[] vertices;
            float r = -1.0F, g = -1.0F, b = -1.0F, a;
            if (object.contains("vertices")) {
                ListTag verticesList = object.getList("vertices", 6);
                vertices = new float[verticesList.size()];
                for (int i = 0; i < verticesList.size(); i++) {
                    vertices[i] = (float) verticesList.getDouble(i);
                }
            } else {
                throw new IllegalArgumentException("vertices is null");
            }

            if (object.contains("r")) r = object.getFloat("r");
            if (object.contains("g")) g = object.getFloat("g");
            if (object.contains("b")) b = object.getFloat("b");
            if (object.contains("a")) {
                a = object.getFloat("a");
            } else {
                throw new IllegalArgumentException("a(alpha) is null");
            }

            if (object.contains("texture_location")) textureLocation = object.getString("texture_location");

            RenderObject renderObject = new RenderObject(vertices, r, g, b, a, textureLocation, type);

            if (object.contains("options")) {
                CompoundTag options = object.getCompound("options");
                if (options.contains("blend")) {
                    renderObject.enableBlend = options.getBoolean("blend");
                }
                if (options.contains("depth_test")) {
                    renderObject.enableDepthTest = options.getBoolean("depth_test");
                }
                if (options.contains("cull")) {
                    renderObject.enableCull = options.getBoolean("cull");
                }
                if (options.contains("facing_player")) {
                    renderObject.enableFacingPlayer = options.getBoolean("facing_player");
                }
            }

            renderObjectMap.put(key, renderObject);
            ConsoleJS.CLIENT.log("Succeed register render object: " + key + "(" + type + ")");
        }
    }
}
