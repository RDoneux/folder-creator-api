package com.filecreatorapi.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SettingsController {

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/settings")
    public String getSettings() {
        return Utils.loadSettings().toString();
    }

    public void handleFileUpload(MultipartFile file, String key)
            throws IllegalStateException, IOException, InterruptedException {

        File file1 = new File(Utils.CUSTOM_FILES_PATH + file.getOriginalFilename());

        if (!file1.exists()) {
            file1.getParentFile().mkdirs();
            file1.createNewFile();
        }

        try (OutputStream os = new FileOutputStream(file1, false)) {
            os.write(file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/editSettings")
    private void handleUpdateFile(@RequestParam("file") MultipartFile file, @RequestParam("key") String key)
            throws IOException, IllegalStateException, InterruptedException {
        handleFileUpload(file, key);
        Utils.updateSettings(key, Utils.CUSTOM_FILES_PATH + file.getOriginalFilename(), SettingsType.DOCUMENT);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/deleteSetting")
    public void handleFileDelete(@RequestParam("key") String key) throws IOException {
        Utils.deleteSetting(key, SettingsType.DOCUMENT);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/addSetting")
    public void handleAddFile(@RequestParam("file") MultipartFile file, @RequestParam("key") String key)
            throws IllegalStateException, IOException, InterruptedException {
        handleFileUpload(file, key);
        Utils.addDocument(key, Utils.CUSTOM_FILES_PATH + key + ".docx", file.getOriginalFilename());
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updateRequiredGeneralDocument")
    public void updateRequiredGeneralDocument(@RequestParam("targetCourse") String targetCourse,
            @RequestParam("targetDocument") String targetDocument, @RequestParam("newValue") String newValue)
            throws IOException {
        Utils.updateRequiredGeneralDocument(targetCourse, targetDocument, newValue);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updateRequiredCandidateDocument")
    public void updateRequiredCandidateDocument(@RequestParam("targetCourse") String targetCourse,
            @RequestParam("targetDocument") String targetDocument, @RequestParam("newValue") String newValue)
            throws IOException {
        Utils.updateRequiredCandidateDocument(targetCourse, targetDocument, newValue);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path="/addNewCourseProfile")
    public void addNewCourseProfile (@RequestParam("courseName") String name) throws IOException {
        Utils.addNewCourseProfile(name);
    }

}