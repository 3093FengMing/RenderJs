package me.fengming.renderjs.core.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import org.joml.Matrix4f;

@RemapPrefixForJS("rjs$")
public class Lines extends Draw {
    protected float lineWidth;

    public Lines(float[] vertices, ObjectType type) {
        super(vertices, type);
    }

    public void rjs$setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void loadInner(CompoundTag object) {
        if (object.contains("line_width")) {
            this.rjs$setLineWidth(object.getFloat("line_width"));
        }
    }

    @Override
    public void renderInner() {
        Matrix4f matrix4f = poseStack.last().pose().normal();
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder builder = tesselator.getBuilder();

        RenderSystem.lineWidth(this.lineWidth);
        builder.begin(type.getMode(), texture ? DefaultVertexFormat.POSITION_COLOR_TEX : DefaultVertexFormat.POSITION_COLOR);
        if (!verticesColor) {
            if (texture) {
                for (int i = 0; i < vertices.length; i += 10) {
                    builder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).color(r, g, b, a).uv(vertices[i + 3], vertices[i + 4]).endVertex();
                    builder.vertex(matrix4f, vertices[i + 5], vertices[i + 6], vertices[i + 7]).color(r, g, b, a).uv(vertices[i + 8], vertices[i + 9]).endVertex();
                }
            } else {
                for (int i = 0; i < vertices.length; i += 6) {
                    builder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).color(r, g, b, a).endVertex();
                    builder.vertex(matrix4f, vertices[i + 3], vertices[i + 4], vertices[i + 5]).color(r, g, b, a).endVertex();
                }
            }
        } else {
            if (texture) {
                for (int i = 0; i < vertices.length; i += 16) {
                    builder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).color(vertices[i + 3], vertices[i + 4], vertices[i + 5], a).uv(vertices[i + 6], vertices[i + 7]).endVertex();
                    builder.vertex(matrix4f, vertices[i + 8], vertices[i + 9], vertices[i + 10]).color(vertices[i + 11], vertices[i + 12], vertices[i + 13], a).uv(vertices[i + 14], vertices[i + 15]).endVertex();
                }
            } else {
                for (int i = 0; i < vertices.length; i += 12) {
                    builder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).color(vertices[i + 3], vertices[i + 4], vertices[i + 5], a).endVertex();
                    builder.vertex(matrix4f, vertices[i + 6], vertices[i + 7], vertices[i + 8]).color(vertices[i + 9], vertices[i + 10], vertices[i + 11], a).endVertex();
                }
            }
        }

        tesselator.end();
    }
}
