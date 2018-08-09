package net.mcbbs.cocoaapi.mod.others;

import java.io.File;
import java.util.concurrent.Callable;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class UpdateImageFileChooser implements Callable<String> {

	@Override
	public String call() throws Exception {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(new pngFileFilter());
		int i = jFileChooser.showOpenDialog(null);
		if (i == jFileChooser.APPROVE_OPTION) {
			String path = jFileChooser.getSelectedFile().getAbsolutePath();
			if (!path.endsWith(".png")) {
				return "cof";
			}
			return path;

		} else {
			return "noc";
		}

	}

}

class pngFileFilter extends FileFilter {
	private static final String desc = "½öÏÞÓÚpngÍ¼Ïñ";

	@Override
	public boolean accept(File f) {
		return f.getName().endsWith(".png");
	}

	@Override
	public String getDescription() {
		return desc;
	}

}