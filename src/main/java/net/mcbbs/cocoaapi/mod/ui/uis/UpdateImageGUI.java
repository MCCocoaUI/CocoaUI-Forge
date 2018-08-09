package net.mcbbs.cocoaapi.mod.ui.uis;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.mcbbs.cocoaapi.mod.others.ImageUpdateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UpdateImageGUI extends GuiScreen {
	boolean waitchoose;
	boolean waitUpdate;
	Future<String> future;
	int state;
	String filename;
	String url;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		try {
			
			super.drawDefaultBackground();
			super.drawScreen(mouseX, mouseY, partialTicks);
			if (waitchoose) {
			
				if (future.isDone()) {
					if (future.get().equalsIgnoreCase("cof")) {
						this.state = 1;
						this.waitchoose = false;
					}
					if (future.get().equalsIgnoreCase("noc")) {
						this.state = 2;
						this.waitchoose = false;
					}
					this.state = 3;
					waitchoose = false;
				
					this.filename = future.get();
					this.future = ImageUpdateManager.INSTANCE.postFile(new File(filename));
					waitUpdate = true;
				}
			}
			if (waitUpdate) {
				if (future.isDone()) {
					if (future.get().equals("fail")) {
						this.state = 4;
						this.waitUpdate = false;
					} else {
						this.state = 5;
						this.url = future.get();
						this.waitUpdate = false;
					}
				}
			}

			int y = super.height / 2;
			int x = super.width / 2;
			switch (state) {
			case 0:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "��ѡ���ϴ�����ͼƬ", x - 40, y - 70, 0xFFFFFF);
				return;
			case 1:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "ͼƬֻ����PNG��������", x - 50, y - 30, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, "��ѡ���ϴ�����ͼƬ", x - 40, y - 70, 0xFFFFFF);
				return;
			case 2:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "���ر��˴��ڣ�������", x - 40, y - 30, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, "��ѡ���ϴ�����ͼƬ", x - 40, y - 70, 0xFFFFFF);
				return;
			case 3:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "���Ѿ��ɹ�ѡ��ͼƬ", x - 40, y - 70, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, "�����ϴ������Ե�", x - 35, y - 30, 0xFFFFFF);
				return;
			case 4:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "�ϴ�ʧ��", x - 20, y - 70, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, "���ȡ�ͻ���������־", x - 40, y - 30, 0xFFFFFF);
				return;
			case 5:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "�ϴ��ɹ�", x - 20, y - 70, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, url, x - 80, y - 30, 0xFFFFFF);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void drawWord() {

	}

	@Override
	public void initGui() {
		int y = super.height / 2;
		int x = super.width / 2;
		this.buttonList.add(new GuiButton(1, x - 100, y + 50, 50, 20, "�ϴ�") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				future = ImageUpdateManager.INSTANCE.readyChooseFile();
				waitchoose = true;
			}
		});
		this.buttonList.add(new GuiButton(1, x + 50, y + 50, 50, 20, "�˳�") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				Minecraft.getMinecraft().player.closeScreen();
			}
		});

	}

}
