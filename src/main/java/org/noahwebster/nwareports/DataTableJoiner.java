package org.noahwebster.nwareports;

import org.noahwebster.nwareports.types.StringRow;

import java.util.ArrayList;

public class DataTableJoiner {
	private String[] joinColumns;
	private String[] reportColumns;
	private boolean uniqueOnly;

	private DataTableJoiner(String[] joinColumns, String[] reportColumns, boolean uniqueOnly) {
		this.joinColumns = joinColumns;
		this.reportColumns = reportColumns;
		this.uniqueOnly = uniqueOnly;
	}

	public DataTable joinTables(DataTable left, DataTable right) {
		if (null == left.getData() || null == right.getData())
			return getErrorTable(left, right);

		ArrayList<StringRow> results = new ArrayList<>();
		for (StringRow leftRow : left.getData()) {
			for (StringRow rightRow : right.getData()) {
				if (rowsMatch(leftRow, rightRow)) {
					results.add(joinRows(leftRow, rightRow));
				}
			}
		}
		return new DataTable(results, uniqueOnly);
	}

	private DataTable getErrorTable(DataTable left, DataTable right) {
		DataTable errorTable = new DataTable();
		for (String error : left.getErrors())
			errorTable.addError(error);
		for (String error : right.getErrors())
			errorTable.addError(error);
		return errorTable;
	}

	private boolean rowsMatch(StringRow leftRow, StringRow rightRow) {
		for (String column : joinColumns) {
			if (!leftRow.hasColumn(column) || !rightRow.hasColumn(column))
				return false;
			String leftValue = leftRow.get(column);
			String rightValue = rightRow.get(column);
			if (!leftValue.equals(rightValue))
				return false;
		}
		return true;
	}

	private StringRow joinRows(StringRow leftRow, StringRow rightRow) {
		StringRow results = new StringRow();
		for (String column : reportColumns) {
			if (leftRow.hasColumn(column))
				results.put(column, leftRow.get(column));
			else if (rightRow.hasColumn(column))
				results.put(column, rightRow.get(column));
			else
				results.put(column, "");
		}
		return results;
	}

	public static class Builder {
		String[] joinColumns;
		String[] reportColumns;
		boolean uniqueOnly = false;

		public Builder joinColumns(String... columns) {
			this.joinColumns = columns;
			return this;
		}

		public Builder reportColumns(String... columns) {
			this.reportColumns = columns;
			return this;
		}

		public Builder uniqueOnly() {
			this.uniqueOnly = true;
			return this;
		}

		public DataTableJoiner build() {
			return new DataTableJoiner(joinColumns, reportColumns, uniqueOnly);
		}
	}
}
