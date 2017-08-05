package org.noahwebster.nwareports;

import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		printTable(table, 15);
	}

	@Test
	public void testLimitedColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withColumnNames("Textbox20", "StudentName")
				.read();
		printTable(table);
	}

	@Test
	public void testLimitedUniqueColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withColumnNames("Textbox20", "StudentName")
				.uniqueOnly()
				.read();
		printTable(table);
	}

	@Test
	public void testLimitedAliasedColumns() {
		DataTable table = new DataTable.Reader()
				.withFilePath("C:\\NWAReports\\StudentAssessment.csv")
				.withColumnNames("Textbox20 as Grade", "StudentName as    Student Name    ")
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
		printTable(table, 0);
	}

	private void printTable(DataTable table, int limit) {
		List<Map<String, String>> theTable = table.getData();
		Set<String> columns = theTable.get(0).keySet();
		for (String colName : columns)
			System.out.print(colName + "\t");
		System.out.println();
//		for (Map<String, String> row : table.getData()) {
		Iterator<Map<String, String>> iterator = theTable.iterator();
		for (int x = 0; (limit == 0 || x < limit) && iterator.hasNext(); x++) {
			Map<String, String> row = iterator.next();
			for (String colName : columns)
				System.out.print(row.get(colName) + "\t");
			System.out.println();
		}
	}
}
