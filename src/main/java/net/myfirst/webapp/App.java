package net.myfirst.webapp;

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
    }
}
