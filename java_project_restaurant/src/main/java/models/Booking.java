package models;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    private int id;
    private Restaurant restaurant;
    private List<Customer> customers;
    private RestaurantTable restaurantTable;
    private Date dateTime;
    private int bookingLength;
    private Bill bill;

    public Booking(){
    }

    public Booking(Restaurant restaurant, List<Customer> customers, RestaurantTable restaurantTable, Date dateTime, int bookingLength) {
        this.restaurant = restaurant;
        this.customers = customers;
        this.restaurantTable = restaurantTable;
        this.dateTime = dateTime;
        this.bookingLength = bookingLength;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToMany
    @JoinTable(name="booking_customer",
            joinColumns = {@JoinColumn(name="booking_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name="customer_id", nullable =false, updatable = false)})
    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_table_id")
    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    @Column(name = "date_time")
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "booking_length")
    public int getBookingLength() {
        return bookingLength;
    }

    public void setBookingLength(int bookingLength) {
        this.bookingLength = bookingLength;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

//    not really sure how to do a One to One here :S

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
