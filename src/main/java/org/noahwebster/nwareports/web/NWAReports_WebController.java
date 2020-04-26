package org.noahwebster.nwareports.web;

import com.dropbox.core.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.noahwebster.nwareports.data.DataTable;
import org.noahwebster.nwareports.data.FileManager;
import org.noahwebster.nwareports.reports.types.Report;
import org.noahwebster.nwareports.reports.util.ReportFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NWAReports_WebController {

	@RequestMapping("/reports")
	public List<Report> getReports() {
		ArrayList<Report> reports = new ArrayList<>();
		for (String report : ReportFactory.getReportNames())
			reports.add(ReportFactory.getReport(report));

		return reports;
	}

	@RequestMapping("/reports/{reportName}")
	public DataTable executeReport(@PathVariable("reportName") String reportName,
	                               @RequestParam String hidepii) {
		Report report = ReportFactory.getReport(reportName);
		if (report != null) {
			report.hidePii(Boolean.parseBoolean(hidepii));
			FileManager fileManager = new FileManager();
			report.setFileManager(fileManager);
			return report.executeReport();
		}
		return null;
	}
}
