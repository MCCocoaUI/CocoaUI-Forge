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

	/**
	 * 构造参数
	 * 
	 * @param url        地址
	 * @param pluginName 插件名称
	 * @param name       资源名称
	 * @param md5        MD5值
	 */
	public Resource(String url, String pluginName, String name, String md5) {
		this.url = url;
		this.pluginName = pluginName;
		this.name = name;
		this.md5 = md5;
		this.loadExtension();
	}

	/**
	 * 来自远程Map的构造参数
	 * 
	 * @param value
	 * @param pluginName
	 */
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

	/**
	 * 获取扩展名（即文件类型）
	 * 
	 * @return 扩展名
	 */
	public String getExtension() {
		return this.extension;
	}

	/**
	 * 获取资源地址
	 * 
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 获取资源的MD5
	 * 
	 * @return MD5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * 获取资源的名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取所属插件名称
	 * 
	 * @return 插件名称
	 */
	public String getPluginName() {
		return pluginName;
	}

	/**
	 * 获取资源的长（如果资源为图片的话）其他类型资源返回-1
	 * 
	 * @return Width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 获取资源的高（如果资源为图片的话）其他类型资源返回-1
	 * 
	 * @return Height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 获取资源的相对路径
	 * 
	 * @return 路径
	 */
	public File getResourceFile() {
		return Resource.getResourceFile(pluginName, md5);
	}

	/**
	 * 重新设定资源的URL，注意这会导致重新下载
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
		this.loadExtension();
		this.updateURLing = true;
		this.forceDownload();
	}

	/**
	 * 删除资源前的操作
	 */
	public void remove() {
		this.getResourceFile().delete();
	}

	/**
	 * 获取资源是否已经加载完成
	 * 
	 * @return 是否加载完成
	 */
	public boolean isLoaded() {
		return this.loaded;
	}

	/**
	 * 获取资源路径是否存在
	 * 
	 * @return 是否存在
	 */
	public boolean isExists() {
		return this.getResourceFile().exists();
	}

	/**
	 * 重新设定资源信息；
	 * 
	 * @param info
	 */
	protected void setResourceInfo(ResourceInfo info) {
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

	/**
	 * 强制下载
	 */
	public void forceDownload() {
		Main.getResourcesManager().setOperater(this.name, this.pluginName, true);
	}

	/**
	 * 获取资源类型
	 * 
	 * @return 资源类型
	 */
	public ResourceType getType() {
		return this.type;
	}

}
