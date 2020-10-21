package at.ac.uibk.ps_swa.ws19.gr7_2.ui.beans;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;
import at.ac.uibk.ps_swa.ws19.gr7_2.ui.beans.SessionInfoBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Some very basic tests for {@link UserService}.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SessionInfoBeanTest {

    @Autowired
    SessionInfoBean sessionInfoBean;

    @Autowired
    UserService userService;

    @Test
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testLoggedIn() {
        Assert.assertTrue("sessionInfoBean.isLoggedIn does not return true for authenticated user", sessionInfoBean.isLoggedIn());
        Assert.assertEquals("sessionInfoBean.getCurrentUserName does not return authenticated user's name", "user4", sessionInfoBean.getCurrentUserName());
        Assert.assertEquals("sessionInfoBean.getCurrentUser does not return authenticated user", "user4", sessionInfoBean.getCurrentUser().getUsername());
        Assert.assertEquals("sessionInfoBean.getCurrentUserRoles does not return authenticated user's roles", "USER", sessionInfoBean.getCurrentUserRoles());
        Assert.assertTrue("sessionInfoBean.hasRole does not return true for a role the authenticated user has", sessionInfoBean.hasRole("USER"));
        Assert.assertFalse("sessionInfoBean.hasRole does not return false for a role the authenticated user does not have", sessionInfoBean.hasRole("ADMIN"));
    }

    @Test
    public void testNotLoggedIn() {
        Assert.assertFalse("sessionInfoBean.isLoggedIn does return true for not authenticated user", sessionInfoBean.isLoggedIn());
        Assert.assertEquals("sessionInfoBean.getCurrentUserName does not return empty string when not logged in", "", sessionInfoBean.getCurrentUserName());
        Assert.assertNull("sessionInfoBean.getCurrentUser does not return null when not logged in", sessionInfoBean.getCurrentUser());
        Assert.assertEquals("sessionInfoBean.getCurrentUserRoles does not return empty string when not logged in", "", sessionInfoBean.getCurrentUserRoles());
        for (UserRole role : UserRole.values()) {
            Assert.assertFalse("sessionInfoBean.hasRole does not return false for all possible roales", sessionInfoBean.hasRole(role.name()));
        }
    }

}
