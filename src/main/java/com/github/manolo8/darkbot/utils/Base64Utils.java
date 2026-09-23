package com.github.manolo8.darkbot.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

public class Base64Utils {

    @Deprecated public static String base64Encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    @Deprecated public static String base64Decode(InputStream input) throws IOException {
        return base64Decode(IOUtils.read(input));
    }

    @Deprecated public static String base64Decode(String text) {
        return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
    }

    public static String decode(InputStream input) throws IOException {
        return decode(IOUtils.read(unwrapGzip(input)));
    }

    public static InputStream decodeStream(InputStream input) {
        try {
            return Base64.getDecoder().wrap(unwrapGzip(input));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Some backpage endpoints (eg: flashAPI/inventory.php) may respond gzip-compressed
     * even without Accept-Encoding, and HttpURLConnection doesn't decompress it.
     * Base64 text never starts with the gzip magic bytes, so it's safe to sniff.
     */
    private static InputStream unwrapGzip(InputStream input) throws IOException {
        InputStream in = input.markSupported() ? input : new BufferedInputStream(input);
        in.mark(2);
        int b1 = in.read(), b2 = in.read();
        in.reset();
        if (b1 == 0x1f && b2 == 0x8b) return new GZIPInputStream(in);
        return in;
    }

    public static String decode(String text) {
        return new String(decodeBytes(text), StandardCharsets.UTF_8);
    }

    public static byte[] decodeBytes(String text) {
        return Base64.getDecoder().decode(text);
    }

    public static String encode(String text) {
        return encodeBytes(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeBytes(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
