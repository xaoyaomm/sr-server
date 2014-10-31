package com.store.api.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteUtil {
    /**
     * 把对象转为字节数组
     * @param obj
     * @return
     * @throws IOException
     */
    public final static byte[] toByte(Object obj) throws IOException{
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        byte[] bs=new byte[bos.size()];
        System.arraycopy(bos.toByteArray(), 0, bs, 0, bs.length);
        return bs;
    }
    /**
     * 把字节数组转为对象
     * @param bs
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public final static Object toObject(byte[] bs) throws IOException, ClassNotFoundException  {
        ByteArrayInputStream bis=new ByteArrayInputStream(bs,0,bs.length);
        
        ObjectInputStream ois=new ObjectInputStream(bis);
        Object oo=ois.readObject();
        ois.close();
        return oo;
    }   
    
    public final static boolean isSerializableByte(byte[] bs){
        if(bs[0]==(byte)-84 && bs[1]==(byte)-19 
                 && bs[2]==(byte)0  && bs[3]==(byte)5){
            return true;
        }
        return false;
    }
}