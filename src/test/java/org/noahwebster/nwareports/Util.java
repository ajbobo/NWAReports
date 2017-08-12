package org.noahwebster.nwareports;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Util {
	public static void printTable(DataTable table) {
		printTable(table, 0);
	}

	public static void printTable(DataTable table, int limit) {
		List<DataRow> theTable = table.getData();
		Set<String> columns = theTable.get(0).columnNames();
		for (String colName : columns)
			System.out.print(colName + "\t");
		System.out.println();
		Iterator<DataRow> iterator = theTable.iterator();
		for (int x = 0; (limit == 0 || x < limit) && iterator.hasNext(); x++) {
			Map<String, String> row = iterator.next();
			for (String colName : columns)
				System.out.print(row.get(colName) + "\t");
			System.out.println();
		}
	}
}
