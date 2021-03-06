package controllers;

import db.DBCustomer;
import db.DBHelper;
import models.Booking;
import models.Customer;
import models.Restaurant;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class CustomerController {
    public CustomerController(){this.setupEndPoints();}

    public void setupEndPoints(){
        get("/customers", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Customer> customers = DBHelper.getAll(Customer.class);
            model.put("template", "templates/customers/index.vtl");
            model.put("customers", customers);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/customers/new", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/customers/new.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("home/restaurants/:num/customers/new", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/customers/new.vtl");
            model.put("restaurant", restaurant);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/customers/:num", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Customer customer = DBHelper.find(Customer.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/customers/show.vtl");
            model.put("customer", customer);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/customers/:num/edit", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Customer customer = DBHelper.find(Customer.class, Integer.parseInt(req.params(":num")));
            model.put("template", "templates/customers/edit.vtl");
            model.put("customer", customer);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/customers", (req,res) -> {
//            String firstName, String lastName, double budget
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            double budget = Double.parseDouble(req.queryParams("budget"));

            Customer customer = new Customer(firstName, lastName, budget);
            DBHelper.save(customer);
            res.redirect("/customers");
            return null;
        }, new VelocityTemplateEngine());

        post("/home/restaurants/:num/customers", (req,res) -> {
//            String firstName, String lastName, double budget
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            double budget = Double.parseDouble(req.queryParams("budget"));

            Customer customer = new Customer(firstName, lastName, budget);
            DBHelper.save(customer);
            String redirect = "/home/restaurants/" + req.params(":num") + "/customers";
            res.redirect(redirect);
            return null;
        }, new VelocityTemplateEngine());

        post("/customers/:num/edit", (req,res) -> {
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            double budget = Double.parseDouble(req.queryParams("budget"));
            Customer customer = DBHelper.find(Customer.class, Integer.parseInt(req.params(":num")));
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setBudget(budget);
            DBHelper.save(customer);
            res.redirect("/customers");
            return null;
        }, new VelocityTemplateEngine());

        post("/customers/:num/delete", (req,res) -> {
            Customer customer = DBHelper.find(Customer.class, Integer.parseInt(req.params(":num")));
            DBHelper.delete(customer);
            res.redirect("/customers");
            return null;
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num/customers", (req,res) -> {
            int restaurantId = Integer.parseInt(req.params(":num"));
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, restaurantId);
            List<Customer> customers = DBCustomer.orderAllByBookingsNumber();
            model.put("customers", customers);
            model.put("restaurant", restaurant);
            model.put("template", "templates/customers/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num1/customers/:num2", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params(":num1")));
            Customer customer = DBHelper.find(Customer.class, Integer.parseInt(req.params(":num2")));
            List<Booking> bookings = DBCustomer.findCustomersBookings(customer);
            model.put("template", "templates/customers/show.vtl");
            model.put("restaurant", restaurant);
            model.put("customer", customer);
            model.put("bookings", bookings);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/home/restaurants/:num1/customers/:num2/edit", (req,res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Customer customer = DBHelper.find(Customer.class, Integer.parseInt(req.params(":num2")));
            Restaurant restaurant = DBHelper.find(Restaurant.class, Integer.parseInt(req.params("num1")));
            model.put("template", "templates/customers/edit.vtl");
            model.put("customer", customer);
            model.put("restaurant", restaurant);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/home/restaurants/:num1/customers/:num2/edit", (req,res) -> {
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            double budget = Double.parseDouble(req.queryParams("budget"));
            Customer customer = DBHelper.find(Customer.class, Integer.parseInt(req.params(":num2")));
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setBudget(budget);
            DBHelper.save(customer);
            String redirect = "/home/restaurants/"+req.params(":num1")+"/customers";
            res.redirect(redirect);
            return null;
        }, new VelocityTemplateEngine());

        post("/home/restaurants/:num1/customers/:num2/delete", (req,res) -> {
            Customer customer = DBHelper.find(Customer.class, Integer.parseInt(req.params(":num2")));
            DBHelper.delete(customer);
            String redirect = "/home/restaurants/"+req.params(":num1")+"/customers";
            res.redirect(redirect);
            return null;
        }, new VelocityTemplateEngine());
    }
}
