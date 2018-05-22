package org.noahwebster.nwareports.reports.util;

import org.noahwebster.nwareports.reports.*;
import org.noahwebster.nwareports.reports.types.Report;

import java.util.LinkedHashMap;

public class ReportFactory {

	private static LinkedHashMap<String, Class> reports;

	static {
		reports = new LinkedHashMap<>();
		reports.put(Attendance2017.REPORT_NAME, Attendance2017.class);
		reports.put(AvgAttendanceByGrade2017.REPORT_NAME, AvgAttendanceByGrade2017.class);
		reports.put(AvgAttendanceByResource2017.REPORT_NAME, AvgAttendanceByResource2017.class);
		reports.put(ScholarAttendanceAssignments2017.REPORT_NAME, ScholarAttendanceAssignments2017.class);
		reports.put(ScholarsReport2017.REPORT_NAME, ScholarsReport2017.class);

		// Removing the 2016 reports
//		reports.put(Attendance2016.REPORT_NAME, Attendance2016.class);
//		reports.put(AvgAttendanceByGrade2016.REPORT_NAME, AvgAttendanceByGrade2016.class);
//		reports.put(ScholarsByResource2016.REPORT_NAME, ScholarsByResource2016.class);
//		reports.put(AvgAttendanceByResource2016.REPORT_NAME, AvgAttendanceByResource2016.class);
	}

	public static Report getReport(String reportName) {
		try {
			if (reports.containsKey(reportName))
				return (Report)reports.get(reportName).newInstance();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String[] getReportNames() {
		String[] names = new String[reports.size()];
		reports.keySet().toArray(names);
		return names;
	}
}
