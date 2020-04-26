package org.ajbobo.scholarreports.reports.types;

import org.ajbobo.scholarreports.data.DataTable;
import org.ajbobo.scholarreports.data.DataTableReducer;
import org.ajbobo.scholarreports.datatypes.StringRow;

import java.util.LinkedHashMap;

public abstract class AvgAttendanceByGrade extends Report {
	protected String attendanceByDayFile;

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
				.withFilePath(attendanceByDayFile)
				.withStartRow(3)
				.withColumns("StudentID", "Grade", "Date", "Period0", "Period2")
				.withFilter(new DataTable.Filter("Date", DataTable.FilterType.NOT_EQUALS, ""))
				.withColumnProcessor("Period0", typePivot)
				.withColumnProcessor("Period2", typePivot)
				.read(fileManager);

		DataTableReducer reducer1 = new DataTableReducer.Builder()
				.withKeyColumns("StudentID", "Grade")
				.withOperation("Late", DataTableReducer.Operation.SUM)
				.withOperation("Excused", DataTableReducer.Operation.SUM)
				.withOperation("Tardy", DataTableReducer.Operation.SUM)
				.withOperation("Absent", DataTableReducer.Operation.SUM)
				.build();

		DataTable totalsByStudent = reducer1.reduce(startingTable);

		DataTableReducer reducer2 = new DataTableReducer.Builder()
				.withKeyColumns("Grade")
				.withOperation("Late", DataTableReducer.Operation.AVERAGE)
				.withOperation("Excused", DataTableReducer.Operation.AVERAGE)
				.withOperation("Tardy", DataTableReducer.Operation.AVERAGE)
				.withOperation("Absent", DataTableReducer.Operation.AVERAGE)
				.build();

		return reducer2.reduce(totalsByStudent);
	}
}
