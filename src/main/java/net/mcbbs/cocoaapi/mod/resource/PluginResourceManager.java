package net.mcbbs.cocoaapi.mod.resource;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.mcbbs.cocoaapi.mod.Main;

public class PluginResourceManager {
	private Map<String, Object> orignData;
	private Map<String, Resource> resources = Maps.newHashMap();
	private String pluginName;
	private static final String FIRSTLOAD = "welcome";

	private Set<String> picSet = Sets.newHashSet();
	private Set<String> fontSet = Sets.newHashSet();
	private Set<String> videoSet = Sets.newHashSet();
	private Set<String> musicset = Sets.newHashSet();
	private Map<String, Integer> errs = Maps.newConcurrentMap();

	public PluginResourceManager(Map<String, Object> orignData) {
		this.orignData = orignData;
		this.loadMap();
	}

	public void addSet(String name, ResourceType type) {
		switch (type) {
		case VIDEO:
			this.videoSet.add(name);
			return;
		case MUSIC:
			this.musicset.add(name);
			return;
		case PICTURE:
			this.picSet.add(name);
			return;
		case FONT:
			this.fontSet.add(name);
			return;
		}
	}

	public void removeSet(String name, ResourceType type) {
		switch (type) {
		case VIDEO:
			this.videoSet.remove(name);
			return;
		case MUSIC:
			this.musicset.remove(name);
			return;
		case PICTURE:
			this.picSet.remove(name);
			return;
		case FONT:
			this.fontSet.remove(name);
			return;
		}
	}

	public void loadMap() {
		this.pluginName = (String) this.orignData.get("pluginName");
		Map<String, Map<String, String>> content = (Map<String, Map<String, String>>) this.orignData.get("content");
		for (Entry<String, Map<String, String>> entry : content.entrySet()) {
			Resource resource = new Resource(entry.getValue(), this.pluginName);
			this.resources.put(entry.getKey(), resource);
			this.addSet(resource.getName(), resource.getType());
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
			Resource resource = this.resources.get(name);
			resource.remove();
			this.removeSet(resource.getName(), resource.getType());
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

	public void loadResource(Resource resource) {
		this.addSet(resource.getName(), resource.getType());
		this.resources.put(resource.getName(), resource);
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
			if (resource.isExists()) {
				System.out.println(resource.getName() + ".skiped");
				continue;
			}
			Main.getResourcesManager().setOperater(resource.getName(), resource.getPluginName(), false);
		}
	}

	public Set<String> getPicSet() {
		return picSet;
	}

	public Set<String> getFontSet() {
		return fontSet;
	}

	public Set<String> getVideoSet() {
		return videoSet;
	}

	public Set<String> getMusicset() {
		return musicset;
	}

}