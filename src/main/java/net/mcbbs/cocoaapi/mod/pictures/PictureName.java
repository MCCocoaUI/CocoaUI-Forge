package net.mcbbs.cocoaapi.mod.pictures;

public class PictureName {
	String name;
	String pluginName;
	String fullName;

	public PictureName(String name) {
		this.name = this.getPicName(name);
		this.pluginName = this.getPluginName(name);
	}

	public PictureName(String name, String pluginName) {
		this.name = name;
		this.pluginName = pluginName;
		this.fullName = this.getPictureName(pluginName, name);
	}

	private String getPluginName(String name) {
		return name.split("\\.")[0];
	}

	private String getPicName(String name) {
		return name.split("\\.")[0];
	}

	private String getPictureName(String pluginname, String name) {
		return pluginname + "." + name;
	}
}