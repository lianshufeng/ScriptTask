package top.dzurl.task.bridge.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanUtil {

    /**
     * 获取bean对象中为null的属性名
     *
     * @param source
     * @return
     */
    public static Set<String> getNullPropertyNames(Object source) {
        Set<String> ret = new HashSet<>();
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors()).filter((pd) -> {
            return src.getPropertyValue(pd.getName()) == null;
        }).map((it) -> {
            return it.getName();
        }).collect(Collectors.toSet());
    }


    /**
     * Bean转到map
     *
     * @param source
     * @return
     */
    public static Map<String, Object> toMap(Object source) {
        Map<String, Object> ret = new HashMap<>();
        final BeanWrapper src = new BeanWrapperImpl(source);
        Arrays.stream(src.getPropertyDescriptors()).forEach((it) -> {
            ret.put(it.getName(), src.getPropertyValue(it.getName()));
        });
        return ret;

    }


    /**
     * 获取有value值的key
     *
     * @param source
     * @return
     */
    public static Set<String> getHasPropertyNames(Object source) {
        Set<String> ret = new HashSet<>();
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors()).filter((pd) -> {
            return src.getPropertyValue(pd.getName()) != null;
        }).map((it) -> {
            return it.getName();
        }).collect(Collectors.toSet());
    }


    /**
     * 获取对象的所有属性名
     *
     * @param source
     * @return
     */
    public static Set<String> getPropertyNames(Object source) {
        Set<String> ret = new HashSet<>();
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors()).map((it) -> {
            return it.getName();
        }).collect(Collectors.toSet());
    }

    /**
     * javaBean转map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> bean2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        try {
            // 获取javaBean的BeanInfo对象
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
            // 获取属性描述器
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                // 获取属性名
                String key = propertyDescriptor.getName();
                // 获取该属性的值
                Method readMethod = propertyDescriptor.getReadMethod();
                // 通过反射来调用javaBean定义的getName()方法
                Object value = readMethod.invoke(obj);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
