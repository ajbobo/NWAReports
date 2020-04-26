package org.ajbobo.scholarreports.web;

import org.ajbobo.scholarreports.data.DataTable;
import org.ajbobo.scholarreports.data.FileManager;
import org.ajbobo.scholarreports.reports.types.Report;
import org.ajbobo.scholarreports.reports.util.ReportFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ScholarReports_WebController {

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
