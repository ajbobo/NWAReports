package org.noahwebster.nwareports;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestDataTableJoiner {
	private DataTable table1;
	private DataTable table2;

	@BeforeTest
	public void beforeTest() {
		table1 = new DataTable.Reader()
				.withFilePath("testTable1.csv")
				.read();
		table2 = new DataTable.Reader()
				.withFilePath("testTable2.csv")
				.read();
	}

	@Test
	public void testJoinOneColumn() {
		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("One")
				.reportColumns("One", "Two", "Value1", "Value2", "Three", "Four")
				.build();

		DataTable res = joiner.joinTables(table1, table2);
		Util.printTable(res);
	}

	@Test
	public void testJoinOneColumn_Unique() {
		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("One")
				.reportColumns("One", "Two", "Value1", "Value2", "Three", "Four")
				.uniqueOnly()
				.build();

		DataTable res = joiner.joinTables(table1, table2);
		Util.printTable(res);
	}

	@Test
	public void testJoinTwoColumns_Unique() {
		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("One", "Two")
				.reportColumns("One", "Two", "Value1", "Value2", "Three", "Four")
				.uniqueOnly()
				.build();

		DataTable res = joiner.joinTables(table1, table2);
		Util.printTable(res);
	}
}
