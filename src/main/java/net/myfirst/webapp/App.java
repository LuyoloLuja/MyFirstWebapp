package net.myfirst.webapp;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        port(1997);
        staticFiles.location("/public");

        get("/greet", (req, res) -> "Hello!");

        get("/greet/:username", (req, res) -> {
            String name = req.params(":username");
            return "Hello, " + name + "!";
        });

        get("/greet/:username/language/:language", (req, res) -> {
            String name = req.params(":username");
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
            if (!username.equals("")) {
                return "Hello, " + username + "!";
            }
            return "Hello!";
        });

        get("/hello", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/hello", (req, res) -> {

            Map<String, Object> map = new HashMap<>();

            String username = req.queryParams("username");
            String language = req.queryParams("language");
            String greetMessage = "";

            if (!username.equals("") && !language.equals("")) {
                if (language.equals("english")) {
                    greetMessage = "Hello, " + username;
                } else if(language.equals("isixhosa")) {
                    greetMessage = "Molo, " + username;
                } else if (language.equals("afrikaans")) {
                    greetMessage = "More, " + username;
                }
            }
            map.put("greeting", greetMessage);

            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/greeted", (req, res) -> {

            Map<String, String> map = new HashMap<>();
            String username = req.queryParams("username");

            map.put("greetedNames", username);

            for (Map.Entry<String, String> entrySet : map.entrySet()){
                System.out.println(entrySet);
                map.put("greetedNames", entrySet.getValue());
            }

            return new ModelAndView(map, "greeted.handlebars");
        });
    }
}
