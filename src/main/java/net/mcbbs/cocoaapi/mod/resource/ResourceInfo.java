package net.mcbbs.cocoaapi.mod.resource;

public class ResourceInfo {
	public ResourceInfo(String url, String md5, int width, int height,ResourceName name) {
		super();
		this.url = url;
		this.md5 = md5;
		this.width = width;
		this.height = height;
		this.name = name;
	}
	String url;
	String md5;
	int width;
	int height;
	ResourceName name;
	boolean force;
}
