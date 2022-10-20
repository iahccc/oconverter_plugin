package top.iahc.oto.converter;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTypeElement;
import org.apache.commons.lang3.StringUtils;
import top.iahc.oto.domain.ClassInfo;
import top.iahc.oto.domain.MethodInfo;

import java.text.MessageFormat;
import java.util.List;

public class ListConverter extends Converter {
    @Override
    String getGenerateCode(MethodInfo methodInfo) {
        ClassInfo returnInfo = methodInfo.getReturnInfo();
        ClassInfo paramInfo = methodInfo.getParamInfo();
        String paramName = methodInfo.getParamName();
        String codeIndentSpace = methodInfo.getCodeIndentSpace();
        List<ClassInfo> returnGenericInfoList = returnInfo.getGenericInfos();
        List<ClassInfo> paramGenericInfoList = paramInfo.getGenericInfos();

        if(returnGenericInfoList.size() != 1 && paramGenericInfoList.size() != 1) {
            return "";
        }
        ClassInfo returnClassGenericInfo = returnGenericInfoList.get(0);
        ClassInfo paramClassGenericInfo = paramGenericInfoList.get(0);
        String paramClassGenericName = paramClassGenericInfo.getClassName();
        String uncapParamClassGenericName = StringUtils.uncapitalize(paramClassGenericName);


        String generateCodePattern =
                "{0}List<{1}> resultList = new ArrayList<>();\n" +
                        "{0}for ({2} {3} : {4}) '{'\n" +
                        "{0}    resultList.add(convertTo{1}({3}));\n" +
                        "{0}}\n" +
                        "{0}return resultList;\n";
        String generateCode = MessageFormat.format(generateCodePattern,
                codeIndentSpace, returnClassGenericInfo.getClassName(), paramClassGenericName,
                uncapParamClassGenericName, paramName);
        return generateCode;
    }
}
