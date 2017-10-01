package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.data.DataTableJoiner;
import org.noahwebster.nwareports.types.StringRow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScholarsByResource extends Report {
	public static final String REPORT_NAME = "Scholars By Resource (2016)";

	public ScholarsByResource() {
		name = REPORT_NAME;
		description = "Scholars by resources - 2016";
	}

	@Override
	public DataTable executeReport() {
		DataTable scholars = new DataTable.Builder()
				.withFilePath("ElementaryReportCard_16.csv")
				.withColumns("Grade", "Name")
				.withColumnProcessor("Name", (column, oldValue) -> {
					StringRow res = new StringRow();
					Pattern pattern = Pattern.compile("(?<Last>.+),\\s+(?<First>\\S+)\\s+[(](?<Id>\\d+)[)]");
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
				.read(fileManager);

		DataTable resources = new DataTable.Builder()
				.withFilePath("SpEd_16.csv")
				.withColumns("ResourceType", "ident as Id")
				.withFilter(new DataTable.Filter("EndDate", DataTable.FilterType.EQUALS, ""))
				.read(fileManager);

		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("Id")
				.reportColumns("Id", "FirstName", "LastName", "ResourceType")
				.build();

		return joiner.joinTables(scholars, resources);
	}
}
