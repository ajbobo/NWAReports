package org.ajbobo.scholarreports;

import org.ajbobo.scholarreports.data.DataTable;
import org.ajbobo.scholarreports.data.FileManager;
import org.ajbobo.scholarreports.datatypes.StringRow;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDataTable {
	private FileManager fileManager;

	@BeforeTest
	public void beforeTest() {
		fileManager = new FileManager();
	}

	@Test
	public void testDataTable() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.read(fileManager);
		Util.printTable(table);
	}

	@Test
	public void testSkipRowsExtraTables() {
		DataTable table = new DataTable.Builder()
				.withFilePath("SpecialProgramDetailByProgram.csv")
				.withStartRow(3)
				.stopAtBlank()
				.read(fileManager);
		Util.printTable(table);
	}

	@Test
	public void testLimitedColumns() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.withColumns("Textbox20", "StudentName")
				.read(fileManager);
		Util.printTable(table);
	}

	@Test
	public void testLimitedUniqueColumns() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.withColumns("Textbox20 as Grade", "StudentName")
				.uniqueOnly()
				.read(fileManager);
		Util.printTable(table);
	}

	@Test
	public void testLimitedUniqueProcessedColumns() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
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
				.read(fileManager);
		Util.printTable(table, 5);
	}

	@Test
	public void testLimitedAliasedColumns() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.withColumns("Textbox20 as Grade", "  StudentName   as    Student Name    ")
				.read(fileManager);
		Util.printTable(table);
	}

	@Test
	public void testFilteredData() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.withFilter(new DataTable.Filter("Textbox20", DataTable.FilterType.EQUALS, "Grade:  2"))
				.read(fileManager);
		Util.printTable(table);
	}

	@Test
	public void testFilteredData2() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.withFilter(new DataTable.Filter("ScoreMethodTitle1", DataTable.FilterType.EQUALS, "Proficiency"))
				.withFilter(new DataTable.Filter("Category", DataTable.FilterType.EQUALS, "2016"))
				.read(fileManager);
		Util.printTable(table);
	}

	@Test
	public void testFilteredDataAndColumns() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
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
				.read(fileManager);
		Util.printTable(table);
	}

	@Test
	public void testDataTableWithPii() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.read(fileManager);
		table.hidePii(true, "StudentName");
		Util.printTable(table);
	}

	@Test
	public void testDataTableWithPii_ColumnAlias() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.withColumns("StudentName as Name", "Textbox20 as Grade")
				.uniqueOnly()
				.read(fileManager);
		table.hidePii(true, "Name");
		Util.printTable(table);
	}

	@Test
	public void testDataTableWithDisabledPii_ColumnAlias() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
				.withColumns("StudentName as Name", "Textbox20 as Grade")
				.uniqueOnly()
				.read(fileManager);
		table.hidePii(false, "Name");
		Util.printTable(table);
	}

	@Test
	public void testFilteredDataAndColumns_WithPii() {
		DataTable table = new DataTable.Builder()
				.withFilePath("StudentAssessment.csv")
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
				.read(fileManager);
		table.hidePii(true, "StudentName");
		Util.printTable(table);
	}
}
