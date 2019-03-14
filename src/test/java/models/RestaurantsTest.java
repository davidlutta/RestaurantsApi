package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RestaurantsTest {
    private Restaurants restaurants;

    @Before
    public void setUp() throws Exception {
        restaurants = new Restaurants("Burger King","Kilimani","90210","1234567","info@bk.com","www.bk.com");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        assertEquals("Burger King",restaurants.getName());
    }

    @Test
    public void setName() {
        restaurants.setName("Pizza inn");
        assertEquals("Pizza inn",restaurants.getName());
    }

    @Test
    public void getAddress() {
        assertEquals("Kilimani",restaurants.getAddress());
    }

    @Test
    public void setAddress() {
        restaurants.setAddress("Kileleshwa");
        assertEquals("Kileleshwa",restaurants.getAddress());
    }

    @Test
    public void getZipcode() {
        assertEquals("90210",restaurants.getZipcode());
    }

    @Test
    public void setZipcode() {
        restaurants.setZipcode("90310");
        assertEquals("90310",restaurants.getZipcode());
    }

    @Test
    public void getPhone() {
        assertEquals("1234567",restaurants.getPhone());
    }

    @Test
    public void setPhone() {
        restaurants.setPhone("67890");
        assertEquals("67890",restaurants.getPhone());
    }

    @Test
    public void getEmail() {
        assertEquals("info@bk.com",restaurants.getEmail());
    }

    @Test
    public void setEmail() {
        restaurants.setEmail("info@bk.burger");
        assertEquals("info@bk.burger",restaurants.getEmail());
    }

    @Test
    public void getWebsite() {
        assertEquals("www.bk.com",restaurants.getWebsite());
    }

    @Test
    public void setWebsite() {
        restaurants.setWebsite("www.bk.burger");
        assertEquals("www.bk.burger",restaurants.getWebsite());
    }

}