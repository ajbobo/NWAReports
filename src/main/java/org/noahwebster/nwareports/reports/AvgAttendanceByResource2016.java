package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.reports.types.AvgAttendanceByResource;

public class AvgAttendanceByResource2016 extends AvgAttendanceByResource {
	public static final String REPORT_NAME = "Average Attendance By Resource (2016)";

	public AvgAttendanceByResource2016() {
		name = REPORT_NAME;
		description = "Average attendance by resources - 2016";
		attendanceFile = "AttendanceByDay_16.csv";
		spedFile = "SpEd_16.csv";
	}
}
