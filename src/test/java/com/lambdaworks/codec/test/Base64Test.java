// Copyright (C) 2011 - Will Glozer.  All rights reserved.

package com.lambdaworks.codec.test;

import com.lambdaworks.codec.Base64Url;
import org.junit.Test;

import java.security.SecureRandom;

import static com.lambdaworks.codec.Base64.decode;
import static com.lambdaworks.codec.Base64.encode;
import static org.junit.Assert.assertArrayEquals;

public class Base64Test {
    @Test
    public void rfc4648() throws Exception {
        assertArrayEquals(chars(""),         encode(bytes("")));
        assertArrayEquals(chars("Zg=="),     encode(bytes("f")));
        assertArrayEquals(chars("Zm8="),     encode(bytes("fo")));
        assertArrayEquals(chars("Zm9v"),     encode(bytes("foo")));
        assertArrayEquals(chars("Zm9vYg=="), encode(bytes("foob")));
        assertArrayEquals(chars("Zm9vYmE="), encode(bytes("fooba")));
        assertArrayEquals(chars("Zm9vYmFy"), encode(bytes("foobar")));

        assertArrayEquals(bytes(""),       decode(chars("")));
        assertArrayEquals(bytes("f"),      decode(chars("Zg==")));
        assertArrayEquals(bytes("fo"),     decode(chars("Zm8=")));
        assertArrayEquals(bytes("foo"),    decode(chars("Zm9v")));
        assertArrayEquals(bytes("foob"),   decode(chars("Zm9vYg==")));
        assertArrayEquals(bytes("fooba"),  decode(chars("Zm9vYmE=")));
        assertArrayEquals(bytes("foobar"), decode(chars("Zm9vYmFy")));
    }

    @Test
    public void rfc4648_no_pad() throws Exception {
        assertArrayEquals(chars(""),         encode(bytes(""),       false));
        assertArrayEquals(chars("Zg"),       encode(bytes("f"),      false));
        assertArrayEquals(chars("Zm8"),      encode(bytes("fo"),     false));
        assertArrayEquals(chars("Zm9v"),     encode(bytes("foo"),    false));
        assertArrayEquals(chars("Zm9vYg"),   encode(bytes("foob"),   false));
        assertArrayEquals(chars("Zm9vYmE"),  encode(bytes("fooba"),  false));
        assertArrayEquals(chars("Zm9vYmFy"), encode(bytes("foobar"), false));

        assertArrayEquals(bytes(""),       decode(chars("")));
        assertArrayEquals(bytes("f"),      decode(chars("Zg")));
        assertArrayEquals(bytes("fo"),     decode(chars("Zm8")));
        assertArrayEquals(bytes("foo"),    decode(chars("Zm9v")));
        assertArrayEquals(bytes("foob"),   decode(chars("Zm9vYg")));
        assertArrayEquals(bytes("fooba"),  decode(chars("Zm9vYmE")));
        assertArrayEquals(bytes("foobar"), decode(chars("Zm9vYmFy")));
    }

    @Test
    public void random() throws Exception {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        for (int i = 0; i < 256; i++) {
            byte[] bytes = new byte[i];
            sr.nextBytes(bytes);

            byte[] bcOut = org.bouncycastle.util.encoders.Base64.encode(bytes);
            char[] bcChars = new char[bcOut.length];
            for (int j = 0; j < bcOut.length; j++) bcChars[j] = (char) bcOut[j];

            assertArrayEquals(bcChars, encode(bytes, true));
            assertArrayEquals(bytes, decode(bcChars));
        }
    }

    @Test
    public void url_safe() throws Exception {
        byte[] bytes = { (byte) 0x2a, (byte) 0xfe, (byte) 0xff, (byte) 0xfa };
        assertArrayEquals(chars("Kv7/+g=="), encode(bytes));
        assertArrayEquals(chars("Kv7_-g.."), Base64Url.encode(bytes));
        assertArrayEquals(chars("Kv7_-g.."), Base64Url.encode(bytes, true));
        assertArrayEquals(chars("Kv7_-g"),   Base64Url.encode(bytes, false));
        assertArrayEquals(bytes, decode(encode(bytes)));
        assertArrayEquals(bytes, Base64Url.decode(Base64Url.encode(bytes)));
        assertArrayEquals(bytes, Base64Url.decode(Base64Url.encode(bytes, false)));
    }

    protected byte[] bytes(String s) throws Exception {
        return s.getBytes("ASCII");
    }

    protected char[] chars(String s) throws Exception {
        return s.toCharArray();
    }
}
