package top.iahc.oto.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import top.iahc.oto.domain.ClassInfo;
import top.iahc.oto.domain.FieldInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PsiUtil {
    private static final String serialVersionUIDField =  "serialVersionUID";
    private static final Pattern filterPattern = Pattern.compile("static.*?final|final.*?static");
    private static final int INDENT_LENGTH = 4;

    public static ClassInfo getClassInfo(Project project, PsiTypeElement psiTypeElement) {
        ClassInfo classInfo = new ClassInfo();
        if(psiTypeElement == null) {
            return null;
        }
        PsiJavaCodeReferenceElement psiJavaCodeReferenceElement = psiTypeElement.getInnermostComponentReferenceElement();
        if(psiJavaCodeReferenceElement == null) {
            return null;
        }
        String className = psiJavaCodeReferenceElement.getReferenceNameElement().getText();
        classInfo.setClassName(className);
        String classQualifiedName = psiJavaCodeReferenceElement.getQualifiedName();
        classInfo.setQualifiedClassName(classQualifiedName);

        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(classQualifiedName, GlobalSearchScope.allScope(project));
        List<FieldInfo> fieldInfoList = PsiUtil.getFieldInfoList(project, psiClass);
        classInfo.setFieldInfos(fieldInfoList);

        PsiReferenceParameterList parameterList = psiJavaCodeReferenceElement.getParameterList();
        if(parameterList != null) {
            PsiTypeElement[] parameterElements = parameterList.getTypeParameterElements();

            List<ClassInfo> genericClassInfoList = new ArrayList<>();
            for(PsiTypeElement parameterElement : parameterElements) {
                if (psiTypeElement.getInnermostComponentReferenceElement() == null) {
                    continue;
                }
                ClassInfo genericClassInfo = getClassInfo(project, parameterElement);
                genericClassInfoList.add(genericClassInfo);
            }
            classInfo.setGenericInfos(genericClassInfoList);
        }
        return classInfo;
    }

    public static List<FieldInfo> getFieldInfoList(Project project, PsiClass psiClass) {
        if(psiClass == null) {
            return Collections.emptyList();
        }

        return Stream.of(psiClass.getAllFields())
                        .map(psiField -> {
                            ClassInfo classInfo = getClassInfo(project, psiField.getTypeElement());
                            return new FieldInfo(psiField.getNameIdentifier().getText(), classInfo);
                        })
                        .filter(fieldInfo -> {
                            String fieldName = fieldInfo.getFieldName();
                            ClassInfo fieldClassInfo = fieldInfo.getFieldClassInfo();
                            if(serialVersionUIDField.equals(fieldName)
                                    || filterPattern.matcher(fieldName).find()
                                    || fieldClassInfo == null) {
                                return false;
                            }
                            return true;
                        })
                        .collect(Collectors.toList());
    }

    public static int getSpaceLengthBeforeMethod(PsiMethod psiMethod) {
        PsiElement prevSibling = psiMethod.getPrevSibling();
        if(prevSibling instanceof PsiWhiteSpace) {
            PsiWhiteSpace psiWhiteSpace = (PsiWhiteSpace) prevSibling;
            String whiteSpaceText = psiWhiteSpace.getText();
            return whiteSpaceText.substring(whiteSpaceText.lastIndexOf("\n") + 1).length();
        }
        return 0;
    }

    public static String getMethodIndentSpace(PsiMethod psiMethod) {
        int whiteSpaceLengthBeforeMethod = getSpaceLengthBeforeMethod(psiMethod);
        return " ".repeat(Math.max(0, whiteSpaceLengthBeforeMethod + INDENT_LENGTH));
    }

    public static List<PsiClass> getPsiClassList(Project project, PsiReferenceParameterList psiReferenceParameterList) {
        List<PsiClass> psiClassList = new ArrayList<>();
        if(psiReferenceParameterList == null) {
            return psiClassList;
        }
        PsiTypeElement[] typeParameterElements = psiReferenceParameterList.getTypeParameterElements();
        for(PsiTypeElement psiTypeElement : typeParameterElements) {
            if(psiTypeElement.getInnermostComponentReferenceElement() == null) {
                continue;
            }
            String qualifiedName = psiTypeElement.getInnermostComponentReferenceElement().getQualifiedName();
            PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName, GlobalSearchScope.allScope(project));
            psiClassList.add(psiClass);
        }
        return psiClassList;
    }
}
