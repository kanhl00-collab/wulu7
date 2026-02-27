package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllTestsADT.class, AllTestsGraph.class, AllTestsSearch.class, RiskAssessmentTest.class,
		SortTest.class })
public class AllTests {

}
