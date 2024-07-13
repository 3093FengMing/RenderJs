package me.fengming.renderjs.core.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

@RemapPrefixForJS("rjs$")
public class IconsDisplay extends RenderObject {
    protected ResourceLocation location;
    protected float textureWidth;
    protected float textureHeight;
    protected float width;
    protected float height;
    protected float u = 0;
    protected float v = 0;

    public IconsDisplay(float[] vertices, ObjectType type) {
        super(vertices, type);
    }

    public void rjs$setLocation(ResourceLocation location) {
        this.location = location;
    }

    public void rjs$setTextureWidth(float textureWidth) {
        this.textureWidth = textureWidth;
    }

    public void rjs$setTextureHeight(float textureHeight) {
        this.textureHeight = textureHeight;
    }

    public void rjs$setWidth(float width) {
        this.width = width;
    }

    public void rjs$setHeight(float height) {
        this.height = height;
    }

    public void rjs$setU(float u) {
        this.u = u;
    }

    public void rjs$setV(float v) {
        this.v = v;
    }

    @Override
    public void loadInner(CompoundTag object) {
        if (object.contains("icon")) {
            this.rjs$setLocation(new ResourceLocation(object.getString("icon")));
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: icon");
            broken = true;
        }
        if (object.contains("texture_width")) {
            this.rjs$setTextureWidth(object.getFloat("texture_width"));
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: texture_width");
            broken = true;
        }
        if (object.contains("texture_height")) {
            this.rjs$setTextureHeight(object.getFloat("texture_height"));
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: texture_height");
            broken = true;
        }
        if (object.contains("width")) {
            this.rjs$setWidth(object.getFloat("width"));
        } else {
            this.rjs$setWidth(textureWidth);
        }
        if (object.contains("height")) {
            this.rjs$setHeight(object.getFloat("height"));
        } else {
            this.rjs$setHeight(textureHeight);
        }
        if (object.contains("u")) {
            this.rjs$setU(object.getFloat("u"));
        }
        if (object.contains("v")) {
            this.rjs$setV(object.getFloat("v"));
        }
    }

    @Override
    public void rjs$render() {
        RenderSystem.setShaderTexture(0, location);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        super.rjs$render();
    }

    @Override
    public void renderInner() {
        float widthProportion = 1.0F / textureWidth;
        float heightProportion = 1.0F / textureHeight;
        Matrix4f matrix4f = poseStack.last().pose();
        for (int i = 0; i < vertices.length; i += 3) {
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f, vertices[i], vertices[i + 1] + height, vertices[i + 2]).uv(u * widthProportion, (v + height) * heightProportion).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f, (vertices[i] + width), (vertices[i + 1] + height), vertices[i + 2]).uv((u + width) * widthProportion, (v + height) * heightProportion).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f, (vertices[i] + width), vertices[i + 1], vertices[i + 2]).uv((u + width) * widthProportion, v * heightProportion).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f, vertices[i], vertices[i + 1], vertices[i + 2]).uv(u * widthProportion, v * heightProportion).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            BufferUploader.draw(bufferbuilder.end());
        }
    }
}
