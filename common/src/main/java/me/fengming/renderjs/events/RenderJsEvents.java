package me.fengming.renderjs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import me.fengming.renderjs.events.level.RenderLevelEventJS;
import me.fengming.renderjs.events.level.RenderEntityEventJS;

public interface RenderJsEvents {
    EventGroup GROUP_LEVEL = EventGroup.of("RenderLevelEvents");
    EventGroup GROUP_ENTITY = EventGroup.of("RenderEntityEvents");

    EventHandler BEFORE_RENDER_SOLID_BLOCK = RenderJsEvents.GROUP_LEVEL.client("beforeSolidBlockRender", () -> RenderLevelEventJS.Before.class);
    EventHandler AFTER_RENDER_SOLID_BLOCK = RenderJsEvents.GROUP_LEVEL.client("afterSolidBlockRender", () -> RenderLevelEventJS.After.class);
    EventHandler BEFORE_RENDER_ENTITY = RenderJsEvents.GROUP_ENTITY.client("beforeRender", () -> RenderEntityEventJS.Before.class);
    EventHandler AFTER_RENDER_ENTITY = RenderJsEvents.GROUP_ENTITY.client("afterRender", () -> RenderEntityEventJS.After.class);
//    EventHandler SPECIAL_SPOUT = RenderJsEvents.GROUP.client("spoutHandler", () -> SpecialSpoutHandlerEvent.class);
//    EventHandler BOILER_HEATER = RenderJsEvents.GROUP.client("boilerHeatHandler", () -> BoilerHeaterHandlerEvent.class);
}
