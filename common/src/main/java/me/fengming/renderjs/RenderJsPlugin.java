package me.fengming.renderjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import me.fengming.renderjs.events.RenderJsEvents;

public class RenderJsPlugin extends KubeJSPlugin {

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.allow("org.joml");
    }

    @Override
    public void registerEvents() {
        RenderJsEvents.GROUP_LEVEL.register();
        RenderJsEvents.GROUP_ENTITY.register();
    }

    @Override
    public void afterInit() {

    }
}
