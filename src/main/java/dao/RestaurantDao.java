package dao;

import models.FoodType;
import models.Restaurants;

import java.util.List;

public interface RestaurantDao {
    //create
    void add(Restaurants restaurants);
    void addRestaurantToFoodType(Restaurants restaurants, FoodType foodType);

    //read
    List<Restaurants> getAll();
    List<FoodType> getFoodTypesByRestaurants(int restaurantId);
    Restaurants findById(int id);

    //update
    void update(int id, String name, String address, String zipcode, String phone, String email, String website);

    //delete
    void deleteById(int id);
    void clearAll();
}
