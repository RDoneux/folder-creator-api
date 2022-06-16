package com.filecreatorapi.api;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.security.RolesAllowed;

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

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/assignee")
    public void updateAssignee(@RequestParam("username") String username,
            @RequestParam("coursePath") String coursePath) throws IOException {
        WorkflowUtils.updateAssignee(username, coursePath);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/documents")
    public String getDocuments(@RequestParam("path") String path) {
        return WorkflowUtils.getDocuments(path).toString();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updateDocumentWorkflow")
    public void updateDocumentWokflow(@RequestParam("path") String coursePath, @RequestParam("type") String type,
            @RequestParam("candidate") String candidate, @RequestParam("fileName") String fileName,
            @RequestParam("progressType") String progressType, @RequestParam("checked") String checked)
            throws IOException {
        WorkflowUtils.updateDocumentWorkflow(coursePath, type, candidate, fileName,
                progressType, checked);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updateDocumentWorkFlowCandidate")
    public void updateDocumentWorkFlowCandidate(@RequestParam("path") String coursePath,
            @RequestParam("candidate") String candidate, @RequestParam("inProgress") String inProgress,
            @RequestParam("written") String written, @RequestParam("checked") String checked) throws IOException {
        WorkflowUtils.updateDocumentWorkFlowCandidate(coursePath, candidate, inProgress, written, checked);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/openFile")
    public void openFile(@RequestParam("fileName") String fileName, @RequestParam("candidate") String candidate,
            @RequestParam("coursePath") String coursePath) throws IOException {
        WorkflowUtils.openFile(fileName, candidate, coursePath);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updatePriority")
    public void updatePriority(@RequestParam("priority") String priority,
            @RequestParam("coursePath") String coursePath) throws IOException {
        WorkflowUtils.updatePriority(priority, coursePath);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updateDescription")
    public void updateDescription(@RequestParam("coursePath") String coursePath,
            @RequestParam("textContent") String textContent) throws IOException {
        WorkflowUtils.updateDescription(coursePath, textContent);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updateDate")
    public void updateDate(@RequestParam("date") String date, @RequestParam("path") String path) throws IOException {
        WorkflowUtils.updateDate(date, path);
    }

}
