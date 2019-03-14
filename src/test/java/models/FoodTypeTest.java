package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FoodTypeTest {
    private FoodType type;

    @Before
    public void setUp() throws Exception {
        type = new FoodType("Burgers");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        assertEquals("Burgers",type.getName());
    }

    @Test
    public void setName() {
        type.setName("Broccoli");
        assertEquals("Broccoli",type.getName());
    }
}