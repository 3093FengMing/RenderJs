package me.fengming.renderjs.core;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

@RemapPrefixForJS("rjs$")
public abstract class RenderObject {
    public static Minecraft mc = Minecraft.getInstance();
    public static Camera camera = mc.gameRenderer.getMainCamera();

    protected PoseStack poseStack = null;
    protected ObjectType type;
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

    private float[] offsets = new float[300];
    private int offsetsLength = 0;

    public RenderObject(float[] vertices, float r, float g, float b, float a, String texLoc, ObjectType type) {
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
        this.type = type;
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
            Transform vertex matrix.
            """)
    public void rjs$transform(float[] transformation) {
        // TODO
    }

    @Info("""
            Directly modify the value of a given vertex.
            """)
    public void rjs$modifyVertices(int index, float value) {
        this.vertices[index] = value;
    }

    @Info(value = """
            Offset vertices by given values.
            """,
            params = {
                    @Param(name = "i", value = "The specified offset index starts from 0. If multiple offsets are to be applied, the index value is increased sequentially. Max to 100."),
                    @Param(name = "x", value = "Offset in the x-direction."),
                    @Param(name = "y", value = "Offset in the y-direction."),
                    @Param(name = "z", value = "Offset in the z-direction.")
            }
    )
    public void rjs$offset(int i, float x, float y, float z) {
        i *= 3;
        offsets[i] = x;
        offsets[i + 1] = y;
        offsets[i + 2] = z;
        offsetsLength = i + 3;
    }

    @Info("""
            Offset vertices by given values.
            """)
    public void rjs$offset(float[] offset) {
        offsets = offset;
    }

    @Info("""
            Get this the type of this object.
            """)
    public ObjectType rjs$getType() {
        return this.type;
    }

    @Info("""
            Render this object.
            """)
    public abstract void rjs$render();

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
        } else {
            RenderSystem.disableCull();
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
        for (int i = 0; i < offsetsLength; i += 3) {
            poseStack.translate(offsets[i], offsets[i + 1], offsets[i + 2]);
        }
        if (enableFacingPlayer) {
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        }
    }

    public enum ObjectType {
        // OpenGL
        LINES,
        LINE_STRIP,
        TRIANGLES,
        TRIANGLE_STRIP,
        TRIANGLE_FAN,
        QUADS,
        RECTANGLES,

        // Minecraft
        BLOCKS,
        ITEMS,
        OVERLAYS,
        MODELS;

        private final VertexFormat.Mode[] modes = {VertexFormat.Mode.LINES, VertexFormat.Mode.LINE_STRIP, VertexFormat.Mode.TRIANGLES, VertexFormat.Mode.TRIANGLE_STRIP, VertexFormat.Mode.TRIANGLE_FAN, VertexFormat.Mode.QUADS};

        public VertexFormat.Mode getMode() {
            return this.modes[this.ordinal()];
        }
    }
}

