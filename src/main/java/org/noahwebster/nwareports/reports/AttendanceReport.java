package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.DataTable;
import org.noahwebster.nwareports.Report;

import java.util.LinkedHashMap;

public class AttendanceReport extends Report {
	public static final String REPORT_NAME = "Attendance";

	public AttendanceReport() {
		name = REPORT_NAME;
		description = "Attendance by Scholar";
	}

	private static LinkedHashMap<String, String> typeMap;
	static {
		typeMap = new LinkedHashMap<>();
		typeMap.put("L", "Late");
		typeMap.put("E", "Excused");
		typeMap.put("T", "Tardy");
		typeMap.put("X", "Absent");
	}

	private static DataTable.ColumnProcessor typePivot = (column, oldValue) -> {
		LinkedHashMap<String, String> res = new LinkedHashMap<>();
		if (typeMap.containsKey(oldValue)) { // Only process cells with a valid value
			for (String key : typeMap.keySet()) // Need all columns in all rows
				res.put(typeMap.get(key), "0");
			res.put(typeMap.get(oldValue), "1");
		}
		return res;
	};

	@Override
	public DataTable executeReport() {
		return new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\AttendanceByDay.csv")
				.withStartRow(3)
				.withColumns("StudentID", "StudentName", "Date", "Period0", "Period2")
				.withFilter(new DataTable.Filter("Date", DataTable.FilterType.NOT_EQUALS, ""))
				.withColumnProcessor("Period0", typePivot)
				.withColumnProcessor("Period2", typePivot)
				.read();
	}
}
