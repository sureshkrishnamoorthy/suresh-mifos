package org.mifos.application.surveys;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.mifos.application.surveys.business.TestSurvey;
import org.mifos.application.surveys.struts.action.TestQuestionsAction;
import org.mifos.application.surveys.struts.action.TestSurveysAction;

public class SurveysTestSuite extends TestSuite {
	
	public static Test suite() throws Exception {
		TestSuite testSuite = new SurveysTestSuite();
		testSuite.addTestSuite(TestSurvey.class);
		testSuite.addTestSuite(TestSurveysAction.class);
		testSuite.addTestSuite(TestQuestionsAction.class);
		return testSuite;
	}

}
