package utilities;

import java.io.File;
import java.util.Base64;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class JiraUtils {

    // 🔹 Jira Base URL
    private static final String JIRA_URL =
            "https://techwaveitsolutions.atlassian.net";

    // 🔹 Jira Login Email
    private static final String EMAIL =
            "techwaveimplementation@gmail.com";

    // 🔹 Jira API Token (ENV VARIABLE ONLY)
    private static final String API_TOKEN =
            System.getenv("JIRA_TOKEN");   // ❌ hardcode kabhi mat karna

    // 🔹 Jira Project Key
    private static final String PROJECT_KEY = "CB";

    /**
     * 🐞 Create Jira Bug
     */
    public static String createBug(String summary, String description, String issueType) {

        try {
            if (API_TOKEN == null || API_TOKEN.isEmpty()) {
                System.err.println("❌ JIRA_TOKEN environment variable not found!");
                return null;
            }

            String auth = EMAIL + ":" + API_TOKEN;
            String encodedAuth =
                    Base64.getEncoder().encodeToString(auth.getBytes());

            RestAssured.baseURI = JIRA_URL;

            Response response = RestAssured
                    .given()
                    .header("Authorization", "Basic " + encodedAuth)
                    .header("Accept", "application/json")
                    .contentType(ContentType.JSON)
                    .body(getPayload(summary, description, issueType))
                    .when()
                    .post("/rest/api/3/issue");

            if (response.statusCode() != 201) {
                System.err.println("❌ Jira Bug creation failed");
                System.err.println("Status Code : " + response.statusCode());
                System.err.println("Response    : " + response.asString());
                return null;
            }

            String issueKey = response.jsonPath().getString("key");
            System.out.println("✅ Jira Bug Created : " + issueKey);
            return issueKey;

        } catch (Exception e) {
            System.err.println("❌ Jira Exception : " + e.getMessage());
            return null;
        }
    }

    /**
     * 📎 Attach Screenshot to Jira Bug
     */
    public static void attachScreenshot(String issueKey, String screenshotPath) {

        try {
            if (API_TOKEN == null || API_TOKEN.isEmpty()) {
                System.err.println("❌ JIRA_TOKEN missing for attachment!");
                return;
            }

            File file = new File(screenshotPath);
            if (!file.exists()) {
                System.err.println("❌ Screenshot file not found: " + screenshotPath);
                return;
            }

            String auth = EMAIL + ":" + API_TOKEN;
            String encodedAuth =
                    Base64.getEncoder().encodeToString(auth.getBytes());

            Response response = RestAssured
                    .given()
                    .baseUri(JIRA_URL)
                    .header("Authorization", "Basic " + encodedAuth)
                    .header("X-Atlassian-Token", "no-check")
                    .multiPart("file", file)
                    .when()
                    .post("/rest/api/3/issue/" + issueKey + "/attachments");

            if (response.statusCode() == 200) {
                System.out.println("📎 Screenshot attached to Jira: " + issueKey);
            } else {
                System.err.println("❌ Screenshot attach failed");
                System.err.println(response.asString());
            }

        } catch (Exception e) {
            System.err.println("❌ Jira Attachment Exception: " + e.getMessage());
        }
    }

    // 🔹 Jira JSON Payload
    private static String getPayload(String summary, String description, String issueType) {

        return "{ \"fields\": { " +
                "\"project\": {\"key\": \"" + PROJECT_KEY + "\"}," +
                "\"summary\": \"" + escape(summary) + "\"," +
                "\"description\": {\"type\": \"doc\", \"version\": 1," +
                "\"content\": [{\"type\": \"paragraph\", \"content\": " +
                "[{\"type\": \"text\", \"text\": \"" + escape(description) + "\"}]}]}," +
                "\"issuetype\": {\"name\": \"" + escape(issueType) + "\"}" +
                "} }";
    }

    // 🔹 Escape special characters
    private static String escape(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "");
    }
}
