package org.noahwebster.nwareports.reports;

import java.util.LinkedHashMap;

public class ReportFactory {

	private static LinkedHashMap<String, Class> reports;

	static {
		reports = new LinkedHashMap<>();
		reports.put(AvgAttendanceByGrade2017.REPORT_NAME, AvgAttendanceByGrade2017.class);
		reports.put(AvgAttendanceByResource2017.REPORT_NAME, AvgAttendanceByResource2017.class);
//		reports.put(ScholarsReport.REPORT_NAME, ScholarsReport.class);
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
