package org.noahwebster.nwareports;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestDataTableJoiner {
	private DataTable table1;
	private DataTable table2;

	@BeforeTest
	public void beforeTest() {
		FileManager fileManager = new FileManager();
		table1 = new DataTable.Builder()
				.withFilePath("testTable1.csv")
				.read(fileManager);
		table2 = new DataTable.Builder()
				.withFilePath("testTable2.csv")
				.read(fileManager);
	}

	@Test
	public void testJoinOneColumn() {
		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("One")
				.reportColumns("One", "Two", "Value1", "Value2", "Three", "Four")
				.build();

		DataTable res = joiner.joinTables(table1, table2);
		Util.printTable(res);
		Assert.assertEquals(res.getData().size(), 32);
	}

	@Test
	public void testJoinOneColumn_Duplicates() {
		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("One")
				.reportColumns("One", "Three", "Four")
				.build();

		DataTable res = joiner.joinTables(table1, table2);
		Util.printTable(res);
		Assert.assertEquals(res.getData().size(), 32);
	}

	@Test
	public void testJoinOneColumn_Unique() {
		DataTableJoiner joiner = new DataTableJoiner.Builder()
				.joinColumns("One")
				.reportColumns("One", "Three", "Four")
				.uniqueOnly()
				.build();

		DataTable res = joiner.joinTables(table1, table2);
		Util.printTable(res);
		Assert.assertEquals(res.getData().size(), 6);
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
		Assert.assertEquals(res.getData().size(), 6);
	}
}
