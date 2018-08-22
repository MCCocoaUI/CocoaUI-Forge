package net.mcbbs.cocoaapi.mod.others;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;

public class KeyBind {
	public static KeyBinding test;

	public KeyBind() {
		test = new KeyBinding("Test", Keyboard.KEY_H, "Open the GUI");
		ClientRegistry.registerKeyBinding(test); 
	}
}
