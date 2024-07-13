package me.fengming.renderjs.events;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import me.fengming.renderjs.core.RenderObject;
import me.fengming.renderjs.core.RenderObjectManager;
import net.minecraft.client.Camera;
import net.minecraft.nbt.CompoundTag;

@Info("""
        Base render event.
        """)
public abstract class RenderEventJS extends EventJS {

    public abstract PoseStack getPoseStack();

    public Camera getMainCamera() {
        return RenderObject.camera;
    }

    @Info(value = """
            Render a object by id.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered.")
            }
    )
    public void render(String id) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.rjs$render();
    }

    @Info(value = """
            Render a object.
            """,
            params = {
                    @Param(name = "object", value = "Create a temporary object and render it.")
            }
    )
    public void renderByObject(CompoundTag object) {
        RenderObject renderObject = RenderObject.loadFromNbt(object);
        renderObject.rjs$setPoseStack(this.getPoseStack());
        renderObject.rjs$render();
    }

    @Info(value = """
            Apply offset to the given objects and then render it.
            Equals to RenderObject#offset.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered."),
                    @Param(name = "offset", value = "Given the amount to be offset, in the order xyz.")
            }
    )
    public void renderWithOffset(String id, float[] offset) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.addInnerOffsets(0, offset[0], offset[1], offset[2]);
        object.rjs$render();
    }

    @Info(value = """
            Modify the given vertex and then render the given object.
            Equals to RenderObject#modifyVertices.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered."),
                    @Param(name = "index", value = "The index of the vertex to be modified."),
                    @Param(name = "value", value = "The value of the vertex to be modified.")
            }
    )
    public void renderWithModification(String id, int index, float value) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.rjs$modifyVertices(index, value);
        object.rjs$render();
    }

    @Info(value = """
            Modify the given vertex and then render the given object.
            Equals to RenderObject#transform.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered."),
                    @Param(name = "index", value = "The index of the vertex to be modified."),
                    @Param(name = "value", value = "The value of the vertex to be modified.")
            }
    )
    public void renderWithTransform(String id, CompoundTag transformation) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.rjs$setTransformation(transformation);
        object.rjs$render();
    }

    @Info(value = """
            Renders the given object with new vertices.
            Equals to RenderObject#setVertices.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered."),
                    @Param(name = "vertices", value = "The new vertices.")
            }
    )
    public void renderWithVertices(String id, float[] vertices) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.rjs$setVertices(vertices);
        object.rjs$render();
    }

    @Info(value = """
            Renders the given object in world.
            Actually it is converting vertices to world position.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered.")
            }
    )
    public void renderInWorld(String id) {
        renderInWorldCameraXYZ(id, getMainCamera(), 0.0F, 0.0F, 0.0F);
    }

    @Info(value = """
            Renders the given object in world.
            Actually it is converting vertices to world position.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered."),
                    @Param(name = "camera", value = "Convert world position based on this camera.")
            }
    )
    public void renderInWorldCamera(String id, Camera camera) {
        this.renderInWorldCameraXYZ(id, camera, 0.0F, 0.0F, 0.0F);
    }

    @Info(value = """
            Renders the given object in world.
            Actually it is converting vertices to world position.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered."),
                    @Param(name = "camera", value = "Convert world position based on this camera."),
                    @Param(name = "x", value = "Based on the offset in the x-direction of the origin."),
                    @Param(name = "y", value = "Based on the offset in the y-direction of the origin."),
                    @Param(name = "z", value = "Based on the offset in the z-direction of the origin.")
            }
    )
    public void renderInWorldCameraXYZ(String id, Camera camera, float x, float y, float z) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.addInnerOffsets(0, (float) (x - camera.getPosition().x), (float) (y - camera.getPosition().y), (float) (z - camera.getPosition().z));
        object.rjs$render();
    }

    @Info(value = """
            Getting RenderObject from RenderObjectManager, equals to RenderObjectManager#get.
            If you only want to render a object, use RenderEventJS#render.
            """,
            params = {
                    @Param(name = "id", value = "The identity of the object to be rendered. Be sure that the object has been registered.")
            }
    )
    public RenderObject getObject(String id) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        return object;
    }
}
