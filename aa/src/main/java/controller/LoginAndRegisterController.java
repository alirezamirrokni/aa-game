package controller;

import controller.Messages.LoginAndRegisterAndProfileMessages;
import model.Settings;
import model.User;

public class LoginAndRegisterController {
    public LoginAndRegisterAndProfileMessages login(String username, String password) {
        if (username.isBlank() || password.isBlank())
            return LoginAndRegisterAndProfileMessages.EMPTY_FIELD;
        User user = User.getUserByUsername(username);
        if (user == null)
            return LoginAndRegisterAndProfileMessages.USERNAME_NOT_FOUND;
        if (!user.checkPassword(password))
            return LoginAndRegisterAndProfileMessages.INCORRECT_PASSWORD;
        AppCenter.setCurrentUser(user);
        return LoginAndRegisterAndProfileMessages.SUCCESS;
    }

    public void eneterAsGuest() throws Exception {
        int number = User.getNumberOfGuestUsers();
        User guest = new User("Guest " + number, null, new Settings());
        User.addUser(guest);
        AppCenter.setCurrentUser(guest);
    }

    public LoginAndRegisterAndProfileMessages register(String username, String password, String confirmPassword) throws Exception {
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank())
            return LoginAndRegisterAndProfileMessages.EMPTY_FIELD;
        if (User.getUserByUsername(username) != null)
            return LoginAndRegisterAndProfileMessages.USERNAME_EXISTS;
        if (!User.isUsernameValid(username))
            return LoginAndRegisterAndProfileMessages.INVALID_USERNAME_FORMAT;
        if (!User.isPasswordStrong(password))
            return LoginAndRegisterAndProfileMessages.WEAK_PASSWORD;
        if (!password.equals(confirmPassword))
            return LoginAndRegisterAndProfileMessages.DIDNT_CONFIRM;
        User.addUser(new User(username, password, new Settings()));
        return LoginAndRegisterAndProfileMessages.SUCCESS;
    }
}
