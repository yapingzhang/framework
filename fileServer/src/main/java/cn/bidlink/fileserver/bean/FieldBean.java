package cn.bidlink.fileserver.bean;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class FieldBean implements Serializable {

    private static final long serialVersionUID = 2554075527479424782L;

    private String filedName; 

    private String fieldSimpleType;  

    private Class<?>  fieldType;  

    private Class <?> fieldDeclClass; 

    private Class<?>  fieldSubClass;  

    private Field field; 

    private Method fieldSetMethod; 

    private Method fieldGetMethod; 
    
    public FieldBean() {
    }
    public FieldBean(String filedName, String fieldSimpleType, Class<?>  fieldType,
            Class <?> fieldDeclClass, Class <?> fieldSubClass, Field field,
            Method fieldSetMethod, Method fieldGetMethod) {
        super();
        this.filedName = filedName;
        this.fieldSimpleType = fieldSimpleType;
        this.fieldType = fieldType;
        this.fieldDeclClass = fieldDeclClass;
        this.fieldSubClass = fieldSubClass;
        this.field = field;
        this.fieldSetMethod = fieldSetMethod;
        this.fieldGetMethod = fieldGetMethod;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Class<?> getFieldDeclClass() {
        return fieldDeclClass;
    }

    public void setFieldDeclClass(Class<?>  fieldDeclClass) {
        this.fieldDeclClass = fieldDeclClass;
    }

    public Method getFieldGetMethod() {
        return fieldGetMethod;
    }

    public void setFieldGetMethod(Method fieldGetMethod) {
        this.fieldGetMethod = fieldGetMethod;
    }

    public Method getFieldSetMethod() {
        return fieldSetMethod;
    }

    public void setFieldSetMethod(Method fieldSetMethod) {
        this.fieldSetMethod = fieldSetMethod;
    }

    public String getFieldSimpleType() {
        return fieldSimpleType;
    }
    public void setFieldSimpleType(String fieldSimpleType) {
        this.fieldSimpleType = fieldSimpleType;
    }
    public Class<?>  getFieldSubClass() {
        return fieldSubClass;
    }
    public void setFieldSubClass(Class <?> fieldSubClass) {
        this.fieldSubClass = fieldSubClass;
    }
    public Class<?>  getFieldType() {
        return fieldType;
    }
    public void setFieldType(Class<?>  fieldType) {
        this.fieldType = fieldType;
    }
    public String getFiledName() {
        return filedName;
    }
    public void setFiledName(String filedName) {
        this.filedName = filedName;
    } 
}