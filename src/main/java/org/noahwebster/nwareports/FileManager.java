package org.noahwebster.nwareports;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

public class FileManager {
	private static final String LOCAL_PATH = "C:\\NWAReports\\";

	public static FileReader getFileReader(String filePath) throws FileNotFoundException {
		return new FileManager().getLocalFile(filePath);
	}

	private FileReader getLocalFile(String filePath) throws FileNotFoundException {
		try {
			return new FileReader(LOCAL_PATH + filePath);
		}
		catch (FileNotFoundException ex) {
			ClassLoader classLoader = getClass().getClassLoader();
			URL url = classLoader.getResource(filePath);
			if (url != null)
				return new FileReader(url.getPath());
			throw new FileNotFoundException(filePath);
		}
	}
}
