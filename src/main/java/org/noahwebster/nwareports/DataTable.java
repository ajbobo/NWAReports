package org.noahwebster.nwareports;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.*;

public class DataTable {
	private List<Map<String, String>> data;

	private DataTable(String filePath, int startRow, String[] columnNames, List<Filter> filters, boolean uniqueOnly) {
		try {
			CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', startRow);

			// Read the headers of the raw data
			String[] headers = reader.readNext();

			// For each row in the raw data, determine which values should be in the results
			data = new ArrayList<>();
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				// Does this row match ALL of the assigned filters?
				// TODO: Can I do this with Java Stream API instead?
				boolean isValidRow = true;
				if (nextLine.length == 1 && nextLine[0].equalsIgnoreCase("")) // Skip empty rows
					isValidRow = false;
				if (filters != null) {
					Iterator iterator = filters.iterator();
					while (isValidRow && iterator.hasNext()) {
						Filter filter = (Filter) iterator.next();
						isValidRow = filter.isValidRow(nextLine, headers);
					}
				}

				// Insert the requested column values from the row into the results
				List<String> requestedColumns = Arrays.asList(columnNames != null ? columnNames : headers);
				if (isValidRow) {
					LinkedHashMap<String, String> dataLine = new LinkedHashMap<>();
					for (int x = 0; x < nextLine.length; x++) {
						String curColumn = headers[x];
						if (requestedColumns.contains(curColumn))
							dataLine.put(curColumn, nextLine[x]);
					}
					if (!uniqueOnly || isUnique(dataLine))
						data.add(dataLine);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			this.data = null;
		}
	}

	private boolean isUnique(Map<String, String> dataLine) {
		boolean unique = true;
		for (Map<String, String> curLine : data) {
			boolean matchesRow = true;
			for (String colName : dataLine.keySet())
				matchesRow &= curLine.get(colName).equalsIgnoreCase(dataLine.get(colName));
			if (matchesRow)
				unique = false;
		}
		return unique;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public static class Reader {
		private String filePath;
		private int startRow;
		private String[] columnNames;
		private List<Filter> filters;
		private boolean uniqueOnly;

		public Reader() {
			this.filePath = null;
			this.startRow = 0;
			this.columnNames = null;
			this.filters = null;
			this.uniqueOnly = false;
		}

		public Reader withFilePath(String filePath) {
			this.filePath = filePath;
			return this;
		}

		public Reader withStartRow(int startRow) {
			this.startRow = startRow;
			return this;
		}

		public Reader withColumnNames(String... columnNames) {
			this.columnNames = columnNames;
			return this;
		}

		public Reader withFilter(Filter filter) {
			if (this.filters == null)
				this.filters = new ArrayList<>();
			this.filters.add(filter);
			return this;
		}

		public Reader uniqueOnly() {
			this.uniqueOnly = true;
			return this;
		}

		public DataTable read() {
			return new DataTable(filePath, startRow, columnNames, filters, uniqueOnly);
		}
	}

	public static class Filter {
		private String column;
		private String value;
		private FilterType type;

		public Filter(String column, FilterType type, String value) {
			this.column = column;
			this.type = type;
			this.value = value;
		}

		public boolean isValidRow(String[] nextLine, String[] headers) {
			int targetCol = -1;
			for (int x = 0; x < headers.length; x++) {
				if (headers[x].equalsIgnoreCase(column))
					targetCol = x;
			}
			if (targetCol == -1) // Didn't find the requested column
				return false;

			String targetValue = nextLine[targetCol];

			switch (type) {
				case EQUALS:
					return value.equalsIgnoreCase(targetValue);
			}

			return false;
		}
	}

	public enum FilterType {
		EQUALS
	}
}
