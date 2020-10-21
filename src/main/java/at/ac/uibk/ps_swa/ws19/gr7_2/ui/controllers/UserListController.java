package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user list view.
 */
@Component
@Scope("view")
public class UserListController {

    @Autowired
    private UserService userService;

    private Collection<User> userCollection;

    /**
     * Returns a list of all users.
     *
     * @return collection of all users
     */
    public Collection<User> getUsers() {
        if(userCollection == null){
            userCollection = userService.getAllUsersWithoutTestUser();
        }
        return userCollection;
    }

}
