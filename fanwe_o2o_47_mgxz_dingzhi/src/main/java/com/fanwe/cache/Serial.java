package com.fanwe.cache;

import android.content.Context;

import com.fanwe.app.App;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 
 * ClassName: Serial <br/>
 * Function: 序列化存储工具类 <br/>
 * Reason: TODO <br/>
 * date: 2015-4-25 上午11:24:39
 * 
 * @author 08861
 * @version
 * @since JDK 1.6
 */
public class Serial implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Context cxt = App.getApplication();

	/**
	 * 通过fileName缓存对象
	 * 
	 * @param <T>
	 * 
	 * @param fileName
	 * @param object
	 * @Description:
	 * @Author:08861
	 * @Since:2015年2月4日
	 */
	public static <T> void saveObjectByFile(String fileName, T object) {
		// 这里的Context对象，主要是下面打开文件时用到
		// save的时候，list对象是一个全局变量，在其他方法中，对list对象做出更改，调用
		// 这边的save方法进行保存
		try {
			FileOutputStream fs = cxt.openFileOutput(fileName,
					cxt.MODE_WORLD_READABLE + cxt.MODE_WORLD_READABLE);
			// ser为序列化专用文件类型
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(object);
			os.flush();
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 通过fileName读取对象
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 * @Description:
	 * @Author:08861
	 * @Since:2015年2月4日
	 */
	public static <T> T readObjectByFile(String fileName) throws Exception {
		T object = null;
		FileInputStream fs = cxt.openFileInput(fileName);
		ObjectInputStream ois = new ObjectInputStream(fs);
		object = (T) ois.readObject(); // 从文件中读取对象
		ois.close();
		return object;
	}

}