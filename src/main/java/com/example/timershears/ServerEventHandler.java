package com.example.timershears;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;

public class ServerEventHandler {

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        if (server.isDedicatedServer()) return; // 멀티 전용 서버 무시

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            long end = player.getPersistentData().getLong("TimerShears_endTime");
            if (end == 0L) continue;
            long now = System.currentTimeMillis();
            if (now >= end) {
                player.getPersistentData().remove("TimerShears_endTime");
                try {
                    server.submit(() -> {
                        try {
                            server.save(() -> {});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new Thread(() -> {
                            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                            System.out.println("TimerShears: shutting down singleplayer world.");
                            System.exit(0);
                        }).start();
                    }).get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
