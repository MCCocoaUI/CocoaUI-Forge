package net.mcbbs.cocoaapi.mod.pluginmessage.event;

import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractInPackage;

public class PackageReceiveEvent {

	boolean isCancelled;
	AbstractInPackage pack;
	int id;

	public PackageReceiveEvent(AbstractInPackage a) {
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

	public AbstractInPackage getPackage() {return pack;}

}
