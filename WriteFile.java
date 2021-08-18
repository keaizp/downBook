package com.zp.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteFile {
	private String fileUrl;
	private File file;

	public WriteFile(String fileUrl) {
		super();
		this.fileUrl = fileUrl;
		file = new File(this.fileUrl);
	}

	public void write(String content) {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, true))) {
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			byte[] contentInBytes = content.getBytes();

			bos.write(contentInBytes);
			bos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
