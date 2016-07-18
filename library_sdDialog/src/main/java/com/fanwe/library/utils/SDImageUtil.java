package com.fanwe.library.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

public class SDImageUtil
{

	public static Bitmap drawable2Bitmap(Drawable drawable)
	{

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static byte[] Bitmap2Bytes(Bitmap bitmap)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap bytes2Bimap(byte[] b)
	{
		if (b != null && b.length != 0)
		{
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else
		{
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static Drawable Bitmap2Drawable(Bitmap bitmap)
	{
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		return bd;
	}

	@SuppressWarnings("deprecation")
	public static String getImageFilePathFromIntent(Intent intent, Activity activity)
	{
		if (intent != null && activity != null)
		{
			Uri uri = intent.getData();
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index);
			return path;
		} else
		{
			return null;
		}
	}

	public static boolean compressImageFileToNewFileSize(File oldFile, File newFile, long finalSize)
	{
		int quality = 100;
		boolean result = false;
		compressImageFileToNewFile(oldFile, newFile, quality);
		while (true)
		{
			long length = newFile.length();
			if (length > finalSize)
			{
				quality = quality - 1;
				if (quality <= 0)
				{
					quality = 1;
				}
				compressImageFileToNewFile(oldFile, newFile, quality);
				if (quality <= 1)
				{
					break;
				}
			} else
			{
				result = true;
				break;
			}
		}
		return result;
	}

	
	 public static Bitmap getResizeBitmap(String path, float maximumMB) {	      

	        float maximumSize = maximumMB * 1024 * 1024 / 5;

	        BitmapFactory.Options opts = new BitmapFactory.Options();
	        opts.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(path, opts);

	        int srcSize = opts.outHeight * opts.outWidth;
	        float rate = srcSize / maximumSize;

	        int sampleSize = 1;
	        if (rate > 0) {
	            float avgRate = rate / 2;
	            if (avgRate <= 2) {
	                sampleSize = 2;
	            } else if (avgRate <= 4) {
	                sampleSize = 4;
	            } else if (avgRate <= 8) {
	                sampleSize = 8;
	            } else {
	                sampleSize = 16;
	            }
	        }

	        opts.inJustDecodeBounds = false;
	        opts.inSampleSize = sampleSize;

	        return BitmapFactory.decodeFile(path, opts);

	    }

	public static boolean compressImageFileToNewFile(File oldFile, File newFile, int quality)
	{
		if (oldFile != null && newFile != null && oldFile.exists())
		{
			try
			{
				if (newFile.exists())
				{
					newFile.delete();
				}
				newFile.createNewFile();

				if (newFile.exists())
				{
					BitmapSize maxSize = new BitmapSize(SDViewUtil.getScreenWidth(), SDViewUtil.getScreenHeight());	
					
					Bitmap bmpOld = BitmapDecoder.decodeSampledBitmapFromFile(oldFile.getAbsolutePath(), maxSize, null);
					FileOutputStream fos = new FileOutputStream(newFile);
					bmpOld.compress(Bitmap.CompressFormat.JPEG, quality, fos);
					fos.close();
					return true;
				}
			} catch (Exception e)
			{
				return false;
			}
		}
		return false;
	}

	public static Bitmap getGrayBitmap(Bitmap bmp)
	{
		Bitmap bmpGray = null;
		if (bmp != null)
		{
			int width = bmp.getWidth();
			int height = bmp.getHeight();

			bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			Canvas c = new Canvas(bmpGray);
			Paint paint = new Paint();
			ColorMatrix cm = new ColorMatrix();
			cm.setSaturation(0);
			ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
			paint.setColorFilter(f);
			c.drawBitmap(bmp, 0, 0, paint);
		}
		return bmpGray;
	}

}
