package net.mcbbs.cocoaapi.mod.pluginmessage.packages;

import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractInPackage;

public class InPictureUpdateSent extends AbstractInPackage {
	private static final int ID = 4;

	public InPictureUpdateSent(byte[] data) {
		super(data, ID);
	}

}
