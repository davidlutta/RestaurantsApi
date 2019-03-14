package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewTest {
    private Review myReview;
    @Before
    public void setUp() throws Exception {
        myReview = new Review("I love it","Arthur Curry",5,1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getContent() {
        assertEquals("I love it",myReview.getContent());
    }

    @Test
    public void setContent() {
        myReview.setContent("I hated it");
        assertEquals("I hated it",myReview.getContent());
    }

    @Test
    public void getWrittenBy() {
        assertEquals("Arthur Curry",myReview.getWrittenBy());
    }

    @Test
    public void setWrittenBy() {
        myReview.setWrittenBy("Batman");
        assertEquals("Batman",myReview.getWrittenBy());
    }

    @Test
    public void getRating() {
        assertEquals(5,myReview.getRating());
    }

    @Test
    public void setRating() {
        myReview.setRating(2);
        assertEquals(2,myReview.getRating());
    }
}