package org.noahwebster.nwareports.types;

import java.util.LinkedHashMap;
import java.util.Set;

public class GenericRow extends LinkedHashMap<String, Object> {
	public Set<String> columnNames() {
		return this.keySet();
	}
}