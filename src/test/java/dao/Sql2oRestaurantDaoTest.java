package dao;

import models.Restaurants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oRestaurantDaoTest {
    private Connection dbConnection;
    private RestaurantDao restaurantDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString,"","");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        dbConnection = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        dbConnection.close();
    }

    @Test
    public void add() {
        Restaurants restaurants = setUpRestaurant();
        assertEquals(1,restaurantDao.getAll().size());
    }

    @Test
    public void getAll() {
        Restaurants restaurants = setUpRestaurant();
        Restaurants altRestaurants = setUpAlternativeRestaurant();
        assertEquals(2,restaurantDao.getAll().size());
    }

    @Test
    public void findById() {
        Restaurants restaurants = setUpRestaurant();
        Restaurants altRestaurants = setUpAlternativeRestaurant();
        assertEquals(altRestaurants,restaurantDao.findById(altRestaurants.getId()));
    }

    @Test
    public void update() {
        Restaurants restaurants = setUpRestaurant();
        restaurantDao.update(restaurants.getId(),"Dominos","Marsabit Plaza","123456","098754","info@dominos.com","www.dominos.com");
        Restaurants retrievedRestaurant = restaurantDao.findById(restaurants.getId());
        assertEquals("Dominos",retrievedRestaurant.getName());
    }

    @Test
    public void deleteById() {
        Restaurants restaurants = setUpRestaurant();
        Restaurants altRestaurants = setUpAlternativeRestaurant();
        restaurantDao.deleteById(restaurants.getId());
        assertNotEquals(restaurants,restaurantDao.findById(restaurants.getId()));
    }

    @Test
    public void clearAll() throws Exception {
        Restaurants restaurants = setUpRestaurant();
        Restaurants altRestaurants = setUpAlternativeRestaurant();
        restaurantDao.clearAll();
        assertEquals(0,restaurantDao.getAll().size());
    }

    //SETUP
    public Restaurants setUpRestaurant(){
        Restaurants myRestaurant = new Restaurants("Burger King","The Hub","90210","1234567","info@bk.com","www.bk.com");
        restaurantDao.add(myRestaurant);
        return myRestaurant;
    }
    public Restaurants setUpAlternativeRestaurant(){
        Restaurants restaurants = new Restaurants("Pizza inn","Capital Center","90210","098765","info@pizza.inn","www.pizza.inn");
        restaurantDao.add(restaurants);
        return restaurants;
    }
}