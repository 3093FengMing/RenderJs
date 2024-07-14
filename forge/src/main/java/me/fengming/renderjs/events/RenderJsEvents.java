package me.fengming.renderjs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import me.fengming.renderjs.events.level.RenderEntityEventJS;
import me.fengming.renderjs.events.level.RenderLevelEventJS;

public interface RenderJsEvents {
    EventGroup GROUP_LEVEL = EventGroup.of("RenderLevelEvents");
    EventGroup GROUP_ENTITY = EventGroup.of("RenderEntityEvents");
    EventGroup GROUP_PLAYER = EventGroup.of("RenderPlayerEvents");

    EventHandler AFTER_RENDER_TERRAIN = RenderJsEvents.GROUP_LEVEL.client("afterTerrain", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_LEVEL = RenderJsEvents.GROUP_LEVEL.client("afterLevel", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_ENTITIES = RenderJsEvents.GROUP_LEVEL.client("afterEntities", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_BLOCK_ENTITIES = RenderJsEvents.GROUP_LEVEL.client("afterBlockEntities", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_SKY = RenderJsEvents.GROUP_LEVEL.client("afterSky", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_WEATHER = RenderJsEvents.GROUP_LEVEL.client("afterWeather", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_PARTICLES = RenderJsEvents.GROUP_LEVEL.client("afterParticles", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_CUTOUT = RenderJsEvents.GROUP_LEVEL.client("afterCutout", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_TRANSLUCENT = RenderJsEvents.GROUP_LEVEL.client("afterTranslucent", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_TRIPWIRES = RenderJsEvents.GROUP_LEVEL.client("afterTripwires", () -> RenderLevelEventJS.class);
    EventHandler AFTER_RENDER_CUTOUT_MIPPED = RenderJsEvents.GROUP_LEVEL.client("afterCutoutMipped", () -> RenderLevelEventJS.class);

    EventHandler BEFORE_RENDER_ENTITY = RenderJsEvents.GROUP_ENTITY.client("beforeRender", () -> RenderEntityEventJS.Before.class);
    EventHandler AFTER_RENDER_ENTITY = RenderJsEvents.GROUP_ENTITY.client("afterRender", () -> RenderEntityEventJS.After.class);

    EventHandler BEFORE_RENDER_PLAYER = RenderJsEvents.GROUP_PLAYER.client("beforeRender", () -> RenderEntityEventJS.Before.class);
    EventHandler AFTER_RENDER_PLAYER = RenderJsEvents.GROUP_PLAYER.client("afterRender", () -> RenderEntityEventJS.After.class);
}
