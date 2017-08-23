package org.noahwebster.nwareports;

import org.noahwebster.nwareports.reports.AttendanceReport;
import org.noahwebster.nwareports.reports.AvgAttendanceByGrade;
import org.noahwebster.nwareports.reports.AvgAttendanceByResource;
import org.noahwebster.nwareports.reports.ScholarsByResource;
import org.testng.annotations.Test;

public class ReportTest {

	@Test
	public void testAttendanceReport() {
		AttendanceReport report = new AttendanceReport();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@Test
	public void testAvgAttendanceByGradeReport() {
		AvgAttendanceByGrade report = new AvgAttendanceByGrade();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@Test
	public void testScholarsByResourceReport() {
		ScholarsByResource report = new ScholarsByResource();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@Test
	public void testAvgAttendanceByResourceReport() {
		AvgAttendanceByResource report = new AvgAttendanceByResource();
		DataTable table = report.executeReport();
		Util.printTable(table);
	}
}
