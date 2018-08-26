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
	private boolean isNative;
	private Set<String> picSet = Sets.newHashSet();
	private Set<String> fontSet = Sets.newHashSet();
	private Set<String> videoSet = Sets.newHashSet();
	private Set<String> musicset = Sets.newHashSet();
	private Map<String, Integer> errs = Maps.newConcurrentMap();

	protected PluginResourceManager(String name) {
		this.isNative = true;
		this.orignData = Maps.newHashMap();
		this.pluginName = name;
	}

	protected PluginResourceManager(Map<String, Object> orignData) {
		this.orignData = orignData;
		this.loadMap();
		this.isNative = false;
	}

	private void addSet(String name, ResourceType type) {
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

	private void removeSet(String name, ResourceType type) {
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
	/**
	 * 加载map
	 */
	private void loadMap() {
		this.pluginName = (String) this.orignData.get("pluginName");
		Map<String, Map<String, String>> content = (Map<String, Map<String, String>>) this.orignData.get("content");
		for (Entry<String, Map<String, String>> entry : content.entrySet()) {
			Resource resource = new Resource(entry.getValue(), this.pluginName);
			this.resources.put(entry.getKey(), resource);
			this.addSet(resource.getName(), resource.getType());
		}
	}
	/**
	 * 重新设定URL
	 * @param name 名称
	 * @param url url
	 * @return 是否成功
	 */
	public boolean setURL(String name, String url) {
		if (this.resources.containsKey(name)) {
			this.resources.get(name).setUrl(url);
			return true;
		}
		return false;
	}
	/**
	 * 删除资源，若资源不存在返回null
	 * @param name 资源名称
	 * @return 是否成功
	 */
	protected boolean removeResource(String name) {
		if (this.resources.containsKey(name)) {
			Resource resource = this.resources.get(name);
			resource.remove();
			this.removeSet(resource.getName(), resource.getType());
			this.resources.remove(name);
			return true;
		}
		return false;
	}
	/**
	 * 获取资源，若资源不存在返回null;
	 * @param name 资源名称
	 * @return 是否成功
	 */
	public Resource getResource(String name) {
		return this.resources.get(name);
	}
	/**
	 * 获取插件名称
	 * @return 插件名称
	 */
	public String getPluginName() {
		return this.pluginName;
	}
	/**
	 * 加载资源
	 * @param resource
	 */
	public void loadResource(Resource resource) {
		this.addSet(resource.getName(), resource.getType());
		this.resources.put(resource.getName(), resource);
	}

	/**
	 * 检查一个资源是否存在
	 * 
	 * @param name 资源名称
	 * @return 是否存在
	 */
	public boolean contains(String name) {
		return this.resources.containsKey(name);
	}

	/**
	 * 获取加载过程中的错误列表
	 * 
	 * @return 列表
	 */
	public Map<String, Integer> getErrors() {
		return this.errs;
	}

	/**
	 * 记录错误
	 * 
	 * @param name 资源名称
	 */
	protected void addErr(String name) {
		this.errs.put(name, 1);
	}

	protected void startCheckResources() {
		for (Resource resource : this.resources.values()) {
			if (resource.isExists()) {
				System.out.println(resource.getName() + ".skiped");
				continue;
			}
			Main.getResourcesManager().setOperater(resource.getName(), resource.getPluginName(), false);
		}
	}

	/**
	 * 获取图片类型资源集合
	 * 
	 * @return 集合
	 */
	public Set<String> getPicSet() {
		return picSet;
	}

	/**
	 * 获取字体类型资源集合
	 * 
	 * @return 集合
	 */
	public Set<String> getFontSet() {
		return fontSet;
	}

	/**
	 * 获取视频类型资源集合
	 * 
	 * @return 集合
	 */
	public Set<String> getVideoSet() {
		return videoSet;
	}

	/**
	 * 获取音乐类型资源集合
	 * 
	 * @return 集合
	 */
	public Set<String> getMusicSet() {
		return musicset;
	}

}