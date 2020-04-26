package org.ajbobo.scholarreports.reports;

import org.ajbobo.scholarreports.reports.types.AvgAttendanceByGrade;

public class AvgAttendanceByGrade2017 extends AvgAttendanceByGrade {
	public static final String REPORT_NAME = "Attendance By Grade";

	public AvgAttendanceByGrade2017() {
		name = REPORT_NAME;
		description = "Average Attendance By Grade";
		attendanceByDayFile = "AttendanceByDay.csv";
	}
}
