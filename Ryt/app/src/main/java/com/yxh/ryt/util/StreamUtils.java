package com.yxh.ryt.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	public static String StreamToStr(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = is.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		String result = bos.toString();
		is.close();
		bos.close();
		return result;
	}

}
