package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.reports.types.AvgAttendanceByGrade;

public class AvgAttendanceByGrade2016 extends AvgAttendanceByGrade {
	public static final String REPORT_NAME = "Attendance By Grade (2016)";

	public AvgAttendanceByGrade2016() {
		name = REPORT_NAME;
		description = "Average Attendance By Grade - 2016";
		attendanceByDayFile = "AttendanceByDay_16.csv";
	}
}
