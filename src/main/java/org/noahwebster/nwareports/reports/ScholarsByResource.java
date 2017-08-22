package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.DataTable;
import org.noahwebster.nwareports.DataTableJoiner;
import org.noahwebster.nwareports.Report;
import org.noahwebster.nwareports.types.StringRow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScholarsByResource extends Report {
	public static final String REPORT_NAME = "Scholars By Resource";

	public ScholarsByResource() {
		name = REPORT_NAME;
		description = "Scholars by resources";
	}

	@Override
	public DataTable executeReport() {
		DataTable scholars = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\ElementaryReportCard.csv")
				.withColumns("Grade", "Name")
				.withColumnProcessor("Name", (column, oldValue) -> {
					StringRow res = new StringRow();
					Pattern pattern = Pattern.compile("(?<Last>\\S+),\\s+(?<First>\\S+)\\s+[(](?<Id>\\d+)[)]");
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

		DataTable resources = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\SpEd.csv")
				.withColumns("ResourceType", "ident as Id")
				.withFilter(new DataTable.Filter("EndDate", DataTable.FilterType.EQUALS, ""))
				.read();

		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("Id")
				.reportColumns("Id", "FirstName", "LastName", "ResourceType")
				.build();

		return joiner.joinTables(scholars, resources);
	}
}
