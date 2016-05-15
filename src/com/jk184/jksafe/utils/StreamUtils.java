package com.jk184.jksafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取流的工具
 * 
 * @author 宁云
 * 
 */
public class StreamUtils {

	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readFormStream(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}

		String result = out.toString();
		in.close();
		out.close();

		return result;
	}
}
