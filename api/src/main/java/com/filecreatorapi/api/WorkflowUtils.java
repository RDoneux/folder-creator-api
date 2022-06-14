package com.filecreatorapi.api;

import java.awt.Desktop;
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

    public static void updateDocumentWorkflow(String coursePath, String type, String candidate, String fileName,
            String progressType, String checked) throws IOException {
        JSONObject courseSettings = Utils.loadJSONObject(coursePath);
        if (type.equals("general")) {
            JSONArray target = courseSettings.getJSONObject("files").getJSONArray(type);
            for (int i = 0; i < target.length(); i++) {
                JSONObject documentFile = target.getJSONObject(i);
                if (documentFile.get("name").equals(fileName)) {
                    switch (progressType) {
                        case "checked":
                            documentFile.put("checked", checked);
                            if (checked.equals("false")) {
                                break;
                            }
                        case "written":
                            documentFile.put("written", checked);
                            if (checked.equals("false")) {
                                break;
                            }
                        case "inProgress":
                            documentFile.put("inProgress", checked);
                            if (checked.equals("false")) {
                                break;
                            }
                    }
                    // for (String pt : progressType) {
                    // documentFile.remove(pt);
                    // documentFile.put(pt, checked);
                    // }
                }
            }
        } else if (type.equals("candidate")) {
            JSONArray target = courseSettings.getJSONObject("files").getJSONArray(type);
            for (int i = 0; i < target.length(); i++) {
                JSONObject cand = target.getJSONObject(i);
                if (cand.get("name").equals(candidate)) {
                    target = courseSettings.getJSONObject("files").getJSONArray(type).getJSONObject(i)
                            .getJSONArray("candidateFiles");
                }
            }
            for (int i = 0; i < target.length(); i++) {
                JSONObject documentFile = target.getJSONObject(i);
                if (documentFile.get("name").equals(fileName)) {
                    switch (progressType) {
                        case "checked":
                            documentFile.put("checked", checked);
                            if (checked.equals("false")) {
                                break;
                            }
                        case "written":
                            documentFile.put("written", checked);
                            if (checked.equals("false")) {
                                break;
                            }
                        case "inProgress":
                            documentFile.put("inProgress", checked);
                            if (checked.equals("false")) {
                                break;
                            }
                    }
                }
            }
        }
        Utils.writeJSON(courseSettings, coursePath);
    }

    public static void updateDocumentWorkFlowCandidate(String coursePath, String candidate, String inProgress,
            String written, String checked) throws IOException {
        JSONObject courseSettings = Utils.loadJSONObject(coursePath);
        JSONArray target = courseSettings.getJSONObject("files").getJSONArray("candidate");
        for (int i = 0; i < target.length(); i++) {
            JSONObject cand = target.getJSONObject(i);
            if (cand.get("name").equals(candidate)) {
                cand.put("inProgress", inProgress);
                cand.put("written", written);
                cand.put("checked", checked);
            }
        }
        Utils.writeJSON(courseSettings, coursePath);
    }

    public static JSONObject getDocuments(String path) {
        return Utils.loadJSONObject(path);
    }

    public static void openFile(String fileName, String candidateName, String filePath) throws IOException {
        File base = new File(filePath).getParentFile();

        if (candidateName.isEmpty()) {
            File target = new File(base.getAbsolutePath() + Utils.fileSeparator() + fileName + " .docx");
            if (target.exists()) {
                Desktop.getDesktop().open(target);
            } else {
                System.out
                        .println("Could not open file at '" + target.getAbsolutePath() + "' because it doesn't exist");
            }
        } else {
            File target = new File(base.getAbsolutePath() + Utils.fileSeparator() + candidateName
                    + Utils.fileSeparator() + candidateName + " - " + fileName + " .docx");
            if (target.exists()) {
                Desktop.getDesktop().open(target);
            } else {
                System.out
                        .println("Could not open file at '" + target.getAbsolutePath() + "' because it doesn't exist");
            }
        }

    }

}
