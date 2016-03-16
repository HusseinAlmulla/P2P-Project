package edu.polyu.comp;

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

}
