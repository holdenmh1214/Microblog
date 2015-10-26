package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Posts> posts = new ArrayList<>();
        User user = new User();


        Spark.staticFileLocation("/public");
        Spark.init();

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    user.name = request.queryParams("name");
                    response.redirect("/posts");
                    return "";
                })
        );

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Posts post = new Posts(); // created a new Post class and stored textArea in per Duke's suggestion
                    post.textArea = request.queryParams("post");
                    posts.add(post);
                    response.redirect("/posts");
                    return "";
                })
        );

        Spark.get(
                "/posts",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("name", user.name);
                    m.put("post", posts);
                    return new ModelAndView(m, "posts.html");
                }),
                new MustacheTemplateEngine()
        );


    }
}
