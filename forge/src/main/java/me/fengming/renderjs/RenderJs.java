package me.fengming.renderjs;

import dev.architectury.event.events.client.ClientPlayerEvent;
import me.fengming.renderjs.events.RegisterCustomEventJS;
import me.fengming.renderjs.events.RenderJsEvents;
import me.fengming.renderjs.events.entity.RenderEntityEventJS;
import me.fengming.renderjs.events.entity.RenderPlayerEventJS;
import me.fengming.renderjs.events.gui.RenderHudEventJS;
import me.fengming.renderjs.events.gui.RenderTooltipEventJS;
import me.fengming.renderjs.events.level.RenderLevelEventJS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(RenderJs.MOD_ID)
public class RenderJs {
    public static final String MOD_ID = "renderjs";

    public RenderJs() {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent e) {
            // It should be called before overlay is registered
            // But I could not find a good place for it to do that
            // So it is simply and rudely placed here
            RenderJsEvents.REGISTER_OBJECT.post(new RegisterCustomEventJS.Object());

            RenderJsEvents.REGISTER_OVERLAY.post(new RegisterCustomEventJS.Overlay(e));
        }
    }

    @Mod.EventBusSubscriber(modid = RenderJs.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onRenderHudAfter(RenderGuiEvent e) {
            if (e instanceof RenderGuiEvent.Post) {
                RenderJsEvents.AFTER_RENDER_HUD.post(new RenderHudEventJS(e));
            } else if (!RenderJsEvents.BEFORE_RENDER_HUD.post(new RenderHudEventJS(e)).pass()) {
                e.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents e) {
            if (!RenderJsEvents.GATHER_TOOLTIP_COMPONENTS.post(new RenderTooltipEventJS.GatherComponents(e)).pass()) {
                e.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onRenderTooltip(RenderTooltipEvent e) {
            if (e instanceof RenderTooltipEvent.Pre ie) {
                if (!RenderJsEvents.BEFORE_RENDER_TOOLTIP.post(new RenderTooltipEventJS.Before(ie)).pass()) {
                    e.setCanceled(true);
                }
            } else if (e instanceof RenderTooltipEvent.Color ie) {
                RenderJsEvents.RENDER_TOOLTIP_COLOR.post(new RenderTooltipEventJS.Color(ie));
            }
        }

        @SubscribeEvent
        public static void onRenderEntity(RenderLivingEvent e) {
            if (e instanceof RenderLivingEvent.Post) {
                RenderJsEvents.AFTER_RENDER_ENTITY.post(new RenderEntityEventJS(e));
            } else if (!RenderJsEvents.BEFORE_RENDER_ENTITY.post(new RenderEntityEventJS(e)).pass()) {
                e.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onRenderPlayer(RenderPlayerEvent e) {
            if (e instanceof RenderPlayerEvent.Post) {
                RenderJsEvents.AFTER_RENDER_PLAYER.post(new RenderPlayerEventJS(e));
            } else if (!RenderJsEvents.BEFORE_RENDER_PLAYER.post(new RenderPlayerEventJS(e)).pass()) {
                e.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent e) {
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
                RenderJsEvents.AFTER_RENDER_TERRAIN.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                RenderJsEvents.AFTER_RENDER_LEVEL.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
                RenderJsEvents.AFTER_RENDER_ENTITIES.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
                RenderJsEvents.AFTER_RENDER_BLOCK_ENTITIES.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
                RenderJsEvents.AFTER_RENDER_SKY.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
                RenderJsEvents.AFTER_RENDER_WEATHER.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                RenderJsEvents.AFTER_RENDER_PARTICLES.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
                RenderJsEvents.AFTER_RENDER_CUTOUT.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
                RenderJsEvents.AFTER_RENDER_TRANSLUCENT.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
                RenderJsEvents.AFTER_RENDER_TRIPWIRES.post(new RenderLevelEventJS(e));
            }
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS) {
                RenderJsEvents.AFTER_RENDER_CUTOUT_MIPPED.post(new RenderLevelEventJS(e));
            }
        }
    }
}