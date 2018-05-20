package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.reports.types.AvgAttendanceByGrade;

public class AvgAttendanceByGrade2017 extends AvgAttendanceByGrade {
	public static final String REPORT_NAME = "Attendance By Grade";

	public AvgAttendanceByGrade2017() {
		name = REPORT_NAME;
		description = "Average Attendance By Grade";
		attendanceByDayFile = "AttendanceByDay.csv";
	}
}
