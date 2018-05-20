package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.reports.types.ScholarsByResource;

public class ScholarsByResource2016 extends ScholarsByResource {
	public static final String REPORT_NAME = "Scholars By Resource (2016)";

	public ScholarsByResource2016() {
		name = REPORT_NAME;
		description = "Scholars by resources - 2016";
		scholarFile = "ElementaryReportCard_16.csv";
		spedFile = "SpEd_16.csv";
	}
}
