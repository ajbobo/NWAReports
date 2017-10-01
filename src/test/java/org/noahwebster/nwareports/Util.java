package org.noahwebster.nwareports;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.types.StringRow;

import java.io.FileReader;
import java.util.*;

public class Util {
	public static void printTable(DataTable table) {
		printTable(table, 0);
	}

	public static void printTable(DataTable table, int limit) {
		if (table.getData() != null) {
			Map<String, String> formats = getColumnFormats(table.getData());
			List<StringRow> theTable = table.getData();
			Set<String> columns = theTable.get(0).columnNames();
			System.out.print("|");
			for (String colName : columns)
				System.out.print(String.format(formats.get(colName), colName));
			System.out.println();
			Iterator<StringRow> iterator = theTable.iterator();
			for (int x = 0; (limit == 0 || x < limit) && iterator.hasNext(); x++) {
				Map<String, String> row = iterator.next();
				System.out.print("|");
				for (String colName : columns)
					System.out.print(String.format(formats.get(colName), row.get(colName)));
				System.out.println();
			}
			System.out.println("Total Rows: " + table.getData().size());
		}
		if (table.getErrors() != null) {
			System.out.println("** Errors **");
			for (String msg : table.getErrors())
				System.out.println(msg);
		}
	}

	private static Map<String, String> getColumnFormats(List<StringRow> data) {
		LinkedHashMap<String, Integer> widths = new LinkedHashMap<>();
		for (String colName : data.get(0).columnNames()) {
			widths.put(colName, colName.length());
		}

		for (StringRow row : data) {
			for (String col : row.columnNames()) {
				String val = row.get(col);
				if (val.length() > widths.get(col))
					widths.put(col, val.length());
			}
		}

		LinkedHashMap<String, String> formats = new LinkedHashMap<>();
		for (String col : widths.keySet()) {
			formats.put(col, String.format(" %%-%ds |", widths.get(col)));
		}

		return formats;
	}

	public static String getProperty(String property, String fileName) {
		try {
			Properties prop = new Properties();
			prop.load(new FileReader(fileName));
			return prop.getProperty(property);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
