package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.reports.types.Attendance;

public class Attendance2016 extends Attendance {
	public static final String REPORT_NAME = "Attendance (2016)";

	public Attendance2016() {
		name = REPORT_NAME;
		description = "Attendance by Scholar - 2016";
		attendanceFile = "AttendanceByDay_16.csv";
	}
}
