package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Posts> posts = new ArrayList<>();


        Spark.staticFileLocation("/public");
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("name");
                    if (name == null){
                        return new ModelAndView(new HashMap<>(),"not-logged-in.html");
                    }
                    HashMap m = new HashMap();
                    m.put("name", name);
                    m.put("posts", posts);
                    return new ModelAndView(m, "logged-in.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    Session session = request.session();
                    String name = request.queryParams("name");
                    session.attribute("name", name);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Posts post = new Posts(); // created a new Post class and stored textArea in per Duke's suggestion
                    post.id = posts.size()+1;
                    post.textArea = request.queryParams("post");
                    posts.add(post);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/delete-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try {
                        int idNum = Integer.valueOf(id);
                        posts.remove(idNum-1);
                        for (int i=0; i< posts.size(); i++){ //for loop to update beer numbers
                            posts.get(i).id = i+1;
                        }
                    }catch (Exception e) {

                    }
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/edit-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try {
                        int idNum = Integer.valueOf(id);
                        posts.get(idNum-1).textArea= request.queryParams("text");
                        for (int i=0; i< posts.size(); i++){ //for loop to update beer numbers
                            posts.get(i).id = i+1;
                        }
                    }catch (Exception e) {

                    }
                    response.redirect("/");
                    return "";
                })
        );


    }
}
