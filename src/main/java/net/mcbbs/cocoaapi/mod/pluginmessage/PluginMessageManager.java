package net.mcbbs.cocoaapi.mod.pluginmessage;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeSet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import io.netty.buffer.Unpooled;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageListener;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageReceiveEvent;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageSendEvent;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InOpenResourceChooser;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InResourceUpdate;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InPictureResourceSent;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InSingleResourceUpdate;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InVerfiyPackage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class PluginMessageManager {
	private Map<Integer, Class<? extends AbstractInPackage>> classes = Maps.newHashMap();
	private TreeSet<PackageListener> listeners = Sets.newTreeSet();

	public PluginMessageManager() {
		this.init();
	}

	public void receiveData(byte[] data) {
		ByteArrayDataInput in = ByteStreams.newDataInput(data);
		int i = in.readInt();
		AbstractInPackage pack = this.getInstance(i, data);
		if (pack == null) {
			return;
		}
		this.dealInPackage(pack);
	}

	public void sendPackage(AbstractOutPackage out) {
		PackageSendEvent e = new PackageSendEvent(out);
		for (PackageListener listener : this.listeners) {
			listener.onPackageSend(e);
		}
		if (e.isCancelled()) {
			return;
		}	
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.copiedBuffer(out.getBytes()));
		CPacketCustomPayload packet = new CPacketCustomPayload("CocoaUI", packetBuffer);
		Minecraft.getMinecraft().getConnection().sendPacket(packet);
		
	}

	public void registerListener(PackageListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(PackageListener listener) {
		this.listeners.remove(listener);
	}

	public void registerPackage(int id, Class<? extends AbstractInPackage> clazz) {
		this.classes.put(id, clazz);
	}

	public void removePackage(int id) {
		classes.remove(id);
	}

	public AbstractInPackage getInstance(int i, byte[] data) {
		if (!this.classes.containsKey(i)) {
			return null;
		}
		try {
			return (AbstractInPackage) this.classes.get(i).getConstructors()[0].newInstance(data);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void dealInPackage(AbstractInPackage pack) {
		PackageReceiveEvent e = new PackageReceiveEvent(pack);
		for (PackageListener p : this.listeners) {
			p.onPackageReceive(e);
		}
	}

	private void init() {
		this.registerPackage(1, InVerfiyPackage.class);
		this.registerPackage(2, InResourceUpdate.class);
		this.registerPackage(3, InSingleResourceUpdate.class);
		this.registerPackage(4, InPictureResourceSent.class);
		this.registerPackage(5, InOpenResourceChooser.class);
	}

}
