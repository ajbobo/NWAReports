package org.noahwebster.nwareports.datatypes;

import java.util.LinkedHashMap;
import java.util.Set;

public class GenericRow extends LinkedHashMap<String, Object> {
	public Set<String> columnNames() {
		return this.keySet();
	}
}
