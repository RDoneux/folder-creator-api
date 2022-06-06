package com.filecreatorapi.api;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.text.DateFormatter;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileGeneratorController {

    private String courseDate;
    private String courseType;
    private static String MASTER_FOLDER = "courses";
    static String BASE_URL = Utils.fileSeparator() + "home" + Utils.fileSeparator() + "robert" + Utils.fileSeparator()
            + "Desktop" + Utils.fileSeparator() + MASTER_FOLDER;
    public String baseFilePath;
    private JSONObject settings;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public String postCandidates(@RequestBody String candidateInformation) {

        Candidate[] candidates = createCandidates(candidateInformation);

        createFolderStructure(candidates);

        dump(candidates);

        JSONObject response = new JSONObject();
        response.put("response", "Success");

        return response.toString();

        // HashMap<String, String> map = new HashMap<>();
        // map.put("response", "Success");
        // return map;

        // return new ResponseEntity<String>("Course Successfully Created",
        // HttpStatus.OK);

    }

    // TODO: REMOVE NAME ATTRIBUTE FROM JSON ARRAY - NOT NECESSARY
    private Candidate[] createCandidates(String candidateInformation) {

        JSONObject course = new JSONObject(candidateInformation);

        JSONArray information = course.getJSONArray("candidates");
        Candidate candidates[] = new Candidate[information.length()];

        courseDate = reverseDate(course.getString("date"));
        courseType = Utils.convertToJSONString(course.getString("courseType"));

        for (int i = 0; i < information.length(); i++) {
            JSONObject targetCandidate = information.getJSONObject(i);
            JSONObject targetCandidateInformation = targetCandidate.getJSONObject("information");
            JSONArray targetCandidateInformationOptions = targetCandidateInformation.getJSONArray("options");

            candidates[i] = Candidate.builder()
                    .name(targetCandidate.getString("name"))
                    .organisation(targetCandidateInformation.getString("organisation"))
                    .score(targetCandidateInformation.getInt("score"))
                    .interventions(targetCandidateInformationOptions.getJSONObject(0).getBoolean("value"))
                    .presentation(targetCandidateInformationOptions.getJSONObject(1).getBoolean("value"))
                    .portfolio(targetCandidateInformationOptions.getJSONObject(2).getBoolean("value"))
                    .outcome(targetCandidateInformationOptions.getJSONObject(3).getBoolean("value"))
                    .build();
        }

        return candidates;
    }

    private String reverseDate(String date) {
        String splitDate[] = date.split("-");
        return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];
    }

    public String createAmericanDate(String date) {
        String splitDate[] = date.split("-");
        return splitDate[1] + "-" + splitDate[0] + "-" + splitDate[2];
    }

    private boolean createFolderStructure(Candidate[] candidates) {

        baseFilePath = BASE_URL + fileSeparator() + courseDate.split("-")[2] + fileSeparator()
                + getMonth(courseDate.split("-")[1]) + fileSeparator() + Utils.convertFromJSONString(courseType) + " | "
                + courseDate;

        File baseFile = new File(baseFilePath);

        if (!baseFile.exists()) {
            System.out.println("created directory at: '" + baseFilePath + "'");
            baseFile.mkdirs();
        } else {
            System.out.println("base directory: '" + baseFilePath + "' already exists, skipping creating directory");
        }

        for (Candidate candidate : candidates) {
            File candidateFile = new File(baseFilePath + System.getProperty("file.separator") + candidate.getName());
            if (!candidateFile.exists()) {
                System.out.println("creating candidate file: '" + candidate.getName() + "' in base directory");
                candidateFile.mkdirs();
            } else {
                System.out.println(
                        "candidate file: '" + candidate.getName() + "' already exists, skipping creating file");
            }
        }

        try {
            copyFiles(candidates, baseFile.getAbsolutePath());
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            createCourseJSONSettings(baseFile.getAbsolutePath());
        } catch (IOException | JSONException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // TODO: return logging information
        return false;
    }

    private boolean copyFiles(Candidate[] candidates, String baseFilePath) throws JSONException, IOException {

        this.settings = Utils.loadSettings();

        for (String s : getRequiredGeneralFiles()) {
            copyFile(s, "", Utils.convertFromJSONString(s));
        }

        for (Candidate candidate : candidates) {
            for (String s : getRequiredCandidateFiles()) {
                copyFile(s, candidate.getName(), candidate.getName() + " - " + Utils.convertFromJSONString(s));
            }
        }
        return true;
    }

    private void createCourseJSONSettings(String location) throws IOException, JSONException, ParseException {
        System.out.println(location);

        File courseSettingsFile = new File(location + "/.workflow.json");
        courseSettingsFile.createNewFile();

        // Calendar cal = Calendar.getInstance();
        // cal.setTime(new Date());
        // cal.add(Calendar.DATE, 2);
        // Date deadline = cal.getTime();

        LocalDate date = LocalDate.parse(reverseDate(this.courseDate));
        LocalDate deadline = date.plusDays(2);
        LocalDate created = LocalDate.now();

        JSONObject fileSettings = new JSONObject();

        fileSettings.put("tag", "courses");
        fileSettings.put("name", courseType);
        fileSettings.put("comments", new JSONArray());
        fileSettings.put("description", "");
        fileSettings.put("assigned", "");
        fileSettings.put("priority", "low");
        fileSettings.put("created", created);
        fileSettings.put("date", date);
        fileSettings.put("deadline", deadline);
        fileSettings.put("path", courseSettingsFile.getAbsolutePath());

        Utils.writeJSON(fileSettings, courseSettingsFile.getAbsolutePath());

    }

    private void dump(Candidate[] candidates) {
        System.out.println("");
        System.out.println(" -------- COURSE ---------");
        System.out.println("Course Date: " + this.courseDate);
        System.out.println("Course Type: " + this.courseType);
        System.out.println("");
        for (Candidate candidate : candidates) {
            System.out.println("------- CANDIDATE ------");
            System.out.println("name: " + candidate.getName());
            System.out.println("organisation: " + candidate.getOrganisation());
            System.out.println("score: " + candidate.getScore());
            System.out.println("interventions: " + candidate.getInterventions());
            System.out.println("presentation: " + candidate.getPresentation());
            System.out.println("portfolio: " + candidate.getPortfolio());
            System.out.println("outcome: " + candidate.getOutcome());
            System.out.println("");
        }
    }

    public String fileSeparator() {
        return System.getProperty("file.separator");
    }

    public String getMonth(String i) {
        if (i.split("")[0].equals("0")) {
            i = i.split("")[1];
        }
        String months[] = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        return months[Integer.parseInt(i) - 1];
    }

    private ArrayList<String> getRequiredGeneralFiles() {
        JSONArray reference = settings.getJSONArray("courseRequirements").getJSONObject(getCourseTypeIndex(courseType))
                .getJSONObject("files").getJSONArray("generalFiles");
        ArrayList<String> response = new ArrayList<>();
        for (int i = 0; i < reference.length(); i++) {
            String stringToReturn = reference.getJSONObject(i).getString("name");
            if (evaluateFormSetting(stringToReturn)) {
                response.add(stringToReturn);
            }
        }
        return response;
    }

    private ArrayList<String> getRequiredCandidateFiles() {
        JSONArray reference = settings.getJSONArray("courseRequirements").getJSONObject(getCourseTypeIndex(courseType))
                .getJSONObject("files").getJSONArray("candidateFiles");
        ArrayList<String> response = new ArrayList<>();
        for (int i = 0; i < reference.length(); i++) {
            String stringToReturn = reference.getJSONObject(i).getString("name");
            if (evaluateFormSetting(stringToReturn)) {
                response.add(stringToReturn);
            }
        }
        return response;
    }

    private boolean evaluateFormSetting(String key) {

        JSONArray files = settings.getJSONArray("courseRequirements").getJSONObject(getCourseTypeIndex(courseType))
                .getJSONObject("files").getJSONArray("generalFiles");
        for (int i = 0; i < files.length(); i++) {
            System.out.println(files.getJSONObject(i).get("name") + " : " + key);
            if (files.getJSONObject(i).get("name").equals(key)) {
                return isTrue(files.getJSONObject(i).getString("required"));
            }
        }
        files = settings.getJSONArray("courseRequirements").getJSONObject(getCourseTypeIndex(courseType))
                .getJSONObject("files").getJSONArray("candidateFiles");
        for (int i = 0; i < files.length(); i++) {
            if (files.getJSONObject(i).get("name").equals(key)) {
                return isTrue(files.getJSONObject(i).getString("required"));
            }
        }
        return false;
    }

    private void copyFile(String src, String dest, String newFileName) throws JSONException, IOException {
        FileUtils.copyFile(new File(getCourseFileLocation(src)),
                new File(baseFilePath + fileSeparator() + dest + fileSeparator(), newFileName + ".docx"));
    }

    private String getCourseFileLocation(String src) {
        for (int i = 0; i < settings.getJSONArray("courseFileLocations").length(); i++) {
            String key = settings.getJSONArray("courseFileLocations").getJSONObject(i).getString("name");
            if (key.equals(src))
                return settings.getJSONArray("courseFileLocations").getJSONObject(i).getString("location");
        }
        System.err.println("Course file location could not be found");
        return null;
    }

    private int getCourseTypeIndex(String courseType) {
        for (int i = 0; i < settings.getJSONArray("courseRequirements").length(); i++) {
            String key = settings.getJSONArray("courseRequirements").getJSONObject(i).getString("name");
            if (key.equals(courseType)) {
                return i;
            }
        }
        System.err.println("Course type could not be found");
        return -1;
    }

    // private String trimJSONKey(String key) {
    // return key.substring(1, key.length() - 1);
    // }

    private boolean isTrue(String value) {
        return value.equals("true") || value.equals("True");
    }

    // private int getDay() {
    // return Integer.valueOf(this.courseDate.split("-")[0]);
    // }

    // private int getMonth() {
    // return Integer.valueOf(this.courseDate.split("-")[1]);
    // }

    // private int getYear() {
    // return Integer.valueOf(this.courseDate.split("-")[2]);
    // }

}
