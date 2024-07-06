package me.fengming.renderjs.core;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Transformation;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display;
import org.joml.Quaternionf;

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
    protected Display.BillboardConstraints billboard = Display.BillboardConstraints.FIXED;
    protected Transformation transformation;

    private float[] innerOffsets = new float[9];
    private int innerOffsetsLength = 0;
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

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    @Info("""
            Transform vertex matrix.
            """)
    public void rjs$setTransformation(Tag tag) {
        Transformation.EXTENDED_CODEC
                .decode(NbtOps.INSTANCE, tag)
                .result()
                .ifPresent(pair -> this.transformation = pair.getFirst());
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
    public void rjs$addOffset(int i, float x, float y, float z) {
        i *= 3;
        offsets[i] = x;
        offsets[i + 1] = y;
        offsets[i + 2] = z;
        offsetsLength = i + 3;
    }

    public void addInnerOffsets(float x, float y, float z) {
        innerOffsets[innerOffsetsLength] = x;
        innerOffsets[innerOffsetsLength + 1] = y;
        innerOffsets[innerOffsetsLength + 2] = z;
        innerOffsetsLength += 3;
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
        for (int i = 0; i < innerOffsetsLength; i += 3) {
            poseStack.translate(innerOffsets[i], innerOffsets[i + 1], innerOffsets[i + 2]);
        }

        switch (billboard) {
            case HORIZONTAL -> poseStack.mulPose(new Quaternionf().rotationYXZ(0.0F, -0.017453292F * camera.getXRot(), 0.0F));
            case VERTICAL -> poseStack.mulPose(new Quaternionf().rotationYXZ((float)Math.PI - ((float)Math.PI / 180F) * camera.getYRot(), (float)Math.PI / 180F, 0.0F));
            case CENTER -> poseStack.mulPose(new Quaternionf().rotationYXZ((float)Math.PI - ((float)Math.PI / 180F) * camera.getYRot(), -0.017453292F * camera.getXRot(), 0.0F));
            default -> {}
        }

        poseStack.mulPoseMatrix(transformation.getMatrix());
        poseStack.last().normal().rotate(transformation.getLeftRotation()).rotate(transformation.getRightRotation());
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

