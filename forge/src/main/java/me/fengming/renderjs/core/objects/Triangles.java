package me.fengming.renderjs.core.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import org.joml.Matrix4f;

@RemapPrefixForJS("rjs$")
public class Triangles extends RenderObject {
    public Triangles(float[] vertices, float r, float g, float b, float a, String texLoc, ObjectType type) {
        super(vertices, r, g, b, a, texLoc, type);
    }

    @Override
    public void rjs$render() {
        prepare();

        Matrix4f matrix4f = poseStack.last().pose();
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder builder = tesselator.getBuilder();

        builder.begin(type.getMode(), texture ? DefaultVertexFormat.POSITION_COLOR_TEX : DefaultVertexFormat.POSITION_COLOR);
        if (!verticesColor) {
            if (texture) {
                for (int i = 0; i < vertices.length; i += 15) {
                    builder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).color(r, g, b, a).uv(vertices[i + 3], vertices[i + 4]).endVertex();
                    builder.vertex(matrix4f, vertices[i + 5], vertices[i + 6], vertices[i + 7]).color(r, g, b, a).uv(vertices[i + 8], vertices[i + 9]).endVertex();
                    builder.vertex(matrix4f, vertices[i + 10], vertices[i + 11], vertices[i + 12]).color(r, g, b, a).uv(vertices[i + 13], vertices[i + 14]).endVertex();
                }
            } else {
                for (int i = 0; i < vertices.length; i += 9) {
                    builder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).color(r, g, b, a).endVertex();
                    builder.vertex(matrix4f, vertices[i + 3], vertices[i + 4], vertices[i + 5]).color(r, g, b, a).endVertex();
                    builder.vertex(matrix4f, vertices[i + 6], vertices[i + 7], vertices[i + 8]).color(r, g, b, a).endVertex();
                }
            }
        } else {
            if (texture) {
                for (int i = 0; i < vertices.length; i += 24) {
                    builder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).color(vertices[i + 3], vertices[i + 4], vertices[i + 5], a).uv(vertices[i + 6], vertices[i + 7]).endVertex();
                    builder.vertex(matrix4f, vertices[i + 8], vertices[i + 9], vertices[i + 10]).color(vertices[i + 11], vertices[i + 12], vertices[i + 13], a).uv(vertices[i + 14], vertices[i + 15]).endVertex();
                    builder.vertex(matrix4f, vertices[i + 16], vertices[i + 17], vertices[i + 18]).color(vertices[i + 19], vertices[i + 20], vertices[i + 21], a).uv(vertices[i + 22], vertices[i + 23]).endVertex();
                }
            } else {
                for (int i = 0; i < vertices.length; i += 18) {
                    builder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).color(vertices[i + 3], vertices[i + 4], vertices[i + 5], a).endVertex();
                    builder.vertex(matrix4f, vertices[i + 6], vertices[i + 7], vertices[i + 8]).color(vertices[i + 9], vertices[i + 10], vertices[i + 11], a).endVertex();
                    builder.vertex(matrix4f, vertices[i + 12], vertices[i + 13], vertices[i + 14]).color(vertices[i + 15], vertices[i + 16], vertices[i + 17], a).endVertex();
                }
            }
        }

        tesselator.end();
        poseStack.popPose();
    }
}
