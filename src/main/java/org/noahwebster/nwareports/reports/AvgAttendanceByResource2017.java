package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.reports.types.AvgAttendanceByResource;

public class AvgAttendanceByResource2017 extends AvgAttendanceByResource {
	public static final String REPORT_NAME = "Average Attendance By Resource";

	public AvgAttendanceByResource2017() {
		name = REPORT_NAME;
		description = "Average attendance by Special Education Resources";
		attendanceFile = "AttendanceByDay.csv";
		spedFile = "SpecialProgramDetailByProgram.csv";
	}
}
