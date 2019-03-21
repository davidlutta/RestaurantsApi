package dao;

import models.FoodType;
import models.Restaurants;

import java.util.List;

public interface FoodTypeDao {
    //create
    void add(FoodType foodType);
    void addFoodTypeToRestaurant(FoodType foodType, Restaurants restaurant);

    //read
    List<FoodType> getAll();
    List<Restaurants> getAllRestaurantsForAFoodType(int foodTypeId);
    FoodType findById(int id);

    //delete
    void deletebyId(int id);
    void clearAll();
}
