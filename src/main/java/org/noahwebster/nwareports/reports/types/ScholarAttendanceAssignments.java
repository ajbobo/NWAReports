package org.noahwebster.nwareports.reports.types;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.data.DataTableJoiner;
import org.noahwebster.nwareports.data.DataTableReducer;
import org.noahwebster.nwareports.data.DataTableReducer.Operation;
import org.noahwebster.nwareports.datatypes.StringRow;

import java.util.LinkedHashMap;

public abstract class ScholarAttendanceAssignments extends Report {
	protected String attendanceFile;
	protected String assignmentFile;

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
		DataTable attendanceTable = new DataTable.Builder()
				.withFilePath(attendanceFile)
				.withStartRow(3)
				.withColumns("StudentName as Name", "StudentID as ID", "Date", "Period0", "Period2")
				.withFilter(new DataTable.Filter("Date", DataTable.FilterType.NOT_EQUALS, ""))
				.withColumnProcessor("Period0", typePivot)
				.withColumnProcessor("Period2", typePivot)
				.read(fileManager);

		DataTableReducer reducer1 = new DataTableReducer.Builder()
				.withKeyColumns("ID", "Name")
				.withOperation("Late", Operation.SUM)
				.withOperation("Excused", Operation.SUM)
				.withOperation("Tardy", Operation.SUM)
				.withOperation("Absent", Operation.SUM)
				.build();

		DataTable totalsByStudent = reducer1.reduce(attendanceTable);

		DataTable missingAssignmentsTable = new DataTable.Builder()
				.withFilePath(assignmentFile)
				.withColumns("ID", "PointsEarned", "Percent")
				.withColumnProcessor("PointsEarned", (column, oldValue) -> {
					StringRow res = new StringRow();
					res.put("Missing", oldValue.toLowerCase().contains("missing") ? "1" : "0");
					return res;
				})
				.withColumnProcessor("Percent", (column, oldValue) -> {
					StringRow res = new StringRow();
					res.put("Assignments", "1");
					return res;
				})
				.withFilter(new DataTable.Filter("Percent", DataTable.FilterType.NOT_EQUALS, ""))
				.read(fileManager);

		DataTableReducer reducer2 = new DataTableReducer.Builder()
				.withKeyColumns("ID")
				.withOperation("Assignments", Operation.SUM)
				.withOperation("Missing", Operation.SUM)
				.build();

		DataTable assignmentsByStudent = reducer2.reduce(missingAssignmentsTable);

		DataTableJoiner joiner1 = new DataTableJoiner.Builder()
				.joinColumns("ID")
				.reportColumns("Name", "Assignments", "Missing", "Late", "Excused", "Tardy", "Absent")
				.build();

		DataTable joined = joiner1.joinTables(totalsByStudent, assignmentsByStudent);

		return joined.hidePii(piiHidden, "Name");
	}
}
