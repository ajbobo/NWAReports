package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.Report;

public class AttendanceReport extends Report {
	public static final String REPORT_NAME = "Attendance";

	public AttendanceReport() {
		name = REPORT_NAME;
		description = "Attendance by Scholar";
	}
}
