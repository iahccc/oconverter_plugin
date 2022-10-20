package top.iahc.oto.domain;

public class MethodInfo {
    private ClassInfo returnInfo;

    private ClassInfo paramInfo;

    private String paramName;

    private String codeIndentSpace;

    public ClassInfo getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(ClassInfo returnInfo) {
        this.returnInfo = returnInfo;
    }

    public ClassInfo getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(ClassInfo paramInfo) {
        this.paramInfo = paramInfo;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getCodeIndentSpace() {
        return codeIndentSpace;
    }

    public void setCodeIndentSpace(String codeIndentSpace) {
        this.codeIndentSpace = codeIndentSpace;
    }
}
