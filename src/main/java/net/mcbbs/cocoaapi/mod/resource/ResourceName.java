package net.mcbbs.cocoaapi.mod.resource;

public class ResourceName {
	String name;
	String pluginName;
	String fullName;
	/**
	 * 构造函数，根据识别名构造
	 * @param name 识别名
	 */
	public ResourceName(String name) {
		this.name = this.getResName(name);
		this.pluginName = this.getPluginName(name);
	}
	/**
	 * 构造函数，根据插件名和资源名构造
	 * @param name 资源名
	 * @param pluginName 插件名
	 */
	public ResourceName(String name, String pluginName) {
		this.name = name;
		this.pluginName = pluginName;
		this.fullName = this.getResourceName(pluginName, name);
	}

	public String getPluginName(String name) {
		return name.split("\\.")[0];
	}

	public String getResName(String name) {
		return name.split("\\.")[0];
	}

	public String getResourceName(String pluginname, String name) {
		return pluginname + "." + name;
	}
}