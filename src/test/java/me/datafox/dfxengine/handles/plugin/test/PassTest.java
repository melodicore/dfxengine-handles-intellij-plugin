package me.datafox.dfxengine.handles.plugin.test;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.HandleSet;
import me.datafox.dfxengine.handles.api.Space;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class PassTest {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public PassTest(HandleSet set, HandleMap<String> map) {
        boolean b = set.contains("string");
        String s = map.get("string");
        List<String> l = new ArrayList<>();
        boolean b1 = set.containsAll(l);
        ExtendedSet es = new ExtendedSet();
        boolean b2 = es.contains("string");
        boolean b3 = es.containsAll(l);
        boolean b4 = es.contains(l.toString());
        boolean b5 = es.contains(l.getClass().toString());
        boolean b6 = es.contains((String) l.getClass().toString());
        boolean b7 = test("string", es::contains);
    }

    private boolean test(String str, Predicate<String> predicate) {
        return predicate.test(str);
    }

    public static class ExtendedSet implements HandleSet {
        @Override
        public Space getSpace() {
            return null;
        }

        @Override
        public Handle get(String id) {
            return null;
        }

        @Override
        public Handle add(String id) {
            return null;
        }

        @Override
        public HandleSet unmodifiable() {
            return null;
        }

        @Override
        public Collection<Handle> getByTag(Object tag) {
            return null;
        }

        @Override
        public Collection<Handle> getByTags(Collection<?> tags) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Handle> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return null;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Handle handle) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Handle> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    }
}