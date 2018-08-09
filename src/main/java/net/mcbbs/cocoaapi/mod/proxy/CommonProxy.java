package net.mcbbs.cocoaapi.mod.proxy;

import net.mcbbs.cocoaapi.mod.listener.ForgeListener;
import net.mcbbs.cocoaapi.mod.listener.PluginMessageListener;
import net.mcbbs.cocoaapi.mod.pluginmessage.Listener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {

	}

	public void init(FMLInitializationEvent event) {
		new ForgeListener();
		new Listener();
		new PluginMessageListener();
	}

	public void postInit(FMLPostInitializationEvent event) {

	}
}
