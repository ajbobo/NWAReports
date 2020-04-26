package org.ajbobo.scholarreports;

import org.ajbobo.scholarreports.data.DataTable;
import org.ajbobo.scholarreports.data.FileManager;
import org.ajbobo.scholarreports.reports.*;
import org.ajbobo.scholarreports.reports.types.Report;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ReportTest {

	@Test(dataProvider = "provideReportClasses")
	public void testReport(Class<Report> reportClass) throws IllegalAccessException, InstantiationException {
		Report report = reportClass.newInstance();
		System.out.println("Testing Report: " + report.getName());
		report.setFileManager(new FileManager());
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@Test(dataProvider = "provideReportClasses")
	public void testReportWithPii(Class<Report> reportClass) throws IllegalAccessException, InstantiationException {
		Report report = reportClass.newInstance();
		report.hidePii(true);
		System.out.println("Testing Report: " + report.getName());
		report.setFileManager(new FileManager());
		DataTable table = report.executeReport();
		Util.printTable(table);
	}

	@DataProvider
	public static Object[][] provideReportClasses() {
		return new Object[][] {
				{ Attendance2017.class },
				{ AvgAttendanceByGrade2017.class },
				{ AvgAttendanceByResource2017.class },
				{ ScholarAttendanceAssignments2017.class },
				{ ScholarsReport2017.class },
				{ ScholarAssignments2017.class },
		};
	}
}
