package me.fengming.renderjs.events;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.core.RenderObject;
import me.fengming.renderjs.core.RenderObjectManager;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Info("""
        Base render event.
        """)
public abstract class RenderEventJS extends EventJS {
    public abstract PoseStack getPoseStack();

    @Info("""
        Render a object.
        """)
    public void render(String id) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.rjs$render();
    }

    @Info("""
        Render the given object and attach the given offset.
        Equals to RenderObject#offset.
        """)
    public void renderWithOffset(String id, float[] offset) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.rjs$offset(offset[0], offset[1], offset[2]);
        object.rjs$render();
    }

    @Info("""
        Renders the given object and modifies the given vertex.
        Equals to RenderObject#modifyVertices.
        """)
    public void renderWithModifier(String id, int index, float value) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.rjs$modifyVertices(index, value);
        object.rjs$render();
    }

    @Info("""
        Renders the given object with given vertices.
        Equals to RenderObject#setVertices.
        """)
    public void renderWithVertices(String id, float[] vertices) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        object.rjs$setVertices(vertices);
        object.rjs$render();
    }

    @Info("""
        Getting RenderObject from RenderObjectManager, equals to RenderObjectManager#get.
        Please use RenderEventJS#render as much as possible instead of this method.
        """)
    public RenderObject getObject(String id) {
        RenderObject object = RenderObjectManager.rjs$get(id);
        object.rjs$setPoseStack(this.getPoseStack());
        return object;
    }
}
