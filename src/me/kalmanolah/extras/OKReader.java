package me.kalmanolah.extras;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OKReader {
	public static String main(String aString) throws IOException {
		URL url = new URL(aString);
		URLConnection con = url.openConnection();
		Pattern p = Pattern.compile("text/plain;\\s+charset=([^\\s]+)\\s*");
		Matcher m = p.matcher(con.getContentType());
		String charset = m.matches() ? m.group(1) : "ISO-8859-1";
		Reader r = new InputStreamReader(con.getInputStream(), charset);
		StringBuilder buf = new StringBuilder();
		while (true) {
			int ch = r.read();
			if (ch < 0) {
				break;
			}
			buf.append((char) ch);
		}
		String str = buf.toString();
		return str;
	}
}