package net.mcbbs.cocoaapi.mod.pluginmessage;

import io.netty.buffer.ByteBuf;
import net.mcbbs.cocoaapi.mod.Main;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class Listener {
	public Listener() {
		FMLEventChannel c = NetworkRegistry.INSTANCE.newEventDrivenChannel("CocoaUI");
		c.register(this);
	}

	@SubscribeEvent
	public void onCustomReceive(ClientCustomPacketEvent event) {
		ByteBuf buf = event.packet.payload();
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		Main.getPluginMessageManager().receiveData(bytes);
	}
}