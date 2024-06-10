package me.fengming.renderjs.forge;

import me.fengming.renderjs.RenderJs;
import me.fengming.renderjs.events.RenderJsEvents;
import me.fengming.renderjs.events.level.RenderLevelEventJS;
import me.fengming.renderjs.events.level.RenderEntityEventJS;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(RenderJs.MOD_ID)
public class RenderJsForge {
    public RenderJsForge() {
        RenderJs.init();
    }
    @Mod.EventBusSubscriber(modid = RenderJs.MOD_ID)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onRenderEntityBefore(RenderLivingEvent.Pre<?, ?> e) {
            RenderJsEvents.BEFORE_RENDER_ENTITY.post(new RenderEntityEventJS.Before(e.getEntity(), e.getRenderer(), e.getPartialTick(), e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight()));
        }
        @SubscribeEvent
        public static void onRenderEntityAfter(RenderLivingEvent.Post<?, ?> e) {
            RenderJsEvents.AFTER_RENDER_ENTITY.post(new RenderEntityEventJS.After(e.getEntity(), e.getRenderer(), e.getPartialTick(), e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight()));
        }

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent e) {
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
                RenderJsEvents.AFTER_RENDER_SOLID_BLOCK.post(new RenderLevelEventJS.After(e.getLevelRenderer(), e.getPoseStack(), e.getProjectionMatrix(), e.getRenderTick(), e.getPartialTick(), e.getCamera(), e.getFrustum()));
            }
        }
    }
}