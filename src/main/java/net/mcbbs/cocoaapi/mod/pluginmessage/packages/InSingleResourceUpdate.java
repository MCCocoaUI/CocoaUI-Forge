package net.mcbbs.cocoaapi.mod.pluginmessage.packages;

import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractInPackage;

public class InSingleResourceUpdate extends AbstractInPackage {
	private static final int ID = 3;
	private String url;
	private String name;
	private String pluginName;
	private int state;

	public InSingleResourceUpdate(byte[] data) {
		super(data, ID);
		readData();
	}

	private void readData() {
		this.state = super.getByteArrayDataInput().readInt();
		this.pluginName = super.getByteArrayDataInput().readUTF();
		this.name = super.getByteArrayDataInput().readUTF();
		if (this.state == 1) {
			this.url = super.getByteArrayDataInput().readUTF();
		}
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public String getPluginName() {
		return pluginName;
	}

	public int getState() {
		return state;
	}

}
