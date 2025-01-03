package anticope.rejects.mixin;

import anticope.rejects.modules.Rendering;
import com.mojang.blaze3d.systems.RenderSystem;
import meteordevelopment.meteorclient.systems.modules.Modules;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", ordinal = 0))
    private void renderShader(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        Rendering renderingModule = Modules.get().get(Rendering.class);
        if (renderingModule == null) return;

        PostEffectProcessor shader = renderingModule.getShaderEffect();

        if (shader != null) {
            int framebufferWidth = client.getWindow().getFramebufferWidth();
            int framebufferHeight = client.getWindow().getFramebufferHeight();
            FrameGraphBuilder frameGraphBuilder = new FrameGraphBuilder();
            PostEffectProcessor.FramebufferSet framebufferSet = new DefaultFramebufferSet();
            shader.render(frameGraphBuilder, framebufferWidth, framebufferHeight, framebufferSet);
        }
    }
}
