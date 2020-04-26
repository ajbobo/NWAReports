package org.noahwebster.nwareports.data;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.*;
import java.net.URL;

public class FileManager {
	private static final String LOCAL_PATH = "ReportData//"; // This is good when using fake test data

	public Reader getFileReader(String filePath) throws FileNotFoundException {
		return getLocalFile(filePath);
	}

	private Reader getLocalFile(String filePath) throws FileNotFoundException {
		System.out.println("Reading local resource file - " + filePath);
		URL resourcePath = getClass().getClassLoader().getResource(LOCAL_PATH + filePath);
		if (resourcePath == null)
			throw new FileNotFoundException(filePath);

		File file = new File(resourcePath.getFile());
		return new FileReader(file);
	}
}
