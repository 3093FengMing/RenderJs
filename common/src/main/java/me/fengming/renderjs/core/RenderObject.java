package me.fengming.renderjs.core;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

@RemapPrefixForJS("rjs$")
public class RenderObject {
    protected PoseStack poseStack = null;
    protected RenderType type;
    protected float[] vertices;
    protected float r = 0.0F;
    protected float g = 0.0F;
    protected float b = 0.0F;
    protected float a = 1.0F;
    protected ResourceLocation textureLocation = null;
    protected boolean texture = false;
    protected boolean verticesColor = false;
    protected boolean enableBlend = true;
    protected boolean enableDepthTest = true;
    protected boolean enableCull = false;
    protected boolean enableFacingPlayer = false;

    private final float[] offsetXYZ = {0.0F, 0.0F, 0.0F};

    public RenderObject(float[] vertices, float r, float g, float b, float a, String texLoc, String type) {
        this.vertices = vertices;
        this.a = a;
        if (r == -1.0F || g == -1.0F || b == -1.0F) {
            this.verticesColor = true;
        } else {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        if (texLoc != null) {
            this.textureLocation = new ResourceLocation(texLoc);
            this.texture = true;
        } else {
            this.textureLocation = null;
        }
        this.type = RenderType.valueOf(type.toUpperCase());
    }

    public void rjs$setPoseStack(PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    @Info("""
            Directly modify vertices to new ones
            """)
    public void rjs$setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    @Info("""
            Directly modify the value of a given vertex.
            """)
    public void rjs$modifyVertices(int index, float value) {
        this.vertices[index] = value;
    }

    @Info("""
            Offset vertices by given values.
            """)
    public void rjs$offset(float x, float y, float z) {
        offsetXYZ[0] = x;
        offsetXYZ[1] = y;
        offsetXYZ[2] = z;
    }

    @Info("""
            Get this the type of this object.
            """)
    public RenderType rjs$getType() {
        return this.type;
    }

    @Info("""
            Render this object by triangles.
            """)
    public void rjs$renderTriangle() {
        prepare();

        Matrix4f matrix4f = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();

        builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        if (!verticesColor) {
            if (texture) {
                for (int i = 0; i < vertices.length; i+=15) {
                    builder.vertex(matrix4f, vertices[ i ], vertices[i+1], vertices[i+2]).color(r, g, b, a).uv(vertices[i+3], vertices[i+4]).endVertex();
                    builder.vertex(matrix4f, vertices[i+5], vertices[i+6], vertices[i+7]).color(r, g, b, a).uv(vertices[i+8], vertices[i+9]).endVertex();
                    builder.vertex(matrix4f, vertices[i+10], vertices[i+11], vertices[i+12]).color(r, g, b, a).uv(vertices[i+13], vertices[i+14]).endVertex();
                }
            } else {
                for (int i = 0; i < vertices.length; i+=9) {
                    builder.vertex(matrix4f, vertices[ i ], vertices[i+1], vertices[i+2]).color(r, g, b, a).endVertex();
                    builder.vertex(matrix4f, vertices[i+3], vertices[i+4], vertices[i+5]).color(r, g, b, a).endVertex();
                    builder.vertex(matrix4f, vertices[i+6], vertices[i+7], vertices[i+8]).color(r, g, b, a).endVertex();
                }
            }
        } else {
            if (texture) {
                for (int i = 0; i < vertices.length; i+=24) {
                    builder.vertex(matrix4f, vertices[ i ], vertices[i+1], vertices[i+2]).color(vertices[i+3], vertices[i+4], vertices[i+5], a).uv(vertices[i+6], vertices[i+7]).endVertex();
                    builder.vertex(matrix4f, vertices[i+8], vertices[i+9], vertices[i+10]).color(vertices[i+11], vertices[i+12], vertices[i+13], a).uv(vertices[i+14], vertices[i+15]).endVertex();
                    builder.vertex(matrix4f, vertices[i+16], vertices[i+17], vertices[i+18]).color(vertices[i+19], vertices[i+20], vertices[i+21], a).uv(vertices[i+22], vertices[i+23]).endVertex();
                }
            } else {
                for (int i = 0; i < vertices.length; i+=18) {
                    builder.vertex(matrix4f, vertices[ i ], vertices[i+1], vertices[i+2]).color(vertices[i+3], vertices[i+4], vertices[i+5], a).endVertex();
                    builder.vertex(matrix4f, vertices[i+6], vertices[i+7], vertices[i+8]).color(vertices[i+9], vertices[i+10], vertices[i+11], a).endVertex();
                    builder.vertex(matrix4f, vertices[i+12], vertices[i+13], vertices[i+14]).color(vertices[i+15], vertices[i+16], vertices[i+17], a).endVertex();
                }
            }
        }

        tesselator.end();
        poseStack.popPose();
    }

    @Info("""
            Render this object by type.
            """)
    public void rjs$render() {
        if (type == RenderType.TRIANGLES) {
            rjs$renderTriangle();
        }
    }

    public void prepare() {
        if (enableBlend) {
            RenderSystem.enableBlend();
        }
        if (enableDepthTest) {
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
        }
        if (enableCull) {
            RenderSystem.enableCull();
        }

        if (texture) {
            RenderSystem.setShaderTexture(99, textureLocation);
            RenderSystem.bindTexture(99);
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        } else {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
        }

        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        poseStack.pushPose();
        poseStack.translate(offsetXYZ[0], offsetXYZ[1], offsetXYZ[2]);
        if (enableFacingPlayer) {
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        }
    }

    enum RenderType {
        TRIANGLES
    }
}

