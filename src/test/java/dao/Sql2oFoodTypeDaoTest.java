package dao;

import models.FoodType;
import models.Restaurants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oFoodTypeDaoTest {
    private Connection dbConnection;
    private Sql2oFoodTypeDao foodTypeDao;
    private Sql2oRestaurantDao restaurantDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString,"","");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodTypeDao = new Sql2oFoodTypeDao(sql2o);
        dbConnection = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        dbConnection.close();
    }

    @Test
    public void add() {
        FoodType testFoodType = setUpFoodType();
        assertEquals(1,testFoodType.getId());
    }

    @Test
    public void getAll() {
        FoodType testFoodType = setUpFoodType();
        FoodType anotherFoodType = setUpAnotherFoodType();
        assertEquals(2,foodTypeDao.getAll().size());
    }

    @Test
    public void deleteAll() {
        FoodType testFoodType = setUpFoodType();
        FoodType anotherFoodType = setUpAnotherFoodType();
        foodTypeDao.deletebyId(testFoodType.getId());
        assertEquals(1,foodTypeDao.getAll().size());
    }

    @Test
    public void clearAll() throws Exception {
        FoodType testFoodType = setUpFoodType();
        FoodType anotherFoodType = setUpAnotherFoodType();
        foodTypeDao.clearAll();
        assertEquals(0,foodTypeDao.getAll().size());
    }

    @Test
    public void addsFoodTypeCorrectlyToRestaurant(){
        Restaurants restaurants = setUpRestaurant();
        Restaurants anotherRestaurant = setUpAlternativeRestaurant();
        restaurantDao.add(restaurants);
        restaurantDao.add(anotherRestaurant);

        FoodType foodType = setUpFoodType();
        foodTypeDao.add(foodType);

        foodTypeDao.addFoodTypeToRestaurant(foodType,restaurants);
        foodTypeDao.addFoodTypeToRestaurant(foodType,anotherRestaurant);
        assertEquals(2,foodTypeDao.getAllRestaurantsForAFoodType(foodType.getId()).size());
    }

    @Test
    public void deletingRestaurantAlsoUpdatesJoinTable(){
        FoodType foodType = new FoodType("Cakes");
        foodTypeDao.add(foodType);
        Restaurants restaurants = setUpRestaurant();
        restaurantDao.add(restaurants);
        Restaurants anotherRestaurant = setUpAlternativeRestaurant();
        restaurantDao.add(anotherRestaurant);

        restaurantDao.addRestaurantToFoodType(restaurants,foodType);
        restaurantDao.addRestaurantToFoodType(anotherRestaurant,foodType);

        restaurantDao.deleteById(anotherRestaurant.getId());

        assertEquals(0,restaurantDao.getFoodTypesByRestaurants(anotherRestaurant.getId()).size());
    }


    //Set Up

    public FoodType setUpFoodType(){
        FoodType foodType = new FoodType("Mochi");
        foodTypeDao.add(foodType);
        return foodType;
    }
    public FoodType setUpAnotherFoodType(){
        FoodType anotherFoodType = new FoodType("Sushi");
        foodTypeDao.add(anotherFoodType);
        return anotherFoodType;
    }
    public Restaurants setUpRestaurant(){
        Restaurants restaurants = new Restaurants("Burger King","The Hub","1234","1234567","info@bk.com","www.bk.com");
        restaurantDao.add(restaurants);
        return restaurants;
    }
    private Restaurants setUpAlternativeRestaurant() {
        Restaurants restaurants = new Restaurants("Mama Rocks","Westlands","0987","0987679","mama@rocks.com","www.mama.rocks");
        restaurantDao.add(restaurants);
        return restaurants;
    }

}