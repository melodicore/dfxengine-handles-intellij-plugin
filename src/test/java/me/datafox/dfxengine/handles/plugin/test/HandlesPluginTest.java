package me.datafox.dfxengine.handles.plugin.test;

import com.intellij.codeInspection.miscGenerics.SuspiciousCollectionsMethodCallsInspection;
import com.intellij.rt.execution.junit.FileComparisonFailure;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase4;
import org.junit.Before;
import org.junit.Test;

/**
 * @author datafox
 */
public class HandlesPluginTest extends LightJavaCodeInsightFixtureTestCase4 {
    public HandlesPluginTest() {
        super(new DefaultLightProjectDescriptor()
                        .withRepositoryLibrary("me.datafox.dfxengine:handles-api:2.0.2"),
                "src/test/java/me/datafox/dfxengine/handles/plugin/test");
    }

    @Before
    public void before() {
        getFixture().enableInspections(new SuspiciousCollectionsMethodCallsInspection());
    }

    @Test
    public void passTest() {
        getFixture().testHighlighting("PassTest.java");
    }

    @Test(expected = FileComparisonFailure.class)
    public void failTest() {
        getFixture().testHighlighting("FailTest.java");
    }
}
