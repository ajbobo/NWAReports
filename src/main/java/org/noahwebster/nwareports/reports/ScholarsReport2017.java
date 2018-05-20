package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.reports.types.ScholarsReport;

public class ScholarsReport2017 extends ScholarsReport {
	public static final String REPORT_NAME = "Scholars";

	public ScholarsReport2017() {
		name = REPORT_NAME;
		description = "List of Scholars";
		scholarsFile = "EnrolledStudents.csv";
	}
}
