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
        PsiExpression expression = PsiTreeUtil.getParentOfType(element, PsiExpression.class);
        if(expression == null) {
            return true;
        }
        loop: while(true) {
            PsiJavaToken token = PsiTreeUtil.getPrevSiblingOfType(expression, PsiJavaToken.class);
            while(true) {
                if(token == null) {
                    expression = PsiTreeUtil.getParentOfType(expression, PsiExpression.class);
                    if(expression == null) {
                        return true;
                    }
                    continue loop;
                }
                if("(".equals(token.getText())) {
                    break loop;
                } else {
                    token = PsiTreeUtil.getPrevSiblingOfType(token, PsiJavaToken.class);
                }
            }
        }
        PsiMethodCallExpression call = PsiTreeUtil.getParentOfType(expression, PsiMethodCallExpression.class);
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