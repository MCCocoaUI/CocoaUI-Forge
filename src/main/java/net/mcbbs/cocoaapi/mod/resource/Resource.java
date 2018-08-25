package net.mcbbs.cocoaapi.mod.resource;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.imageio.ImageIO;

import net.mcbbs.cocoaapi.mod.Main;
import net.mcbbs.cocoaapi.mod.utils.MD5Tool;

public class Resource {

	public static final File PARENTFILE = new File("CocoaUI/Resources");

	public static File getResourceFile(String pluginName, String md5) {
		return new File(Resource.PARENTFILE + "/" + pluginName + "/" + md5);
	}

	private String url;
	private String md5;

	private String name;
	private String pluginName;
	private int width;
	private int height;
	private boolean loaded;
	private String extension;
	private ResourceType type;
	private boolean updateURLing = false;

	public Resource(String url, String pluginName, String name, String md5) {
		this.url = url;
		this.pluginName = pluginName;
		this.name = name;
		this.md5 = md5;
		this.loadExtension();
	}

	public Resource(Map<String, String> value, String pluginName) {
		this.pluginName = pluginName;
		this.md5 = value.get("md5");
		this.name = value.get("name");
		this.url = value.get("url");
		this.type = ResourceType.toResourceType(value.get("type"));
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

	public File getResourceFile() {
		return Resource.getResourceFile(pluginName, md5);
	}

	public void setUrl(String url) {
		this.url = url;
		this.loadExtension();
		this.updateURLing = true;
		this.forceDownload();
	}

	public void remove() {
		this.getResourceFile().delete();
	}

	public boolean isLoaded() {
		return this.loaded;
	}

	public boolean isExists() {
		return this.getResourceFile().exists();
	}

	public void setResourceInfo(ResourceInfo info) {
		this.width = info.width;
		this.height = info.height;
		if (updateURLing) {
			this.md5 = info.md5;
			this.updateURLing = false;
			return;
		}

		if (this.width == -1) {
			Main.getResourcesManager().getPluginResource(this.pluginName).addErr(name);
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
		Main.getResourcesManager().setOperater(this.name, this.pluginName, true);
	}

	public ResourceType getType() {
		return this.type;
	}

}
