package net.mcbbs.cocoaapi.mod.listener;

import net.mcbbs.cocoaapi.mod.Main;
import net.mcbbs.cocoaapi.mod.pictures.PluginPicture;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageListener;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageReceiveEvent;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageSendEvent;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InPictureUpdate;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InSinglePictureUpdate;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.OutVerfiyPackage;
import net.mcbbs.cocoaapi.mod.ui.uis.UpdateImageGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PluginMessageListener extends PackageListener {
	public PluginMessageListener() {
		Main.getPluginMessageManager().registerListener(this);
	}

	@Override
	public void onPackageReceive(PackageReceiveEvent e) {
		System.out.println("ReceiveID" + e.getID());
		switch (e.getID()) {
		case 1:
			Main.getPluginMessageManager().sendPackage(new OutVerfiyPackage());
			return;
		case 2:
			Main.getPictureManager().onInPictrureUpdate((InPictureUpdate) e.getPackage());
			return;
		case 3:
			InSinglePictureUpdate pck = (InSinglePictureUpdate) e.getPackage();
			PluginPicture plc = Main.getPictureManager().getPluginPicture(pck.getPluginName());
			plc.setURL(pck.getName(), pck.getUrl());
			return;
		case 4:
			Main.getPictureManager().startCheck();
			return;
		case 5:
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			Minecraft.getMinecraft().displayGuiScreen(new UpdateImageGUI());
			return;
		}

	}

	@Override
	public void onPackageSend(PackageSendEvent e) {

	}

}
