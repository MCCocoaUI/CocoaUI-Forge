package net.mcbbs.cocoaapi.mod.pluginmessage.packages;

import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractOutPackage;

public class OutResourceChooserBack extends AbstractOutPackage {
	private static final int ID = 5;
	private String url;

	public OutResourceChooserBack(String url) {
		super(ID);
		this.url = url;
		this.writeData();
	}

	private void writeData() {
		super.getByteArrayDataOutput().writeUTF(this.url);
	}

}
