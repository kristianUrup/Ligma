package com.example.ligma.LOGIC;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;


public class Base64Decoder {

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
