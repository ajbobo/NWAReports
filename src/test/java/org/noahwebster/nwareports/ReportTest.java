package org.noahwebster.nwareports;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.reports.*;
import org.testng.annotations.Test;

public class ReportTest {

	@Test
	public void testScholarsReport() {
		ScholarsReport report = new ScholarsReport();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@Test
	public void testAttendanceReport() {
		Attendance2016 report = new Attendance2016();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@Test
	public void testAvgAttendanceByGradeReport() {
		AvgAttendanceByGrade2016 report = new AvgAttendanceByGrade2016();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@Test
	public void testScholarsByResourceReport() {
		ScholarsByResource2016 report = new ScholarsByResource2016();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@Test
	public void testAvgAttendanceByResourceReport() {
		AvgAttendanceByResource2016 report = new AvgAttendanceByResource2016();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}
}
