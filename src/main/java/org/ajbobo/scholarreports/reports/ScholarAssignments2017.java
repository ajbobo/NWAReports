package org.ajbobo.scholarreports.reports;

import org.ajbobo.scholarreports.reports.types.ScholarAssignments;

public class ScholarAssignments2017 extends ScholarAssignments {
	public static final String REPORT_NAME = "Scholar Assignments";

	public ScholarAssignments2017() {
		name = REPORT_NAME;
		description = "Scholar Assignments by Subject";
		assignmentFile = "ElementaryStudentProgress.csv";
	}
}
