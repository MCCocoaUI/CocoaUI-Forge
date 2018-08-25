package net.mcbbs.cocoaapi.mod.pluginmessage.packages;

import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractInPackage;

public class InPictureResourceSent extends AbstractInPackage {
	private static final int ID = 4;

	public InPictureResourceSent(byte[] data) {
		super(data, ID);
	}

}
