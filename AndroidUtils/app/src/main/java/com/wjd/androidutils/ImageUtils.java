package com.wjd.androidutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static final String APP_DIRECTORY_NAME = ".androidutils_img";
    public static final String APP_DIRECTORY_NAME_SHOW = "A_Photos";
    public static final String ATTACHMENT_DIRTORY_NAME = "attachment";
    public static final String APK_DIRCTORY_NAME = "apk";

    /**
     * 通过目录名取得目录file
     *
     * @param name
     * @return
     */
    public static File getDirctoryByName(String name) {
        if (name == null)
            return null;
        File dir = new File(Environment.getExternalStorageDirectory(),
                APP_DIRECTORY_NAME);
        if (dir != null && !dir.exists()) {
            dir = createDirectory(Environment.getExternalStorageDirectory(),
                    APP_DIRECTORY_NAME);
        }
        if (dir != null && dir.exists()) {
            dir = new File(dir, name);
            if (dir != null && !dir.exists())
                dir.mkdirs();
            if (dir != null && dir.exists())
                return dir;
        }
        return null;
    }

    /**
     * 创建目录
     *
     * @param parent
     * @param directoryName
     * @return
     */
    public static File createDirectory(File parent, String directoryName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (parent == null || directoryName == null)
                return null;
            File file = new File(parent, directoryName);
            if (!file.exists())
                file.mkdirs();
            try {
                new File(file, ".Nomedia").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }

    /**
     * 将bitmap保存到sdcard上
     *
     * @param bmp
     * @param diretoryName 保存目录
     * @param name         文件名
     */
    public static void bitmap2File(Bitmap bmp, String diretoryName, String name) {
        if (bmp == null || diretoryName == null || name == null)
            return;
        File dir = createDirectory(Environment.getExternalStorageDirectory(),
                APP_DIRECTORY_NAME);
        if (dir != null) {
            dir = createDirectory(dir, ATTACHMENT_DIRTORY_NAME);
            if (dir != null && dir.exists()) {
                File bitmapFile = new File(dir, name);
                if (!bitmapFile.exists()) {
                    try {
                        bitmapFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(bitmapFile);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 将bitmap保存到sdcard上
     *
     * @param bmp
     * @param diretoryName 保存目录
     * @param name         文件名
     */
    public static File bitmapToFile(Bitmap bmp, String diretoryName, String name) {
        if (bmp == null || diretoryName == null || name == null)
            return null;
        File bitmapFile = null;
        File dir = createDirectory(Environment.getExternalStorageDirectory(),
                APP_DIRECTORY_NAME);
        if (dir != null) {
            dir = createDirectory(dir, ATTACHMENT_DIRTORY_NAME);
            if (dir != null && dir.exists()) {
                bitmapFile = new File(dir, name);
                if (!bitmapFile.exists()) {
                    try {
                        bitmapFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(bitmapFile);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bitmapFile;
    }

    /**
     * 将bitmap保存到sdcard上（可见文件夹）
     *
     * @param bmp
     * @param diretoryName 保存目录
     * @param name         文件名
     */
    public static File bitmapToFileIsShow(Bitmap bmp, String diretoryName, String name) {
        if (bmp == null || diretoryName == null || name == null)
            return null;
        File bitmapFile = null;
        File dir = createDirectory(Environment.getExternalStorageDirectory(),
                APP_DIRECTORY_NAME_SHOW);
//        if (dir != null) {
//            dir = createDirectory(dir, ATTACHMENT_DIRTORY_NAME);
        if (dir != null && dir.exists()) {
            bitmapFile = new File(dir, name);
            if (!bitmapFile.exists()) {
                try {
                    bitmapFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(bitmapFile);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            }
        }
        return bitmapFile;
    }

    /**
     * 从缓存取图片
     *
     * @param fileName
     * @return
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable file2BitmapDrawable(String fileName) {
        if (fileName == null)
            return null;
        File attachmentDir = getDirctoryByName(ATTACHMENT_DIRTORY_NAME);
        if (attachmentDir != null) {
            File imageFile = new File(attachmentDir, fileName);
            if (imageFile.exists())
                return new BitmapDrawable(BitmapFactory.decodeFile(imageFile
                        .getAbsolutePath()));
        }
        return null;
    }

    /**
     * 从缓存取图片
     *
     * @param fileName
     * @return
     */
    public static Bitmap file2Bitmap(String fileName) {
        if (fileName == null)
            return null;
        File attachmentDir = getDirctoryByName(ATTACHMENT_DIRTORY_NAME);
        if (attachmentDir != null) {
            File imageFile = new File(attachmentDir, fileName);
            if (imageFile.exists())
                return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }
        return null;
    }

    /**
     * 将图片resize成side*side里盛放的大小，不拉伸
     *
     * @param side
     * @return
     */
    public static Bitmap resizeToFitSquare(Bitmap resBitmap, float side) {
        if (resBitmap == null)
            return null;
        float scale = Math.min(side / resBitmap.getWidth(),
                side / resBitmap.getHeight());
        // 如果是放大(原图比square小), 则直接返回原图
        if (scale >= 1)
            return resBitmap;
        return Bitmap.createScaledBitmap(resBitmap,
                (int) (resBitmap.getWidth() * scale),
                (int) (resBitmap.getHeight() * scale), false);
    }

    /**
     * 保持长宽比缩小Bitmap
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null)
            return null;
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        // no need to resize
        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }

        int width = originWidth;
        int height = originHeight;

        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth) {
            width = maxWidth;

            double i = originWidth * 1.0 / maxWidth;
            height = (int) Math.floor(originHeight / i);

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }

        // 若图片过长, 则从上端截取
        if (height > maxHeight) {
            height = maxHeight;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        }
        return bitmap;
    }

    /**
     * 取图片中最大的正方形部分
     *
     * @return
     */
    public static Bitmap clipToSquare(Bitmap resBitmap) {
        if (resBitmap == null)
            return null;
        float side = Math.min(resBitmap.getWidth(), resBitmap.getHeight());
        return Bitmap.createBitmap(resBitmap,
                (int) ((resBitmap.getWidth() - side) / 2),
                (int) ((resBitmap.getHeight() - side) / 2), (int) side,
                (int) side);
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // 按比例大小压缩图片（根据路径获取图片并压缩）
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    // 按比例大小压缩图片（根据Bitmap图片压缩）
    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 85, baos);// 这里压缩50%，把压缩后的数据存放到baos中

            System.out.println("compress: 75");

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        LogUtils.e("compress", "" + bitmap.getRowBytes());

        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap decodeBitmap(String srcPath, float width) {
        return decodeBitmap(srcPath, width, width);
    }

    public static Bitmap decodeBitmap(String srcPath, float width, float height) {
        if (srcPath == null)
            return null;
        // //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        width = width == 0 ? 480f : width; // 这里设置宽度为480f
        height = height == 0 ? 800f : height; // 这里设置高度为800f
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / width);
        } else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 获取压缩后的图片
     *
     * @param res
     * @param resId
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // 首先不加载图片,仅获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true;
        // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
        BitmapFactory.decodeResource(res, resId, options);

        // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
        options.inJustDecodeBounds = false;
        // 利用计算的比例值获取压缩后的图片对象
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                                     int reqHeight) {

        // 首先不加载图片,仅获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true;
        // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
        BitmapFactory.decodeFile(path, options);
        // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
        options.inJustDecodeBounds = false;
        // 利用计算的比例值获取压缩后的图片对象
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 计算压缩比例值
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // 保存图片原宽高值
        final int height = options.outHeight;
        final int width = options.outWidth;
        // 初始化压缩比例为1
        int inSampleSize = 1;

        // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;// 半径
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public static Bitmap toRoundCornerBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final RectF dst = new RectF((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(dst, 15.0f, 15.0f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public static Bitmap toRectBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRect(dst, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public static Bitmap rotateBitmap(Bitmap bp) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(90);
        Bitmap nowBp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(),
                bp.getHeight(), matrix, true);
        if (bp.isRecycled()) {
            bp.recycle();
        }
        return nowBp;
    }

}
