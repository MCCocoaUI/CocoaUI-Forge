package net.mcbbs.cocoaapi.mod.pluginmessage.packages;

import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractInPackage;

public class InResourceSent extends AbstractInPackage {
	private static final int ID = 4;

	public InResourceSent(byte[] data) {
		super(data, ID);
	}

}
