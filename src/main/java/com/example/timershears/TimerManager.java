package com.example.timershears;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;

public class TimerManager {
    private long duration = 150_000L;
    private long startTime;
    private boolean paused = false;
    private long pauseStart;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void tick(Minecraft mc) {
        if (mc.player == null) return;

        boolean holdingBookAndQuill = mc.player.getMainHandItem().getItem() == Items.WRITABLE_BOOK;
        boolean windowFocused = mc.isWindowActive();

        if (holdingBookAndQuill) {
            if (!paused) {
                paused = true;
                pauseStart = System.currentTimeMillis();
            }
        } else {
            if (paused) {
                paused = false;
                long pauseTime = System.currentTimeMillis() - pauseStart;
                startTime += pauseTime;
            }
        }

        if (!paused && windowFocused) {
            long elapsed = System.currentTimeMillis() - startTime;
            long remaining = duration - elapsed;
            if (remaining <= 0) {
                mc.submit(() -> {
                    mc.level.save(() -> {});
                    new Thread(() -> {
                        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                        System.out.println("TimerShears: shutting down singleplayer world.");
                        System.exit(0);
                    }).start();
                });
            }
        }
    }

    public long getRemaining() {
        if (paused) return duration - (pauseStart - startTime);
        return duration - (System.currentTimeMillis() - startTime);
    }
}
