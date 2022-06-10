package com.filecreatorapi.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class WorkflowUtils {

    public static JSONObject getCourses() throws FileNotFoundException {

        File file = new File(FileGeneratorController.BASE_URL);
        JSONArray files = new JSONArray();

        for (File f : recursivlyGetDirectories(null, file)) {
            Scanner scan = new Scanner(new FileInputStream(f));
            StringBuilder builder = new StringBuilder();

            while (scan.hasNext()) {
                builder.append(scan.nextLine());
            }

            files.put(new JSONObject(builder.toString()));

        }
        return new JSONObject().put("files", files);
    }

    private static ArrayList<File> recursivlyGetDirectories(ArrayList<File> files, File file) {
        ArrayList<File> toReturn = files;
        if (toReturn == null) {
            toReturn = new ArrayList<>();
        }
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                File settings = getWorkflowSettings(f);
                System.out.println(settings + " : " + f.getAbsolutePath() + " : " + file.listFiles().length);
                if (settings == null) {
                    recursivlyGetDirectories(toReturn, f);
                } else {
                    toReturn.add(settings);
                }
            }
        }
        return toReturn;
    }

    private static File getWorkflowSettings(File directory) {
        for (File f : directory.listFiles()) {
            if (f.isHidden() && f.getName().equals(".workflow.json")) {
                return f;
            }
        }
        return null;
    }

    public static void updateTag(String tag, String path) throws IOException {
        JSONObject courseSettings = Utils.loadJSONObject(path);
        courseSettings.put("tag", tag);

        Utils.writeJSON(courseSettings, path);
    }

    public static void addComment(String workFlowLocation, String comment, String user) throws IOException {
        JSONObject courseSettings = Utils.loadJSONObject(workFlowLocation);
        courseSettings.getJSONArray("comments")
                .put(comment + ":::" + user + ":::" + Utils.generateReadableDateWithTime(new Date()));
        Utils.writeJSON(courseSettings, workFlowLocation);
    }

    public static void updateAssignee(String username, String coursePath) throws IOException {
        JSONObject courseSettings = Utils.loadJSONObject(coursePath);
        courseSettings.put("assigned", username);
        Utils.writeJSON(courseSettings, coursePath);
    }

    public static JSONObject getDocuments(String path) {
        return Utils.loadJSONObject(path);
    }

}
