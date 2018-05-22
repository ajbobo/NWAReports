package org.noahwebster.nwareports;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.data.FileManager;
import org.noahwebster.nwareports.reports.*;
import org.noahwebster.nwareports.reports.types.Report;
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
//				{ AvgAttendanceByGrade2016.class },
//				{ Attendance2016.class },
//				{ AvgAttendanceByResource2016.class },
//				{ ScholarsByResource2016.class },
		};
	}
}
