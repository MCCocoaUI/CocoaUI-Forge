package net.mcbbs.cocoaapi.mod.pluginmessage.packages;

import net.mcbbs.cocoaapi.mod.Main;
import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractOutPackage;

public class OutVerfiyPackage extends AbstractOutPackage{
	public static int id = 1;
	
	public OutVerfiyPackage() {
		super(id);
		this.writeData();
	
	}
	private void writeData() {
		super.getByteArrayDataOutput().writeUTF(Main.VERSION);
	}
}
