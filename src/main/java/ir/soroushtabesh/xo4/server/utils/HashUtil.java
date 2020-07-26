package ir.soroushtabesh.xo4.server.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {
    private HashUtil() {
    }

    public static String hash(String string) {
        return DigestUtils.sha256Hex(string);
    }
}
