package com.store.api.utils;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.StringUtils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class UploadUtils {
    private static final Logger LOG = LoggerFactory.getLogger(UploadUtils.class);

	    /** 
	     *  
	     * @param fullPath  
	     *            接收到的图片保存路径 
	     * @param  file
	     * 			  MultipartFile 类型    
	     * @param getUploadContentType 
	     *            原文件类型 
	     * @param getUploadFileName 
	     *            原文件名 
	     * @return 
	     * @throws IOException 
	     */  
	    public static void uploadImage1(String fullPath,File file, String getUploadContentType, String getUploadFileName) throws IOException  
	    {  
	        // 得到上传文件的后缀名  
	        String uploadName = getUploadContentType;  
	        //后缀名
	        String lastuploadName = uploadName.substring(uploadName.indexOf("/") + 1, uploadName.length());  
	  
	        // 得到文件的新名字  
	        String fileNewName = generateFileName(getUploadFileName);  
	  
	        // 最后返回图片路径   还是原文件的路径 名字更改
	        String  imagePath = fullPath + "/" + fileNewName;  
	  
	        BufferedImage srcBufferImage =  javax.imageio.ImageIO.read(new FileInputStream(file));  
	        BufferedImage scaledImage;  
	        ScaleImage scaleImage = ScaleImage.getInstance();  
	        int yw = srcBufferImage.getWidth();  
	        int yh = srcBufferImage.getHeight();  
	        int w = 80, h = 80;  
	        // 如果上传图片 宽高 比 压缩的要小 则不压缩  
	        if (w > yw && h > yh)  
	        {  
	            FileOutputStream fos = new FileOutputStream(fullPath + "/" + fileNewName);  
	  
	            FileInputStream fis = new FileInputStream(file);  
	            byte[] buffer = new byte[1024];  
	            int len = 0;  
	            while ((len = fis.read(buffer)) > 0)  
	            {  
	                fos.write(buffer, 0, len);  
	            }  
	        }  
	        else  
	        {  
	            scaledImage = scaleImage.imageZoomOut(srcBufferImage, w, h);  
	            FileOutputStream out = new FileOutputStream(fullPath + "/" + fileNewName);  
	            ImageIO.write(scaledImage, "jpeg", out);  
	  
	        }  
	    }  
	  
	    /** 
	     * 传入原图名称，，获得一个以时间格式的新名称 
	     *  
	     * @param fileName 
	     *            　原图名称 
	     * @return 
	     */  
	    public static String generateFileName(String fileName)  
	    {  
	        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");  
	        String formatDate = format.format(new Date());  
	        int random = new Random().nextInt(10000);  
	        int position = fileName.lastIndexOf(".");  
	        String extension = fileName.substring(position);  
	        return formatDate + random + extension;  
	    }  
	  
	    /**
	     * 把图片印刷到图片上
	     * 
	     * @param pressImg --
	     *            水印文件
	     * @param targetImg --
	     *            目标文件
	     * @param position
	     * 			     水印位置
	     */
	    public final static void pressImage(String pressImg, String targetImg,int position) {
	        try {
	        	
	        	
	            //目标文件
	            File _file = new File(targetImg);
	            Image src = ImageIO.read(_file);
	            int wideth = src.getWidth(null);
	            int height = src.getHeight(null);
	            BufferedImage image = new BufferedImage(wideth, height,
	                    BufferedImage.TYPE_INT_RGB);
	            Graphics g = image.createGraphics();
	            g.drawImage(src, 0, 0, wideth, height, null);

	            //水印文件
	            File _filebiao = new File(pressImg);
	            Image src_biao = ImageIO.read(_filebiao);
	            int wideth_biao = src_biao.getWidth(null);
	            int height_biao = src_biao.getHeight(null);
	            int pos_x = 0;
	            int pos_y = 0;
	            switch(position){
	    		case 1:
	    			pos_x = wideth - wideth_biao;
	    			pos_y = height - height_biao;
	    			break;
	    		case 2:
	    			pos_x = 0;
	    			pos_y = height - height_biao;
	    			break;
	    		case 3:
	    			pos_x = wideth - wideth_biao;
	    			pos_y = 0;
	    			break;
	    		case 4:
	    			pos_x = 0;
	    			pos_y = 0;
	    			break;
	    		case 5:
	    			pos_x = (wideth - wideth_biao) / 2;
	    			pos_y = (height - height_biao) / 2;
	    			break;
	            }
	            g.drawImage(src_biao,pos_x,pos_y, wideth_biao, height_biao, null);
	            //水印文件结束
	            g.dispose();
	            FileOutputStream out = new FileOutputStream(targetImg);
	            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	            encoder.encode(image);
	            out.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	 
        public static String getAudioPath(String basePath,String year,String month,String day,String hour){
        	if(StringUtils.isNullOrEmpty(year)||StringUtils.isNullOrEmpty(month)||StringUtils.isNullOrEmpty(day)||StringUtils.isNullOrEmpty(hour)){
        		return null;
        	}
        	
        	if(new File(basePath+"/"+year).isDirectory()){
        		if(new File(basePath+"/"+year).mkdir()||new File(basePath+"/"+year).setWritable(true, false)){
        			return null;
        		}
        	}
        	if(new File(basePath+"/"+year+"/"+month).isDirectory()){
        		if(new File(basePath+"/"+year+"/"+month).mkdir()||new File(basePath+"/"+year+"/"+month).setWritable(true, false)){
        			return null;
        		}
        	}
        	if(new File(basePath+"/"+year+"/"+month+"/"+day).isDirectory()){
        		if(new File(basePath+"/"+year+"/"+month+"/"+day).mkdir()||new File(basePath+"/"+year+"/"+month+"/"+day).setWritable(true, false)){
        			return null;
        		}
        	}
        	if(new File(basePath+"/"+year+"/"+month+"/"+day+"/"+hour).isDirectory()){
        		if(new File(basePath+"/"+year+"/"+month+"/"+day+"/"+hour).mkdir()||new File(basePath+"/"+year+"/"+month+"/"+day+"/"+hour).setWritable(true, false)){
        			return null;
        		}
        	}
        	return "/"+basePath+"/"+year+"/"+month+"/"+day+"/"+hour;
        }
        
}