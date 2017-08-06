package org.noahwebster.nwareports;

import org.noahwebster.nwareports.reports.AttendanceReport;
import org.testng.annotations.Test;

public class ReportTest {

	@Test
	public void testAttendanceReport() {
		AttendanceReport report = new AttendanceReport();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}
}
