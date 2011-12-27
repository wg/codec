// Copyright (C) 2011 - Will Glozer.  All rights reserved.

package com.lambdaworks.codec;

import java.util.Arrays;

/**
 * URL-safe {@link Base64} variant using {@code '-_'} instead of {@code '+/'}
 * and padding with {@code '.'}.
 *
 * @author Will Glozer
 */
public class Base64Url extends Base64 {
    private static final char[] encode = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
    private static final int[]  decode = new int[128];
    private static final char   pad    = '.';

    static {
        Arrays.fill(decode, -1);
        for (int i = 0; i < encode.length; i++) {
            decode[encode[i]] = i;
        }
        decode[pad] = 0;
    }

    /**
     * Decode url-safe base64 chars to bytes.
     *
     * @param chars Chars to encode.
     *
     * @return Decoded bytes.
     */
    public static byte[] decode(char[] chars) {
        return decode(chars, decode, pad);
    }

    /**
     * Encode bytes to url-safe base64 chars, with padding.
     *
     * @param bytes Bytes to encode.
     *
     * @return Encoded chars.
     */
    public static char[] encode(byte[] bytes) {
        return encode(bytes, encode, pad);
    }

    /**
     * Encode bytes to url-safe base64 chars, with optional padding.
     *
     * @param bytes     Bytes to encode.
     * @param padded    Add padding to output.
     *
     * @return Encoded chars.
     */
    public static char[] encode(byte[] bytes, boolean padded) {
        return encode(bytes, encode, padded ? pad : 0);
    }
}
