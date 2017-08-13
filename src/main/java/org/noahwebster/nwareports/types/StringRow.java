package org.noahwebster.nwareports.types;

import java.util.LinkedHashMap;
import java.util.Set;

public class StringRow extends LinkedHashMap<String, String>{
	public Set<String> columnNames() {
		return this.keySet();
	}
}
