package com.example.timershears;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.example.timershears.client.ClientOverlay;

@Mod(TimerShearsMod.MODID)
public class TimerShearsMod {
    public static final String MODID = "timershears";

    public TimerShearsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(bus);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new ClientOverlay());
    }
}
