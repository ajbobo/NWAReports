package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.types.StringRow;
import org.noahwebster.nwareports.DataTable;
import org.noahwebster.nwareports.Report;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScholarsReport extends Report {
	public static final String REPORT_NAME = "Scholars";

	public ScholarsReport() {
		name = REPORT_NAME;
		description = "List of Scholars";
	}


	@Override
	public DataTable executeReport() {
		return new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\ElementaryReportCard.csv")
				.withColumns("Name", "Grade")
				.withColumnProcessor("Name", (column, oldValue) -> {
					StringRow res = new StringRow();
					Pattern pattern = Pattern.compile("(?<Last>.+),\\s+(?<First>.+)\\s+[(](?<Id>\\d+)[)]");
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
				.uniqueOnly()
				.read();
	}
}
