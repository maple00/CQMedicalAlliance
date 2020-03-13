package com.rainwood.scantools.encode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class CodeCreator {

    /**
     * 创建二维码
     *
     * @param url 内容
     * @return
     */
    public static Bitmap createQRCode(String url) {
        return createQRCode(url, 300, 300);
    }


    /**
     * 生成QRCode（二维码）
     *
     * @param url      内容
     * @param qrWidth  宽
     * @param qrHeight 搞
     * @return
     */
    public static Bitmap createQRCode(String url, int qrWidth, int qrHeight) {
        if (url == null || url.equals("")) {
            return null;
        }
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
