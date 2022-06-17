package com.filecreatorapi.api;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportControllerUtils {

    // private File reportFile = new File(System.getProperty("user.dir") +
    // "/reports/Reports.json");
    private JSONArray reportJson;

    public ReportControllerUtils() {

        File file = new File(System.getProperty("user.dir") + "/reports/Reports.json");

        if (!file.exists()) {

            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                Utils.writeJSON(new JSONArray(), System.getProperty("user.dir") + "/reports/Reports.json");

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        reportJson = Utils.loadJSONArray(System.getProperty("user.dir") + "/reports/Reports.json");

    }

    public void addReport(String title, String description, String type) throws IOException {
        reportJson.put(new JSONObject().put("title", title).put("description", description).put("type", type));
        Utils.writeJSON(reportJson, System.getProperty("user.dir") + "/reports/Reports.json");
    }

    public JSONArray getReports() {
        return Utils.loadJSONArray(System.getProperty("user.dir") + "/reports/Reports.json");
    }

}
