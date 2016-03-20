package edu.polyu.comp.util;

import java.io.InputStream;

public class StringUtil {

	// convert request input stream to string
	public static String convertInputStreamToString(InputStream requestBodyStream) throws Exception {
		StringBuffer buffer = new StringBuffer();
		int bufferContent = 0;

		do {
			bufferContent = requestBodyStream.read();
			System.out.println(bufferContent);
			if (bufferContent > 0) {
				buffer.append((char) bufferContent);
			}
		} while (bufferContent > 0);

		return buffer.toString();
	}
	
	public static boolean isEmpty(String s) {
        return (s == null || s.trim().equalsIgnoreCase(""));
    }

    public static String convertNull(String s) {
        return s == null ? "" : s;
    }

}
