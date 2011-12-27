// Copyright (C) 2011 - Will Glozer.  All rights reserved.

package com.lambdaworks.codec.test;

import org.junit.Test;

import java.security.SecureRandom;

import static com.lambdaworks.codec.Base16.decode;
import static com.lambdaworks.codec.Base16.encode;
import static org.junit.Assert.assertArrayEquals;

public class Base16Test {
    @Test
    public void upper() throws Exception {
        assertArrayEquals(chars("1234ABCD"), encode(bytes(0x12, 0x34, 0xab, 0xcd), true));
        assertArrayEquals(chars("567890EF"), encode(bytes(0x56, 0x78, 0x90, 0xef), true));
        assertArrayEquals(bytes(), decode(chars("")));
        assertArrayEquals(bytes(0x12, 0x34, 0xab, 0xcd), decode(chars("1234ABCD")));
        assertArrayEquals(bytes(0x56, 0x78, 0x90, 0xef), decode(chars("567890EF")));
    }

    @Test
    public void lower() throws Exception {
        assertArrayEquals(chars("1234abcd"), encode(bytes(0x12, 0x34, 0xab, 0xcd), false));
        assertArrayEquals(chars("567890ef"), encode(bytes(0x56, 0x78, 0x90, 0xef), false));
        assertArrayEquals(bytes(0x12, 0x34, 0xab, 0xcd), decode(chars("1234abcd")));
        assertArrayEquals(bytes(0x56, 0x78, 0x90, 0xef), decode(chars("567890ef")));
    }

    @Test
    public void random() throws Exception {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

        for (int i = 0; i < 256; i++) {
            byte[] bytes = new byte[i];
            sr.nextBytes(bytes);
            assertArrayEquals(bytes, decode(encode(bytes, true)));
            assertArrayEquals(bytes, decode(encode(bytes, false)));
        }
    }

    @Test
    public void invalid() throws Exception {
        assertArrayEquals(bytes(), decode(chars("1")));
        assertArrayEquals(bytes(0x01), decode(chars("012")));
        assertArrayEquals(bytes(0x00), decode(chars(0x00, 0x01)));
        assertArrayEquals(bytes(0x00), decode(chars(0xff, 0xfe)));
        assertArrayEquals(bytes(0x00), decode(chars(0xffff, 0xfffe)));
    }

    protected byte[] bytes(int... n) {
        byte[] bytes = new byte[n.length];
        for (int i = 0; i < n.length; i++) {
            bytes[i] = (byte) n[i];
        }
        return bytes;
    }

    protected char[] chars(int... n) {
        char[] chars = new char[n.length];
        for (int i = 0; i < n.length; i++) {
            chars[i] = (char) n[i];
        }
        return chars;
    }

    protected char[] chars(String s) {
        return s.toCharArray();
    }
}
