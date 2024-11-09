package me.datafox.dfxengine.handles.plugin.test;

import me.datafox.dfxengine.handles.api.HandleSet;

import java.util.ArrayList;
import java.util.List;

public class FailTest {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public FailTest(HandleSet set) {
        List<Comparable<?>> l = new ArrayList<>();
        boolean b1 = set.containsAll(l);
    }
}