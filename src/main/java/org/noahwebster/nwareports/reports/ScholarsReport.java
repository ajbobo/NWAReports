package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.types.StringRow;
import org.noahwebster.nwareports.DataTable;
import org.noahwebster.nwareports.Report;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScholarsReport extends Report {
	public static final String REPORT_NAME = "Scholars (2016)";

	public ScholarsReport() {
		name = REPORT_NAME;
		description = "List of Scholars - 2016";
	}


	@Override
	public DataTable executeReport() {
		return new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.withColumns("StudentName", "Textbox20")
				.withColumnProcessor("StudentName", (column, oldValue) -> {
					StringRow res = new StringRow();
					Pattern pattern = Pattern.compile("(?<First>\\S+)\\s+(?<Last>\\S+)\\s+[(](?<Id>\\d+)[)]");
					Matcher matcher = pattern.matcher(oldValue);
					if (matcher.find()) {
						res.put("FirstName", matcher.group("First"));
						res.put("LastName", matcher.group("Last"));
						res.put("Id", matcher.group("Id"));
					}
					else {
						res.put("FirstName", "");
						res.put("LastName", oldValue);
						res.put("Id", "0");
					}
					return res;
				})
				.withColumnProcessor("Textbox20", (column, oldValue) -> {
					StringRow res = new StringRow();
					Pattern pattern = Pattern.compile("Grade:\\s+(?<Grade>\\d+)");
					Matcher matcher = pattern.matcher(oldValue);
					if (matcher.find())
						res.put("Grade", matcher.group("Grade"));
					else
						res.put("Grade", oldValue);
					return res;
				})
				.uniqueOnly()
				.read();
	}
}
