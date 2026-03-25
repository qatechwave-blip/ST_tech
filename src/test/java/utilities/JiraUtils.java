package utilities;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;

import org.json.JSONObject;

public class JiraUtils {

    private static final String JIRA_URL =
            "https://techwaveitsolutions.atlassian.net";

    private static final String PROJECT_KEY = "CB";

    private static final String JIRA_EMAIL =
            "techwaveimplementation@gmail.com";

    private static final String JIRA_TOKEN =
            System.getProperty("jira.token");

    private static String getAuthHeader() {
        String auth = JIRA_EMAIL + ":" + JIRA_TOKEN;
        return "Basic " + Base64.getEncoder()
                .encodeToString(auth.getBytes());
    }

    // ================= CREATE BUG =================
    public static String createBug(String summary,
                                   String description,
                                   String issueType) {
        try {
            URL url = new URL(JIRA_URL + "/rest/api/2/issue");
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", getAuthHeader());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject fields = new JSONObject();
            fields.put("project",
                    new JSONObject().put("key", PROJECT_KEY));
            fields.put("summary", summary);
            fields.put("description", description);
            fields.put("issuetype",
                    new JSONObject().put("name", issueType));

            JSONObject payload = new JSONObject();
            payload.put("fields", fields);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.toString().getBytes());
            }

            int code = conn.getResponseCode();

            if (code == 201) {
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                JSONObject res =
                        new JSONObject(br.readLine());
                return res.getString("key");
            } else {
                System.out.println(" Jira create failed: " + code);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= ATTACH SCREENSHOT =================
    public static void attachScreenshot(String issueKey,
                                        String filePath) {
        try {
            String boundary = "----JiraBoundary";

            URL url = new URL(
                    JIRA_URL + "/rest/api/2/issue/"
                            + issueKey + "/attachments");

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", getAuthHeader());
            conn.setRequestProperty("X-Atlassian-Token", "no-check");
            conn.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            conn.setDoOutput(true);

            File file = new File(filePath);

            OutputStream os = conn.getOutputStream();
            PrintWriter writer =
                    new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true);

            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + file.getName() + "\"\r\n");
            writer.append("Content-Type: image/png\r\n\r\n");
            writer.flush();

            Files.copy(file.toPath(), os);
            os.flush();

            writer.append("\r\n--" + boundary + "--\r\n");
            writer.close();

            if (conn.getResponseCode() == 200) {
                System.out.println(" Screenshot attached: " + issueKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
