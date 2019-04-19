package dao;

import models.Review;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oReviewDao implements ReviewDao {
    private final Sql2o sql2o;

    public Sql2oReviewDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Review review) {
        String sql = "INSERT INTO reviews (writtenby, content, rating, restaurantid, createdat) VALUES (:writtenBy, :content, :rating, :restaurantId, :createdAt)";
        try (Connection connection = sql2o.open()){
            int id = (int) connection.createQuery(sql,true)
                    .bind(review)
                    .executeUpdate()
                    .getKey();
            review.setId(id);
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Review> getAll() {
        try(Connection connection = sql2o.open()) {
            return connection.createQuery("SELECT * FROM reviews")
                    .executeAndFetch(Review.class);
        }
    }

//    @Override
//    public List<Review> getAllReviewsByRestaurantId(int restaurantId) {
//        String sql = "SELECT * FROM reviews WHERE restaurantId = :restaurantId";
//        String Sql = "SELECT * FROM reviews WHERE restaurantid = :restaurantId";
//        try(Connection connection = sql2o.open()) {
//            return connection.createQuery(sql)
//                    .addParameter("restaurantId",restaurantId)
//                    .executeAndFetch(Review.class);
//        }
//    }

    @Override
    public List<Review> getAllReviewsByRestaurantId(int restaurantId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM reviews WHERE restaurantId = :restaurantId")
                    .addParameter("restaurantId", restaurantId)
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from reviews where id = :id";
        try(Connection connection = sql2o.open()) {
            connection.createQuery(sql)
                    .addParameter("id",id)
                    .executeUpdate();
        } catch (Sql2oException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM reviews";
        try(Connection connection = sql2o.open()){
            connection.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
