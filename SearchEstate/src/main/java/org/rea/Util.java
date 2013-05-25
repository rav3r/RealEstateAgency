package org.rea;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.logging.Logger;


public class Util {
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			md.update(str.getBytes(), 0, str.getBytes().length);
			BigInteger zwr = new BigInteger(1, md.digest());
			return String.format("%1$032X", zwr);
		} catch (Exception e) {
			Logger.getLogger("HelloWorld").info("Util.md5 method exception: " + e.toString());
		}
		return null;
	}

}
