package org.noahwebster.nwareports;

import com.opencsv.CSVReader;
import org.noahwebster.nwareports.types.StringRow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class DataTable {
	private List<StringRow> data;
	private String[] columnNames;

	private DataTable(String filePath, int startRow, String[] columnNames, List<Filter> filters,
	                  Map<String, ColumnProcessor> processors, boolean uniqueOnly) {
		try {
			FileReader fileReader = getFileReader(filePath);
			CSVReader reader = new CSVReader(fileReader, ',', '"', startRow);

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
				ArrayList<String> requestedColumns = new ArrayList<>(Arrays.asList(columnNames != null ? columnNames : headers));
				generateColumnAliasProcessors(requestedColumns, processors);
				if (isValidRow) {
					StringRow dataLine = new StringRow();
					for (int x = 0; x < nextLine.length; x++) {
						String curColumn = headers[x];
						if (requestedColumns.contains(curColumn)) {
							if (processors == null || !processors.containsKey(curColumn)) {
								dataLine.put(curColumn, nextLine[x]);
							}
							else {
								StringRow newCells = processors.get(curColumn).processCell(curColumn, nextLine[x]);
								for (String col : newCells.columnNames())
									dataLine.put(col, newCells.get(col));
							}
						}
					}
					if (!uniqueOnly || isUnique(dataLine))
						data.add(dataLine);
				}
			}

			this.columnNames = new String[data.get(0).columnNames().size()];
			data.get(0).columnNames().toArray(this.columnNames);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			this.data = null;
		}
	}

	private FileReader getFileReader(String filePath) throws FileNotFoundException {
		try {
			return new FileReader(filePath);
		}
		catch (FileNotFoundException ex) {
			ClassLoader classLoader = getClass().getClassLoader();
			URL url = classLoader.getResource(filePath);
			if (url != null)
				return new FileReader(url.getPath());
			return null;
		}
	}

	public DataTable(List<StringRow> tableData) {
		this.data = tableData;
		if (tableData.size() > 1) {
			this.columnNames = new String[tableData.get(0).columnNames().size()];
			tableData.get(0).columnNames().toArray(this.columnNames);
		}
	}

	private void generateColumnAliasProcessors(List<String> requestedColumns, Map<String, ColumnProcessor> processors) {
		// For "Column as NewName" requests, create a Processor to do the work
		ArrayList<String> columnsToRemove = new ArrayList<>();
		ArrayList<String> columnsToAdd = new ArrayList<>();
		for (String column : requestedColumns) {
			if (column.contains(" as ")) {
				String[] names = column.split(" as ");
				columnsToRemove.add(column);
				columnsToAdd.add(names[0].trim());
				processors.put(names[0].trim(), (column1, oldValue) -> {
					StringRow res = new StringRow();
					res.put(names[1].trim(), oldValue);
					return res;
				});
			}
		}
		requestedColumns.removeAll(columnsToRemove);
		requestedColumns.addAll(columnsToAdd);
	}

	private boolean isUnique(StringRow dataLine) {
		boolean unique = true;
		for (StringRow curLine : data) {
			boolean matchesRow = true;
			for (String colName : dataLine.columnNames())
				matchesRow &= curLine.get(colName).equalsIgnoreCase(dataLine.get(colName));
			if (matchesRow)
				unique = false;
		}
		return unique;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public List<StringRow> getData() {
		return data;
	}

	public static class Reader {
		private String filePath;
		private int startRow;
		private String[] columnNames;
		private List<Filter> filters;
		private Map<String, ColumnProcessor> processors;
		private boolean uniqueOnly;

		public Reader() {
			this.filePath = null;
			this.startRow = 0;
			this.columnNames = null;
			this.filters = new ArrayList<>();
			this.processors = new LinkedHashMap<>();
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

		public Reader withColumns(String... columnNames) {
			this.columnNames = columnNames;
			return this;
		}

		public Reader withFilter(Filter filter) {
			this.filters.add(filter);
			return this;
		}

		public Reader withColumnProcessor(String columnName, ColumnProcessor processor) {
			this.processors.put(columnName, processor);
			return this;
		}

		public Reader uniqueOnly() {
			this.uniqueOnly = true;
			return this;
		}

		public DataTable read() {
			return new DataTable(filePath, startRow, columnNames, filters, processors, uniqueOnly);
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
				case NOT_EQUALS:
					return !value.equalsIgnoreCase(targetValue);
				case CONTAINS:
					return value.contains(targetValue);
			}

			return false;
		}
	}

	public enum FilterType {
		EQUALS,
		NOT_EQUALS,
		CONTAINS
	}

	public interface ColumnProcessor {
		// Given the column and value of a cell, return a map of the new columns
		StringRow processCell(String column, String oldValue);
	}
}
