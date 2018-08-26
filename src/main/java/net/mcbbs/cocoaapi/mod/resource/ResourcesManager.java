package net.mcbbs.cocoaapi.mod.resource;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.mcbbs.cocoaapi.mod.others.ImageUpdateManager;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InResourceUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ResourcesManager {
	private ExecutorService threadPool = Executors.newFixedThreadPool(4);
	private boolean isloaded = false;
	private boolean firstLoad = true;
	private int counter;
	private Map<ResourceName, Future<ResourceInfo>> resourceOperaters = Maps.newConcurrentMap();
	private Set<ResourceName> finishedSet = Sets.newHashSet();
	public Map<String, PluginResourceManager> pluginResourcesManagers = Maps.newConcurrentMap();
	private int loadedCount;
	private boolean checking = false;

	public void onInResourceUpdate(InResourceUpdate pack) {
		String name = (String) pack.getData().get("pluginName");
		this.pluginResourcesManagers.put(name, new PluginResourceManager(pack.getData()));
	}

	public PluginResourceManager getPluginResource(String name) {
		return this.pluginResourcesManagers.get(name);
	}

	public void finishedSetCheck() {
		Map<String, Map<String, Integer>> cache = Maps.newHashMap();
		for (PluginResourceManager pic : this.pluginResourcesManagers.values()) {
			cache.put(pic.getPluginName(), pic.getErrors());
		}
		if (cache.isEmpty()) {
			Minecraft.getMinecraft().player
					.sendMessage(new TextComponentString("§a[CocoaUI]§c来自服务器的所有资源已经检查完毕。异常均已修正。"));
		} else {
			Minecraft.getMinecraft().player
					.sendMessage(new TextComponentString("§a[CocoaUI]§c来自服务器的所有资源已经检查完毕。以下资源异常："));
			for (Entry<String, Map<String, Integer>> entry : cache.entrySet()) {
				StringBuilder builder = new StringBuilder();
				builder.append("§a[CocoaUI]§c插件 §a").append(entry.getKey()).append(" §c注册的资源:");
				for (Entry<String, Integer> err : entry.getValue().entrySet()) {
					builder.append(err.getKey());
					builder.append(" ,");
				}
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString(builder.toString()));
			}
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("无法正常加载，可能会影响到您的体验。请检查网络或联系服务器管理员."));
		}
	}

	public Resource getResource(ResourceName name) {
		String pluginName = name.pluginName;
		String picName = name.name;
		if (this.pluginResourcesManagers.containsKey(pluginName)) {
			PluginResourceManager Resource = this.pluginResourcesManagers.get(pluginName);
			if (Resource.contains(picName)) {
				return Resource.getResource(picName);
			}
		}
		return null;
	}

	public Resource getResource(String nam) {
		ResourceName name = new ResourceName(nam);
		return this.getResource(name);
	}

	public File getResourceFile(String name) {
		Resource pic = this.getResource(name);
		if (pic == null)
			return null;
		if (!pic.isLoaded())
			return null;

		return pic.getResourceFile();
	}

	public int getLoadedCount() {
		return this.loadedCount;
	}

	public int getTotalLoaderCount() {
		return this.loadedCount + this.resourceOperaters.size();
	}

	public void tick() {
		if (!checking) {
			return;
		}
		if (this.resourceOperaters.isEmpty()) {
			if (this.firstLoad) {
				this.firstLoad = false;
				System.out.println("[CocoaUI]插件所有资源已经加载完成");
				finishedSetCheck();
				return;
			}
			return;
		}
		if (counter >= 30) {
			if (!this.isfinishedSet()) {
				System.out.println("已经加载(" + this.loadedCount + "/" + this.getTotalLoaderCount() + ")");
				this.counter = 0;
			}
		}
		for (Entry<ResourceName, Future<ResourceInfo>> entry : this.resourceOperaters.entrySet()) {
			if (entry.getValue().isDone()) {
				try {
					this.updateInfo(entry.getKey(), entry.getValue().get());
				} catch (InterruptedException e) {
					e.printStackTrace();

				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				this.finishedSet.add(entry.getKey());
				this.loadedCount++;
			}
		}
		for (ResourceName pl : this.finishedSet) {
			this.resourceOperaters.remove(pl);
		}
	}

	private void updateInfo(ResourceName key, ResourceInfo ResourceInfo) {
		Resource pic = this.getResource(key);
		if (pic != null) {
			pic.setResourceInfo(ResourceInfo);
		}
	}

	private boolean isfinishedSet() {
		return !this.firstLoad;
	}

	public void setOperater(String name, String pluginName, boolean download) {
		PluginResourceManager picma = this.getPluginResource(pluginName);
		Resource pic;
		if (picma != null) {
			pic = picma.getResource(name);
		} else {
			return;
		}
		File f = pic.getResourceFile();
		String url = pic.getUrl();
		this.resourceOperaters.put(new ResourceName(name, pluginName), this.threadPool.submit(new ResourceOperater(url,
				new ResourceName(name, pluginName), f, download, pic.getType().equals(ResourceType.PICTURE))));

	}

	public void startCheck() {
		System.out.println("[CocoaUI]开始检查资源");
		isloaded = false;
		firstLoad = true;
		this.finishedSet.clear();
		this.resourceOperaters.clear();
		this.counter = 0;
		for (PluginResourceManager Resource : this.pluginResourcesManagers.values()) {
			System.out.println("[CocoaUI]开始检查插件：" + Resource.getPluginName());
			Resource.startCheckResources();
		}
		this.checking = true;

	}
}
