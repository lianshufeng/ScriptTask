package com.github.script.task.bridge.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConvertUtil {

    @FunctionalInterface
    public interface Invoke{
        Object invoke(String val);
    }

    public static Object conver(Class c,String val){
        return type.get(c).invoke(val);
    }



    //默认的基础类型的实例化初始值
    private static final Map<Class, Invoke> type = new ConcurrentHashMap<>() {
        {

            put(int.class, val -> {
                return Integer.parseInt(val);
            });
            put(long.class, val -> {
                return Long.parseLong(val);
            });
            put(short.class, val -> {
                return Short.parseShort(val);
            });
            put(float.class, val -> {
                return Float.parseFloat(val);
            });
            put(double.class, val -> {
                return Double.parseDouble(val);
            });
            put(boolean.class, val -> {
                return Boolean.parseBoolean(val);
            });


            put(Integer.class, val -> {
                return Integer.valueOf(val);
            });
            put(Long.class, val -> {
                return Long.valueOf(val);
            });
            put(Short.class, val -> {
                return Short.valueOf(val);
            });
            put(Float.class, val -> {
                return Float.valueOf(val);
            });
            put(Double.class, val -> {
                return Double.valueOf(val);
            });
            put(Boolean.class, val -> {
                return Boolean.valueOf(val);
            });
            put(String.class, val -> {
                return val;
            });
        }
    };



}
