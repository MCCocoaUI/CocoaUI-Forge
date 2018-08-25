package net.mcbbs.cocoaapi.mod.resource;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.mcbbs.cocoaapi.mod.Main;

public class PluginResourceManager {
	private Map<String, Object> orignData;
	private Map<String, Resource> resources = Maps.newHashMap();
	private String pluginName;
	private static final String FIRSTLOAD = "welcome";

	private Map<String, Integer> errs = Maps.newConcurrentMap();

	public PluginResourceManager(Map<String, Object> orignData) {
		this.orignData = orignData;
		this.loadMap();
	}

	public void loadMap() {
		this.pluginName = (String) this.orignData.get("pluginName");
		Map<String, Map<String, String>> content = (Map<String, Map<String, String>>) this.orignData.get("content");
		for (Entry<String, Map<String, String>> entry : content.entrySet()) {
			this.resources.put(entry.getKey(), new Resource(entry.getValue(), this.pluginName));
		}
	}

	public boolean setURL(String name, String url) {
		if (this.resources.containsKey(name)) {
			this.resources.get(name).setUrl(url);
			return true;
		}
		return false;
	}

	public boolean removeResource(String name) {
		if (this.resources.containsKey(name)) {
			this.resources.get(name).remove();
			this.resources.remove(name);
			return true;
		}
		return false;
	}

	public Resource getResource(String name) {
		return this.resources.get(name);
	}

	public String getPluginName() {
		return this.pluginName;
	}

	public void loadResource(Resource p) {
		this.resources.put(p.getName(), p);
	}

	public boolean contains(String name) {
		return this.resources.containsKey(name);
	}

	public Map<String, Integer> getErrors() {
		return this.errs;
	}

	public void addErr(String name) {
		this.errs.put(name, 1);
	}

	public void startCheckResources() {
		for (Resource resource : this.resources.values()) {
			if(resource.isExists()) {System.out.println(resource.getName()+".skiped");continue;}
			Main.getResourcesManager().setOperater(resource.getName(), resource.getPluginName(), false);
		}
	}

}