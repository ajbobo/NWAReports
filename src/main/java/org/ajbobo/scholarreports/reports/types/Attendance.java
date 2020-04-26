package org.ajbobo.scholarreports.reports.types;

import org.ajbobo.scholarreports.data.DataTable;
import org.ajbobo.scholarreports.data.DataTableReducer;
import org.ajbobo.scholarreports.datatypes.StringRow;

import java.util.LinkedHashMap;

public abstract class Attendance extends Report {
	protected String attendanceFile;

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
				.withFilePath(attendanceFile)
				.withStartRow(3)
				.withColumns("StudentID", "StudentName", "Date", "Period0", "Period2")
				.withFilter(new DataTable.Filter("Date", DataTable.FilterType.NOT_EQUALS, ""))
				.withColumnProcessor("Period0", typePivot)
				.withColumnProcessor("Period2", typePivot)
				.read(fileManager);

		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("StudentID", "StudentName")
				.withOperation("Late", DataTableReducer.Operation.SUM)
				.withOperation("Excused", DataTableReducer.Operation.SUM)
				.withOperation("Tardy", DataTableReducer.Operation.SUM)
				.withOperation("Absent", DataTableReducer.Operation.SUM)
				.build();

		DataTable reduced = reducer.reduce(startingTable);
		return reduced.hidePii(piiHidden, "StudentID", "StudentName");
	}
}
