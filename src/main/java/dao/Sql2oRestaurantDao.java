package dao;

import models.FoodType;
import models.Restaurants;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oRestaurantDao implements RestaurantDao {
    private final Sql2o sql2o;

    public Sql2oRestaurantDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Restaurants restaurants) {
        String sql = "INSERT INTO restaurants (name, address, zipcode, phone, website, email) VALUES (:name, :address, :zipcode, :phone, :website, :email)";
        try (Connection connection = sql2o.open()){
            int id = (int) connection.createQuery(sql,true)
                    .bind(restaurants)
                    .executeUpdate()
                    .getKey();
            restaurants.setId(id);
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Restaurants> getAll() {
        try(Connection connection = sql2o.open()) {
            return connection.createQuery("SELECT * FROM restaurants")
                    .executeAndFetch(Restaurants.class);
        }

    }

    @Override
    public void addRestaurantToFoodType(Restaurants restaurants, FoodType foodType) {
     String sql = "INSERT INTO restaurants_foodtypes(foodtypeid,restaurantid) VALUES(:foodtypeId,:restaurantId)";
     try(Connection connection = sql2o.open()){
         connection.createQuery(sql)
                 .addParameter("foodtypeId",foodType.getId())
                 .addParameter("restaurantId",restaurants.getId())
                 .executeUpdate();
     } catch (Sql2oException e){
         e.printStackTrace();
     }
    }

    @Override
    public List<FoodType> getFoodTypesByRestaurants(int restaurantId) {
        List<FoodType> foodTypes = new ArrayList<>();
        String query = "SELECT foodTypeId FROM restaurants_foodtypes WHERE restaurantid = :restaurantId";
        try(Connection connection = sql2o.open()){
            List<Integer> allFoodTypesId = connection.createQuery(query)
                    .addParameter("restaurantId",restaurantId)
                    .executeAndFetch(Integer.class);
            for (Integer foodTypeId : allFoodTypesId){
                String sql = "SELECT * FROM foodtypes WHERE id = :foodtypeid";
                foodTypes.add(
                        connection.createQuery(sql)
                        .addParameter("foodtypeid",foodTypeId)
                        .executeAndFetchFirst(FoodType.class)
                );
            }
        }catch (Sql2oException e){
            e.printStackTrace();
        }
        return foodTypes;
    }

    @Override
    public Restaurants findById(int id) {
        String sql = "SELECT * FROM restaurants where id = :id";
        //SELECT * FROM restaurants WHERE id = :id
        try(Connection connection = sql2o.open()) {
            return connection.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Restaurants.class);
        }
    }

    @Override
    public void update(int id, String newName, String newAddress, String newZipcode, String newPhone, String newEmail, String newWebsite) {
        String sql = "UPDATE restaurants SET (name, address, zipcode, phone, website, email) = (:name, :address, :zipcode, :phone, :website, :email) WHERE id=:id";
        try (Connection connection=sql2o.open()) {
            connection.createQuery(sql)
                    .addParameter("name",newName)
                    .addParameter("address",newAddress)
                    .addParameter("zipcode",newZipcode)
                    .addParameter("phone",newPhone)
                    .addParameter("email",newEmail)
                    .addParameter("website",newWebsite)
                    .addParameter("id",id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM restaurants where id = :id";
        String joinTableQuery = "DELETE FROM restaurants_foodtypes WHERE restaurantid = :restaurantId";
        try(Connection connection = sql2o.open()){
            connection.createQuery(sql)
                    .addParameter("id",id)
                    .executeUpdate();
            connection.createQuery(joinTableQuery)
                    .addParameter("restaurantId",id)
                    .executeUpdate();
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM restaurants";
        try (Connection connection = sql2o.open()) {
            connection.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
