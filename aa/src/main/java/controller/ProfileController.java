package controller;

import controller.Messages.LoginAndRegisterAndProfileMessages;
import model.User;

import java.io.IOException;

public class ProfileController {
    public void logout() {
        AppCenter.setCurrentUser(null);
    }

    public void deleteAccount() throws IOException {
        User.deleteUser(AppCenter.getCurrentUser());
        logout();
    }

    public LoginAndRegisterAndProfileMessages changeUsername(String username) throws IOException {
        if (AppCenter.getCurrentUser().isGuest() || username.isBlank())
            return LoginAndRegisterAndProfileMessages.EMPTY_FIELD;
        if (User.getUserByUsername(username) != null)
            return LoginAndRegisterAndProfileMessages.USERNAME_EXISTS;
        if (!User.isUsernameValid(username))
            return LoginAndRegisterAndProfileMessages.INVALID_USERNAME_FORMAT;
        AppCenter.getCurrentUser().setUsername(username);
        User.updateUsers();
        return LoginAndRegisterAndProfileMessages.SUCCESS;
    }

    public LoginAndRegisterAndProfileMessages changePassword(String currentPass, String newPass, String confirmNewPass) throws IOException {
        if (currentPass.isBlank() || newPass.isBlank() || confirmNewPass.isBlank())
            return LoginAndRegisterAndProfileMessages.EMPTY_FIELD;
        User user = AppCenter.getCurrentUser();
        if (!user.checkPassword(currentPass))
            return LoginAndRegisterAndProfileMessages.INCORRECT_PASSWORD;
        if (!User.isPasswordStrong(newPass))
            return LoginAndRegisterAndProfileMessages.WEAK_PASSWORD;
        if (!newPass.equals(confirmNewPass))
            return LoginAndRegisterAndProfileMessages.DIDNT_CONFIRM;
        user.setPassword(newPass);
        User.updateUsers();
        return LoginAndRegisterAndProfileMessages.SUCCESS;
    }

    public static void changeAvatar(String avatarAddress) throws IOException {
        AppCenter.getCurrentUser().setAvatarAddress(avatarAddress);
        User.updateUsers();
    }

    public boolean isGuest() {
        return AppCenter.getCurrentUser().isGuest();
    }
}
