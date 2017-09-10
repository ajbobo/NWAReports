package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.DataTable;
import org.noahwebster.nwareports.DataTableJoiner;
import org.noahwebster.nwareports.DataTableReducer;
import org.noahwebster.nwareports.Report;
import org.noahwebster.nwareports.types.StringRow;

import java.util.LinkedHashMap;

public class AvgAttendanceByResource extends Report {
	public static final String REPORT_NAME = "Average Attendance By Resource (2016)";

	public AvgAttendanceByResource() {
		name = REPORT_NAME;
		description = "Average attendance by resources - 2016";
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
		DataTable scholars = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\AttendanceByDay_16.csv")
				.withStartRow(3)
				.withColumns("StudentID as Id", "Grade", "Date", "Period0", "Period2")
				.withFilter(new DataTable.Filter("Date", DataTable.FilterType.NOT_EQUALS, ""))
				.withColumnProcessor("Period0", typePivot)
				.withColumnProcessor("Period2", typePivot)
				.read();

		DataTable resources = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\SpEd_16.csv")
				.withColumns("ResourceType", "ident as Id")
				.withFilter(new DataTable.Filter("EndDate", DataTable.FilterType.EQUALS, ""))
				.read();

		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("Id")
				.reportColumns("Id", "ResourceType", "Late", "Excused", "Tardy", "Absent")
				.build();

		DataTable startingTable = joiner.joinTables(scholars, resources);

		DataTableReducer reducer1 = new DataTableReducer.Builder()
				.withKeyColumns("Id", "ResourceType")
				.withOperation("Late", DataTableReducer.Operation.SUM)
				.withOperation("Excused", DataTableReducer.Operation.SUM)
				.withOperation("Tardy", DataTableReducer.Operation.SUM)
				.withOperation("Absent", DataTableReducer.Operation.SUM)
				.build();

		DataTable totals = reducer1.reduce(startingTable);

		DataTableReducer reducer2 = new DataTableReducer.Builder()
				.withKeyColumns("ResourceType")
				.withOperation("Late", DataTableReducer.Operation.AVERAGE)
				.withOperation("Excused", DataTableReducer.Operation.AVERAGE)
				.withOperation("Tardy", DataTableReducer.Operation.AVERAGE)
				.withOperation("Absent", DataTableReducer.Operation.AVERAGE)
				.build();

		return reducer2.reduce(totals);
	}
}
