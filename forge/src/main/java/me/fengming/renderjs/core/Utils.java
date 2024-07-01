package me.fengming.renderjs.core;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.multiplayer.ClientRegistryLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils {
    private static final Map<String, RenderType> renderTypeMap = new HashMap<>();

    static {
        try {
            for (Field field : RenderType.class.getFields()) {
                if (field.getType().getName().equals("net.minecraft.client.renderer.RenderType")) {
                    renderTypeMap.put(field.getName().toLowerCase(), (RenderType) field.get(null));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error getting render type: ", e);
        }
        // renderTypeMap.put("block_layer_top", CustomRenderType.BLOCK_LAYER_TOP);
    }

    public static BlockState parseBlock(String s, boolean allowNbt) {
        try {
            return BlockStateParser.parseForBlock(ClientRegistryLayer.createRegistryAccess().compositeAccess().lookupOrThrow(Registries.BLOCK), s, allowNbt).blockState();
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("Error parsing a block: '" + s + "': ", e);
        }
    }

    public static ItemStack parseItem(String s, int count) {
        try {
            ItemParser.ItemResult result = ItemParser.parseForItem(ClientRegistryLayer.createRegistryAccess().compositeAccess().lookupOrThrow(Registries.ITEM), new StringReader(s));
            ItemStack item = new ItemStack(result.item());
            item.setCount(count == 0 ? 1 : count);
            item.setTag(result.nbt());
            return item;
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("Error parsing a block: '" + s + "': ", e);
        }
    }

    public static RenderType getRenderTypeById(String id) {
        return Objects.requireNonNull(renderTypeMap.getOrDefault(id, null), "Error getting render type: '" + id + "'");
    }
}
