package net.mcbbs.cocoaapi.mod.others;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;

import com.google.gson.Gson;

public class ImageUpdater implements Callable<String> {
	private static String BOUNDARY = "----WebKitFormBoundarypAIqI1RWBfPWiOKq";
	private static String WITHPERFIX = "------WebKitFormBoundarypAIqI1RWBfPWiOKq";
	private static String URL = "https://sm.ms/api/upload";
	private File file;
	private String filename;

	public ImageUpdater(File file) {
		this.file = file;
		filename = file.getName();
	}

	@Override
	public String call() throws Exception {
		HttpURLConnection conn = this.getConnection();
		if (conn == null) {
			return "fail";
		}

		this.setHeader(conn);
		OutputStream out = conn.getOutputStream();
		this.writeFileHead(out);
		this.writeFile(out);
		String s = this.getbackurl(conn);
		if (s == null) {
			return "fail";
		}
		conn.disconnect();
		return s;
	}

	public String getbackurl(HttpURLConnection conn) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));) {

			StringBuilder b = new StringBuilder();
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				b.append(line);
			}
			Map<String, Object> back = (Map<String, Object>) new Gson().fromJson(b.toString(), Map.class);
			if (((String) back.get("code")).equals("error")) {
				return (String) back.get("msg");
			} else {
				Map<String, String> data = (Map<String, String>) back.get("data");
				return data.get("url");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setHeader(HttpURLConnection conn) {
		try {
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
	}

	private void writeFileHead(OutputStream out) {
		String s = WITHPERFIX + "\r\n" + "Content-Disposition: form-data; name=\"smfile\"; filename=\"" + this.filename
				+ "\"\r\nContent-Type: image/jpg\r\n\r\n";
		try {
			out.write(s.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(OutputStream out) {
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));) {
			byte[] pic = new byte[in.available()];
			in.read(pic);
			out.write(pic);
			out.write((WITHPERFIX + "\r\nContent-Disposition: form-data; name=\"file_id\"\r\n\r\n0\r\n" + WITHPERFIX
					+ "--").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HttpURLConnection getConnection() {
		try {
			URL url = new URL(URL);
			return (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
