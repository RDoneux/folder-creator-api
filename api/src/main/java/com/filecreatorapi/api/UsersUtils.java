package com.filecreatorapi.api;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class UsersUtils {

    private static final String FILE_NAME = "/users.json";

    public UsersUtils() {
        try {
            createUsersFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUsersFile() throws IOException {

        File usersFile = new File(Utils.USERS_FILE_PATH + FILE_NAME);

        if (!usersFile.exists()) {
            usersFile.getParentFile().mkdirs();
            usersFile.createNewFile();

            JSONObject usersJSON = new JSONObject();
            usersJSON.put("users",
                    new JSONArray()
                            .put(new JSONObject().put("name", "Super User").put("colour", generateRandomColour()).put(
                                    "initials", "SU")));

            Utils.writeJSON(usersJSON, Utils.USERS_FILE_PATH + FILE_NAME);
        }

    }

    public void updateColour(String name, String colour) throws IOException {
        JSONObject users = Utils.loadJSONObject(Utils.USERS_FILE_PATH + FILE_NAME);
        JSONArray target = users.getJSONArray("users");
        for (int i = 0; i < target.length(); i++) {
            JSONObject userTarget = target.getJSONObject(i);
            if (userTarget.get("name").equals(name)) {
                userTarget.put("colour", colour);
            }
        }
        Utils.writeJSON(users, Utils.USERS_FILE_PATH + FILE_NAME);
    }

    public JSONObject getUsers() {
        return Utils.loadJSONObject(Utils.USERS_FILE_PATH + FILE_NAME);
    }

    public JSONObject getUser(String name) {
        JSONArray users = Utils.loadJSONObject(Utils.USERS_FILE_PATH + FILE_NAME).getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.get("name").equals(name)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(String name) throws IOException {
        JSONObject users = Utils.loadJSONObject(Utils.USERS_FILE_PATH + FILE_NAME);
        JSONArray target = users.getJSONArray("users");
        target.put(new JSONObject().put("name", name).put("colour", generateRandomColour()).put("initials",
                generateInitials(name)));

        Utils.writeJSON(users, Utils.USERS_FILE_PATH + FILE_NAME);
    }

    public void deleteUser(String name) throws IOException {
        JSONObject users = Utils.loadJSONObject(Utils.USERS_FILE_PATH + FILE_NAME);
        JSONArray target = users.getJSONArray("users");
        for (int i = 0; i < target.length(); i++) {
            JSONObject userTarget = target.getJSONObject(i);
            if (userTarget.get("name").equals(name)) {
                target.remove(i);
            }
        }
        Utils.writeJSON(users, Utils.USERS_FILE_PATH + FILE_NAME);
    }

    private String generateRandomColour() {
        Random random = new Random();
        return "rgb(" + random.nextInt(255) + ", " + random.nextInt(255) + ", " + random.nextInt(255)
                + ")";

    }

    private String generateInitials(String name) {
        String split[] = name.split(" ");
        return (split[0].charAt(0) + "" + split[1].charAt(0)).toUpperCase();
    }

}
