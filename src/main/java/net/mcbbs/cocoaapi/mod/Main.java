package net.mcbbs.cocoaapi.mod;
import net.mcbbs.cocoaapi.mod.pluginmessage.PluginMessageManager;
import net.mcbbs.cocoaapi.mod.proxy.CommonProxy;
import net.mcbbs.cocoaapi.mod.resource.ResourcesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
	@Instance(Main.MODID)
	public static Main INSTANCE;
	public static final String MODID = "cocoaapi";
	public static final String NAME = "CocoaAPI";
	public static final String VERSION = "1.0";

	private static PluginMessageManager manager = new PluginMessageManager();
	private static ResourcesManager resourcesManager = new ResourcesManager();
	private static Logger logger;

	@SidedProxy(clientSide = "net.mcbbs.cocoaapi.mod.proxy.ClientProxy", serverSide = "net.mcbbs.cocoaapi.mod.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	public static PluginMessageManager getPluginMessageManager() {
		return manager;
	}

	public static ResourcesManager getResourcesManager() {
		return Main.resourcesManager;
	}

}
