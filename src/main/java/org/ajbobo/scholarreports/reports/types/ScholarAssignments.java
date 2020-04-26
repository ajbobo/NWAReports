package org.ajbobo.scholarreports.reports.types;

import org.ajbobo.scholarreports.data.DataTable;
import org.ajbobo.scholarreports.data.DataTableReducer;
import org.ajbobo.scholarreports.datatypes.StringRow;

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
				.withOperation("Assignments", DataTableReducer.Operation.SUM)
				.withOperation("Missing", DataTableReducer.Operation.SUM)
				.build();

		DataTable assignmentsByStudent = reducer2.reduce(missingAssignmentsTable);

		return assignmentsByStudent.hidePii(piiHidden, "ID", "FirstName", "LastName");
	}
}
