package me.fengming.renderjs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import me.fengming.renderjs.core.RenderObjectManager;
import me.fengming.renderjs.core.objects.gui.CustomOverlay;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;

@Info("""
        Invoked before the client enters the world and when the client is reloaded.
        """)
public class RegisterCustomEventJS extends EventJS {
    public static class Object extends EventJS {
        @Info("""
            Register new objects. Its function is equivalent to RenderObjectManager#register.
            """)
        public void register(CompoundTag objects) {
            RenderObjectManager.rjs$registerObject(objects);
        }
    }

    public static class Overlay extends EventJS {
        private final RegisterGuiOverlaysEvent event;

        public Overlay(RegisterGuiOverlaysEvent event) {
            this.event = event;
        }

        @Info("""
            Register new overlays. Reload Client will not modify the content of the overlays.
            """)
        public void register(String id, CompoundTag tag, CustomOverlay.RenderCallback callback) {
            RenderObjectManager.rjs$registerOverlay(id, tag, callback, overlay -> {
                if (overlay.ordering() == CustomOverlay.Ordering.BEFORE_ALL) {
                    this.event.registerBelowAll(overlay.id(), overlay);
                }
                if (overlay.ordering() == CustomOverlay.Ordering.AFTER_ALL) {
                    this.event.registerAboveAll(overlay.id(), overlay);
                }
                if (overlay.ordering() == CustomOverlay.Ordering.BEFORE) {
                    this.event.registerBelow(overlay.other(), overlay.id(), overlay);
                }
                if (overlay.ordering() == CustomOverlay.Ordering.AFTER) {
                    this.event.registerAbove(overlay.other(), overlay.id(), overlay);
                }
            });
        }
    }
}
