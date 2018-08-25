package net.mcbbs.cocoaapi.mod.resource;

public class ResourceName {
	String name;
	String pluginName;
	String fullName;

	public ResourceName(String name) {
		this.name = this.getPicName(name);
		this.pluginName = this.getPluginName(name);
	}

	public ResourceName(String name, String pluginName) {
		this.name = name;
		this.pluginName = pluginName;
		this.fullName = this.getResourceName(pluginName, name);
	}

	private String getPluginName(String name) {
		return name.split("\\.")[0];
	}

	private String getPicName(String name) {
		return name.split("\\.")[0];
	}

	private String getResourceName(String pluginname, String name) {
		return pluginname + "." + name;
	}
}