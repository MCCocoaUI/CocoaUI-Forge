package net.mcbbs.cocoaapi.mod.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public abstract class AbstractInPackage {

	private ByteArrayDataInput in;
	protected byte[] orgianData;
	protected int id;

	public AbstractInPackage(byte[] data, int id) {
		this.orgianData = data;
		this.id = id;
		in = ByteStreams.newDataInput(data);
		in.readInt();
	}

	protected ByteArrayDataInput getByteArrayDataInput() {
		return this.in;
	}

	public final byte[] getOrginanData() {
		return this.orgianData;
	}

	public final int getID() {
		return this.id;
	}

	public final PackageType getType() {
		return PackageType.IN;
	}

	protected String readJson() {

		if (!"Json".equalsIgnoreCase(in.readUTF())) {
			return null;
		}
		String json = in.readUTF();
		return json;
	}

}
