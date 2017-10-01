package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.DataTable;
import org.noahwebster.nwareports.DataTableReducer;
import org.noahwebster.nwareports.DataTableReducer.Operation;
import org.noahwebster.nwareports.Report;
import org.noahwebster.nwareports.types.StringRow;

import java.util.LinkedHashMap;

public class AvgAttendanceByGrade extends Report {
	public static final String REPORT_NAME = "Attendance By Grade (2016)";

	public AvgAttendanceByGrade() {
		name = REPORT_NAME;
		description = "Average Attendance By Grade - 2016";
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
		StringRow res = new StringRow();
		if (typeMap.containsKey(oldValue)) { // Only process cells with a valid value
			for (String key : typeMap.keySet()) // Need all columns in all rows
				res.put(typeMap.get(key), "0");
			res.put(typeMap.get(oldValue), "1");
		}
		return res;
	};

	@Override
	public DataTable executeReport() {
		DataTable startingTable = new DataTable.Builder()
				.withFilePath("AttendanceByDay_16.csv")
				.withStartRow(3)
				.withColumns("StudentID", "Grade", "Date", "Period0", "Period2")
				.withFilter(new DataTable.Filter("Date", DataTable.FilterType.NOT_EQUALS, ""))
				.withColumnProcessor("Period0", typePivot)
				.withColumnProcessor("Period2", typePivot)
				.read(fileManager);

		DataTableReducer reducer1 = new DataTableReducer.Builder()
				.withKeyColumns("StudentID", "Grade")
				.withOperation("Late", Operation.SUM)
				.withOperation("Excused", Operation.SUM)
				.withOperation("Tardy", Operation.SUM)
				.withOperation("Absent", Operation.SUM)
				.build();

		DataTable totalsByStudent = reducer1.reduce(startingTable);
		
		DataTableReducer reducer2 = new DataTableReducer.Builder()
				.withKeyColumns("Grade")
				.withOperation("Late", Operation.AVERAGE)
				.withOperation("Excused", Operation.AVERAGE)
				.withOperation("Tardy", Operation.AVERAGE)
				.withOperation("Absent", Operation.AVERAGE)
				.build();

		return reducer2.reduce(totalsByStudent);
	}
}
