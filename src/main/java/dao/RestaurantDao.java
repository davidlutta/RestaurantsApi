package dao;

import models.Restaurants;

import java.util.List;

public interface RestaurantDao {
    //create
    void add(Restaurants restaurants);

    //read
    List<Restaurants> getAll();
    Restaurants findById(int id);

    //update
    void update(int id, String name, String address, String zipcode, String phone, String email, String website);

    //delete
    void deleteById(int id);
    void clearAll();
}
