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
