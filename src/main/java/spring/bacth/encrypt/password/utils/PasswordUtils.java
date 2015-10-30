package spring.bacth.encrypt.password.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author JCZOBEIDE
 *
 */
public final class PasswordUtils {

	private static final String ALGORITHM = "SHA-256";

	private static final String ENCODING = "UTF-8";

	private PasswordUtils() {
		super();
	}

	public static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String encrypted = null;
		if (password != null) {
			// In normal case already encrypted in md5
			MessageDigest digester = MessageDigest.getInstance(ALGORITHM);
			for (int i = 0; i < 5; i++) {
				digester.update(password.getBytes(ENCODING));
			}
			encrypted = toHexString(digester.digest());
		}
		return encrypted;
	}

	public static boolean checkPassword(String inputPassword, String hashedPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// In normal case already encrypted in md5
		return hashedPassword.equals(encryptPassword(inputPassword));
	}

	private static String toHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString();
	}

}
