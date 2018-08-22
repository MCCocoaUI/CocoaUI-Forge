package net.mcbbs.cocoaapi.mod.ui.uis;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.mcbbs.cocoaapi.mod.Main;
import net.mcbbs.cocoaapi.mod.others.ImageUpdateManager;
import net.mcbbs.cocoaapi.mod.pluginmessage.packages.OutPictureChooserBack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UpdateImageGUI extends GuiScreen {
	boolean waitchoose;
	boolean waitUpdate;
	Future<String> future;
	int state = 0;
	String filename;
	String url;
	boolean sent;

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
				super.drawString(Minecraft.getMinecraft().fontRenderer, "请选择上传您的图片", x - 40, y - 70, 0xFFFFFF);
				return;
			case 1:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "图片只能是PNG，请重试", x - 50, y - 30, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, "请选择上传您的图片", x - 40, y - 70, 0xFFFFFF);
				return;
			case 2:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "您关闭了窗口，请重试", x - 40, y - 30, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, "请选择上传您的图片", x - 40, y - 70, 0xFFFFFF);
				return;
			case 3:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "您已经成功选择图片", x - 40, y - 70, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, "正在上传，请稍等", x - 35, y - 30, 0xFFFFFF);
				return;
			case 4:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "上传失败", x - 20, y - 70, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, "请调取客户端运行日志", x - 40, y - 30, 0xFFFFFF);
				return;
			case 5:
				super.drawString(Minecraft.getMinecraft().fontRenderer, "上传成功,数据包已经发送到服务器", x - 60, y - 70, 0xFFFFFF);
				super.drawString(Minecraft.getMinecraft().fontRenderer, url, x - 80, y - 30, 0xFFFFFF);
				OutPictureChooserBack bac = new OutPictureChooserBack(url);
				if (!sent) {
					Main.getPluginMessageManager().sendPackage(bac);
					sent = true;
				}
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
		if (state==0) {
			this.buttonList.add(new GuiButton(1, x - 100, y + 50, 50, 20, "上传") {
				@Override
				public void mouseReleased(int mouseX, int mouseY) {
					future = ImageUpdateManager.INSTANCE.readyChooseFile();
					waitchoose = true;
				}
			});
			this.buttonList.add(new GuiButton(1, x + 50, y + 50, 50, 20, "退出") {
				@Override
				public void mouseReleased(int mouseX, int mouseY) {
					Minecraft.getMinecraft().thePlayer.closeScreen();
				}
			});
		}else {
			if(sent) {
				this.buttonList.add(new GuiButton(1, x -100, y + 50, 100, 20, "退出") {
					@Override
					public void mouseReleased(int mouseX, int mouseY) {
						Minecraft.getMinecraft().thePlayer.closeScreen();
					}
				});
			}
		}
	}

}
