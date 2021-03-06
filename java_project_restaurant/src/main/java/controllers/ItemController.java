package controllers;

import db.*;
import models.*;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class ItemController {

    public ItemController(){this.setupEndPoints();}

    public void setupEndPoints(){
        get("/items", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Item> items = DBHelper.getAll(Item.class);
            model.put("template", "templates/items/index.vtl");
            model.put("items", items);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

//        get("/items/new", (req,res) -> {
//            HashMap<String, Object> model = new HashMap<>();
//            List<ItemType> types = DBItem.allItemTypes();
//            model.put("template", "templates/items/new_deprecated.vtl");
//            model.put("itemTypes", types);
//            return new ModelAndView(model, "templates/layout.vtl");
//        }, new VelocityTemplateEngine());

        get("/items/:num", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Item item = DBHelper.find(Item.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/items/show.vtl");
            model.put("item", item);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/items/:num/edit", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Item item = DBHelper.find(Item.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/items/edit_deprecated.vtl");
            model.put("item", item);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/items", (req,res) -> {
//          ItemType type

            String type = req.queryParams("type");
            Item item = new Item(ItemType.valueOf(type));

            DBHelper.save(item);
            res.redirect("/items");
            return null;
        }, new VelocityTemplateEngine());

        post("/items/:num/edit", (req,res) -> {
            Item item = DBHelper.find(Item.class, Integer.parseInt(req.params(":num")));
            String type = req.queryParams("type");
            item.setItemType(ItemType.valueOf(type));
            DBHelper.save(item);
            res.redirect("/items");
            return null;
        }, new VelocityTemplateEngine());

        post("/items/:num/delete", (req,res) -> {
            Item item = DBHelper.find(Item.class, Integer.parseInt(req.params(":num")));
            DBHelper.delete(item);
            HashMap<String, Object> model = new HashMap<>();
            res.redirect("/item");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("restaurant/:rest_id/bill/:bill_id/items/:num/delete", (req,res) -> {
            int restaurantId = Integer.parseInt(req.params(":rest_id"));
            int billId = Integer.parseInt(req.params(":bill_id"));
            Item item = DBHelper.find(Item.class, Integer.parseInt(req.params(":num")));
            DBHelper.delete(item);
            HashMap<String, Object> model = new HashMap<>();
            res.redirect("/home/restaurants/"+restaurantId+"/bills/"+billId);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num/items", (req,res) -> {
            int restaurantId = Integer.parseInt(req.params(":num"));
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, restaurantId);
            List<Booking> bookings = DBRestaurant.getRestaurantsBookings(restaurant);
            List<Bill> bills = DBBooking.findBookingsBills(bookings);
            List<Item> items = DBBill.findBillsItems(bills);
            model.put("items", items);
            model.put("restaurant", restaurant);
            model.put("template", "templates/items/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num1/items/:num2", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params(":num1")));
            Item item = DBHelper.find(Item.class, Integer.parseInt(req.params(":num2")));
            model.put("template", "templates/items/show.vtl");
            model.put("restaurant", restaurant);
            model.put("item", item);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/home/restaurants/:num1/bookings/:num2/items/new", (req, res) -> {
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params(":num1")));
            Booking booking = DBHelper.find(Booking.class, Integer.parseInt(req.params(":num2")));
            Bill bill = DBBooking.findBookingBill(booking);
            Item item = new Item(ItemType.valueOf(req.queryParams("type")));
            item.setBill(bill);
            DBHelper.save(item);
            String redirect = "/home/restaurants/" + req.params(":num1") + "/bookings/" + req.params(":num2");
            res.redirect(redirect);
            return null;
        }, new VelocityTemplateEngine());

        post("/home/restaurants/:num1/bookings/:num2/items/:num3/delete", (req, res) -> {
            Item item = DBHelper.find(Item.class, Integer.parseInt(req.params(":num3")));
            DBHelper.delete(item);
            String redirect = "/home/restaurants/" + req.params(":num1") + "/bookings/" + req.params(":num2");
            res.redirect(redirect);
            return null;
        }, new VelocityTemplateEngine());
    }
}
