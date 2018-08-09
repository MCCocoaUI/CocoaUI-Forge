package net.mcbbs.cocoaapi.mod.pictures;

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

public class Picture {
	private static final File PARENTFILE = new File("CocoaUI/Pictures");

	private String url;
	private String md5;

	private String name;
	private String pluginName;
	private int width;
	private int height;
	private boolean loaded;
	private String extension;
	private boolean updateURLing;

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
		return new File(Picture.PARENTFILE + "/" + this.pluginName + "/" + this.name + "." + this.extension);
	}

	public void setUrl(String url) {
		this.url = url;
		this.updateURLing=true;
		Main.getPictureManager().Operater(name, pluginName, true);
	}

	public void remove() {
		this.getPictureFile().delete();
	}

	public boolean isLoaded() {
		return this.loaded;
	}

	public void setPictureInfo(PictureInfo info) {
		this.width = info.width;
		this.height = info.height;
		if (updateURLing) {

			this.md5 = info.md5;
			return;
		}

		if (this.width == -1) {
			Main.getPictureManager().getPluginPicture(this.pluginName).addErr(name);
		}
		if (!this.md5.equalsIgnoreCase(info.md5)) {
			this.forceDownload();
		}
	}

	public void forceDownload() {
		Main.getPictureManager().Operater(this.name, this.pluginName, true);
	}

}
