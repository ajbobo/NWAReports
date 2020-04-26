package org.ajbobo.scholarreports.reports;

import org.ajbobo.scholarreports.reports.types.ScholarAttendanceAssignments;

public class ScholarAttendanceAssignments2017 extends ScholarAttendanceAssignments {
	public static final String REPORT_NAME = "Scholar Attendance and Assignments";

	public ScholarAttendanceAssignments2017() {
		name = REPORT_NAME;
		description = "Scholar Attendance and Assignment Status";
		attendanceFile = "AttendanceByDay.csv";
		assignmentFile = "ElementaryStudentProgress.csv";
	}
}
