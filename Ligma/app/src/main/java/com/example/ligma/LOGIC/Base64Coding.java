package com.example.ligma.LOGIC;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;


public class Base64Coding {

    /**
     * Compresses image and returns Base64 encoded to string.
     * @param image The image getting encoded
     * @param compressFormat The compressed format
     * @param quality Quality of the compression
     * @return The base64 encoding as a string
     */
    private static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Turns image bitmap into string, using encodeToBase64.
     * @param bitmap The bitmap to encode
     * @return string The bitmap encoded to a string
     */
    public static String imageToString(Bitmap bitmap) {
        String encodedImage = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
        return encodedImage;
    }

    /**
     * Decodes string into bitmap
     * @param stringImage The image string to decode
     * @return bitmap the image string decoded as a bitmap
     */
    public static Bitmap decodeToBitmap(String stringImage){
        byte[] decodedBytes = Base64.decode(stringImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bitmap;
    }


}
