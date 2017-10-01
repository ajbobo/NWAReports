package org.noahwebster.nwareports.data;

import org.noahwebster.nwareports.types.GenericRow;
import org.noahwebster.nwareports.types.StringRow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DataTableReducer {
	private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
	private String[] keyColumns;
	private LinkedHashMap<String, Operation> columnOperations;

	private DataTableReducer(String[] keyColumns, LinkedHashMap<String, Operation> columnOperations) {
		this.keyColumns = keyColumns;
		this.columnOperations = columnOperations;
	}

	public DataTable reduce(DataTable startingTable) {
		List<StringRow> startingData = startingTable.getData();
		if (null == startingData)
			return startingTable;

		List<GenericRow> dataToReduce = getDataToReduce(startingData);
		List<StringRow> results = calculateReducedData(dataToReduce);
		return new DataTable(results);
	}

	private StringRow getKeyColumnsFromRow(StringRow curRow) {
		StringRow results = new StringRow();
		for (String col : keyColumns)
			results.put(col, curRow.get(col));
		return results;
	}

	private GenericRow findReducedRow(StringRow keyColumns, List<GenericRow> newData) {
		// Is there already a row with these key columns?
		for (GenericRow curRow : newData) {
			boolean foundIt = true;
			for (String col : keyColumns.keySet()) {
				String val1 = keyColumns.get(col);
				String val2 = curRow.get(col).toString();
				if (!val1.equals(val2))
					foundIt = false;
			}
			if (foundIt)
				return curRow;
		}
		// If we get here, there isn't a row with these key columns. Create it
		GenericRow newRow = new GenericRow();
		for (String col : keyColumns.columnNames())
			newRow.put(col, keyColumns.get(col));
		newData.add(newRow);
		return newRow;
	}

	private List<GenericRow> getDataToReduce(List<StringRow> startingData) {
		// Build a table that has the key columns, plus a column for each column to reduce
		// The reduce columns with have an ArrayList of all the raw data that will be fed to the Stream API
		List<GenericRow> dataToReduce = new ArrayList<>();

		for (StringRow curRow : startingData) {
			StringRow keyColumns = getKeyColumnsFromRow(curRow);
			GenericRow newRow = findReducedRow(keyColumns, dataToReduce);
			for (String curColumn : columnOperations.keySet()) {
				if (!newRow.containsKey(curColumn))
					newRow.put(curColumn, new ArrayList<Double>());
				Object colObj = newRow.get(curColumn);
				if (colObj instanceof ArrayList) {
					@SuppressWarnings("unchecked") ArrayList<Double> array = (ArrayList<Double>)colObj;
					array.add(Double.parseDouble(curRow.get(curColumn)));
				}
			}
		}

		return dataToReduce;
	}

	private List<StringRow> calculateReducedData(List<GenericRow> dataToReduce) {
		List<StringRow> finalData = new ArrayList<>();
		for (GenericRow rawRow : dataToReduce) {
			StringRow newRow = new StringRow();
			for (String column : rawRow.columnNames()) {
				if (columnOperations.containsKey(column)) {
					@SuppressWarnings("unchecked") ArrayList<Double> dataList = (ArrayList<Double>)rawRow.get(column);
					newRow.put(column, decimalFormat.format(columnOperations.get(column).Calculate(dataList)));
				}
				else {
					newRow.put(column, (String)rawRow.get(column));
				}
			}
			finalData.add(newRow);
		}
		return finalData;
	}

	public static class Builder {
		private String[] keyColumns;
		private LinkedHashMap<String, Operation> columnOperations;

		public Builder() {
			columnOperations = new LinkedHashMap<>();
			keyColumns = new String[]{};
		}

		public Builder withKeyColumns(String... columns) {
			this.keyColumns = columns;
			return this;
		}

		public Builder withOperation(String column, Operation operation) {
			this.columnOperations.put(column, operation);
			return this;
		}

		public DataTableReducer build() {
			return new DataTableReducer(keyColumns, columnOperations);
		}
	}

	public enum Operation {
		SUM((a) -> a.stream().reduce(0.0, Double::sum)),
		MINIMUM((a) -> a.stream().mapToDouble(b -> b).min().orElse(0.0)),
		MAXIMUM((a) -> a.stream().mapToDouble(b -> b).max().orElse(0.0)),
		AVERAGE((a) -> a.stream().mapToDouble(b -> b).average().orElse(Double.NaN));

		private OperationFunc function;

		Operation(OperationFunc function) {
			this.function = function;
		}

		public double Calculate(ArrayList<Double> values) {
			return function.PerformOperation(values);
		}
	}

	private interface OperationFunc {
		double PerformOperation(ArrayList<Double> rawData);
	}
}
