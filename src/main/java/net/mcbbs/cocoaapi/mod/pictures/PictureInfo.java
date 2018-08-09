package net.mcbbs.cocoaapi.mod.pictures;

public class PictureInfo {
	public PictureInfo(String url, String md5, int width, int height,PictureName name) {
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
	PictureName name;
	boolean force;
}
