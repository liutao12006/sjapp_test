package wbkj.sjapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by jianghan on 16/7/28.
 */
public class Base64Util {

    private int screenWidth;
    private int screenHeight;

    public Base64Util(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = manager.getDefaultDisplay().getWidth();
        screenHeight = manager.getDefaultDisplay().getHeight();
    }

    /**
     * 获取一个按屏幕比例压缩过后的bitmap
     *
     * @param imageFilePath 图片路径
     * @return bitmap
     */
    public Bitmap getCompressBitmap(String imageFilePath) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

        // 去获得图片的附属信息，比如宽高
        BitmapFactory.decodeFile(imageFilePath, opts);

        // 为了计算压缩比例，最好用手机屏幕的宽高去除，得到一个比例值

        int width = opts.outWidth;
        int height = opts.outHeight;

        int widthScale = width / (screenWidth / 3);
        int heiScale = height / (screenHeight / 3);

        // 取比例大的那个数
        int scale = widthScale > heiScale ? widthScale : heiScale;

        opts.inSampleSize = scale;  //
        Log.e("inSampleSize::", scale + "");
        // 将这个开关关闭，真正的加载到内存中
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        // 这次才拿到图片，但是比例是原先的 widthScale这个倍数
        return BitmapFactory.decodeFile(imageFilePath, opts);
    }

    /**
     * bitmap转base64编码
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @Descriptionmap 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @author temdy
     * @Date 2015-01-26
     * @param path 图片路径
     * @return
     */
    public static String imageToBase64(String path) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * @Descriptionmap 对字节数组字符串进行Base64解码并生成图片
     * @author temdy
     * @Date 2015-01-26
     * @param base64 图片Base64数据
     * @param path 图片路径
     * @return
     */
    public static boolean base64ToImage(String base64, String path) {// 对字节数组字符串进行Base64解码并生成图片
        if (base64 == null){ // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
