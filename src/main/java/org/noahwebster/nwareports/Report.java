package org.noahwebster.nwareports;

@SuppressWarnings("unused")
public abstract class Report {
	protected String name;
	protected String description;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public abstract DataTable executeReport();
}
