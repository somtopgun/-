package com.example.timershears.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;
import com.example.timershears.TimerManager;

@Mod.EventBusSubscriber(modid = com.example.timershears.TimerShearsMod.MODID, value = Dist.CLIENT)
public class ClientOverlay {

    private static final TimerManager timer = new TimerManager();
    private static boolean timerStarted = false;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        PoseStack pose = event.getPoseStack();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (!timerStarted) {
            timer.start();
            timerStarted = true;
        }

        timer.tick(mc);

        long remain = timer.getRemaining();
        if (remain < 0) remain = 0;

        long seconds = remain / 1000;
        long mins = seconds / 60;
        long secs = seconds % 60;
        String text = String.format("Timer: %d:%02d", mins, secs);

        int x = mc.getWindow().getGuiScaledWidth() - 10;
        int y = 10;

        int width = mc.font.width(text) + 6;
        int height = mc.font.lineHeight + 4;

        mc.gui.fill(pose, x - width, y, x, y + height, 0x80000000);
        mc.font.drawShadow(pose, text, x - width + 3, y + 2, 0xFFFFFF);
    }
}
