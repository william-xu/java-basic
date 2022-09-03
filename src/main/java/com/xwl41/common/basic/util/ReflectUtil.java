package com.xwl41.common.basic.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

class ReflectUtil {

    /**
     * 获取对象内所有定义的字段
     * @param obj   要操作的实例对象
     * @return      返回所有定义的字段列表
     */
    public static List<String> getDeclaredFields(Object obj){
        return getDeclaredFields(obj, null);
    }

    /**
     * 获取对象内部定义的，并且不包含在传入列表中的所有字段
     * @param obj               要操作的实例对象
     * @param excludeFields     不需要获取的字段列表
     * @return                  返回不需要获取的字段之外的所有定义的列表
     */
    public static List<String> getDeclaredFields(Object obj, List<String> excludeFields){
        List<String> fieldList = new LinkedList<>();
        if(obj == null) {
            return fieldList;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        if(fields.length > 0){
            for(Field field : fields){
                if(excludeFields != null && excludeFields.contains(field.getName())) {
                    continue;
                }
                fieldList.add(field.getName());
            }
        }
        return fieldList;
    }

    /**
     * 获取对象内部定义的，并且不包含在传入列表中的字段以及值，并返回一个字段名称-值映射
     * @param obj               要操作的实例对象
     * @param excludeFields     不需要包含的字段列表
     * @return                  返回除不需要包含的字段之外所有定义字段的名称-值映射
     */
    public static Map<String, Object> getDeclaredFieldsWithValue(Object obj, List<String> excludeFields) throws IllegalAccessException {
        Map<String, Object> fieldValueMap = new LinkedHashMap<>();
        List<String> excludes = excludeFields == null || excludeFields.isEmpty() ? new ArrayList<>() : excludeFields;
        if(obj == null) {
            return fieldValueMap;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        if(fields.length > 0){
            for(Field field : fields){
                if(excludes.contains(field.getName())) {
                    continue;
                }
                field.setAccessible(true);
                fieldValueMap.put(field.getName(), field.get(obj));
            }
        }
        return fieldValueMap;
    }

    /**
     * 获取实例对象指定字段的值
     * @param obj           要操作的实例对象
     * @param fieldName     要获取值的字段名称
     * @return              返回获取到字段对应的值
     */
    public static Object getDeclaredFieldValue(Object obj, String fieldName){
        if(obj == null) {
            return null;
        }
        Object value = null;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            value = field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean setFieldValue(Object obj, String fieldName, Object value){
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static <T> T newInstance(Class<? extends T> clazz){
        T t;
        try {
            if(Modifier.isPublic(clazz.getModifiers())){
                t = clazz.getDeclaredConstructor().newInstance();
            }else{
                Constructor<? extends T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                t = constructor.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

}
