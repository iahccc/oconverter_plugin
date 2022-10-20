package top.iahc.oto.domain;

public class FieldInfo {
    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 类信息
     */
    private ClassInfo fieldClassInfo;

    public FieldInfo() {
    }

    public FieldInfo(String fieldName, ClassInfo fieldClassInfo) {
        this.fieldName = fieldName;
        this.fieldClassInfo = fieldClassInfo;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ClassInfo getFieldClassInfo() {
        return fieldClassInfo;
    }

    public void setFieldClassInfo(ClassInfo fieldClassInfo) {
        this.fieldClassInfo = fieldClassInfo;
    }
}
