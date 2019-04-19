package dao;

import models.Restaurants;
import models.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oReviewDaoTest {
    private Connection DBConnection;
    private Sql2oReviewDao reviewDao;
    private Sql2oRestaurantDao restaurantDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString,"","");
        reviewDao = new Sql2oReviewDao(sql2o);
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        DBConnection = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        DBConnection.close();
    }

    @Test
    public void add() {
        Review testReview = setUpReview();
        assertEquals(1,testReview.getId());
    }

    @Test
    public void getAll() {
        Review testReview1 = setUpReview();
        Review testReview2 = setUpReview();
        assertEquals(2,reviewDao.getAll().size());
    }

    @Test
    public void getAllReviewsByRestaurantId() {
        Restaurants testRestaurant1 = setUpRestaurant();
        Restaurants testRestaurant2 = setUpRestaurant();//Just adding more data
        Review review1 = setUpReviewForRestaurant(testRestaurant1);
        Review review2 = setUpReviewForRestaurant(testRestaurant1);
        Review reviewForOtherRestaurant = setUpReviewForRestaurant(testRestaurant2);
        System.out.println("Restaurant Id:"+testRestaurant1.getId());
        System.out.println(review1.toString());
        System.out.println("Review 2 "+review2.getRestaurantId());
        assertEquals(2,reviewDao.getAllReviewsByRestaurantId(testRestaurant1.getId()).size());
    }

//    @Test
//    public void timeStampIsRetturnedCorrectly()throws Exception{
//        Restaurants testRestaurant = setUpRestaurant();
//        restaurantDao.add(testRestaurant);
//
//        Review testReview = new Review("I really enjoyed my experience","James Kulia",4,testRestaurant.getId());
//        reviewDao.add(testReview);
//
//        long creationTime = testReview.getCreatedAt();
//        long savedTime = reviewDao.getAll().get(0).getCreatedAt();
//        assertEquals(creationTime,savedTime);
//    }


    @Test
    public void timeStampIsReturnedCorrectly() throws Exception {
        Restaurants testRestaurant = setUpRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("I really enjoyed my experience","James Kulia",4,testRestaurant.getId());
        reviewDao.add(testReview);
        long creationTime = testReview.getCreatedAt();
        long savedTime = reviewDao.getAll().get(0).getCreatedAt();
        String formattedCreationTime = testReview.getFormattedCreatedAt();
        String formattedSavedTime = reviewDao.getAll().get(0).getFormattedCreatedAt();
        assertEquals(formattedCreationTime,formattedSavedTime);
        assertEquals(creationTime, reviewDao.getAll().get(0).getCreatedAt());
    }

    @Test
    public void deleteById() {
        Review review = setUpReview();
        Review review1 = setUpReview();
        assertEquals(2,reviewDao.getAll().size());
        reviewDao.deleteById(review1.getId());
        assertEquals(1,reviewDao.getAll().size());
    }

    @Test
    public void clearAll() throws Exception{
        Review review1 = setUpReview();
        Review review2 = setUpReview();
        assertEquals(2,reviewDao.getAll().size());
        reviewDao.clearAll();
        assertEquals(0,reviewDao.getAll().size());
    }


    //SETUPS
    public Review setUpReview(){
        Review review = new Review("Arthur Curry","I Love the restaurant",5,102);
        reviewDao.add(review);
        return review;
    }

    public Review setUpReviewForRestaurant(Restaurants restaurant){
        Review review = new Review("Steve Curry","Amazing !",4,restaurant.getId());
        reviewDao.add(review);
        return review;
    }
    public Restaurants setUpRestaurant(){
        Restaurants restaurant = new Restaurants("Burger King","The Hub","90210","123456","info@bk.com","www.bk.com");
        restaurantDao.add(restaurant);
        return restaurant;
    }
}