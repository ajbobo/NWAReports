package org.ajbobo.scholarreports.reports.types;

import org.ajbobo.scholarreports.data.DataTable;
import org.ajbobo.scholarreports.data.FileManager;

@SuppressWarnings("unused")
public abstract class Report {
	protected String name;
	protected String description;
	protected FileManager fileManager;
	protected boolean piiHidden;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void hidePii(boolean enable) { this.piiHidden = enable; }

	public abstract DataTable executeReport();
}
