package me.datafox.dfxengine.handles.plugin;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoFilter;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
        if(!info.getDescription().matches("'.+' may not contain (objects|keys) of type 'String'")) {
            return true;
        }
        PsiElement element = file.findElementAt(info.getActualStartOffset());
        if(element == null) {
            return true;
        }
        PsiExpression target;
        if(element.getParent() instanceof PsiMethodReferenceExpression reference) {
            if(reference.getFirstChild() instanceof PsiReferenceExpression expression) {
                target = expression;
            } else {
                return true;
            }
        } else {
            PsiExpressionList list = PsiTreeUtil.getParentOfType(element, PsiExpressionList.class);
            if(list == null) {
                return true;
            }
            PsiReferenceExpression expression = PsiTreeUtil.getPrevSiblingOfType(list, PsiReferenceExpression.class);
            if(expression == null) {
                return true;
            }
            if(expression.getQualifier() instanceof PsiExpression qualifier) {
                target = qualifier;
            } else {
                return true;
            }
        }
        return !(isTypeOrSupertype("me.datafox.dfxengine.handles.api.HandleSet", target.getType(), false) ||
                isTypeOrSupertype("me.datafox.dfxengine.handles.api.HandleMap", target.getType(), true));
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
        return getAllSuperTypes(type).anyMatch(t -> isType(name, t, ignoreParams));
    }

    private Stream<PsiType> getAllSuperTypes(PsiType type) {
        Set<PsiType> types = new HashSet<>(List.of(type.getSuperTypes()));
        int size;
        do {
            size = types.size();
            for(PsiType t : types.toArray(PsiType[]::new)) {
                types.addAll(List.of(t.getSuperTypes()));
            }
        } while(size != types.size());
        return types.stream();
    }
}