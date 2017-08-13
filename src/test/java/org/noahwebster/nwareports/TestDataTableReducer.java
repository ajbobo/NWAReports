package org.noahwebster.nwareports;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestDataTableReducer {
	private DataTable testData;

	@BeforeTest
	public void beforeTest() {
		testData = new DataTable.Reader()
				.withFilePath("testTable1.csv")
				.read();
	}

	@Test
	public void testOneKey_Sum() {
		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("One")
				.withOperation("Value1", DataTableReducer.Operation.SUM)
				.build();
		DataTable res = reducer.reduce(testData);
		Util.printTable(res);
	}

	@Test
	public void testTwoKey_Sum() {
		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("One", "Two")
				.withOperation("Value1", DataTableReducer.Operation.SUM)
				.build();
		DataTable res = reducer.reduce(testData);
		Util.printTable(res);
	}

	@Test
	public void testOneKey_SumAvg() {
		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("One")
				.withOperation("Value1", DataTableReducer.Operation.SUM)
				.withOperation("Value2", DataTableReducer.Operation.AVERAGE)
				.build();
		DataTable res = reducer.reduce(testData);
		Util.printTable(res);
	}

	@Test
	public void testOneKey_SumAvg2() {
		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("Two")
				.withOperation("Value1", DataTableReducer.Operation.SUM)
				.withOperation("Value2", DataTableReducer.Operation.AVERAGE)
				.build();
		DataTable res = reducer.reduce(testData);
		Util.printTable(res);
	}

	@Test
	public void testOneKey_MinMax() {
		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("Two")
				.withOperation("Value1", DataTableReducer.Operation.MINIMUM)
				.withOperation("Value2", DataTableReducer.Operation.MAXIMUM)
				.build();
		DataTable res = reducer.reduce(testData);
		Util.printTable(res);
	}
}
