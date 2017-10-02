package org.noahwebster.nwareports.reports;

import java.util.LinkedHashMap;

public class ReportFactory {

	private static LinkedHashMap<String, Class> reports;

	static {
		reports = new LinkedHashMap<>();
		reports.put(ScholarsReport.REPORT_NAME, ScholarsReport.class);
		reports.put(AttendanceReport.REPORT_NAME, AttendanceReport.class);
		reports.put(AvgAttendanceByGrade.REPORT_NAME, AvgAttendanceByGrade.class);
		reports.put(ScholarsByResource.REPORT_NAME, ScholarsByResource.class);
		reports.put(AvgAttendanceByResource.REPORT_NAME, AvgAttendanceByResource.class);
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