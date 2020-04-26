package org.ajbobo.scholarreports.reports.util;

import org.ajbobo.scholarreports.reports.*;
import org.ajbobo.scholarreports.reports.types.Report;

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
		reports.put(ScholarAssignments2017.REPORT_NAME, ScholarAssignments2017.class);
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
