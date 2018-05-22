package org.noahwebster.nwareports.reports.types;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.data.DataTableReducer;
import org.noahwebster.nwareports.data.DataTableReducer.Operation;
import org.noahwebster.nwareports.datatypes.StringRow;

public abstract class ScholarAssignments extends Report {
	protected String assignmentFile;

	@Override
	public DataTable executeReport() {

		DataTable missingAssignmentsTable = new DataTable.Builder()
				.withFilePath(assignmentFile)
				.withColumns("ID", "FirstName", "LastName", "Subject", "PointsEarned", "Percent")
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
				.withKeyColumns("ID", "FirstName", "LastName", "Subject")
				.withOperation("Assignments", Operation.SUM)
				.withOperation("Missing", Operation.SUM)
				.build();

		DataTable assignmentsByStudent = reducer2.reduce(missingAssignmentsTable);

		return assignmentsByStudent.hidePii(piiHidden, "ID", "FirstName", "LastName");
	}
}
