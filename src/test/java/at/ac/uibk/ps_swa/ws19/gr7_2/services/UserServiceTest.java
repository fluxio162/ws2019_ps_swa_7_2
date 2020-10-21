package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
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
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDatainitialization() {
        Assert.assertEquals("Insufficient amount of users initialized for test data source", 143, userService.getAllUsers().size());
        for (User user : userService.getAllUsers()) {
            if ("admin".equals(user.getUsername())) {
                Assert.assertTrue("User \"admin\" does not have role ADMIN", user.getRoles().contains(UserRole.ADMIN));
                Assert.assertNotNull("User \"admin\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"admin\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"admin\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"admin\" has a updateDate defined", user.getUpdateDate());
            } else if ("user1".equals(user.getUsername())) {
                Assert.assertTrue("User \"user1\" does not have role MANAGER", user.getRoles().contains(UserRole.MANAGER));
                Assert.assertNotNull("User \"user1\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"user1\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"user1\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"user1\" has a updateDate defined", user.getUpdateDate());
            } else if ("user2".equals(user.getUsername())) {
                Assert.assertTrue("User \"user2\" does not have role PILOT", user.getRoles().contains(UserRole.PILOT));
                Assert.assertNotNull("User \"user2\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"user2\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"user2\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"user2\" has a updateDate defined", user.getUpdateDate());
            } else if ("user3".equals(user.getUsername())) {
                Assert.assertTrue("User \"user3\" does not have role CREW", user.getRoles().contains(UserRole.CREW));
                Assert.assertNotNull("User \"user3\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"user3\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"user3\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"user3\" has a updateDate defined", user.getUpdateDate());
            } else if ("user4".equals(user.getUsername())) {
                Assert.assertTrue("User \"user4\" does not have role USER", user.getRoles().contains(UserRole.USER));
                Assert.assertNotNull("User \"user4\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"user4\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"user4\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"user4\" has a updateDate defined", user.getUpdateDate());
            }
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})

    public void testDeleteUser() {	
   
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User toBeDeletedUser = userService.loadUser("user4");
        Assert.assertNotNull("User4 could not be loaded from test data source", toBeDeletedUser);

        userService.deleteUser(toBeDeletedUser);

        Assertions.assertFalse(userService.getAllUsers().contains(toBeDeletedUser), "No user has been deleted after calling UserService.deleteUser");
        User deletedUser = userService.loadUser("user4");
        Assert.assertFalse("Deleted User4 is still enabled after deletion.", deletedUser.isEnabled());

        for (User remainingUser : userService.getAllUsers()) {
            Assert.assertNotEquals("Deleted User4 could still be loaded from test data source via UserService.getAllUsers", toBeDeletedUser.getUsername(), remainingUser.getUsername());
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateUserAsAdmin() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User toBeSavedUser = userService.loadUser("user4");
        Assert.assertNotNull("User4 could not be loaded from test data source", toBeSavedUser);

        Assert.assertNull("User \"user4\" has a updateUser defined", toBeSavedUser.getUpdateUser());
        Assert.assertNull("User \"user4\" has a updateDate defined", toBeSavedUser.getUpdateDate());

        toBeSavedUser.setEmail("changed-email@whatever.wherever");
        userService.saveUser(toBeSavedUser);

        User freshlyLoadedUser = userService.loadUser("user4");
        Assert.assertNotNull("User4 could not be loaded from test data source after being saved", freshlyLoadedUser);
        Assert.assertNotNull("User \"user4\" does not have a updateUser defined after being saved", freshlyLoadedUser.getUpdateUser());
        Assert.assertEquals("User \"user4\" has wrong updateUser set", adminUser, freshlyLoadedUser.getUpdateUser());
        Assert.assertNotNull("User \"user4\" does not have a updateDate defined after being saved", freshlyLoadedUser.getUpdateDate());
        Assert.assertEquals("User \"user4\" does not have a the correct email attribute stored being saved", "changed-email@whatever.wherever", freshlyLoadedUser.getEmail());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"MANAGER"})
    public void testUpdateUserAsManager() {
        User adminUser = userService.loadUser("user1");
        Assert.assertNotNull("Manager user could not be loaded from test data source", adminUser);
        User toBeSavedUser = userService.loadUser("user2");
        Assert.assertNotNull("User2 could not be loaded from test data source", toBeSavedUser);

        Assert.assertNull("User \"user2\" has a updateUser defined", toBeSavedUser.getUpdateUser());
        Assert.assertNull("User \"user2\" has a updateDate defined", toBeSavedUser.getUpdateDate());

        toBeSavedUser.setEmail("changed-email@whatever.wherever");
        userService.saveUser(toBeSavedUser);

        User freshlyLoadedUser = userService.loadUser("user2");
        Assert.assertNotNull("User2 could not be loaded from test data source after being saved", freshlyLoadedUser);
        Assert.assertNotNull("User \"user2\" does not have a updateUser defined after being saved", freshlyLoadedUser.getUpdateUser());
        Assert.assertEquals("User \"user2\" has wrong updateUser set", adminUser, freshlyLoadedUser.getUpdateUser());
        Assert.assertNotNull("User \"user2\" does not have a updateDate defined after being saved", freshlyLoadedUser.getUpdateDate());
        Assert.assertEquals("User \"user2\" does not have a the correct email attribute stored being saved", "changed-email@whatever.wherever", freshlyLoadedUser.getEmail());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        User toBeCreatedUser = new User();
        toBeCreatedUser.setUsername("newuser");
        toBeCreatedUser.setPassword("passwd");
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName("New");
        toBeCreatedUser.setLastName("User");
        toBeCreatedUser.setEmail("new-email@whatever.wherever");
        toBeCreatedUser.setPhone("+12 345 67890");
        toBeCreatedUser.setRoles(Sets.newSet(UserRole.USER, UserRole.MANAGER));
        userService.saveUser(toBeCreatedUser);

        User freshlyCreatedUser = userService.loadUser("newuser");
        Assert.assertNotNull("New user could not be loaded from test data source after being saved", freshlyCreatedUser);
        Assert.assertEquals("User \"newuser\" does not have a the correct username attribute stored being saved", "newuser", freshlyCreatedUser.getUsername());
        Assert.assertEquals("User \"newuser\" does not have a the correct password attribute stored being saved", "passwd", freshlyCreatedUser.getPassword());
        Assert.assertEquals("User \"newuser\" does not have a the correct firstName attribute stored being saved", "New", freshlyCreatedUser.getFirstName());
        Assert.assertEquals("User \"newuser\" does not have a the correct lastName attribute stored being saved", "User", freshlyCreatedUser.getLastName());
        Assert.assertEquals("User \"newuser\" does not have a the correct email attribute stored being saved", "new-email@whatever.wherever", freshlyCreatedUser.getEmail());
        Assert.assertEquals("User \"newuser\" does not have a the correct phone attribute stored being saved", "+12 345 67890", freshlyCreatedUser.getPhone());
        Assert.assertTrue("User \"newuser\" does not have role MANAGER", freshlyCreatedUser.getRoles().contains(UserRole.MANAGER));
        Assert.assertTrue("User \"newuser\" does not have role USER", freshlyCreatedUser.getRoles().contains(UserRole.USER));
        Assert.assertNotNull("User \"newuser\" does not have a createUser defined after being saved", freshlyCreatedUser.getCreateUser());
        Assert.assertEquals("User \"newuser\" has wrong createUser set", adminUser, freshlyCreatedUser.getCreateUser());
        Assert.assertNotNull("User \"newuser\" does not have a createDate defined after being saved", freshlyCreatedUser.getCreateDate());
    }

    @Test(expected = org.springframework.orm.jpa.JpaSystemException.class)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testExceptionForEmptyUsername() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        User toBeCreatedUser = new User();
        userService.saveUser(toBeCreatedUser);
    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadUsers() {
        for (User user : userService.getAllUsers()) {
            Assert.fail("Call to userService.getAllUsers should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user", authorities = {"USER"})
    public void testUnauthorizedLoadUsers() {
        for (User user : userService.getAllUsers()) {
            Assert.fail("Call to userService.getAllUsers should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testUnauthorizedLoadUser() {
        User user = userService.loadUser("admin");
        Assert.fail("Call to userService.loadUser should not work without proper authorization for other users than the authenticated one");
    }

    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testAuthorizedLoadUser() {
        User user = userService.loadUser("user4");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user4", user.getUsername());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testUnauthorizedSaveUser() {
        User user = userService.loadUser("user4");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user4", user.getUsername());
        userService.saveUser(user);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"CREW"})
    public void testUnauthorizedSaveUserAsCrew() {
        User user = userService.loadUser("user4");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user4", user.getUsername());
        userService.saveUser(user);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"PILOT"})
    public void testUnauthorizedSaveUserAsPilot() {
        User user = userService.loadUser("user4");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user4", user.getUsername());
        userService.saveUser(user);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testUnauthorizedDeleteUser() {
        User user = userService.loadUser("user4");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user4", user.getUsername());
        userService.deleteUser(user);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"CREW"})
    public void testUnauthorizedDeleteUserAsCrew() {
        User user = userService.loadUser("user4");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user4", user.getUsername());
        userService.deleteUser(user);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"PILOT"})
    public void testUnauthorizedDeleteUserAsPilot() {
        User user = userService.loadUser("user4");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user4", user.getUsername());
        userService.deleteUser(user);
    }
    
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"PILOT"})
    public void testGetAllUsersWithRole() {
    	List<UserRole> testRoles = new ArrayList<UserRole>();
    	List<User> userList;
    	
    	testRoles.add(UserRole.PILOT);
    	testRoles.add(UserRole.CREW);
    	testRoles.add(UserRole.MANAGER);
    	
    	for (UserRole role : testRoles) {
			userList = new ArrayList<User>();
			userList.addAll(userService.getAllUsers(role));
			
			for (User user : userList) {
				Assert.assertTrue("User without the correct role was returned.", user.getRoles().contains(role));
			}
		}
    }

}
