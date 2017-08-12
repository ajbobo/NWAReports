package org.noahwebster.nwareports;

import java.util.LinkedHashMap;

public class DataTableReducer {
	private String[] keyColumns;
	private LinkedHashMap<String, Operation> columnOperations;

	private DataTableReducer(String[] keyColumns, LinkedHashMap<String, Operation> columnOperations) {
		this.keyColumns = keyColumns;
		this.columnOperations = columnOperations;
	}

	public DataTable reduce(DataTable startingTable) {
		return null;
	}

	public class Builder {
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
		SUM,
		MINIMUM,
		MAXIMUM,
		AVERAGE
	}
}
