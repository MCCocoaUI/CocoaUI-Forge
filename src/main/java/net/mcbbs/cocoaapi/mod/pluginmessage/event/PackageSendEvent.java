package net.mcbbs.cocoaapi.mod.pluginmessage.event;

import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractOutPackage;

public class PackageSendEvent {
	boolean isCancelled;
	AbstractOutPackage pack;
	int id;

	public PackageSendEvent(AbstractOutPackage a) {
		this.pack = a;
		this.id = a.getID();
	}

	public void setCancelled(boolean arg) {
		this.isCancelled = arg;
	}

	public boolean isCancelled() {
		return this.isCancelled;
	}

	public int getID() {
		return this.id;
	}

	public AbstractOutPackage getPackage() {
		return pack;
	}

}
