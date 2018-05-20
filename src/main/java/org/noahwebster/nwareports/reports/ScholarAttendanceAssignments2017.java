package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.reports.types.ScholarAttendanceAssignments;

public class ScholarAttendanceAssignments2017 extends ScholarAttendanceAssignments {
	public static final String REPORT_NAME = "Scholar Attendance and Assignments";

	public ScholarAttendanceAssignments2017() {
		name = REPORT_NAME;
		description = "Scholar Attendance and Assignment Status";
		attendanceFile = "AttendanceByDay.csv";
		assignmentFile = "ElementaryStudentProgress.csv";
	}
}
