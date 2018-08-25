package net.mcbbs.cocoaapi.mod.others;

import java.io.File;
import java.util.concurrent.Callable;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class UpdateImageFileChooser implements Callable<String> {

	@Override
	public String call() throws Exception {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(pngFileFilter);
		int i = jFileChooser.showOpenDialog(null);
		if (i == jFileChooser.APPROVE_OPTION) {
			String f = jFileChooser.getSelectedFile().getAbsolutePath();
			if (!(f.endsWith(".png") || f.endsWith("jpg") || f.endsWith("gif") || f.endsWith("bmp"))) {
				return "cof";
			}
			return f;

		} else {
			return "noc";
		}

	}

    public static final FileFilter pngFileFilter = new FileFilter() {

        private static final String desc = "仅限图片";

        @Override
        public boolean accept(File f) {
            return f.getName().endsWith(".png") || f.getName().endsWith("jpg") || f.getName().endsWith("gif")
                    || f.getName().endsWith("bmp");
        }

        @Override
        public String getDescription() {
            return desc;
        }

    };

<<<<<<< Upstream, based on branch 'master' of https://github.com/MCCocoaUI/CocoaUI-Forge
}
=======
}
>>>>>>> 54f3f08 1
