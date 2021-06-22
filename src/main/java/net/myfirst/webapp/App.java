package net.myfirst.webapp;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static Map<String, Object> map = new HashMap<>();
    static Map<String, Integer> namesCounter = new HashMap<>();

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 1997; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        get("/greet", (req, res) -> "Hello!");

        get("/greet/:username", (req, res) -> {
            String name = req.params(":username");
            return "Hello, " + name + "!";
        });

        get("/greet/:username/language/:language", (req, res) -> {
            String name = req.params(":username");
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

            String language = req.params(":language");

            if (language.equals("english")) {
                return "Hello, " + name;
            } else if (language.equals("isixhosa")) {
                return "Molo, " + name;
            } else if (language.equals("afrikaans")) {
                return "More, " + name;
            }
            return "Hello" + name;
        });

        post("/greet", (req, res) -> {
            String username = req.queryParams("username");
            username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();

            if (!username.equals("")) {
                return "Hello, " + username + "!";
            }
            return "Hello!";
        });

        get("/hello", (req, res) -> {
            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/hello", (req, res) -> {

            String username = req.queryParams("username");
            username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();

            String language = req.queryParams("language");
            String greetMessage = "";

            if (!username.equals("") && !language.equals("")) {
                switch (language) {
                    case "english":
                        greetMessage = "Hello, " + username;
                        break;
                    case "isixhosa":
                        greetMessage = "Molo, " + username;
                        break;
                    case "afrikaans":
                        greetMessage = "More, " + username;
                        break;
                }
            }

            if (!namesCounter.containsKey(username)) {
                namesCounter.put(username, 1);
            } else if (namesCounter.containsKey(username)) {
                namesCounter.put(username, namesCounter.get(username) + 1);
            }
            int counterForAll = namesCounter.size();

            map.put("greeting", greetMessage);
            map.put("counterForAll", counterForAll);

            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/greeted", (req, res) -> {

            ArrayList<String> greetedNames = new ArrayList<>();

            for (Map.Entry<String, Integer> names : namesCounter.entrySet()) {
                String availableNames = names.getKey();
                greetedNames.add(availableNames);
            }

            map.put("getNames", greetedNames);

            return new ModelAndView(map, "greeted.handlebars");
        }, new HandlebarsTemplateEngine());

        get("/greeted/:username", (req, res) -> {
            String username = req.params("username");
            username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
            int counterForOneUser = 0;

            if (namesCounter.containsKey(username)) {
                counterForOneUser = namesCounter.get(username);
            }
            System.out.println(username + counterForOneUser);

            map.put("counterForOneUser", counterForOneUser);

            return new ModelAndView(map, "greeted.handlebars");
        }, new HandlebarsTemplateEngine());
    }
}
