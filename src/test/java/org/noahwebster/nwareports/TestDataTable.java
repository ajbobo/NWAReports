package org.noahwebster.nwareports;

import org.noahwebster.nwareports.types.StringRow;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDataTable {
	@Test
	public void testDataTable() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.read();
		Util.printTable(table);
	}

	@Test
	public void testSkipRows() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\AttendanceByDay_16.csv")
				.withStartRow(3)
				.read();
		Util.printTable(table, 15);
	}

	@Test
	public void testLimitedColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.withColumns("Textbox20", "StudentName")
				.read();
		Util.printTable(table);
	}

	@Test
	public void testLimitedUniqueColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.withColumns("Textbox20 as Grade", "StudentName")
				.uniqueOnly()
				.read();
		Util.printTable(table);
	}

	@Test
	public void testLimitedUniqueProcessedColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.withColumns("Textbox20", "StudentName")
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
		Util.printTable(table, 5);
	}

	@Test
	public void testLimitedAliasedColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.withColumns("Textbox20 as Grade", "  StudentName   as    Student Name    ")
				.read();
		Util.printTable(table);
	}

	@Test
	public void testFilteredData() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.withFilter(new DataTable.Filter("Textbox20", DataTable.FilterType.EQUALS, "Grade:  2"))
				.read();
		Util.printTable(table);
	}

	@Test
	public void testFilteredData2() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.withFilter(new DataTable.Filter("ScoreMethodTitle1", DataTable.FilterType.EQUALS, "Proficiency"))
				.withFilter(new DataTable.Filter("Category", DataTable.FilterType.EQUALS, " 2016"))
				.read();
		Util.printTable(table);
	}

	@Test
	public void testFilteredDataAndColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment_16.csv")
				.withFilter(new DataTable.Filter("Textbox20", DataTable.FilterType.EQUALS, "Grade:  2"))
				.withColumns("StudentName", "Textbox20")
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
		Util.printTable(table);
	}

	
}
