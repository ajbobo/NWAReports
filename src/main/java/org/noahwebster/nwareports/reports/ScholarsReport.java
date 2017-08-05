package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.DataTable;
import org.noahwebster.nwareports.Report;

public class ScholarsReport extends Report {
	public static final String REPORT_NAME = "Scholars";

	public ScholarsReport() {
		name = REPORT_NAME;
		description = "List of Scholars";
	}


	@Override
	public DataTable executeReport() {
		return new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withColumns("StudentName", "Textbox20")
//				.withColumns("StudentName", "Textbox20 as Grade")
				.uniqueOnly()
				.read();
	}
}
