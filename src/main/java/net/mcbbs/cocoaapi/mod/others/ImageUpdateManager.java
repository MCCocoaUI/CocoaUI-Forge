package net.mcbbs.cocoaapi.mod.others;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageUpdateManager {
	public static ImageUpdateManager INSTANCE = new ImageUpdateManager();
	ExecutorService threadPool = Executors.newSingleThreadExecutor();

	public Future<String> readyChooseFile() {
		return this.threadPool.submit(new UpdateImageFileChooser());
	}

	public Future<String> postFile(File file) {
		return this.threadPool.submit(new ImageUpdater(file));
	}
}
