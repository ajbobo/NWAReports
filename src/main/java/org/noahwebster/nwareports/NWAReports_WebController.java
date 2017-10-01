package org.noahwebster.nwareports;

import com.dropbox.core.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NWAReports_WebController {

	private static final String TOKEN_SESSION_KEY = "DbAccessToken";

	private final static String APP_ID = "7kgahrejtycnnpx";
	private final static String APP_SECRET = "di01xk7ljjh4pki";
	private final static String REDIRECT_URI = "/dbredirect";

	private static DbxWebAuth auth = null;
	private static String redirectUrl;

	private void setupAuth(HttpServletRequest request) {
		DbxRequestConfig requestConfig = new DbxRequestConfig("nwareports/0.1");
		DbxAppInfo appInfo = new DbxAppInfo(APP_ID, APP_SECRET);
		auth = new DbxWebAuth(requestConfig, appInfo);

		String fullpath = new String(request.getRequestURL());
		String endpoint = request.getRequestURI();
		String callingdomain = fullpath.replace(endpoint, "");
		redirectUrl = callingdomain + REDIRECT_URI;
	}

	@RequestMapping("/reports")
	public List<Report> getReports() {
		ArrayList<Report> reports = new ArrayList<>();
		for (String report : ReportFactory.getReportNames())
			reports.add(ReportFactory.getReport(report));

		return reports;
	}

	@RequestMapping("/reports/{reportName}")
	public DataTable executeReport(@PathVariable("reportName") String reportName, HttpSession httpSession) {
		Report report = ReportFactory.getReport(reportName);
		if (report != null) {
			Object tokenObj = httpSession.getAttribute(TOKEN_SESSION_KEY);
			FileManager fileManager = new FileManager();
			fileManager.setDbAccessToken(tokenObj != null ? tokenObj.toString() : null);
			report.setFileManager(fileManager);
			return report.executeReport();
		}
		return null;
	}

	@RequestMapping("/login")
	public SimpleMessage login(HttpServletRequest request, HttpSession httpSession) {
		if (auth == null)
			setupAuth(request);

		String sessionKey = "dropbox-auth-csrf-token";
		DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
				.withRedirectUri(redirectUrl, new DbxStandardSessionStore(httpSession, sessionKey))
				.build();

		String authorizeUrl = auth.authorize(authRequest);

		return new SimpleMessage(authorizeUrl);
	}

	@RequestMapping("/dbredirect")
	public void completeAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Fetch the session to verify our CSRF token
		HttpSession session = request.getSession(true);
		String sessionKey = "dropbox-auth-csrf-token";
		DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);

		DbxAuthFinish authFinish;
		try {
			authFinish = auth.finishFromRedirect(redirectUrl, csrfTokenStore, request.getParameterMap());
		}
		catch (DbxWebAuth.BadRequestException ex) {
//			log("On /dropbox-auth-finish: Bad request: " + ex.getMessage());
			response.sendError(400);
			request.getSession().setAttribute(TOKEN_SESSION_KEY, null);
			return;
		}
		catch (DbxWebAuth.BadStateException ex) {
			// Send them back to the start of the auth flow.
			response.sendRedirect(REDIRECT_URI);
			request.getSession().setAttribute(TOKEN_SESSION_KEY, null);
			return;
		}
		catch (DbxWebAuth.CsrfException ex) {
//			log("On /dropbox-auth-finish: CSRF mismatch: " + ex.getMessage());
			response.sendError(403, "Forbidden.");
			request.getSession().setAttribute(TOKEN_SESSION_KEY, null);
			return;
		}
		catch (DbxWebAuth.NotApprovedException ex) {
			// When Dropbox asked "Do you want to allow this app to access your
			// Dropbox account?", the user clicked "No".
			request.getSession().setAttribute(TOKEN_SESSION_KEY, null);
			return;
		}
		catch (DbxWebAuth.ProviderException | DbxException ex) {
//			log("On /dropbox-auth-finish: Auth failed: " + ex.getMessage());
			response.sendError(503, "Error communicating with Dropbox.");
			request.getSession().setAttribute(TOKEN_SESSION_KEY, null);
			return;
		}
		String accessToken = authFinish.getAccessToken();
		request.getSession().setAttribute(TOKEN_SESSION_KEY, accessToken);
	}

	@RequestMapping("/isconnected")
	public SimpleMessage isConnected() {
//		if (null != fileManager && fileManager.hasDbAccessToken())
//			return new SimpleMessage("true");
		return new SimpleMessage("false");
	}

	class SimpleMessage {
		private String message;

		public SimpleMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}
