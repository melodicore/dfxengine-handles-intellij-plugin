package me.datafox.dfxengine.handles.plugin.test;

import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.HandleSet;

import java.util.ArrayList;
import java.util.List;

public class PassTest {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public PassTest(HandleSet set, HandleMap<String> map) {
        boolean b = set.contains("string");
        String s = map.get("string");
        List<String> l = new ArrayList<>();
        boolean b1 = set.containsAll(l);
    }
}