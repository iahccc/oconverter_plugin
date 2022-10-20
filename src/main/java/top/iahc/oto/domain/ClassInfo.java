package top.iahc.oto.domain;

import java.util.List;
import java.util.Objects;

public class ClassInfo {
    /**
     * 类名
     */
    private String className;

    /**
     * 全限定类名
     */
    private String qualifiedClassName;

    /**
     * 字段信息
     */
    private List<FieldInfo> fieldInfos;
    /**
     * 泛型信息
     */
    private List<ClassInfo> genericInfos;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getQualifiedClassName() {
        return qualifiedClassName;
    }

    public void setQualifiedClassName(String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public List<ClassInfo> getGenericInfos() {
        return genericInfos;
    }

    public void setGenericInfos(List<ClassInfo> genericInfos) {
        this.genericInfos = genericInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassInfo classInfo = (ClassInfo) o;
        return Objects.equals(className, classInfo.className) &&
                Objects.equals(qualifiedClassName, classInfo.qualifiedClassName) &&
                Objects.equals(fieldInfos, classInfo.fieldInfos) &&
                Objects.equals(genericInfos, classInfo.genericInfos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, qualifiedClassName, fieldInfos, genericInfos);
    }
}
