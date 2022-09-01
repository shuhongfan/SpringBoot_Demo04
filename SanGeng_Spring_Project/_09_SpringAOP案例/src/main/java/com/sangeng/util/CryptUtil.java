package com.sangeng.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class CryptUtil {
    private static final String AES = "AES";

    private static int keysizeAES = 128;

    private static String charset = "utf-8";

    public static String parseByte2HexStr(final byte buf[]) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(final String hexStr) {
        if (hexStr.length() < 1)
            return null;
        final byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    private static String keyGeneratorES(final String res, final String algorithm, final String key, final Integer keysize, final Boolean bEncode) {
        try {
            final KeyGenerator g = KeyGenerator.getInstance(algorithm);
            if (keysize == 0) {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                g.init(new SecureRandom(keyBytes));
            } else if (key == null) {
                g.init(keysize);
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(keyBytes);
                g.init(keysize, random);
            }
            final SecretKey sk = g.generateKey();
            final SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            final Cipher cipher = Cipher.getInstance(algorithm);
            if (bEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                final byte[] resBytes = charset == null? res.getBytes() : res.getBytes(charset);
                return parseByte2HexStr(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                return new String(cipher.doFinal(parseHexStr2Byte(res)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String AESencode(final String res) {
        return keyGeneratorES(res, AES, "aA11*-%", keysizeAES, true);
    }

    public static String AESdecode(final String res) {
        return keyGeneratorES(res, AES, "aA11*-%", keysizeAES, false);
    }

    public static void main(String[] args) {
        System.out.println(
                "加密后:" + AESencode("你三连了吗？")
        );
        System.out.println(
                "解密后:" + AESdecode("02C1F5B472FD65E61A8BE88194DBEAC102D8875DAC9633CFA0B068CA63B32303")
        );
    }
}