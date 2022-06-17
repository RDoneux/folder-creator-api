package com.filecreatorapi.api;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ReportController {

    private ReportControllerUtils utils;

    public ReportController() {
        utils = new ReportControllerUtils();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/addReport")
    public void addReport(@RequestParam("title") String title, @RequestParam("description") String description,
            @RequestParam("type") String type) throws IOException {
        utils.addReport(title, description, type);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/getReports")
    public String getReports() {
        return utils.getReports().toString();
    }

}
