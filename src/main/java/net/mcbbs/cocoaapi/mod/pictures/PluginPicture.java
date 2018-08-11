package net.mcbbs.cocoaapi.mod.pictures;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.mcbbs.cocoaapi.mod.Main;

public class PluginPicture {
	private Map<String, Object> data;
	private Map<String, Picture> pics = Maps.newHashMap();
	private String pluginName;
	private static final String FIRSTLOAD = "welcome";

	private Map<String, Integer> errs = Maps.newConcurrentMap();

	public PluginPicture(Map<String, Object> data) {
		this.data = data;
		this.loadMap();
	}

	public void loadMap() {
		this.pluginName = (String) this.data.get("pluginName");
		Map<String, Map<String, String>> content = (Map<String, Map<String, String>>) this.data.get("content");
		for (Entry<String, Map<String, String>> entry : content.entrySet()) {
			this.pics.put(entry.getKey(), new Picture(entry.getValue(), this.pluginName));
		}
	}

	public boolean setURL(String name, String url) {
		if (this.pics.containsKey(name)) {
			this.pics.get(name).setUrl(url);
			return true;
		}
		return false;
	}

	public boolean removePicture(String name) {
		if (this.pics.containsKey(name)) {
			this.pics.get(name).remove();
			this.pics.remove(name);
			return true;
		}
		return false;
	}

	public Picture getPicture(String name) {
		return this.pics.get(name);
	}

	public String getpluginName() {
		return this.pluginName;
	}

	public void loadPicture(Picture p) {
		this.pics.put(p.getName(), p);
	}

	public boolean contains(String name) {
		return this.pics.containsKey(name);
	}

	public Map<String, Integer> getErrors() {
		return this.errs;
	}

	public void addErr(String name) {
		this.errs.put(name, 1);
	}

	public void startCheckPictures() {
		for (Picture pic : this.pics.values()) {
			if(pic.isExists()) {System.out.println(pic.getName()+".skiped");continue;}
			Main.getPictureManager().setOperater(pic.getName(), pic.getPluginName(), false);
		}
	}

}