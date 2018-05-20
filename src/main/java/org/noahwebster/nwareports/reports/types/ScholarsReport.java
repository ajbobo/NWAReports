package org.noahwebster.nwareports.reports.types;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.datatypes.StringRow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ScholarsReport extends Report {
	protected String scholarsFile;

	@Override
	public DataTable executeReport() {
		return new DataTable.Builder()
				.withFilePath(scholarsFile)
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
				.hidePii(piiHidden, "FirstName", "LastName", "StudentID");
	}
}
