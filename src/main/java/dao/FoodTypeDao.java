package dao;

import models.FoodType;

import java.util.List;

public interface FoodTypeDao {
    //create
    void add(FoodType foodType);

    //read
    List<FoodType> getAll();

    //delete
    void deletebyId(int id);
    void clearAll();
}
