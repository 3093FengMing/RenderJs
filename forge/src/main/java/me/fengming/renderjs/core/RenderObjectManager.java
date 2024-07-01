package me.fengming.renderjs.core;

import com.google.common.collect.Maps;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.objects.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
            RenderObject object = createObjectByTag(tag.getCompound(key));
            renderObjectMap.put(key, object);
            ConsoleJS.CLIENT.log("Succeed register render object: " + key + "(" + object.type + ")");
        }
    }

    public static RenderObject createObjectByTag(CompoundTag object) {
        if (!object.contains("type")) {
            throw new IllegalArgumentException("type is null");
        }
        String type = object.getString("type");

        if (!Arrays.stream(RenderObject.ObjectType.values()).map(Enum::toString).toList().contains(type.toUpperCase())) {
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

        RenderObject renderObject = Objects.requireNonNull(createObject(vertices, r, g, b, a, textureLocation, type));

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

        // Lines
        if (renderObject instanceof Lines lines && object.contains("line_width")) {
            lines.rjs$setLineWidth(object.getFloat("line_width"));
        }

        // Block
        if (renderObject instanceof BlocksDisplay blocksDisplay) {
            if (object.contains("block")) {
                blocksDisplay.rjs$setBlockState(Utils.parseBlock(object.getString("block"), true));
            }
            if (object.contains("world_light")) {
                blocksDisplay.rjs$setWorldLight(object.getInt("world_light"));
            }
            if (object.contains("block_light")) {
                blocksDisplay.rjs$setBlockLight(object.getInt("block_light"));
            }
            if (object.contains("render_type")) {
                blocksDisplay.rjs$setRenderType(Utils.getRenderTypeById(object.getString("render_type")));
            }
        }

        // Item
        if (renderObject instanceof ItemsDisplay itemsDisplay) {
            if (object.contains("item")) {
                itemsDisplay.rjs$setItem(Utils.parseItem(object.getString("item"), object.getInt("count")));
            }
            if (object.contains("context")) {
                itemsDisplay.rjs$setItemDisplayContext(ItemDisplayContext.valueOf(object.getString("context").toUpperCase()));
            }
            if (object.contains("left_hand")) {
                itemsDisplay.rjs$setLeftHand(object.getBoolean("left_hand"));
            }
            if (object.contains("light")) {
                itemsDisplay.rjs$setLight(object.getInt("light"));
            }
        }
        return renderObject;
    }

    public static RenderObject createObject(float[] vertices, float r, float g, float b, float a, String texLoc, String type) {
        RenderObject.ObjectType objectType = RenderObject.ObjectType.valueOf(type.toUpperCase());
        switch (objectType) {
            case LINES, LINE_STRIP -> {
                return new Lines(vertices, r, g, b, a, texLoc, objectType);
            }
            case TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN -> {
                return new Triangles(vertices, r, g, b, a, texLoc, objectType);
            }
            case QUADS, RECTANGLES -> {
                return new Quads(vertices, r, g, b, a, texLoc, objectType);
            }
            case BLOCKS -> {
                return new BlocksDisplay(vertices, r, g, b, a, texLoc, objectType);
            }
            case ITEMS -> {
                return new ItemsDisplay(vertices, r, g, b, a, texLoc, objectType);
            }
        }
        return null;
    }
}
