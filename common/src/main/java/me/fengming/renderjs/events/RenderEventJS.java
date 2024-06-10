package me.fengming.renderjs.events;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;

@Info("""
        Base render event.
        """)
public abstract class RenderEventJS extends EventJS {
    public abstract PoseStack getPoseStack();
}
