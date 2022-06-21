package com.filecreatorapi.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.plaf.FileChooserUI;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCaseStrategy;
import java.awt.GraphicsEnvironment;

import org.apache.tomcat.util.json.JSONParserConstants;
import org.json.JSONArray;
import org.json.JSONObject;

public class Utils {

    private static File SETTINGS_FILE = new File(System.getProperty("user.dir") + Utils.fileSeparator() + "support"
            + Utils.fileSeparator() + "FileGeneratorSettings.json");

    public static String USERS_FILE_PATH = System.getProperty("user.dir") + Utils.fileSeparator() + "support"
            + Utils.fileSeparator() + "users";
    public static String CUSTOM_FILES_PATH = System.getProperty("user.dir") + Utils.fileSeparator() + "support"
            + Utils.fileSeparator()
            + "customFiles" + Utils.fileSeparator();

    public Candidate parseJsonInput(String input) {

        for (String candidate : getStringBetweenIdentifiers(input, "{", "}")) {
            createKeyPairs(candidate);
        }

        return null;

    }

    public static JSONObject loadJSONObject(String path) {
        File targetFile = new File(path);
        if (targetFile.exists()) {
            System.out.println("loading JSONObject from: '" + path + "'");
        } else {
            System.out.println("could not create settings file");
        }

        Scanner scan;
        String JSONString = "";
        try {
            scan = new Scanner(new FileInputStream(targetFile));

            while (scan.hasNext()) {
                JSONString += scan.nextLine();
            }

            return new JSONObject(JSONString);

        } catch (FileNotFoundException e) {
            System.out.println("could not read targetFile file at '" + path + "'");
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray loadJSONArray(String path) {
        File targetFile = new File(path);
        if (targetFile.exists()) {
            System.out.println("loading JSONObject from: '" + path + "'");
        } else {
            System.out.println("could not create settings file");
        }

        Scanner scan;
        String JSONString = "";
        try {
            scan = new Scanner(new FileInputStream(targetFile));

            while (scan.hasNext()) {
                JSONString += scan.nextLine();
            }

            return new JSONArray(JSONString);

        } catch (FileNotFoundException e) {
            System.out.println("could not read targetFile file at '" + path + "'");
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject loadSettings() {

        if (!SETTINGS_FILE.exists()) {
            try {
                createSettingsFile();
                System.out.println("created settings file");
            } catch (IOException e) {
                System.out.println("could not create settings file");
                e.printStackTrace();
            }
        }

        Scanner scan;
        String JSONString = "";
        try {
            scan = new Scanner(new FileInputStream(SETTINGS_FILE));

            while (scan.hasNext()) {
                JSONString += scan.nextLine();
            }

            return new JSONObject(JSONString);

        } catch (FileNotFoundException e) {
            System.out.println("could not read settings file");
            e.printStackTrace();
            return null;
        }

    }

    public static void addDocument(String key, String path, String fileName) throws IOException {
        JSONObject settings = loadSettings();

        System.out.println(key + " : " + path);

        JSONArray fileLocations = settings.getJSONArray("courseFileLocations");
        fileLocations.put(
                new JSONObject().put("display", key + ".docx").put("location", path)
                        .put("name", key)
                        .put("version",
                                getVersion(fileName)));

        JSONArray courses = settings.getJSONArray("courseRequirements");
        for (int i = 0; i < courses.length(); i++) {
            JSONObject course = courses.getJSONObject(i).getJSONObject("files");
            JSONArray general = course.getJSONArray("generalFiles");
            JSONArray candidate = course.getJSONArray("candidateFiles");

            general.put(new JSONObject().put("name", key).put("required", "true"));
            candidate.put(new JSONObject().put("name", key).put("required", "true"));
        }
        writeSettings(settings);
    }

    public static void deleteSetting(String key, SettingsType settingsTag) throws IOException {
        JSONObject settings = loadSettings();

        switch (settingsTag) {
            case DOCUMENT:
                JSONArray fileLocations = settings.getJSONArray("courseFileLocations");
                for (int i = 0; i < fileLocations.length(); i++) {
                    JSONObject target = fileLocations.getJSONObject(i);
                    System.out.println(target.get("name"));
                    if (target.get("name").equals(key)) {
                        fileLocations.remove(i);
                        break;
                    }
                }
                JSONArray courseRequirements = settings.getJSONArray("courseRequirements");
                for (int i = 0; i < courseRequirements.length(); i++) {
                    JSONObject course = courseRequirements.getJSONObject(i);
                    JSONArray general = course.getJSONObject("files").getJSONArray("generalFiles");
                    for (int j = 0; j < general.length(); j++) {
                        JSONObject target = general.getJSONObject(j);
                        System.out.println(Utils.convertFromJSONString(target.getString("name")).toLowerCase() + " : "
                                + key.toLowerCase());
                        if ((Utils.convertFromJSONString(target.getString("name")).toLowerCase()
                                .equals(key.toLowerCase()))) {
                            general.remove(j);
                        }
                    }

                    JSONArray candidate = course.getJSONObject("files").getJSONArray("candidateFiles");
                    for (int j = 0; j < candidate.length(); j++) {
                        JSONObject target = candidate.getJSONObject(j);
                        Utils.convertFromJSONString(target.getString("name").toLowerCase()).equals(key.toLowerCase());
                        if ((Utils.convertFromJSONString(target.getString("name")).toLowerCase()
                                .equals(key.toLowerCase()))) {
                            candidate.remove(j);
                        }
                    }
                }
                System.err.println("DOCUMENT setting with key: '" + key + "', could not be found");
                break;
            case GENERAL:
                System.err.println("no settings created for GENERAL yet");
                break;
            case CANDIDATE:
                System.err.println("no settings created for CANDIDATE yet");
            default:
                break;
        }

        writeSettings(settings);

    }

    public static void updateSettings(String key, String value, SettingsType settingsTag) throws IOException {

        JSONObject settings = loadSettings();

        switch (settingsTag) {
            case DOCUMENT:
                JSONArray fileLocations = settings.getJSONArray("courseFileLocations");
                for (int i = 0; i < fileLocations.length(); i++) {
                    JSONObject target = fileLocations.getJSONObject(i);
                    if (target.get("name").equals(key)) {
                        target.put("location_old - " + new Date(), target.get("location"));
                        target.put("location", value);
                        target.put("display", getFileName(value));
                        target.put("version", getVersion(getFileName(value)));
                        break;
                    }
                }
                System.err.println("DOCUMENT setting with key: '" + key + "', could not be found");
                break;
            case GENERAL:
                System.err.println("no settings created for GENERAL yet");
                break;
            case CANDIDATE:
                System.err.println("no settings created for CANDIDATE yet");
            default:
                break;
        }

        writeSettings(settings);

    }

    public static void updateRequiredGeneralDocument(String targetCourse, String targetDocument, String newValue)
            throws IOException {

        JSONObject settings = loadSettings();

        JSONArray courses = settings.getJSONArray("courseRequirements");

        for (int i = 0; i < courses.length(); i++) {
            JSONObject course = courses.getJSONObject(i);
            if (course.get("name").equals(targetCourse)) {
                JSONArray generalFiles = course.getJSONObject("files").getJSONArray("generalFiles");
                for (int j = 0; j < generalFiles.length(); j++) {
                    JSONObject document = generalFiles.getJSONObject(j);
                    if (document.getString("name").equals(targetDocument)) {
                        document.put("required", newValue);
                        break;
                    }
                }
            }
        }
        writeSettings(settings);
    }

    public static void updateRequiredCandidateDocument(String targetCourse, String targetDocument, String newValue)
            throws IOException {

        JSONObject settings = loadSettings();

        JSONArray courses = settings.getJSONArray("courseRequirements");

        for (int i = 0; i < courses.length(); i++) {
            JSONObject course = courses.getJSONObject(i);
            if (course.get("name").equals(targetCourse)) {
                JSONArray candidateFiles = course.getJSONObject("files").getJSONArray("candidateFiles");
                for (int j = 0; j < candidateFiles.length(); j++) {
                    JSONObject document = candidateFiles.getJSONObject(j);
                    if (document.getString("name").equals(targetDocument)) {
                        document.put("required", newValue);
                        break;
                    }
                }
            }
        }
        writeSettings(settings);
    }

    public static void addNewCourseProfile(String name) throws IOException {
        JSONObject settings = loadSettings();

        JSONArray courseRequirements = settings.getJSONArray("courseRequirements");

        courseRequirements.put(new JSONObject().put("name", name)
                .put("files", new JSONObject()
                        .put("generalFiles", new JSONArray())
                        .put("candidateFiles", new JSONArray())));

        System.out.println(courseRequirements);

        JSONArray fileLocations = settings.getJSONArray("courseFileLocations");
        for (int i = 0; i < fileLocations.length(); i++) {
            JSONObject target = fileLocations.getJSONObject(i);
            courseRequirements.getJSONObject(courseRequirements.length() - 1).getJSONObject("files")
                    .getJSONArray("generalFiles")
                    .put(new JSONObject().put("name", target.get("name")).put("required", "false"));
            courseRequirements.getJSONObject(courseRequirements.length() - 1).getJSONObject("files")
                    .getJSONArray("candidateFiles")
                    .put(new JSONObject().put("name", target.get("name")).put("required", "false"));
        }
        writeSettings(settings);
    }

    private static String getFileName(String filePath) {
        String[] splitFilePath = filePath.split(fileSeparator());
        return splitFilePath[splitFilePath.length - 1];
    }

    public static String getVersion(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        String version = "-1";
        if (lowerCaseFileName.contains("v")) {
            if (Character.isDigit(lowerCaseFileName.charAt(lowerCaseFileName.indexOf("v") + 1))) {
                version = lowerCaseFileName.substring(lowerCaseFileName.indexOf("v") + 1,
                        lowerCaseFileName.indexOf("."));
            } else {
                return getVersion(lowerCaseFileName.substring(lowerCaseFileName.indexOf("v") + 1));
            }
        }
        return (version == "-1") ? "no version information" : "Version | " + version;
    }

    public static String fileSeparator() {
        return System.getProperty("file.separator");
    }

    private static void createSettingsFile() throws IOException {

        SETTINGS_FILE.getParentFile().mkdirs();
        SETTINGS_FILE.createNewFile();

        JSONObject settingsJSON = new JSONObject();

        JSONArray courseFileLocations = new JSONArray();

        // courseFileLocations.put(new JSONObject().put("name",
        // "issuesArisingForm").put("location",
        // System.getProperty("user.dir")
        // + convertToPlatformIndependantFilePath("/support/templates/Issues Arising
        // Form.docx"))
        // .put("display", "Issues Arising Form.docx").put("version", "no version
        // information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "evaluationCollationForm").put("location",
        // System.getProperty("user.dir") + convertToPlatformIndependantFilePath(
        // "/support/templates/General - Evaluation Collation Form.docx"))
        // .put("display", "Evaluation Collation Form.docx").put("version", "no version
        // information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "attendanceSheet").put("location",
        // System.getProperty("user.dir")
        // + convertToPlatformIndependantFilePath("/support/templates/Attendance
        // Sheet.docx"))
        // .put("display", "Attendance Sheet.docx")
        // .put("version", "no version information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "generalRecordSheet").put("location",
        // System.getProperty("user.dir")
        // + convertToPlatformIndependantFilePath("/support/templates/General - Record
        // Sheet.docx"))
        // .put("display", "Record Sheet.docx").put("version", "no version
        // information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "interventionAssessmentPWP").put("location",
        // System.getProperty("user.dir") + convertToPlatformIndependantFilePath(
        // "/support/templates/General - Physical Intervention Records PWP.docx"))
        // .put("display", "Physical Intervention Records PWP.docx").put("version", "no
        // version information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "interventionAssessmentKS").put("location",
        // System.getProperty("user.dir") + convertToPlatformIndependantFilePath(
        // "/support/templates/General - Physical Intervention Records KS.docx"))
        // .put("display", "Physical Intervention Records KS.docx").put("version", "no
        // version information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "interventionAssessmentPS").put("location",
        // System.getProperty("user.dir") + convertToPlatformIndependantFilePath(
        // "/support/templates/General - Physical Intervention Records PS.docx"))
        // .put("display", "Physical Intervention Records PS.docx").put("version", "no
        // version information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "interventionAssessmentRPS").put("location",
        // System.getProperty("user.dir") + convertToPlatformIndependantFilePath(
        // "/support/templates/General - Physical Intervention Records RPS.docx"))
        // .put("display", "Physical Intervention Records RPS.docx").put("version", "no
        // version information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "recertificationPresentationAssessment").put("location",
        // System.getProperty("user.dir") + convertToPlatformIndependantFilePath(
        // "/support/templates/Re-certification - Presentation Feedback.docx"))
        // .put("display", "Re-certification - Presentation Feedback.docx")
        // .put("version", "no version information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "assessmentDayPresentationAssessment").put("location",
        // System.getProperty("user.dir") + convertToPlatformIndependantFilePath(
        // "/support/templates/Assessment Day - Presentation Feedback.docx"))
        // .put("display", "Assessment Day - Presentation Feedback.docx")
        // .put("version", "no version information"));
        // courseFileLocations.put(new JSONObject().put("name",
        // "instructorPresentationAssessment").put("location",
        // System.getProperty("user.dir") + convertToPlatformIndependantFilePath(
        // "/support/templates/Instructor Course - Presentation Feedback.docx"))
        // .put("display", "Instructor Course - Presentation Feedback.docx")
        // .put("version", "no version information"));

        JSONObject filesRequired = new JSONObject();
        JSONArray candidateFiles = new JSONArray();
        JSONArray generalFiles = new JSONArray();
        JSONArray courses = new JSONArray();

        // generalFiles.put(new JSONObject().put("name",
        // "issuesArisingForm").put("required", "true"));
        // generalFiles.put(new JSONObject().put("name",
        // "attendanceSheet").put("required", "true"));
        // generalFiles.put(new JSONObject().put("name",
        // "evaluationCollationForm").put("required", "true"));

        // candidateFiles.put(new JSONObject().put("name",
        // "generalRecordSheet").put("required", "true"));
        // candidateFiles.put(new JSONObject().put("name",
        // "interventionAssessmentPWP").put("required", "true"));
        // candidateFiles.put(new JSONObject().put("name",
        // "interventionAssessmentKS").put("required", "true"));
        // candidateFiles.put(new JSONObject().put("name",
        // "interventionAssessmentPS").put("required", "true"));
        // candidateFiles.put(new JSONObject().put("name",
        // "interventionAssessmentRPS").put("required", "true"));
        // candidateFiles
        // .put(new JSONObject().put("name",
        // "recertificationPresentationAssessment").put("required", "true"));
        // candidateFiles.put(new JSONObject().put("name",
        // "assessmentDayPresentationAssessment").put("required", "true"));
        // candidateFiles.put(new JSONObject().put("name",
        // "instructorPresentationAssessment").put("required", "true"));

        filesRequired.put("generalFiles", generalFiles);
        filesRequired.put("candidateFiles", candidateFiles);

        // courses.put(new JSONObject().put("name", "instructorCourse").put("files",
        // filesRequired));
        // courses.put(new JSONObject().put("name",
        // "introductoryAndFoundation").put("files", filesRequired));
        // courses.put(new JSONObject().put("name",
        // "principalInstructorCourse").put("files", filesRequired));

        settingsJSON.put("courseFileLocations", courseFileLocations);
        settingsJSON.put("courseRequirements", courses);
        settingsJSON.put("outputLocation",
                new JSONObject().put("location",
                        System.getProperty("user.dir") + FileGeneratorController.MASTER_FOLDER));
        FileGeneratorController.BASE_URL = settingsJSON.getJSONObject("outputLocation").getString("location");

        writeSettings(settingsJSON);

    }

    public static String convertToPlatformIndependantFilePath(String path) {
        String toReturn = path.replace("\\\\", Utils.fileSeparator());
        return toReturn.replace("/", Utils.fileSeparator());
    }

    private static void writeSettings(JSONObject settings) throws IOException {
        FileWriter writer = new FileWriter(SETTINGS_FILE);
        writer.write(settings.toString());
        writer.close();
    }

    public static void writeJSON(JSONObject jsonObject, String fileLocation) throws IOException {
        FileWriter writer = new FileWriter(fileLocation);
        writer.write(jsonObject.toString());
        writer.close();
    }

    public static void writeJSON(JSONArray jsonObject, String fileLocation) throws IOException {
        FileWriter writer = new FileWriter(fileLocation);
        writer.write(jsonObject.toString());
        writer.close();
    }

    public static String convertToJSONString(String input) {
        String output[] = input.split(" ");
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < output.length; i++) {
            String word = output[i];
            if (i == 0) {
                word = upperOrLowerCase("lower", word);
            } else {
                word = upperOrLowerCase("upper", word);
            }
            build.append(word);
        }
        return build.toString();
    }

    public static String convertFromJSONString(String input) {
        String[] output = input.split("(?=p\\{Upper})");

        StringBuilder build = new StringBuilder();
        for (int i = 0; i < output.length; i++) {
            String word = output[i];
            if (i + 1 == output.length) {
                word = upperOrLowerCase("upper", word);
            } else {
                word = upperOrLowerCase("upper", word) + " ";
            }
            build.append(word);
        }
        return build.toString();
    }

    public static String upperOrLowerCase(String upperOrLower, String input) {
        String mal[] = input.split("");
        if (upperOrLower.equals("upper")) {
            mal[0] = mal[0].toUpperCase();
        } else if (upperOrLower.equals("lower")) {
            mal[0] = mal[0].toLowerCase();
        } else {
            System.err.println(upperOrLower + "is not recognised");
        }
        StringBuilder builder = new StringBuilder();
        for (String s : mal) {
            builder.append(s);
        }
        return builder.toString();
    }

    public ArrayList<KeyPair> createKeyPairs(String details) {

        System.out.println(details);

        // for (String s : getStringBetweenIdentifiers(details, "[", "]")) {
        // // System.out.println("s=" + s);
        // }

        return null;

    }

    // public Candidate createCandidate(String details) {

    // }

    public ArrayList<String> getStringBetweenIdentifiers(String input, String opening, String closing) {
        String splitInput[] = input.split("");

        int countOpening = 0;
        int countClosing = 0;
        int startingIndex = -1;
        int endingIndex = -1;
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < splitInput.length; i++) {
            String s = splitInput[i];
            if (s.equals(opening))
                countOpening++;
            if (s.equals(closing))
                countClosing++;

            if (countOpening == 1 && startingIndex == -1) {
                startingIndex = i;
            }
            if (countOpening == countClosing && (countOpening > 0 && countClosing > 0) && endingIndex == -1) {
                endingIndex = i;
                output.add(input.substring(startingIndex, endingIndex));
                countOpening = 0;
                countClosing = 0;
                startingIndex = -1;
                endingIndex = -1;
            }
        }
        return output;
    }

    public static String generateReadableDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String format = formatter.format(date);
        return format;
    }

    public static String generateReadableDateWithTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy|HH:mm");
        String format = formatter.format(date);
        return format;
    }

    public static JSONObject getOutputFile() {
        JSONObject settings = loadJSONObject(SETTINGS_FILE.getAbsolutePath());
        return settings.getJSONObject("outputLocation");
    }

    public static void editOutputFile(String path) throws IOException {
        JSONObject settings = loadJSONObject(SETTINGS_FILE.getAbsolutePath());
        settings.put("outputLocation",
                new JSONObject().put("location",
                        path + FileGeneratorController.MASTER_FOLDER));
        writeJSON(settings, SETTINGS_FILE.getAbsolutePath());
    }

}
