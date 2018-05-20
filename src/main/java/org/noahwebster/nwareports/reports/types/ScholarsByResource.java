package org.noahwebster.nwareports.reports.types;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.data.DataTableJoiner;
import org.noahwebster.nwareports.datatypes.StringRow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ScholarsByResource extends Report {
	protected String scholarFile;
	protected String spedFile;

	@Override
	public DataTable executeReport() {
		DataTable scholars = new DataTable.Builder()
				.withFilePath(scholarFile)
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
				.withFilePath(spedFile)
				.withColumns("ResourceType", "ident as Id")
				.withFilter(new DataTable.Filter("EndDate", DataTable.FilterType.EQUALS, ""))
				.read(fileManager);

		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("Id")
				.reportColumns("Id", "FirstName", "LastName", "ResourceType")
				.build();

		DataTable joined = joiner.joinTables(scholars, resources);

		return joined.hidePii(piiHidden, "Id", "FirstName", "LastName");
	}
}
