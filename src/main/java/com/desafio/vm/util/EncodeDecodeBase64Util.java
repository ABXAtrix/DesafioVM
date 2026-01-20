package com.desafio.vm.util;

import java.util.Base64;

public class EncodeDecodeBase64Util {

	public static String decode(String input) {
		String encodedString = "";

		if (!input.isEmpty()) {
			byte[] bytesDecoded = Base64.getDecoder().decode(input);
			encodedString = new String(bytesDecoded);
		}

		return encodedString;
	}

	public static String encode(String input) {
		String decodedString = "";

		if (!input.isEmpty()) {
			decodedString = Base64.getEncoder().encodeToString(input.getBytes());
		}

		return decodedString;
	}

}
