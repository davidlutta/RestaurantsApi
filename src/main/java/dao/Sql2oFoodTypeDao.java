package dao;

import models.FoodType;
import models.Restaurants;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oFoodTypeDao implements FoodTypeDao {
    private final Sql2o sql2o;

    public Sql2oFoodTypeDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(FoodType foodType) {
        String sql = "INSERT INTO foodtypes(name) VALUES(:name)";
        try(Connection connection = sql2o.open()) {
            int id = (int) connection.createQuery(sql,true)
                    .bind(foodType)
                    .executeUpdate()
                    .getKey();
            foodType.setId(id);
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<FoodType> getAll() {
        try(Connection connection = sql2o.open()) {
            return connection.createQuery("SELECT * FROM foodtypes")
                    .executeAndFetch(FoodType.class);
        }
    }

    @Override
    public void addFoodTypeToRestaurant(FoodType foodType, Restaurants restaurant) {
        String sql ="INSERT INTO restaurants_foodtypes(foodtypeid,restaurantid) VALUES(:foodtypeId,:restaurantId)";
        try(Connection connection = sql2o.open()) {
            connection.createQuery(sql)
                    .addParameter("foodtypeId",foodType.getId())
                    .addParameter("restaurantId",restaurant.getId())
                    .executeUpdate();
            System.out.println("Success in Inserting values");
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Restaurants> getAllRestaurantsForAFoodType(int foodTypeId) {
        List<Restaurants> restaurants = new ArrayList<>();
        String query = "SELECT restaurantid FROM restaurants_foodtypes WHERE foodtypeid = :foodtypeId";
        try(Connection connection = sql2o.open()) {
            List<Integer> allRestaurantIds = connection.createQuery(query)
                    .addParameter("foodtypeId",foodTypeId)
                    .executeAndFetch(Integer.class);
            for (Integer restaurantId : allRestaurantIds){
                String sql = "SELECT * FROM restaurants WHERE id = :restaurantid";
                restaurants.add(
                  connection.createQuery(sql)
                  .addParameter("restaurantid",restaurantId)
                  .executeAndFetchFirst(Restaurants.class)
                );
            }
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return restaurants;

    }

    @Override
    public void deletebyId(int id) {
        String sql = "DELETE from foodtypes where id = :id";
        String joinedTableQuery = "DELETE FROM restaurants_foodtypes WHERE foodtypeid = :foodtypeId";
        try(Connection connection = sql2o.open()) {
            connection.createQuery(sql)
                    .addParameter("id",id)
                    .executeUpdate();
            connection.createQuery(joinedTableQuery)
                    .addParameter("foodtypeId",id)
                    .executeUpdate();
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM foodtypes";
        try(Connection connection = sql2o.open()){
            connection.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
