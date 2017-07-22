package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.Report;

import java.util.ArrayList;

public class ReportFactory {

	private enum ReportDef {
		SCHOLARS(ScholarsReport.REPORT_NAME, ScholarsReport.class),
		ATTENDANCE(AttendanceReport.REPORT_NAME, AttendanceReport.class);

		private String name;
		private java.lang.Class reportClass;

		<T extends Report> ReportDef(String name, Class<T> reportClass) {
			this.name = name;
			this.reportClass = reportClass;
		}

		public String getName() {
			return name;
		}

		public Class getReportClass() {
			return reportClass;
		}
	}

	public static Report getReport(String reportName) {
		try {
			for (ReportDef def : ReportDef.values()) {
				if (def.getName().equalsIgnoreCase(reportName))
					return (Report)def.getReportClass().newInstance();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String[] getReportNames() {
		ArrayList<String> reportNames = new ArrayList<>();
		for (ReportDef report : ReportDef.values())
			reportNames.add(report.getName());

		return reportNames.toArray(new String[ReportDef.values().length]);
	}
}
