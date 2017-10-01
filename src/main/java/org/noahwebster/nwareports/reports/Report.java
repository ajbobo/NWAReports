package org.noahwebster.nwareports.reports;

import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.data.FileManager;

@SuppressWarnings("unused")
public abstract class Report {
	protected String name;
	protected String description;
	protected FileManager fileManager;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public abstract DataTable executeReport();
}
