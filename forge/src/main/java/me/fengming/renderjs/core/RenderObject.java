package me.fengming.renderjs.core;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Transformation;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.objects.draw.Lines;
import me.fengming.renderjs.core.objects.draw.Quads;
import me.fengming.renderjs.core.objects.draw.Triangles;
import me.fengming.renderjs.core.objects.vanilla.BlocksDisplay;
import me.fengming.renderjs.core.objects.vanilla.IconsDisplay;
import me.fengming.renderjs.core.objects.vanilla.ItemsDisplay;
import me.fengming.renderjs.core.objects.vanilla.ModelsDisplay;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Display;
import org.joml.Quaternionf;

import java.util.Arrays;

@RemapPrefixForJS("rjs$")
public abstract class RenderObject {
    public static Minecraft mc = Minecraft.getInstance();
    public static Camera camera = mc.gameRenderer.getMainCamera();

    protected boolean broken;

    protected PoseStack poseStack = null;
    protected ObjectType type;
    protected float[] vertices;
    protected boolean enableBlend = true;
    protected boolean enableDepthTest = true;
    protected boolean enableCull = false;
    protected Display.BillboardConstraints billboard = Display.BillboardConstraints.FIXED;
    protected Transformation transformation = new Transformation(null);

    protected final float[] innerOffsets = new float[300];
    protected int innerOffsetsLength = 0;
    protected final float[] offsets = new float[300];
    protected int offsetsLength = 0;
    protected final float[] scales = new float[300];
    protected int scalesLength = 0;
    protected float[] innerScale = new float[3];


    public RenderObject(ObjectType type) {
        this.type = type;
    }

    public abstract void loadInner(CompoundTag object);

    public void load(CompoundTag object) {
        if (object.contains("options")) {
            CompoundTag options = object.getCompound("options");
            if (options.contains("blend")) {
                this.enableBlend = options.getBoolean("blend");
            }
            if (options.contains("depth_test")) {
                this.enableDepthTest = options.getBoolean("depth_test");
            }
            if (options.contains("cull")) {
                this.enableCull = options.getBoolean("cull");
            }
            if (options.contains("billboard")) {
                this.billboard = Display.BillboardConstraints.valueOf(options.getString("billboard").toUpperCase());
            }
            if (options.contains("transformation")) {
                this.rjs$setTransformation(options.get("transformation"));
            }
        }

        loadInner(object);
    }

    public static RenderObject loadFromNbt(CompoundTag object) {
        if (!object.contains("type")) {
            ConsoleJS.CLIENT.error("Missing a necessary key: type");
            return null;
        }

        String type = object.getString("type");
        if (!Arrays.stream(RenderObject.ObjectType.values()).map(Enum::toString).toList().contains(type.toUpperCase())) {
            ConsoleJS.CLIENT.error("Type " + type + " does not exist");
            return null;
        }
        ObjectType objectType = ObjectType.valueOf(type.toUpperCase());

        float[] vertices;
        if (object.contains("vertices")) {
            ListTag verticesList = object.getList("vertices", 6);
            vertices = new float[verticesList.size()];
            for (int i = 0; i < verticesList.size(); i++) {
                vertices[i] = (float) verticesList.getDouble(i);
            }
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: vertices");
            return null;
        }

        float[] scale = new float[3];
        if (object.contains("scale")) {
            ListTag scaleList = object.getList("scale", 6);
            for (int i = 0; i < 3; i++) {
                scale[i] = (float) scaleList.getDouble(i);
            }
        }

        RenderObject renderObject = null;
        switch (objectType) {
            case LINES, LINE_STRIP -> renderObject = new Lines(objectType);
            case TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN -> renderObject = new Triangles(objectType);
            case QUADS, RECTANGLES -> renderObject = new Quads(objectType);
            case BLOCKS -> renderObject = new BlocksDisplay(objectType);
            case ITEMS -> renderObject = new ItemsDisplay(objectType);
            case ICONS -> renderObject = new IconsDisplay(objectType);
            case MODELS -> renderObject = new ModelsDisplay(objectType);
        }
        renderObject.load(object);

        renderObject.vertices = vertices;
        renderObject.innerScale = scale;
        return renderObject;
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
                .ifPresent(pair -> setTransformation(pair.getFirst()));
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
                    @Param(name = "i", value = "The specified offset index starts from 0. If multiple offsets are to be applied, the index value is increased sequentially. Max to 99."),
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

    @Info(value = """
            Scale vertices by given values.
            """,
            params = {
                    @Param(name = "i", value = "The specified scale index starts from 0. If multiple scales are to be applied, the index value is increased sequentially. Max to 99."),
                    @Param(name = "x", value = "Scale in the x-direction."),
                    @Param(name = "y", value = "Scale in the y-direction."),
                    @Param(name = "z", value = "Scale in the z-direction.")
            }
    )
    public void rjs$addScale(int i, float x, float y, float z) {
        i *= 3;
        scales[i] = x;
        scales[i + 1] = y;
        scales[i + 2] = z;
        scalesLength = i + 3;
    }


    public void addInnerOffsets(int i, float x, float y, float z) {
        i *= 3;
        innerOffsets[i] = x;
        innerOffsets[i + 1] = y;
        innerOffsets[i + 2] = z;
        innerOffsetsLength = i + 3;
    }

    @Info("""
            Get this the type of this object.
            """)
    public ObjectType rjs$getType() {
        return this.type;
    }

    protected abstract void renderInner();

    @Info("""
            Render this object. Before that, make sure PoseStack has been set.
            """)
    public void rjs$render() {
        if (broken || poseStack == null) return;

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

        // RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        poseStack.pushPose();
        for (int i = 0; i < innerOffsetsLength; i += 3) {
            poseStack.translate(innerOffsets[i], innerOffsets[i + 1], innerOffsets[i + 2]);
        }
        for (int i = 0; i < offsetsLength; i += 3) {
            poseStack.translate(offsets[i], offsets[i + 1], offsets[i + 2]);
        }

        poseStack.scale(innerScale[0], innerScale[1], innerScale[2]);
        for (int i = 0; i < scalesLength; i += 3) {
            poseStack.scale(scales[i], scales[i + 1], scales[i + 2]);
        }

        switch (billboard) {
            case HORIZONTAL -> poseStack.mulPose(new Quaternionf().rotationYXZ(0.0F, -0.017453292F * camera.getXRot(), 0.0F));
            case VERTICAL -> poseStack.mulPose(new Quaternionf().rotationYXZ((float)Math.PI - ((float)Math.PI / 180F) * camera.getYRot(), (float)Math.PI / 180F, 0.0F));
            case CENTER -> poseStack.mulPose(new Quaternionf().rotationYXZ((float)Math.PI - ((float)Math.PI / 180F) * camera.getYRot(), -0.017453292F * camera.getXRot(), 0.0F));
            default -> {}
        }

        poseStack.mulPoseMatrix(transformation.getMatrix());
        poseStack.last().normal().rotate(transformation.getLeftRotation()).rotate(transformation.getRightRotation());

        this.renderInner();
        poseStack.popPose();
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
        ICONS,
        MODELS;

        private final VertexFormat.Mode[] modes = {VertexFormat.Mode.LINES, VertexFormat.Mode.LINE_STRIP, VertexFormat.Mode.TRIANGLES, VertexFormat.Mode.TRIANGLE_STRIP, VertexFormat.Mode.TRIANGLE_FAN, VertexFormat.Mode.QUADS};

        public VertexFormat.Mode getMode() {
            return this.modes[this.ordinal()];
        }
    }
}

