package net.mcbbs.cocoaapi.mod.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import io.netty.buffer.ByteBuf;
import net.mcbbs.cocoaapi.mod.Main;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class Listener {
	public Listener() {
		FMLEventChannel c = NetworkRegistry.INSTANCE.newEventDrivenChannel("CocoaUI");
		c.register(this);
	}

	@SubscribeEvent
	public void onCustomReceive(ClientCustomPacketEvent event) {
		ByteBuf buf = event.getPacket().payload();
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		Main.getPluginMessageManager().receiveData(bytes);
	}
}