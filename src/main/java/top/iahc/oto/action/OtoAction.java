package top.iahc.oto.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import top.iahc.oto.converter.holder.ConverterHolder;


public class OtoAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
        if(psiFile == null) {
            return;
        }
        Editor editor = CommonDataKeys.EDITOR.getData(event.getDataContext());
        if(editor == null) {
            return ;
        }
        PsiElement psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
        while (!(psiElement instanceof PsiMethod)) {
            if(psiElement == null) {
                break;
            }
            psiElement = psiElement.getParent();
        }

        if(psiElement != null) {
            PsiMethod psiMethod = (PsiMethod) psiElement;
            PsiTypeElement returnPTE = psiMethod.getReturnTypeElement();
            if(returnPTE == null) {
                return;
            }
            PsiJavaCodeReferenceElement returnTypePJCRE = returnPTE.getInnermostComponentReferenceElement();
            if(returnTypePJCRE == null) {
                return;
            }
            ConverterHolder.route(returnTypePJCRE.getQualifiedName()).generateConvertCode(psiMethod, event);
        }
    }

}
