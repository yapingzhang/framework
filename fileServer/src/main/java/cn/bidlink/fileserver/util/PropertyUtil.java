package cn.bidlink.fileserver.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.bidlink.fileserver.bean.FieldBean;


/**
 * 通用反射类
 */
public class PropertyUtil {
    static List<String> list = new ArrayList<String>();
    private static final Logger LOGGER = Logger.getLogger(PropertyUtil.class);
    static {
        list.add("String");
        list.add("int");
        list.add("Integer");
        list.add("double");
        list.add("Double");
        list.add("long");
        list.add("Long");
        list.add("char");
        list.add("Character");
        list.add("short");
        list.add("Short");
        list.add("float");
        list.add("Float");
        list.add("BigDecimal");     
    }

    
    /**
     * 通过字段与前缀分别得到set或get方法
     * @param field
     * @param prefix
     * @return
     * @throws Exception
     */
    private static Method getMethod(Field field, String prefix, Class<?> cls)
            throws Exception {
        String methodName = prefix;
        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        methodName = prefix + firstLetter + fieldName.substring(1);
        if (prefix.equals("set")) {
            return cls.getMethod(methodName, new Class[] { field.getType() });
        }else {
            return cls.getMethod(methodName, new Class[] {});
        }
    }

    /**
     * 
     * @param cls 将一个字段的名与该字段对应的所有的属性的邦定起来
     * @param tempMap  一个类字段与该字段对应的FieldBean的邦定
     * @throws Exception 抛出异常
     */
    private static void addFieldMap(Class<?> cls, Map<String, FieldBean> tempMap)
            throws Exception {
        Field[] fields = cls.getDeclaredFields();
        // 通过这样设置可以使得私有变量可以被访问
        AccessibleObject.setAccessible(fields, true);

        for (int i = 0; fields != null && i < fields.length; i++) {
            FieldBean temp = new FieldBean(fields[i].getName(), fields[i]
                    .getType().getSimpleName(), fields[i].getType(), fields[i]
                    .getDeclaringClass(), fields[i].getDeclaringClass()
                    .getSuperclass(), fields[i], getMethod(fields[i], "set",
                    cls), getMethod(fields[i], "get", cls));
            if (!list.contains(fields[i].getType().getSimpleName())) {
                cls = fields[i].getType();
                tempMap.putAll(getFiledMap(cls));
            }
            tempMap.put(fields[i].getName(), temp);
        }
    }

    /**
     * @param cls 将一个字段的名与该字段对应的所有的属性的邦定起来
     * @param tempMap 类字段名与该字段FieldBean的邦定
     * @param colName 数据库列名集合
     * @throws Exception  抛出异常
     */
    private static void addFieldMap(Class<?> cls, Map<String, FieldBean> tempMap,List<String> colName)
            throws Exception {
        Field[] fields = cls.getDeclaredFields();
 
        
        // 通过这样设置可以使得私有变量可以被访问
        AccessibleObject.setAccessible(fields, true);

        for (int i = 0; fields != null && i < fields.length; i++) {
          if (colName.contains(fields[i].getName().toUpperCase())) { 
                FieldBean temp = new FieldBean(fields[i].getName(), fields[i]
                        .getType().getSimpleName(), fields[i].getType(), fields[i]
                        .getDeclaringClass(), fields[i].getDeclaringClass()
                        .getSuperclass(), fields[i], getMethod(fields[i], "set",
                        cls), getMethod(fields[i], "get", cls));
                
                if (!list.contains(fields[i].getType().getSimpleName())) {
                    cls = fields[i].getType();
                    tempMap.putAll(getFiledMap(cls, colName));
                }
                tempMap.put(fields[i].getName(), temp);
          }  
          
        }
    }
    /**
     * @description 返回一个类中所有声明的变量,包括它继承的父类的变量 与 该变量的FieldBean的邦定
     * @param cls  类模板
     * @return  返回一个类中所有声明的变量,包括它继承的父类的变量 与 该变量的FieldBean的邦定
     * @throws Exception  抛出异常
     */
    public static synchronized Map<String, FieldBean> getFiledMap(Class<?> cls)
            throws Exception {
        Map<String, FieldBean> tempMap = null;
        tempMap = new HashMap<String, FieldBean>();
       try {
           addFieldMap(cls, tempMap);
            while (cls.getSuperclass() != null && !cls.getSuperclass().getSimpleName().equals("Object") 
            		&& cls!=Date.class ) {
                	
                cls = cls.getSuperclass();
                addFieldMap(cls, tempMap);
            }
       }catch (Exception e) {
           LOGGER.error(e.getMessage(),e);
    }
        return tempMap;
     
    }
    /**
     * @description 返回一个类中所有声明的变量,包括它继承的父类的变量
     * @param cls  类模板
     * @param colName 数据库列名集合
     * @return  返回与数据库列对应的字段与字段的FieldBean的邦定
     * @throws Exception  抛出异常
     */
    public static synchronized Map<String, FieldBean> getFiledMap(Class<?> cls,List<String> colName)
            throws Exception {
        Map<String, FieldBean> tempMap = null;
        tempMap = new HashMap<String, FieldBean>();
       try {
           addFieldMap(cls, tempMap,colName);
            while (cls.getSuperclass() != null
                    && !cls.getSuperclass().getSimpleName().equals("Object")) {
                cls = cls.getSuperclass();
                addFieldMap(cls, tempMap,colName);
            }           
       }catch (Exception e) {
           LOGGER.error(e.getMessage(),e);
    }
        return tempMap;    
    }
     
    

}
