package wbkj.sjapp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import wbkj.sjapp.models.User;

/**
 * SharedPreferences存储用户对象(序列化)
 * Created by jianghan on 16/8/24.
 */
public class SerializeUtil {

    /**
     * 序列化对象
     *
     * @param user
     * @return
     * @throws IOException
     */
    public static String serialize(User user) throws IOException {
//        startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(user);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
//        endTime = System.currentTimeMillis();
//        Log.d("serial", "序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static User deSerialization(String str) throws IOException,
            ClassNotFoundException {
//        startTime = System.currentTimeMillis();
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        User person = (User) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
//        endTime = System.currentTimeMillis();
//        Log.d("serial", "反序列化耗时为:" + (endTime - startTime));
        return person;
    }

}
