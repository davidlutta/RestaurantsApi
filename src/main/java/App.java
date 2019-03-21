import com.google.gson.Gson;
import dao.Sql2oFoodTypeDao;
import dao.Sql2oRestaurantDao;
import dao.Sql2oReviewDao;
import exceptions.ApiException;
import models.FoodType;
import models.Restaurants;
import models.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Sql2oRestaurantDao restaurantDao;
        Sql2oFoodTypeDao foodTypeDao;
        Sql2oReviewDao reviewDao;
        Connection connection;
        Gson gson = new Gson();
        Logger logger = LoggerFactory.getLogger(App.class);

        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/RestaurantApi.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodTypeDao = new Sql2oFoodTypeDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);
//        connection = sql2o.open();

        // FIXME: 3/17/19

        //CREATE
        post("/restaurants/new", "application/json", (req, res) -> {
            Restaurants restaurant = gson.fromJson(req.body(), Restaurants.class);
            restaurantDao.add(restaurant);
            res.status(201);
            req.body();
            return gson.toJson(restaurant);
        });
        post("/foodtypes/new","application/json",(request, response) -> {
            FoodType foodType = gson.fromJson(request.body(),FoodType.class);
            foodTypeDao.add(foodType);
            response.status(201);
            return gson.toJson(foodType);
        });
        post("/restaurants/:restaurantId/reviews/new","application/json",(request, response) -> {
            String restId = request.params("restaurantId");
            Review review = gson.fromJson(request.body(),Review.class);
            review.setRestaurantId(Integer.parseInt(restId));
            reviewDao.add(review);
            response.status(201);
            return gson.toJson(review);
        });
        post("/restaurants/:restaurantId/foodtype/:foodtypeId","application/json",(request, response) -> {
            String restId = request.params("restaurantId");
            String foodId = request.params("foodtypeId");

            Restaurants foundRestaurant = restaurantDao.findById(Integer.parseInt(restId));
            FoodType foundFoodtype = foodTypeDao.findById(Integer.parseInt(foodId));

            if (foundRestaurant != null && foundFoodtype != null){
                foodTypeDao.addFoodTypeToRestaurant(foundFoodtype,foundRestaurant);
                response.status(201);
                return gson.toJson(String.format("Restaurant '%s' and Foodtype '%s' have been associated",foundRestaurant.getName(),foundFoodtype.getName()));
            } else {
                throw new ApiException(404,String.format("Restaurant or foodtype do not exist"));
            }
        });


        //READ
        get("/restaurants","application/json",(request, response) -> {
            if (restaurantDao.getAll().size() > 0) {
                return gson.toJson(restaurantDao.getAll());
            } else {
                return "{\"message\":\"I'm sorry, but no restaurants are currently listed in the database.\"}";
            }
        });

        get("restaurants/:id","application/json",(request, response) -> {
            String restId = request.params("id");
            Restaurants restaurantToBeFound = restaurantDao.findById(Integer.parseInt(restId));
            if (restaurantToBeFound == null){
                throw new ApiException(404,String.format("No restaurant with the id: \"%s\" exists", request.params("id")));
            } else {
                return gson.toJson(restaurantDao.findById(Integer.parseInt(restId)));
            }
        });
        get("restaurants/:id/reviews","application/json",(request, response) -> {
            String restId = request.params("id");
            List<Review> allReviews;
            Restaurants restaurantToBeFound = restaurantDao.findById(Integer.parseInt(restId));
            if (restaurantToBeFound == null){
                throw new ApiException(404,String.format("No restaurant with the id: \"%s\" exists", request.params("id")));
            }
            allReviews = reviewDao.getAllReviewsByRestaurantId(Integer.parseInt(restId));
            if (allReviews.size() <= 0){
                return "{\"message\":\"I'm sorry, but no reviews are currently listed for this restaurant in the database Would you like to add one ?\"}";
            } else {
                return gson.toJson(allReviews);
            }
        });
        get("restaurants/:id/foodtypes","application/json",(request, response) -> {
            String restId = request.params("id");
            Restaurants restaurantFound = restaurantDao.findById(Integer.parseInt(restId));
            if (restaurantFound == null){
                throw new ApiException(404,String.format("No restaurant with the id: \"%s\" exists", request.params("id")));
            } else if (restaurantDao.getFoodTypesByRestaurants(Integer.parseInt(restId)).size() ==0){
                return "{\"message\":\"I'm sorry, but no foodtypes are listed for this restaurant.\"}";
            } else {
                return gson.toJson(restaurantDao.getFoodTypesByRestaurants(Integer.parseInt(restId)));
            }
        });
        get("/foodtypes","application/json",(request, response) -> {
            if (foodTypeDao.getAll().size() > 0){
                return gson.toJson(foodTypeDao.getAll());
            } else {
                return "{\"message\":\"I'm sorry, but no Foodtypes are currently listed in the database.\"}";
            }
        });
        get("/foodtypes/:id/restaurants","application/json",(request, response) -> {
            String foodId = request.params("id");
            FoodType foodTypeToBeFound = foodTypeDao.findById(Integer.parseInt(foodId));
            if (foodTypeToBeFound == null){
                throw new ApiException(404,String.format("No foodtype with the id \"%s\" exists",foodId));
            } else if(foodTypeDao.getAllRestaurantsForAFoodType(Integer.parseInt(foodId)).size() == 0){
                return "{\"message\":\"I'm sorry, but no restaurants are listed for this foodtype.\"}";
            } else {
                return gson.toJson(foodTypeDao.getAllRestaurantsForAFoodType(Integer.parseInt(foodId)));
            }
        });



        //Filters
        after((request, response) -> {
            response.type("application/json");
        });

        exception(ApiException.class,((exception, request, response) -> {
            ApiException error = (ApiException) exception;

            Map<String,Object> jsonMap = new HashMap<>();

            jsonMap.put("status",error.getStatusCode());
            jsonMap.put("errorMessage",error.getMessage());
            response.type("application/json");
            response.status(error.getStatusCode());
            response.body(gson.toJson(jsonMap));
        }));
    }
}
