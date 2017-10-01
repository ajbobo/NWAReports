package org.noahwebster.nwareports;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestDataTableReducer {
	private DataTable testData;

	@BeforeTest
	public void beforeTest() {
		FileManager fileManager = new FileManager();
		testData = new DataTable.Builder()
				.withFilePath("testTable1.csv")
				.read(fileManager);
	}

	@Test
	public void testOneKey_Sum() {
		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("One")
				.withOperation("Value1", DataTableReducer.Operation.SUM)
				.build();
		DataTable res = reducer.reduce(testData);
		Util.printTable(res);
		Assert.assertEquals(res.getData().size(), 3);
		Assert.assertEquals(res.getData().get(0).get("Value1"), "7");
		Assert.assertEquals(res.getData().get(1).get("Value1"), "8");
		Assert.assertEquals(res.getData().get(2).get("Value1"), "25");
	}

	@Test
	public void testTwoKey_Sum() {
		DataTableReducer reducer = new DataTableReducer.Builder()
				.withKeyColumns("One", "Two")
				.withOperation("Value1", DataTableReducer.Operation.SUM)
				.build();
		DataTable res = reducer.reduce(testData);
		Util.printTable(res);Assert.assertEquals(res.getData().size(), 13);
		Assert.assertEquals(res.getData().get(0).get("Value1"), "1");
		Assert.assertEquals(res.getData().get(3).get("Value1"), "4");
		Assert.assertEquals(res.getData().get(12).get("Value1"), "7");
		Assert.assertEquals(res.getData().get(12).get("Two"), "E");
		Assert.assertEquals(res.getData().get(12).get("One"), "Three");
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
		Assert.assertEquals(res.getData().size(), 3);
		Assert.assertEquals(res.getData().get(1).get("Value1"), "8");
		Assert.assertEquals(res.getData().get(1).get("Value2"), "9.5");
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
		Assert.assertEquals(res.getData().size(), 5);
		Assert.assertEquals(res.getData().get(1).get("Value1"), "7");
		Assert.assertEquals(res.getData().get(1).get("Value2"), "4.67");
		Assert.assertEquals(res.getData().get(3).get("Value1"), "12");
		Assert.assertEquals(res.getData().get(3).get("Value2"), "6.83");
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
		Assert.assertEquals(res.getData().size(), 5);
		Assert.assertEquals(res.getData().get(4).get("Value1"), "7");
		Assert.assertEquals(res.getData().get(4).get("Value2"), "5");
	}
}
