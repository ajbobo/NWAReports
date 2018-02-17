package org.noahwebster.nwareports.data;

import com.opencsv.CSVReader;
import org.noahwebster.nwareports.types.StringRow;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.*;

public class DataTable {
	private List<StringRow> data;
	private String[] columnNames;
	private List<String> errors;

	private DataTable(String filePath, int startRow, String[] columnNames, List<Filter> filters,
	                  Map<String, ColumnProcessor> processors, boolean uniqueOnly, String[] piiColumns, FileManager fileManager) {
		try {
			if (null == fileManager) {
				addError("No FileManager defined - Are you logged into Dropbox?");
				return;
			}

			Reader fileReader = fileManager.getFileReader(filePath);
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
								addToRow(nextLine[x], curColumn, dataLine);
							}
							else {
								StringRow newCells = processors.get(curColumn).processCell(curColumn, nextLine[x]);
								for (String col : newCells.columnNames())
									addToRow(newCells.get(col), col, dataLine);
							}
						}
					}
					if (!uniqueOnly || isUnique(dataLine))
						data.add(dataLine);
				}
			}

			// Replace Pii columns
			for (StringRow row : data) {
				for (String column : piiColumns)
					row.put(column, "(...)");
			}

			this.columnNames = new String[data.get(0).columnNames().size()];
			data.get(0).columnNames().toArray(this.columnNames);
		}
		catch (FileNotFoundException ex) {
			this.data = null;
			addError("Unable to find or open file: " + ex.getLocalizedMessage());
		}
		catch (Exception ex) {
			this.data = null;
			addError("An unknown error occurred: " + ex.getMessage());
		}
	}

	private void addToRow(String value, String curColumn, StringRow dataLine) {
		dataLine.put(curColumn, value);
	}

	public DataTable() {
		this.data = null;
		this.errors = null;
	}

	public DataTable(List<StringRow> tableData) {
		this(tableData, false);
	}

	public DataTable(List<StringRow> tableData, boolean uniqueOnly) {
		this.data = new ArrayList<>();
		for (StringRow row : tableData) {
			if (!uniqueOnly || isUnique(row)) {
				this.data.add(row);

			}
		}
		if (tableData.size() > 0) {
			Set<String> columns = tableData.get(0).columnNames();
			this.columnNames = columns.toArray(new String[columns.size()]);
		}
	}

	public void addError(String message) {
		if (null == errors)
			errors = new ArrayList<>();
		errors.add(message);
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
					addToRow(oldValue, names[1].trim(), res);
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

	@SuppressWarnings("unused")
	public String[] getColumnNames() {
		return columnNames;
	}

	public List<StringRow> getData() {
		return data;
	}

	public List<String> getErrors() { return errors; }

	public static class Builder {
		private String filePath;
		private int startRow;
		private String[] columnNames;
		private List<Filter> filters;
		private Map<String, ColumnProcessor> processors;
		private boolean uniqueOnly;
		private String[] piiColumns;

		public Builder() {
			this.filePath = null;
			this.startRow = 0;
			this.columnNames = null;
			this.filters = new ArrayList<>();
			this.processors = new LinkedHashMap<>();
			this.uniqueOnly = false;
		}

		public Builder withFilePath(String filePath) {
			this.filePath = filePath;
			return this;
		}

		public Builder withStartRow(int startRow) {
			this.startRow = startRow;
			return this;
		}

		public Builder withColumns(String... columnNames) {
			this.columnNames = columnNames;
			return this;
		}

		public Builder withFilter(Filter filter) {
			this.filters.add(filter);
			return this;
		}

		public Builder withColumnProcessor(String columnName, ColumnProcessor processor) {
			this.processors.put(columnName, processor);
			return this;
		}

		public Builder uniqueOnly() {
			this.uniqueOnly = true;
			return this;
		}

		public DataTable read(FileManager fileManager) {
			return new DataTable(filePath, startRow, columnNames, filters, processors, uniqueOnly, piiColumns, fileManager);
		}

		public Builder enablePii(boolean piiEnabled, String... piiColumns) {
			if (piiEnabled)
				this.piiColumns = piiColumns;
			else
				this.piiColumns = new String[] {}; // The DataTable wants an array, even if it's empty
			return this;
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
