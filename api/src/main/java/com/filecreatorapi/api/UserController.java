package com.filecreatorapi.api;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UsersUtils userUtils;

    public UserController() {
        this.userUtils = new UsersUtils();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/users")
    public String getUsers() {
        return userUtils.getUsers().toString();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/user/{name}")
    public String getUser(@PathVariable String name) {
        return userUtils.getUser(name).toString();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/addUser")
    public void addUser(@RequestParam("name") String name) throws IOException {
        userUtils.addUser(name);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/deleteUser")
    public void deleteUser(@RequestParam("name") String name) throws IOException {
        userUtils.deleteUser(name);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/updateColour")
    public void udpateColour(@RequestParam("name") String name, @RequestParam("colour") String colour)
            throws IOException {
        userUtils.updateColour(name, colour);
    }
}
