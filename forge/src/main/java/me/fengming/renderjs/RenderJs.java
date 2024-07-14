package me.fengming.renderjs;

import me.fengming.renderjs.events.RenderEventJS;
import me.fengming.renderjs.events.RenderJsEvents;
import me.fengming.renderjs.events.level.RenderEntityEventJS;
import me.fengming.renderjs.events.level.RenderLevelEventJS;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(RenderJs.MOD_ID)
public class RenderJs {
    public static final String MOD_ID = "renderjs";

    public RenderJs() {

    }

    @Mod.EventBusSubscriber(modid = RenderJs.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onRenderEntityBefore(RenderLivingEvent.Pre<?, ?> e) {
            RenderJsEvents.BEFORE_RENDER_ENTITY.post(new RenderEntityEventJS.Before(e));
        }

        @SubscribeEvent
        public static void onRenderEntityAfter(RenderLivingEvent.Post<?, ?> e) {
            RenderJsEvents.AFTER_RENDER_ENTITY.post(new RenderEntityEventJS.After(e));
        }

        @SubscribeEvent
        public static void onRenderPlayerBefore(RenderPlayerEvent.Pre e) {
            RenderJsEvents.BEFORE_RENDER_PLAYER.post(new RenderEntityEventJS.Before(e));
        }

        @SubscribeEvent
        public static void onRenderPlayerAfter(RenderPlayerEvent.Post e) {
            RenderJsEvents.AFTER_RENDER_PLAYER.post(new RenderEntityEventJS.After(e));
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

    @Mod.EventBusSubscriber(modid = RenderJs.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onSetup(FMLClientSetupEvent event) {

        }
    }
}