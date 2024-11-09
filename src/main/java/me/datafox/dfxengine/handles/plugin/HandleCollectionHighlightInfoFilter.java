package me.datafox.dfxengine.handles.plugin;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoFilter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author datafox
 */
public class HandleCollectionHighlightInfoFilter implements HighlightInfoFilter {
    @Override
    public boolean accept(@NotNull HighlightInfo info, @Nullable PsiFile file) {
        if(info.getDescription() == null) {
            return true;
        }
        String s = info.getDescription();
        int i = 1;
        if(s.startsWith("Hash", i) || s.startsWith("Tree", i)) {
            i += 4;
        }
        if(!s.startsWith("Handle", i)) {
            return true;
        }
        i += 6;
        if(s.startsWith("Set' may not contain objects of type 'String'", i)) {
            return false;
        } else if(s.startsWith("Map", i)) {
            i = s.indexOf(" may ");
            if(i != -1) {
                return !s.startsWith(" may not contain keys of type 'String'", i);
            }
        }
        return true;
    }
}