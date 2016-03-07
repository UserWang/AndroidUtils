package com.wjd.androidutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * android开发文件系统常用方法
 * 
 *         <p>
 *         本工具类中覆盖的范围为:
 *         </p>
 *         <ul>
 *         <li>按照创建，缩放，将图片保存到SD卡上，翻转,图片处理，转换整理了一些方法</li>
 *         <li>--创建 or 解码--</li>
 *         <li>从Darwable资源目录下加载图片,将本地图片文件解码为图片对象,将输入流解码为图片对象</li>
 *         <li>--缩放--</li>
 *         <li>将原Bitmap缩放到任意指定大小</li>
 *         <li>--翻转--</li>
 *         <li>将原Bitmap上下，左右翻转</li>
 *         <li>--图片处理--</li>
 *         <li>圆角处理，图片灰化，提起图片的Alpha位图</li>
 *         <li>--Bitmap转换--</li>
 *         <li>将Bitmap转换成Byte数组，将Bitmap转换成输入流</li>
 *         <li>--Drawable转换--</li>
 *         <li>将Drawable转换成输入流,将Drawable转换成Bitmap</li>
 *         <li>--合并--</li>
 *         <li>将两张图片按照水平顺序合并为一张，将两个Drawable重叠融合成一个Drawable</li>
 *         </ul>
 * 
 */
public class BitmapHelper {
	/**
	 * 从Darwable资源目录下加载图片
	 * 
	 * @param context
	 *            上下文，用于获取Resource对象
	 * @param resId
	 *            资源对应的ID
	 * @return 获取的Bitmap
	 */
	public static Bitmap getBitmapFromResource(Context context, int resId) {
		Resources resource = context.getResources();

		InputStream is = resource.openRawResource(resId);
		Bitmap bitmap = BitmapFactory.decodeStream(is);

		try {
			resource = null;
			if (is != null) {
				is.close();
				is = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 通过解码本地图片文件得到图片对象
	 * 
	 * @param localImgPath
	 *            本地图片路径
	 * @return 解码之后得到的图片
	 */
	public static Bitmap decodeLocalPicFile(String localImgPath) {
		InputStream is = null;
		try {
			is = new FileInputStream(localImgPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return decodeStream(is, 1);
	}

	/**
	 * 从网络获取图片（注意图片的大小没有做控制，很可能会引发OOM）
	 * 
	 * @param netPicUrl
	 *            图片的网络地址
	 * @return 获取的图片
	 */
	public static Bitmap decodeNetPicFile(String netPicUrl) {
		Bitmap bitmap = null;

		InputStream inputStream = null;
		try {
			inputStream = new java.net.URL(netPicUrl).openStream();
			bitmap = decodeStream(inputStream, 1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * 解码图片流，据说可以避免内存溢出：bitmap size exceeds VM budget
	 * 
	 * @param picInStream
	 *            图片对应的输入流
	 * @param quality
	 *            新图预期大小比例 （要注意新图质量是按照 1/quality 来计算的，eg 1 就是 1/1 = 100%原质 4 就是
	 *            1/4 = 25%就是将图片压缩到25%大小）
	 * @return 解码之后得到的图片
	 */
	public static Bitmap decodeStream(InputStream picInStream, int quality) {
		Bitmap bitmap = null;
		BitmapFactory.Options bitmapOptions = null;
		try {
			bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = quality;

			bitmap = BitmapFactory.decodeStream(picInStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bitmapOptions = null;
		}
		return bitmap;
	}

	/**
	 * 将图片保存到SD卡上
	 * 
	 * @param filename
	 *            本地文件路径
	 * @param bitmap
	 *            待保存的图片
	 * @param format
	 *            待保存的图片格式
	 * @param quality
	 *            图片质量
	 */
	public static synchronized void saveBitmap2SDCard(String filename,
			Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
		File rFile = null; // 自己指定的相对路径
		ByteArrayOutputStream byteArrayOs = null;
		FileOutputStream fileOs = null;

		if (DeviceHelper.isSDCardMounted()) {
			try {
				rFile = DeviceHelper.creatSDCardRootRPath(filename);
				byteArrayOs = new ByteArrayOutputStream();
				bitmap.compress(format, quality, byteArrayOs);
				fileOs = new FileOutputStream(rFile);

				fileOs.write(byteArrayOs.toByteArray());
				fileOs.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fileOs != null) {
						fileOs.close();
						fileOs = null;
					}
					if (byteArrayOs != null) {
						byteArrayOs.close();
						byteArrayOs = null;
					}
					if (rFile != null) {
						rFile = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 图片任意形状的放大缩小,从资源ID读取图片
	 * 
	 * @param context
	 *            上下文，用于从android资源中读取Bitmap
	 * @param resId
	 *            资源ID
	 * @param aimWidth
	 *            预期的宽
	 * @param aimHeight
	 *            预期的高
	 * @return 符合预期宽高的Bitmap
	 */
	public static Bitmap scalePicResource(Context context, int resId,
			int aimWidth, int aimHeight) {

		Bitmap pic1 = BitmapFactory.decodeResource(context.getResources(),
				resId);
		return scaleBitmap(pic1, aimWidth, aimHeight);
	}

	/**
	 * 图片任意形状的放大缩小，原始Bitmap的回收要小心，因为可能返回原Bitmap对象。(会创建一个新的Bitmap，
	 * 除非需要的Bitmap和原Bitmap是一样的且原Bitmap是Immutable) {@link Bitmap.createBitmap}
	 * 
	 * @param oriBitmap
	 * @param aimWidth
	 *            期望的宽
	 * @param aimHeight
	 *            期望的高
	 * @return 缩放之后的Bitmap
	 */
	static public Bitmap scaleBitmap(Bitmap oriBitmap, int aimWidth,
			int aimHeight) {
		Bitmap tempBitmap = null;
		int bitH = oriBitmap.getHeight();
		int bitW = oriBitmap.getWidth();
		Matrix mMatrix = new Matrix();

		float scoleW = (float) aimWidth / (float) bitW;
		float scoleH = (float) aimHeight / (float) bitH;

		mMatrix.reset();
		mMatrix.postScale(scoleW, scoleH);
		tempBitmap = Bitmap.createBitmap(oriBitmap, 0, 0, bitW, bitH, mMatrix,
				true);
		return tempBitmap;

	}

	/**
	 * 翻转图片
	 * 
	 * @param oriBitmap
	 *            原图
	 * @param xflip
	 *            x轴向的翻转 （取值只能是 1 和 -1） eg 1 代表不翻转 -1 代表翻转
	 * @param yflip
	 *            y轴向的翻转 （取值只能是 1 和 -1） eg 1 代表不翻转 -1 代表翻转
	 * @return 翻转之后的图片
	 */
	public static Bitmap getFlipedBitmap(Bitmap oriBitmap, int xflip, int yflip) {
		int width = oriBitmap.getWidth();
		int height = oriBitmap.getHeight();

		Matrix matrix = new Matrix();
		// 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
		matrix.preScale(1, -1);

		Bitmap mInverseBitmap = Bitmap.createBitmap(oriBitmap, 0, 0, width,
				height, matrix, false);
		Bitmap mReflectedBitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);

		// 把新建的位图作为画板
		Canvas mCanvas = new Canvas(mReflectedBitmap);
		// 绘制图片
		mCanvas.drawBitmap(mInverseBitmap, 0, 0, null);

		return mReflectedBitmap;
	}

	/**
	 * 图片圆角处理（注意这个算法可能会导致图片内存翻倍，后期需要测试下）
	 * 
	 * @param oriBitmap
	 *            待处理的图片对象
	 * @param roundPx
	 *            圆角的大小 eg 12.0f
	 * @return 带圆角的图片
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap oriBitmap, float roundPx) {
		Bitmap roundCornerBitmap = null;

		int color = 0;
		Canvas canvas = null;
		Paint paint = null;
		Rect rect = null;
		RectF rectF = null;
		PorterDuffXfermode pdmode = null;

		try {
			// createBitmap() 绝大部分会产生新的图片对象，但是像这个地方和原来一样的图片返回的是原图
			roundCornerBitmap = Bitmap.createBitmap(oriBitmap.getWidth(),
					oriBitmap.getHeight(), Config.ARGB_8888);
			color = 0xff424242;
			canvas = new Canvas(roundCornerBitmap);
			paint = new Paint();
			rect = new Rect(0, 0, oriBitmap.getWidth(), oriBitmap.getHeight());
			rectF = new RectF(rect);
			pdmode = new PorterDuffXfermode(Mode.SRC_IN);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(pdmode);
			canvas.drawBitmap(oriBitmap, rect, rect, paint);
		} catch (Exception e) {
			roundCornerBitmap = null;
		} finally {
			// 回收资源
			if (canvas != null) {
				canvas = null;
			}
			if (paint != null) {
				paint = null;
			}
			if (rect != null) {
				rect = null;
			}
			if (rectF != null) {
				rectF = null;
			}
			if (pdmode != null) {
				pdmode = null;
			}
		}

		return roundCornerBitmap;
	}

	/**
	 * 图片灰化处理
	 * 
	 * @param mBitmap
	 *            原图
	 * @return 灰化处理之后的图
	 */
	public static Bitmap getGrayBitmap(Bitmap mBitmap) {
		Bitmap mGrayBitmap = null;

		NinePatchDrawable drawable = null;
		Canvas mCanvas = null;
		Paint mPaint = null;
		ColorMatrix mColorMatrix = null;
		ColorMatrixColorFilter mColorFilter = null;

		try {
			mGrayBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
					mBitmap.getHeight(), Config.ARGB_8888);
			drawable = new NinePatchDrawable(mGrayBitmap,
					mGrayBitmap.getNinePatchChunk(), new Rect(), null);
			mCanvas = new Canvas(mGrayBitmap);
			mPaint = new Paint();

			// 创建颜色变换矩阵
			mColorMatrix = new ColorMatrix();
			// 设置灰度影响范围
			mColorMatrix.setSaturation(0);
			// 创建颜色过滤矩阵
			mColorFilter = new ColorMatrixColorFilter(mColorMatrix);
			// 设置画笔的颜色过滤矩阵
			mPaint.setColorFilter(mColorFilter);
			// 使用处理后的画笔绘制图像
			mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
		} catch (Exception e) {
			mGrayBitmap = null;
			e.printStackTrace();
		} finally {
			if (drawable != null) {
				drawable = null;
			}
			if (mCanvas != null) {
				mCanvas = null;
			}
			if (mPaint != null) {
				mPaint = null;
			}
			if (mColorMatrix != null) {
				mColorMatrix = null;
			}
			if (mColorFilter != null) {
				mColorFilter = null;
			}
		}

		return mGrayBitmap;
	}

	/**
	 * 提取图像Alpha位图
	 * 
	 * @param oriBitmap
	 *            原图
	 * @param newColor
	 *            新的颜色
	 * @return 处理之后的图
	 */
	public static Bitmap getAlphaBitmap(Bitmap oriBitmap, int newColor) {
		Bitmap mAlphaBitmap = null;

		Canvas mCanvas = null;
		Paint mPaint = null;
		Bitmap alphaBitmap = null;
		try {
			mAlphaBitmap = Bitmap.createBitmap(oriBitmap.getWidth(),
					oriBitmap.getHeight(), Config.ARGB_8888);

			mCanvas = new Canvas(mAlphaBitmap);
			mPaint = new Paint();

			mPaint.setColor(newColor);
			// 从原位图中提取只包含alpha的位图
			alphaBitmap = oriBitmap.extractAlpha();
			// 在画布上（mAlphaBitmap）绘制alpha位图
			mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);
		} catch (Exception e) {
			mAlphaBitmap = null;
			e.printStackTrace();
		} finally {
			if (mCanvas != null) {
				mCanvas = null;
			}
			if (mPaint != null) {
				mPaint = null;
			}
			if (alphaBitmap != null) {
				alphaBitmap = null;
			}
		}

		return mAlphaBitmap;
	}

	/**
	 * 将Bitmap转换成byte数组
	 * 
	 * @param bitmap
	 *            原始的Bitmap
	 * @param format
	 *            预期的格式 eg. Bitmap.CompressFormat.JPEG
	 * @param quality
	 *            一期的图片质量(0-100之间的值) eg. 50
	 * @return byte数组
	 */
	public static byte[] convertBitmapToByteArrayWithSpecify(Bitmap bitmap,
			Bitmap.CompressFormat format, int quality) {
		byte[] result = null;
		ByteArrayOutputStream byteArrayOs = null;
		try {
			// 压缩成指定大小的JPG文件
			byteArrayOs = new ByteArrayOutputStream();
			bitmap.compress(format, quality, byteArrayOs);
			result = byteArrayOs.toByteArray();

			// 释放流资源
			byteArrayOs.close();
		} catch (IOException e) {
			result = null;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将Bitmap转换成InputStream
	 * 
	 * @param oriBitmap
	 *            原图
	 * @param format
	 *            图片格式
	 * @return 将原图转换成的的InputStream
	 */
	public static InputStream convertBitmap2InputStream(Bitmap oriBitmap,
			Bitmap.CompressFormat format) {
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		try {
			baos = new ByteArrayOutputStream();
			oriBitmap.compress(format, 100, baos);
			is = new ByteArrayInputStream(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return is;
	}

	/**
	 * Drawable转换成InputStream
	 * 
	 * @param oriDrawable
	 *            原图
	 * @param format
	 *            图片格式
	 * @return
	 */
	public static InputStream convertDrawable2InputStream(Drawable oriDrawable,
			Bitmap.CompressFormat format) {
		Bitmap bitmap = convertDrawable2Bitmap(oriDrawable);
		return convertBitmap2InputStream(bitmap, format);
	}

	/**
	 * 将Drawable转换成Bitmap
	 * 
	 * @param oriDrawable
	 *            Drawable
	 * @return 通过Drawable得到的Bitmap
	 */
	public static Bitmap convertDrawable2Bitmap(Drawable oriDrawable) {
		if (oriDrawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) oriDrawable).getBitmap();
		} else if (oriDrawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							oriDrawable.getIntrinsicWidth(),
							oriDrawable.getIntrinsicHeight(),
							oriDrawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			oriDrawable.setBounds(0, 0, oriDrawable.getIntrinsicWidth(),
					oriDrawable.getIntrinsicHeight());
			oriDrawable.draw(canvas);
			return bitmap;
		} else if (oriDrawable instanceof StateListDrawable) {
			StateListDrawable stateListDrawable = ((StateListDrawable) oriDrawable);
			Drawable currentDrawable = stateListDrawable.getCurrent();
			Bitmap bitmap = ((BitmapDrawable) currentDrawable).getBitmap();
			return bitmap;
		} else if (oriDrawable instanceof AnimationDrawable) {
			AnimationDrawable animationDrawable = (AnimationDrawable) oriDrawable;
			Drawable currentDrawable = animationDrawable.getCurrent();
			Bitmap bitmap = ((BitmapDrawable) currentDrawable).getBitmap();
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 合并两张图片为一张图片。 按照水平方向顺次排列
	 * 
	 * @param firstBitmap
	 * @param secondBitmap
	 * @return 合并之后的图片
	 */
	public static Bitmap horizentalMergeBitmaps(Bitmap firstBitmap,
			Bitmap secondBitmap) {
		int width = firstBitmap.getWidth() + secondBitmap.getWidth();
		int height = Math
				.max(firstBitmap.getHeight(), secondBitmap.getHeight());
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(firstBitmap, 0, 0, null);
		canvas.drawBitmap(secondBitmap, firstBitmap.getWidth(), 0, null);
		return result;
	}

	/**
	 * 将两个Drawable融合为一个
	 * 
	 * @param frontDrawable
	 *            前景Drawable
	 * @param bgDrawable
	 *            背景Drawable
	 * @return 融合之后的Drawable
	 */
	public static Drawable mergeDrawables(Drawable frontDrawable,
			Drawable bgDrawable) {
		Drawable[] array = new Drawable[2];
		array[0] = bgDrawable;
		array[1] = frontDrawable;
		array[0].setAlpha(40);
		LayerDrawable layoutDrawable = new LayerDrawable(array);
		layoutDrawable.setAlpha(255);
		return layoutDrawable;

	}
}
