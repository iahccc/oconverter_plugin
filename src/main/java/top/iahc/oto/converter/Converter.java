package top.iahc.oto.converter;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import top.iahc.oto.domain.ClassInfo;
import top.iahc.oto.domain.MethodInfo;
import top.iahc.oto.util.PsiUtil;

public abstract class Converter {
    public void generateConvertCode(PsiMethod psiMethod, AnActionEvent event) {
        PsiTypeElement returnPTE = psiMethod.getReturnTypeElement();
        ClassInfo returnInfo = PsiUtil.getClassInfo(event.getProject(), returnPTE);

        PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
        if (parameters.length != 1) {
            return;
        }
        PsiParameter parameter = parameters[0];
        String paramName = parameter.getName();
        PsiTypeElement paramPTE = parameter.getTypeElement();
        ClassInfo paramInfo = PsiUtil.getClassInfo(event.getProject(), paramPTE);

        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setReturnInfo(returnInfo);
        methodInfo.setParamInfo(paramInfo);
        methodInfo.setParamName(paramName);
        methodInfo.setCodeIndentSpace(PsiUtil.getMethodIndentSpace(psiMethod));

        String generateCode = getGenerateCode(methodInfo);

        Document document = CommonDataKeys.EDITOR.getData(event.getDataContext()).getDocument();
        int insertStartLineNum = document.getLineNumber(psiMethod.getTextOffset());
        WriteCommandAction.runWriteCommandAction(event.getProject(), () -> {
            document.insertString(document.getLineStartOffset(insertStartLineNum + 1), generateCode);
        });
    }

    abstract String getGenerateCode(MethodInfo methodInfo);
}
