package com.cisoft.shop.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件操作类
 * */
public class FileHelper {
	
	/**
	 * 存储目录的根目录，
	 * 当有SD卡的时候，目录是/storage/sdcard0/
	 * 当没有SD卡点时候，目录是/data/
	 * */
	private File root = null;
	
	/**
	 * 组织（公司）目录名
	 * */
	private String orgRootDirName = "cisoft";
	
	/**
	 * 应用目录名
	 * */
	private String appRootDirName = "lazyorder";

	//APK存储目录
	private String apkDirName = "apk";
	private File apkDir = null;
	/**
	 * 应用目录
	 * */
	private File appRoot = null;
	
	/**
	 * 唯一实例
	 * */
	private static FileHelper instance = null;
	
	private FileHelper() {
		
	}
	
	/**
	 * 获取本类唯一的实例
	 * */
	public static FileHelper getInstance() {
		if (instance == null) {
			instance = new FileHelper();
		}
		return instance;
	}
	

	/**
	 * 初始化文件系统，在可用的存储器里创建该应用的目录，优先在SD卡中创建目录
	 * */
	public void init() {
		if (haveSdcard()) {
			root = Environment.getExternalStorageDirectory();
		} else {
			root = Environment.getDataDirectory();
		}
		String appRootPath = root.getAbsolutePath() + File.separator + orgRootDirName + File.separator + appRootDirName;
		appRoot = new File(appRootPath);
		
		createDir(appRoot);

		String apkPath = appRoot.getAbsolutePath() + File.separator + apkDirName;
		apkDir = new File(apkPath);
		createDir(apkDir);
	}
	
	/**
	 * 判断是否有sd卡
	 * @return boolean 有SD卡返回true，否则返回false
	 * */
	public boolean haveSdcard() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 创建目录
	 * @param dirPath 待创建的目录的绝对路径
	 *
	 * */
	private void createDir(String dirPath) {
		File f = new File(dirPath);
		createDir(f);
	}
	
	/**
	 * 创建目录
	 * @param dir 带创建的目录对象
	 * */
	private void createDir(File dir) {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {

			}
		}
	}
	
	/**
	 * 向目标文件写字符串数据
	 * @param content 需要写入的数据
	 * @param file 目标文件
	 * */
	private void writeStringToFile(String content, File file) {
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从目标文件获取字符串数据
	 * @param file 目标文件
	 * @return String 文件中的字符串数据
	 * @throws java.io.FileNotFoundException
	 * */
	private String getStringFromFile(File file) throws FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		String content = "";
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				content = content + line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * 获取apk存储目录
	 * */
	public File getApkDir() {
		return apkDir;
	}
}
