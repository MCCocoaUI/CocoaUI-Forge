package net.mcbbs.cocoaapi.mod.pluginmessage.packages;

import java.util.Map;

import com.google.gson.Gson;

import net.mcbbs.cocoaapi.mod.pluginmessage.AbstractInPackage;

public class InResourceUpdate extends AbstractInPackage {
	public static final int ID = 2;
	private Map<String, Object> data;

	public InResourceUpdate(byte[] data) {
		super(data, ID);
		dealData();
	}

	private void dealData() {
		String json = super.readJson();
		this.data = new Gson().fromJson(json, Map.class);
	}

	public Map<String, Object> getData() {
		return this.data;
	}

}
