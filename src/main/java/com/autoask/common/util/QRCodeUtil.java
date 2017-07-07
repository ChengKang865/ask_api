package com.autoask.common.util;

import com.autoask.common.exception.ApiException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

/**
 * Created by hp on 16-9-11.
 * <p>
 * 二维码生成工具
 */
public class QRCodeUtil {
    /**
     * 图片宽
     */
    public static final Integer imageWeight = 200;

    /**
     * 图片高
     */
    public static final Integer imageHeight = 200;

    /**
     * QRCodeCreate(生成二维码)
     *
     * @param content 二维码内容
     * @return BufferImage
     */
    public static BufferedImage createQRCode(String content) throws ApiException {
        //生成二维码
        MultiFormatWriter mfw = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);

        try {
            bitMatrix = mfw.encode(content, BarcodeFormat.QR_CODE, imageWeight, imageHeight, hints);
        } catch (WriterException e) {
            throw new ApiException(e.getMessage());
        }
        assert bitMatrix != null;
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }
}
