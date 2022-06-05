package com.filecreatorapi.api;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowController {

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/getCourses")
    public String getCourses() throws FileNotFoundException {
        return WorkflowUtils.getCourses().toString();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updateTag")
    public void updateTag(@RequestParam("tag") String tag, @RequestParam("path") String path) throws IOException {
        WorkflowUtils.updateTag(tag, path);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/addComment")
    public void addComment(@RequestParam("workFlowLocation") String workFlowLocation,
            @RequestParam("comment") String comment, @RequestParam("user") String user) throws IOException {
        WorkflowUtils.addComment(workFlowLocation, comment, user);
    }
}
