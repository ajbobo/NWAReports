package org.noahwebster.nwareports.data;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class FileManager {
	private static final String LOCAL_PATH = "C:\\NWAReports\\";
	private static final String FOLDER_ID = "id:V6swvdgbfbIAAAAAAAAqfQ";
	private String dbAccessToken;

	public Reader getFileReader(String filePath) throws FileNotFoundException {
		Reader res = null;
		if (null != dbAccessToken)
			res = getDropboxFile(filePath);
		if (res == null)
			res = getLocalFile(filePath);
		return res;
	}

	private Reader getDropboxFile(String filePath) {
		try {
			System.out.println("Reading from Dropbox - " + filePath);
			DbxRequestConfig config = new DbxRequestConfig("NWAReports");
			DbxClientV2 client = new DbxClientV2(config, dbAccessToken);

			DbxUserFilesRequests files = client.files();
			DbxDownloader<FileMetadata> download = files.download(FOLDER_ID + "/" + filePath);

			System.out.println("Success");
			return new InputStreamReader(download.getInputStream());
		}
		catch (DbxException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private Reader getLocalFile(String filePath) throws FileNotFoundException {
		System.out.println("Reading local file - " + filePath);
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

	public void setDbAccessToken(String dbAccessToken) {
		this.dbAccessToken = dbAccessToken;
	}

	public boolean hasDbAccessToken() {
//		System.out.println("AccessToken: " + (dbAccessToken != null ? dbAccessToken : "<<null>>"));
		return this.dbAccessToken != null;
	}


}
