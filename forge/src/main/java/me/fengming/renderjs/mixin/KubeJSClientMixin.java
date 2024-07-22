package me.fengming.renderjs.mixin;

import dev.latvian.mods.kubejs.client.KubeJSClient;
import me.fengming.renderjs.events.RegisterCustomEventJS;
import me.fengming.renderjs.events.RenderJsEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = KubeJSClient.class, remap = false)
public class KubeJSClientMixin {
    @Inject(method = "reloadClientInternal", at = @At("HEAD"))
    private void injected_Head_reloadClientInternal(CallbackInfo callbackInfo) {
        RenderJsEvents.REGISTER_OBJECT.post(new RegisterCustomEventJS.Object());
    }
}
