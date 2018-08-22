package net.mcbbs.cocoaapi.mod.pictures;

import java.io.File;
import java.util.Map;

import net.mcbbs.cocoaapi.mod.Main;

public class Picture {
	
	public static final File PARENTFILE = new File("CocoaUI/Pictures");
	public static File getPictureFile(String pluginName,String md5) {
		return new File(Picture.PARENTFILE + "/" + pluginName + "/" + md5);
	}
	private String url;
	private String md5;

	private String name;
	private String pluginName;
	private int width;
	private int height;
	private boolean loaded;
	private String extension;
	private boolean updateURLing = false;

	public Picture(String url, String pluginName, String name, String md5) {
		this.url = url;
		this.pluginName = pluginName;
		this.name = name;
		this.md5 = md5;
		this.loadExtension();
	}

	public Picture(Map<String, String> value, String pluginName) {
		this.pluginName = pluginName;
		this.md5 = value.get("md5");
		this.name = value.get("name");
		this.url = value.get("url");
		this.loadExtension();
	}

	private void loadExtension() {
		this.extension = this.url.substring(this.url.length() - 3, this.url.length());
	}

	public String getExtension() {
		return this.extension;
	}

	public String getUrl() {
		return url;
	}

	public String getMd5() {
		return md5;
	}

	public String getName() {
		return name;
	}

	public String getPluginName() {
		return pluginName;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public File getPictureFile() {
		return Picture.getPictureFile(pluginName, md5);
	}

	public void setUrl(String url) {
		this.url = url;
		this.loadExtension();
		this.updateURLing = true;
		this.forceDownload();
	}

	public void remove() {
		this.getPictureFile().delete();
	}

	public boolean isLoaded() {
		return this.loaded;
	}
	public boolean isExists() {
		return this.getPictureFile().exists();
	}
	public void setPictureInfo(PictureInfo info) {
		this.width = info.width;
		this.height = info.height;
		if (updateURLing) {
			this.md5 = info.md5;
			this.updateURLing = false;
			return;
		}

		if (this.width == -1) {
			Main.getPictureManager().getPluginPicture(this.pluginName).addErr(name);
			return;
		}
		if (!this.md5.equalsIgnoreCase(info.md5)) {
			this.forceDownload();
			this.updateURLing = true;
		} else {
			this.md5 = info.md5;
		}
	}

	public void forceDownload() {
		Main.getPictureManager().setOperater(this.name, this.pluginName, true);
	}

}
