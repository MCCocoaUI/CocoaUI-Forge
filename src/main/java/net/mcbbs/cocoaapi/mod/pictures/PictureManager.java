package net.mcbbs.cocoaapi.mod.pictures;

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
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InPictureUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class PictureManager {
	ExecutorService threadPool = Executors.newFixedThreadPool(4);
	boolean isloaded = false;
	boolean firstLoad = true;
	int timer;
	private Map<PictureName, Future<PictureInfo>> operaters = Maps.newConcurrentMap();
	private Set<PictureName> finish = Sets.newHashSet();
	public Map<String, PluginPicture> pms = Maps.newConcurrentMap();
	private int loaded;
	boolean checking = false;

	public void onInPictrureUpdate(InPictureUpdate pack) {
		String name = (String) pack.getData().get("pluginName");
		this.pms.put(name, new PluginPicture(pack.getData()));
	}

	public PluginPicture getPluginPicture(String name) {
		return this.pms.get(name);
	}

	public void finishCheck() {
		Map<String, Map<String, Integer>> cache = Maps.newHashMap();
		for (PluginPicture pic : this.pms.values()) {
			cache.put(pic.getpluginName(), pic.getErrors());
		}
		if (cache.isEmpty()) {
			Minecraft.getMinecraft().player
					.sendMessage(new TextComponentString("��a[CocoaUI]��c���Է�����������ͼƬ�Ѿ������ϡ��쳣����������"));
		} else {
			Minecraft.getMinecraft().player
					.sendMessage(new TextComponentString("��a[CocoaUI]��c���Է�����������ͼƬ�Ѿ������ϡ�����ͼƬ�쳣��"));
			for (Entry<String, Map<String, Integer>> entry : cache.entrySet()) {
				StringBuilder builder = new StringBuilder();
				builder.append("��a[CocoaUI]��c��� ��a").append(entry.getKey()).append(" ��cע���ͼƬ:");
				for (Entry<String, Integer> err : entry.getValue().entrySet()) {
					builder.append(err.getKey());
					builder.append(" ,");
				}
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString(builder.toString()));
			}
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("�޷��������أ����ܻ�Ӱ�쵽�������顣�����������ϵ����������Ա."));

		}
	}

	public Picture getPicture(PictureName name) {
		String pluginName = name.pluginName;
		String picName = name.name;
		if (this.pms.containsKey(pluginName)) {
			PluginPicture picture = this.pms.get(pluginName);
			if (picture.contains(picName)) {
				return picture.getPicture(picName);
			}
		}
		return null;
	}

	public Picture getPicture(String nam) {
		PictureName name = new PictureName(nam);
		return this.getPicture(name);
	}

	public File getPictureFile(String name) {
		Picture pic = this.getPicture(name);
		if (pic == null)
			return null;
		if (!pic.isLoaded())
			return null;

		return pic.getPictureFile();
	}

	public void tick() {
		if (!checking) {
			return;
		}
		if (this.operaters.isEmpty()) {
			if (this.firstLoad) {
				this.firstLoad = false;
				System.out.println("[CocoaUI]�������ͼƬ�Ѿ��������");
				return;
			}
			return;
		}
		if (timer >= 30) {
			if (!this.isFinish()) {
				System.out.println("�Ѿ�����(" + this.loaded + "/" + (this.loaded + this.operaters.size() + ")"));
				this.timer = 0;
			}
		}
		for (Entry<PictureName, Future<PictureInfo>> entry : this.operaters.entrySet()) {
			if (entry.getValue().isDone()) {
				try {
					this.updateInfo(entry.getKey(), entry.getValue().get());
					this.finish.add(entry.getKey());
					this.loaded++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		for (PictureName pl : this.finish) {
			this.operaters.remove(pl);
		}
	}

	private void updateInfo(PictureName key, PictureInfo pictureInfo) {
		Picture pic = this.getPicture(key);
		if (pic != null) {
			pic.setPictureInfo(pictureInfo);
		}
	}

	private boolean isFinish() {
		return !this.firstLoad;
	}

	public void Operater(String name, String pluginName, boolean download) {
		PluginPicture picma = this.getPluginPicture(pluginName);
		Picture pic;
		if (picma != null) {
			pic = picma.getPicture(name);
		} else {
			return;
		}
		File f = pic.getPictureFile();
		String url = pic.getUrl();
		this.operaters.put(new PictureName(name, pluginName),
		this.threadPool.submit(new PictureOperater(url, new PictureName(name, pluginName), f, download)));

	}

	public void startCheck() {
		isloaded = false;
		firstLoad = true;
		this.finish.clear();
		this.operaters.clear();
		this.timer = 0;
		for (PluginPicture picture : this.pms.values()) {
			picture.startCheck();
		}
		this.checking = true;

	}

}
