package controllers;

import db.DBBooking;
import db.DBHelper;
import db.DBRestaurant;
import models.Bill;
import models.Booking;
import models.Restaurant;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class BillController {

    public BillController(){this.setupEndPoints();}

    public void setupEndPoints(){
        get("/bills", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Bill> bills = DBHelper.getAll(Bill.class);
            List<Booking> bookings = DBHelper.getAll(Booking.class);
            model.put("template", "templates/bills/index.vtl");
            model.put("bookings", bookings);
            model.put("bills", bills);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/bills/new", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/bills/new.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/bills/:num", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Bill bill = DBHelper.find(Bill.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/bills/show.vtl");
            model.put("bill", bill);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());



        get("/bills/:num/edit", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Bill bill = DBHelper.find(Bill.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/bills/edit.vtl");
            model.put("bill", bill);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("bills", (req,res) -> {
//            Booking booking
            Booking booking = DBHelper.find(Booking.class, Integer.parseInt(req.queryParams("booking")));
            Bill bill = new Bill(booking);

            DBHelper.save(bill);
            return null;
        }, new VelocityTemplateEngine());

//        post("bills/:num/edit", (req,res) -> {
//            Bill bill = DBHelper.find(Bill.class, Integer.parseInt(req.params(":num")));
//            bill.setBooking(req.queryParams("booking"));
//            bill.setItems(req.queryParams("item"));
//
//
//            DBHelper.save(bill);
//            return null;
//        }, new VelocityTemplateEngine());

        post("bills/:num/delete", (req,res) -> {
            Bill bill = DBHelper.find(Bill.class, Integer.parseInt(req.params(":num")));
            DBHelper.delete(bill);
            res.redirect("/bills");
            return null;
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num/bills", (req,res) -> {
            int restaurantId = Integer.parseInt(req.params(":num"));
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, restaurantId);
            List<Booking> bookings = DBRestaurant.getRestaurantsBookings(restaurant);
            List<Bill> bills = DBBooking.findBookingsBills(bookings);
            model.put("bills", bills);
            model.put("bookings", bookings);
            model.put("restaurant", restaurant);
            model.put("template", "templates/bills/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num1/bills/:num2", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params(":num1")));
            List<Booking> bookings = DBRestaurant.getRestaurantsBookings(restaurant);
            Bill bill = DBHelper.find(Bill.class, Integer.parseInt(req.params(":num2")));
            model.put("template", "templates/bills/show.vtl");
            model.put("restaurant", restaurant);
            model.put("bookings", bookings);
            model.put("bill", bill);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
    }
}
