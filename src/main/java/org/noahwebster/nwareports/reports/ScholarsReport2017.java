package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.types.StringRow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScholarsReport2017 extends Report {
	public static final String REPORT_NAME = "Scholars";

	public ScholarsReport2017() {
		name = REPORT_NAME;
		description = "List of Scholars";
	}

	@Override
	public DataTable executeReport() {
		return new DataTable.Builder()
				.withFilePath("EnrolledStudents.csv")
				.withColumns("StudentName", "StudentID", "GradeLevel", "Gender", "Advisor")
				.withColumnProcessor("StudentName", (column, oldValue) -> {
					StringRow res = new StringRow();
					Pattern pattern = Pattern.compile("(?<Last>\\S.*\\S)\\s*,\\s*(?<First>\\S.*\\S)");
					Matcher matcher = pattern.matcher(oldValue);
					if (matcher.find()) {
						res.put("FirstName", matcher.group("First"));
						res.put("LastName", matcher.group("Last"));
					}
					else {
						res.put("FirstName", "");
						res.put("LastName", oldValue);
					}
					return res;
				})
				.uniqueOnly()
				.read(fileManager)
				.clearPii(piiEnabled, "FirstName", "LastName", "StudentID");
	}
}
