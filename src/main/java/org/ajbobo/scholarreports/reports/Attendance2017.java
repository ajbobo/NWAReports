package org.ajbobo.scholarreports.reports;

import org.ajbobo.scholarreports.reports.types.Attendance;

public class Attendance2017 extends Attendance {
	public static final String REPORT_NAME = "Attendance";

	public Attendance2017() {
		name = REPORT_NAME;
		description = "Attendance by Scholar";
		attendanceFile = "AttendanceByDay.csv";
	}
}
