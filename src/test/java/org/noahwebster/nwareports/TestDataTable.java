package org.noahwebster.nwareports;

import org.testng.annotations.Test;

public class TestDataTable {
	@Test
	public void testDataTable() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.read();
		printTable(table);
	}

	@Test
	public void testSkipRows() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\AttendanceByDay.csv")
				.withStartRow(3)
				.read();
		printTable(table);
	}

	@Test
	public void testFilteredColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withColumnNames(new String[]{"Textbox20", "StudentName"})
				.read();
		printTable(table);
	}

	@Test
	public void testFilteredUniqueColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withColumnNames("Textbox20 as Grade", "StudentName")
				.uniqueOnly()
				.read();
		printTable(table);
	}

	@Test
	public void testFilteredAliasedColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withColumnNames(new String[]{"Textbox20 as Grade", "StudentName as    Student Name    "})
				.read();
		printTable(table);
	}

	@Test
	public void testFilteredData() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withFilter(new DataTable.Filter("Textbox20", DataTable.FilterType.EQUALS, "Grade:  2"))
				.read();
		printTable(table);
	}

	@Test
	public void testFilteredData2() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withFilter(new DataTable.Filter("ScoreMethodTitle1", DataTable.FilterType.EQUALS, "Proficiency"))
				.withFilter(new DataTable.Filter("Category", DataTable.FilterType.EQUALS, " 2016"))
				.read();
		printTable(table);
	}

	@Test
	public void testFilteredDataAndColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withFilter(new DataTable.Filter("Textbox20", DataTable.FilterType.EQUALS, "Grade:  2"))
				.withColumnNames("StudentName", "Textbox20")
				.uniqueOnly()
				.read();
		printTable(table);
	}

	private void printTable(DataTable table) {
		for (String colName : table.getColumnNames())
			System.out.print(colName + "\t");
		System.out.println();
		for (String[] row : table.getData()) {
			for (String val : row)
				System.out.print(val + "\t");
			System.out.println();
		}
	}
}
