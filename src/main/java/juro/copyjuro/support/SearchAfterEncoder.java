package juro.copyjuro.support;

import juro.copyjuro.exception.ErrorCode;
import juro.copyjuro.exception.ServerException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SearchAfterEncoder {
    public static String encode(String... values) {
        if (values == null || values.length == 0) {
            return null;
        }

        String collect = String.join(";", values);
        return Base64.getUrlEncoder().encodeToString(collect.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeSingle(String encoded) {
        String[] decode = decode(encoded);
        if (decode.length != 1) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Invalid encoded string. it must contain one : " + encoded
            );
        }

        return decode[0];
    }

    public static String[] decode(String encoded) {
        byte[] decode = Base64.getUrlDecoder().decode(encoded);
        String collect = new String(decode, StandardCharsets.UTF_8);
        return collect.split(";");
    }
}
