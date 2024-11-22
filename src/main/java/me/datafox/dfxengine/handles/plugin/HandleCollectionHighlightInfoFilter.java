package me.datafox.dfxengine.handles.plugin;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoFilter;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @author datafox
 */
public class HandleCollectionHighlightInfoFilter implements HighlightInfoFilter {

    @Override
    public boolean accept(@NotNull HighlightInfo info, @Nullable PsiFile file) {
        if(file == null) {
            return true;
        }
        if(info.getDescription() == null) {
            return true;
        }
        if(!info.getDescription().contains(" may not contain ")) {
            return true;
        }
        PsiElement element = file.findElementAt(info.getActualStartOffset());
        PsiExpression expression = PsiTreeUtil.getParentOfType(element, PsiExpression.class);
        if(expression == null || expression.getType() == null) {
            return true;
        }
        if(!isType("java.lang.String", expression.getType(), false) &&
                !isTypeOrSupertype("java.util.Collection<java.lang.String>", expression.getType(), false)) {
            return true;
        }
        PsiMethodCallExpression call = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression.class);
        if(call == null) {
            return true;
        }
        if(call.getMethodExpression().getQualifier() == null) {
            return true;
        }
        if(!(call.getMethodExpression().getQualifier() instanceof PsiReferenceExpression qualifier)) {
            return true;
        }
        if(qualifier.getType() == null) {
            return true;
        }
        return !(isTypeOrSupertype("me.datafox.dfxengine.handles.api.HandleSet", qualifier.getType(), false) ||
                isTypeOrSupertype("me.datafox.dfxengine.handles.api.HandleMap", qualifier.getType(), true));
    }

    private boolean isType(String name, PsiType type, boolean ignoreParams) {
        String text = type.getCanonicalText();
        if(ignoreParams) {
            text = text.split("<", 2)[0];
        }
        return text.equals(name);
    }

    private boolean isTypeOrSupertype(String name, PsiType type, boolean ignoreParams) {
        if(isType(name, type, ignoreParams)) {
            return true;
        }

        return Arrays.stream(type.getSuperTypes()).anyMatch(t -> isType(name, t, ignoreParams));
    }
}