package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Review {
    private String content;
    private String writtenBy;
    private int rating;
    private int id;
    private int restaurantId;
    private long createdAt;
    private String formattedCreatedAt;

    public Review(String content, String writtenBy, int rating, int restaurantId) {
        this.content = content;
        this.writtenBy = writtenBy;
        this.rating = rating;
        this.restaurantId = restaurantId;
        this.createdAt = System.currentTimeMillis();
        setFormattedCreatedAt();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = System.currentTimeMillis();
    }

    public String getFormattedCreatedAt() {
        Date date  = new Date(createdAt);
        String datePattern = "MM/dd/yyyy @ k:mm a";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        return simpleDateFormat.format(date);
    }

    public void setFormattedCreatedAt() {
        Date date = new Date(createdAt);
        String datepattern = "MM/dd/yyyy @ k:mm a";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datepattern);
        this.formattedCreatedAt = simpleDateFormat.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return rating == review.rating &&
                id == review.id &&
                restaurantId == review.restaurantId &&
                Objects.equals(content, review.content) &&
                Objects.equals(writtenBy, review.writtenBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, writtenBy, rating, id, restaurantId);
    }

    @Override
    public String toString() {
        return "Review{" +
                "content='" + content + '\'' +
                ", writtenBy='" + writtenBy + '\'' +
                ", rating=" + rating +
                ", id=" + id +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
