package net.mcbbs.cocoaapi.mod.listener;

import net.mcbbs.cocoaapi.mod.Main;
import net.mcbbs.cocoaapi.mod.others.KeyBind;
import net.mcbbs.cocoaapi.mod.ui.uis.UpdateImageGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ForgeListener {
	public ForgeListener() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (KeyBind.test.isPressed()) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			Minecraft.getMinecraft().displayGuiScreen(new UpdateImageGUI());
		}
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTick(TickEvent event) {
	
		Main.getPictureManager().tick();
	}

}
