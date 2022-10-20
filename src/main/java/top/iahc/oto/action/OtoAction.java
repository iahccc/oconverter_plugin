package top.iahc.oto.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;
import top.iahc.oto.converter.holder.ConverterHolder;


public class OtoAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        PsiElement psiElement = event.getData(LangDataKeys.PSI_ELEMENT);

        if(psiElement instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) psiElement;
            PsiTypeElement returnPTE = psiMethod.getReturnTypeElement();
            if(returnPTE == null) {
                return;
            }
            PsiJavaCodeReferenceElement returnTypePJCRE = returnPTE.getInnermostComponentReferenceElement();
            if(returnTypePJCRE == null) {
                return;
            }
            ConverterHolder.route(returnTypePJCRE.getQualifiedName()).generateConvertCode(event);
        } else if(psiElement instanceof PsiClass) {
            PsiClass psiClass = (PsiClass) psiElement;
            System.out.println("hhl");
        }
    }

}
