package net.mcbbs.cocoaapi.mod.resource;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.mcbbs.cocoaapi.mod.utils.MD5Tool;

public class ResourceOperater implements Callable<ResourceInfo> {
	private String url;
	private byte[] bytes;
	private int width;
	private int height;
	private String md5;
	private ResourceName name;
	private File file;
	private boolean download;
	private boolean isPicture;

	public ResourceOperater(String url, ResourceName name, File file, boolean download, boolean isPicture) {
		this.url = url;
		this.name = name;
		this.file = file;
		this.download = download;
		this.isPicture = isPicture;
	}

	@Override
	public ResourceInfo call() throws Exception {
		if (!this.loadBytes()) {
			return new ResourceInfo(null, null, -1, -1, this.name);
		}
		this.md5 = MD5Tool.md5(this.bytes);
		this.file = Resource.getResourceFile(this.name.pluginName, md5);
		if (isPicture) {
			if (!this.loadSize()) {
				return new ResourceInfo(null, null, -1, -1, this.name);
			}
		}
		if (!file.exists()) {
			this.saveBytes();
		} else if (download) {
			this.saveBytes();
		}
		return this.getInfo();
	}

	private ResourceInfo getInfo() {
		ResourceInfo info = new ResourceInfo(this.url, this.md5, this.width, this.height, name);
		info.force = this.download;
		return info;
	}

	private boolean loadBytes() {
		if (download) {
			return loadBytesFromNet();
		}
		if (this.file.exists()) {
			return loadBytesFromFile();
		} else {
			return loadBytesFromNet();
		}
	}

	private boolean loadBytesFromFile() {
		try (InputStream in = new FileInputStream(this.file)) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			byte[] bs = new byte[16384];
			int len;
			while ((len = in.read(bs)) != -1) {
				out.write(bs, 0, len);
			}
			bytes = out.toByteArray();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean loadBytesFromNet() {
		try (InputStream in = new URI(this.url).toURL().openStream()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			byte[] bs = new byte[16384];
			int len;
			while ((len = in.read(bs)) != -1) {
				out.write(bs, 0, len);
			}
			bytes = out.toByteArray();
			return true;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean loadSize() {
		BufferedImage img;
		try {
			img = ImageIO.read(new ByteArrayInputStream(this.bytes));
			this.width = img.getWidth();
			this.height = img.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void saveBytes() {
		this.createfile();
		try (BufferedOutputStream buff = new BufferedOutputStream(new FileOutputStream(this.file))) {
			buff.write(this.bytes, 0, this.bytes.length);
			buff.flush();
			buff.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createfile() {
		if (this.file.exists()) {
		}
		File parent = this.file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		try {
			this.file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
