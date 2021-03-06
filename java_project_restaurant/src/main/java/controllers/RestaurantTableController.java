package controllers;

import db.DBHelper;
import db.DBRestaurant;
import db.DBRestaurantTable;
import models.Booking;
import models.Restaurant;
import models.RestaurantTable;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class RestaurantTableController {

    public RestaurantTableController(){this.setupEndPoints();}

    public void setupEndPoints(){
        get("/tables", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            List<Restaurant> restaurants = DBHelper.getAll(Restaurant.class);
            model.put("template", "templates/tables/index.vtl");
            model.put("tables", tables);
            model.put("restaurants",restaurants);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/tables/restaurant/:id", (req, res) -> {
            int restaurantId = Integer.parseInt(req.params(":id"));
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, restaurantId);
            List<RestaurantTable> tables = DBRestaurant.getRestaurantsTables(restaurant);
            model.put("template", "templates/tables/index.vtl");
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/tables/new", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Restaurant> restaurants = DBHelper.getAll(Restaurant.class);
            model.put("template", "templates/tables/new.vtl");
            model.put("restaurants", restaurants);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num/tables/new", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Restaurant> restaurants = DBHelper.getAll(Restaurant.class);
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/tables/new.vtl");
            model.put("restaurants", restaurants);
            model.put("selectedRestaurant", restaurant);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/tables/:num", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            RestaurantTable table = DBHelper.find(RestaurantTable.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/tables/show.vtl");
            model.put("table", table);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/tables/:num/edit", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            RestaurantTable table = DBHelper.find(RestaurantTable.class, Integer.parseInt(req.params(":num")));
            List<Restaurant> restaurants = DBHelper.getAll(Restaurant.class);
            model.put("template", "templates/tables/edit.vtl");
            model.put("table", table);
            model.put("restaurants", restaurants);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/tables", (req,res) -> {
//          int tableNumber, int capacity, Restaurant restaurant

            int tableNumber = Integer.parseInt(req.queryParams("tableNumber"));
            int capacity = Integer.parseInt(req.queryParams("capacity"));
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.queryParams("restaurant")));

            RestaurantTable restaurantTable = new RestaurantTable(tableNumber, capacity, restaurant);
            DBHelper.save(restaurantTable);
            res.redirect("/tables");
            return null;
        }, new VelocityTemplateEngine());

        post("/home/restaurants/:num/tables", (req,res) -> {
//          int tableNumber, int capacity, Restaurant restaurant

            int tableNumber = Integer.parseInt(req.queryParams("tableNumber"));
            int capacity = Integer.parseInt(req.queryParams("capacity"));
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.queryParams("restaurant")));

            RestaurantTable restaurantTable = new RestaurantTable(tableNumber, capacity, restaurant);
            DBHelper.save(restaurantTable);
            String redirect = "/home/restaurants/" + req.queryParams("restaurant") + "/tables";
            res.redirect(redirect);
            return null;
        }, new VelocityTemplateEngine());

        post("/tables/:num/delete", (req,res) -> {
            RestaurantTable table = DBHelper.find(RestaurantTable.class, Integer.parseInt(req.params(":num")));
            DBHelper.delete(table);
            res.redirect("/tables");
            return null;
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num/tables", (req,res) -> {
            int restaurantId = Integer.parseInt(req.params(":num"));
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, restaurantId);
            List<RestaurantTable> tables = DBRestaurant.getRestaurantsTables(restaurant);
            model.put("tables", tables);
            model.put("restaurant", restaurant);
            model.put("template", "templates/tables/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num1/tables/:num2", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params(":num1")));
            RestaurantTable table = DBHelper.find(RestaurantTable.class, Integer.parseInt(req.params(":num2")));
            List<Booking> bookings = DBRestaurantTable.getTableBookings(table);
            model.put("template", "templates/tables/show.vtl");
            model.put("restaurant", restaurant);
            model.put("table", table);
            model.put("bookings", bookings);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num1/tables/:num2/edit", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params(":num1")));
            RestaurantTable table = DBHelper.find(RestaurantTable.class, Integer.parseInt(req.params(":num2")));
            List<Restaurant> restaurants = DBHelper.getAll(Restaurant.class);
            model.put("template", "templates/tables/edit.vtl");
            model.put("table", table);
            model.put("selectedRestaurant", restaurant);
            model.put("restaurants", restaurants);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/home/restaurants/:num1/tables/:num2" , (req,res) -> {
            RestaurantTable table = DBHelper.find(RestaurantTable.class, Integer.parseInt(req.params(":num2")));
            int tableNumber = Integer.parseInt(req.queryParams("tableNumber"));
            int capacity = Integer.parseInt(req.queryParams("capacity"));
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.queryParams("restaurant")));

            table.setCapacity(capacity);
            table.setRestaurant(restaurant);
            table.setTableNumber(tableNumber);
            DBHelper.save(table);

            String redirect = "/home/restaurants/" + restaurant.getId() + "/tables/" + req.params(":num2");
            res.redirect(redirect);
            return null;
        }, new VelocityTemplateEngine());

        post("/home/restaurants/:num1/tables/:num2/delete", (req,res) -> {
            RestaurantTable table = DBHelper.find(RestaurantTable.class, Integer.parseInt(req.params(":num2")));
            DBHelper.delete(table);
            String redirect = "/home/restaurants/" + req.params(":num1") + "/tables";
            res.redirect(redirect);
            return null;
        }, new VelocityTemplateEngine());
    }
}
