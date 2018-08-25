package net.mcbbs.cocoaapi.mod.listener;

import java.io.File;
import java.io.IOException;

import net.mcbbs.cocoaapi.mod.Main;
import net.mcbbs.cocoaapi.mod.others.ImageUpdateManager;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageListener;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageReceiveEvent;
import net.mcbbs.cocoaapi.mod.pluginmessage.event.PackageSendEvent;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InResourceUpdate;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.InSingleResourceUpdate;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.OutVerfiyPackage;
import net.mcbbs.cocoaapi.mod.resource.PluginResourceManager;
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
			Main.getResourcesManager().onInResourceUpdate((InResourceUpdate) e.getPackage());
			return;
		case 3:
			InSingleResourceUpdate pck = (InSingleResourceUpdate) e.getPackage();
			PluginResourceManager plc = Main.getResourcesManager().getPluginResource(pck.getPluginName());
			plc.setURL(pck.getName(), pck.getUrl());
			return;
		case 4:
			Main.getResourcesManager().startCheck();
			return;
		case 5:
			EntityPlayer player = Minecraft.getMinecraft().player;
			Minecraft.getMinecraft().displayGuiScreen(new UpdateImageGUI());
			return;
		}

	}

	@Override
	public void onPackageSend(PackageSendEvent e) {

	}

}
