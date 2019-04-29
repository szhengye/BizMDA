package com.bizmda.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

public class GenUtils {
	public static List<File> getFileList(String strPath) {
		List<File> filelist = new ArrayList<File>();
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (fileName.endsWith("yml")) { // 判断文件名是否以.yml结尾
                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println("---" + strFileName);
                    filelist.add(files[i]);
                } else {
                    continue;
                }
            }

        }
 //       System.out.println("a:"+filelist.size());
        return filelist;
    }
	
	public static String camelName(String name) {
		return WordUtils.capitalizeFully(name, new char[]{'_','-'}).replace("_", "" ).replace("-", "" );
//	    StringBuilder result = new StringBuilder();
//	    // 快速检查
//	    if (name == null || name.isEmpty()) {
//	        // 没必要转换
//	        return "";
//	    } else if (!name.contains("_")) {
//	        // 不含下划线，仅将首字母小写
//	        return name.substring(0, 1).toLowerCase() + name.substring(1);
//	    }
//	    // 用下划线将原始字符串分割
//	    String camels[] = name.split("_");
//	    for (String camel :  camels) {
//	        // 跳过原始字符串中开头、结尾的下换线或双重下划线
//	        if (camel.isEmpty()) {
//	            continue;
//	        }
//	        // 处理真正的驼峰片段
//	        if (result.length() == 0) {
//	            // 第一个驼峰片段，全部字母都小写
//	            result.append(camel.toLowerCase());
//	        } else {
//	            // 其他的驼峰片段，首字母大写
//	            result.append(camel.substring(0, 1).toUpperCase());
//	            result.append(camel.substring(1).toLowerCase());
//	        }
//	    }
//	    return result.toString();
	}
	
	public static void mkdir(String fileName) {
		int index = Math.max(fileName.lastIndexOf('/'),fileName.lastIndexOf('\\'));
		if (index <= 0)
			return;
        File directory = new File(fileName.substring(0, index));  
        directory.mkdirs();  
	}
}
