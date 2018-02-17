package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.data.DataTableReducer;
import org.noahwebster.nwareports.data.DataTableReducer.Operation;
import org.noahwebster.nwareports.types.StringRow;
import org.noahwebster.nwareports.data.DataTable;

import java.util.LinkedHashMap;

public class Attendance2016 extends Report {
	public static final String REPORT_NAME = "Attendance (2016)";

	public Attendance2016() {
		name = REPORT_NAME;
		description = "Attendance by Scholar - 2016";
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
				.withColumns("StudentID", "StudentName", "Date", "Period0", "Period2")
				.withFilter(new DataTable.Filter("Date", DataTable.FilterType.NOT_EQUALS, ""))
				.withColumnProcessor("Period0", typePivot)
				.withColumnProcessor("Period2", typePivot)
				.read(fileManager);

		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("StudentID", "StudentName")
				.withOperation("Late", Operation.SUM)
				.withOperation("Excused", Operation.SUM)
				.withOperation("Tardy", Operation.SUM)
				.withOperation("Absent", Operation.SUM)
				.build();

		DataTable reduced = reducer.reduce(startingTable);
		return reduced.clearPii(piiEnabled, "StudentID", "StudentName");
	}
}
