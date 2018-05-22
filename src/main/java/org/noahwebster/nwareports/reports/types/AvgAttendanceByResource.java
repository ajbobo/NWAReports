package org.noahwebster.nwareports.reports.types;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.data.DataTableJoiner;
import org.noahwebster.nwareports.data.DataTableReducer;
import org.noahwebster.nwareports.datatypes.StringRow;

import java.util.LinkedHashMap;

public abstract class AvgAttendanceByResource extends Report {
	protected String attendanceFile;
	protected String spedFile;

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
		DataTable scholars = new DataTable.Builder()
				.withFilePath(attendanceFile)
				.withStartRow(3)
				.withColumns("StudentID as Id", "Grade", "Date", "Period0", "Period2")
				.withFilter(new DataTable.Filter("Date", DataTable.FilterType.NOT_EQUALS, ""))
				.withColumnProcessor("Period0", typePivot)
				.withColumnProcessor("Period2", typePivot)
				.read(fileManager);

		DataTable resources = new DataTable.Builder()
				.withFilePath(spedFile)
				.withColumns("ResourceType", "ident as Id")
				.withStartRow(3)
				.withFilter(new DataTable.Filter("EndDate", DataTable.FilterType.EQUALS, ""))
				.stopAtBlank()
				.read(fileManager);

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
