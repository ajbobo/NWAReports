package org.noahwebster.nwareports;

import org.testng.annotations.Test;

public class TestDropboxRead {

	@Test
	public void testReadFromDropBox() {
		String access_token = Util.getProperty("dropbox.access_token", System.getProperty("user.home") + "/credentials.properties");
		System.out.println("Access token: " + access_token);

		FileManager fileManager = new FileManager();
		fileManager.setDbAccessToken(access_token);
		DataTable res = new DataTable.Builder()
				.withFilePath("EnrolledStudents.csv")
				.read(fileManager);
		Util.printTable(res);
	}
}
