package org.noahwebster.nwareports;

import java.util.LinkedHashMap;
import java.util.Set;

public class DataRow extends LinkedHashMap<String, String>{
	public Set<String> columnNames() {
		return this.keySet();
	}
}
