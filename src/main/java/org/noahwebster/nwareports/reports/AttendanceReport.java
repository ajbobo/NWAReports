package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.DataTable;
import org.noahwebster.nwareports.Report;

public class AttendanceReport extends Report {
	public static final String REPORT_NAME = "Attendance";

	public AttendanceReport() {
		name = REPORT_NAME;
		description = "Attendance by Scholar";
	}

	@Override
	public DataTable executeReport() {
		return new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\AttendanceByDay.csv")
				.withStartRow(3)
				.withColumnNames("StudentID", "StudentName", "Date", "Period0 as Status1", "Period2 as Status2")
				.read();
	}
}
