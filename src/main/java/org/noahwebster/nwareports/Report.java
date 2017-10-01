package org.noahwebster.nwareports;

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
