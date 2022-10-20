package top.iahc.oto.converter;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import top.iahc.oto.domain.ClassInfo;
import top.iahc.oto.domain.FieldInfo;
import top.iahc.oto.domain.MethodInfo;
import top.iahc.oto.util.PsiUtil;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

public class DefaultConverter extends Converter{
    @Override
    String getGenerateCode(MethodInfo methodInfo) {
        ClassInfo returnInfo = methodInfo.getReturnInfo();
        ClassInfo paramInfo = methodInfo.getParamInfo();
        String paramName = methodInfo.getParamName();
        String codeIndentSpace = methodInfo.getCodeIndentSpace();
        String returnClassName = returnInfo.getClassName();
        List<FieldInfo> returnClassFieldInfoList = returnInfo.getFieldInfos();
        List<FieldInfo> paramClassFieldInfoList = paramInfo.getFieldInfos();

        String returnVariableName = Objects.equals(StringUtils.uncapitalize(returnClassName), paramName) ?
                StringUtils.uncapitalize(returnClassName) + "Result" : StringUtils.uncapitalize(returnClassName);

        StringBuilder convertSb = new StringBuilder();

        String returnVariableDefinition = MessageFormat.format("{0}{1} {2} = new {1}();\n",
                codeIndentSpace, returnClassName, returnVariableName);
        convertSb.append(returnVariableDefinition);

        for(FieldInfo fieldInfo : returnClassFieldInfoList) {
            String fieldName = fieldInfo.getFieldName();
            ClassInfo fieldClassInfo = fieldInfo.getFieldClassInfo();
            String capFieldName = StringUtils.capitalize(fieldName);

            String setValue = "";
            FieldInfo paramFieldInfo = paramClassFieldInfoList
                    .stream()
                    .filter(e -> Objects.equals(e.getFieldName(), fieldName))
                    .findFirst()
                    .orElse(null);
            if(paramFieldInfo != null) {
                String getMethod = MessageFormat.format("{0}.get{1}()", paramName, capFieldName);

                ClassInfo paramFieldClassInfo = paramFieldInfo.getFieldClassInfo();
                if(Objects.equals(paramFieldClassInfo, fieldClassInfo)) {
                    setValue = getMethod;
                } else {
                    setValue = MessageFormat.format("convert{0}({1})",
                            capFieldName, getMethod);

//                    String methodCodePattern = "{0}public {1} convertTo{1}({2} {3}) '{'\n" +
//                            "{0}    {4}\n" +
//                            "{0}}\n";
//
//                    MethodInfo converterMethodInfo = new MethodInfo();
//                    converterMethodInfo.setReturnInfo(fieldClassInfo);
//                    converterMethodInfo.setParamInfo(paramFieldClassInfo);
//                    converterMethodInfo.setParamName(paramFieldClassInfo.getClassName());
//                    converterMethodInfo.setCodeIndentSpace("        ");
//
//
//                    String methodCode = MessageFormat.format(methodCodePattern,
//                            "    ", fieldClassInfo.getClassName(), paramFieldClassInfo.getClassName(),
//                            StringUtils.uncapitalize(paramFieldClassInfo.getClassName()),
//                            getGenerateCode(converterMethodInfo));
                }
            }

            String setMethod = MessageFormat.format("{0}{1}.set{2}({3});\n",
                    codeIndentSpace, returnVariableName, capFieldName, setValue);
            convertSb.append(setMethod);
        }
        String returnLine = MessageFormat.format("{0}return {1};\n",
                codeIndentSpace, returnVariableName);
        convertSb.append(returnLine);

        return convertSb.toString();
    }
}
