package at.ac.uibk.ps_swa.ws19.gr7_2.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static String testFirstname = "Firstname";
    private static String testLastname = "Lastname";

    private static User user;

    @BeforeAll
    static void setupAll() {
        user = new User();
        user.setFirstName(testFirstname);
        user.setLastName(testLastname);
    }

    @Test
    void testGetFullName() {
        assertEquals(testFirstname + " " + testLastname,  user.getFullName());
    }
}