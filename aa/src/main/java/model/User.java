package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class User {
    private String username;
    private String password;
    private Settings settings;
    private HashMap<String, int[]> difficultyInformation = new HashMap<>();
    private String avatarAddress;
    private Game lastGame;
    private static JsonArray usersJson = new JsonArray();
    private static String usersFileAddress = "C:\\aa final\\src\\main\\resources\\DataBase\\Users.json";
    private static int numberOfGuestUsers = 0;

    private static ArrayList<User> users = new ArrayList<>();

    public User(String username, String password, Settings settings) {
        this.username = username;
        this.password = password;
        this.settings = settings;
        difficultyInformation.put("Easy", new int[]{0, 0});
        difficultyInformation.put("Medium", new int[]{0, 0});
        difficultyInformation.put("Hard", new int[]{0, 0});
        this.avatarAddress = User.class.getResource("/Images/1.png").toString();
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarAddress() {
        return avatarAddress;
    }

    public void setAvatarAddress(String avatarAddress) {
        this.avatarAddress = avatarAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastGame(Game lastGame) {
        this.lastGame = lastGame;
    }

    public Game getLastGame() {
        return lastGame;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Settings getSettings() {
        return settings;
    }

    public static void addUser(User user) throws Exception {
        users.add(user);
        updateUsers();
    }

    public static void deleteUser(User user) throws IOException {
        users.remove(user);
        updateUsers();
    }

    public boolean isGuest() {
        return password == null;
    }

    public static void updateUsers() throws IOException {
        Gson gson = new Gson();
        usersJson = new JsonArray();
        for (User user : users)
            usersJson.add(gson.toJsonTree(user).getAsJsonObject());
        FileWriter file = new FileWriter(usersFileAddress);
        file.write(usersJson.toString());
        file.close();
    }

    public static void loadUsers() throws IOException {
        FileReader file = new FileReader(usersFileAddress);
        Scanner scanner = new Scanner(file);
        if (!scanner.hasNextLine()) {
            scanner.close();
            return;
        }
        String allUsers = scanner.nextLine();
        scanner.close();
        file.close();
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(allUsers, JsonArray.class);
        users.clear();
        for (JsonElement jsonElement : jsonArray)
            users.add(gson.fromJson(jsonElement, User.class));
        for (User user : users)
            if (user.isGuest())
                numberOfGuestUsers++;
        usersJson = jsonArray;
    }

    public static User getUserByUsername(String username) {
        for (User user : users)
            if (user.getUsername().equals(username) && !user.isGuest())
                return user;
        return null;
    }

    public static int getNumberOfGuestUsers() {
        return numberOfGuestUsers;
    }

    public static void addGuestUser() {
        numberOfGuestUsers++;
    }

    public static boolean isPasswordStrong(String password) {
        if (password.length() < 6)
            return false;
        if (!Pattern.compile("[A-Z]").matcher(password).find())
            return false;
        if (!Pattern.compile("[a-z]").matcher(password).find())
            return false;
        if (!Pattern.compile("\\d").matcher(password).find())
            return false;
        return true;
    }

    public static boolean isUsernameValid(String username) {
        return (username.matches("[a-zA-Z\\d_]+") && username.length() <= 8);
    }

    public static ArrayList<User> getSortedUsersByDifficulty(String difficulty) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users)
            result.add(user);
        sortUsers(result, difficulty);
        return result;
    }

    private static void sortUsers(ArrayList<User> usersToBeSorted, String difficulty) {
        for (int i = 0; i < usersToBeSorted.size(); i++)
            for (int j = i + 1; j < usersToBeSorted.size(); j++) {
                User first = usersToBeSorted.get(i);
                User second = usersToBeSorted.get(j);
                if (doesHaveHigherRank(second, first, difficulty)) {
                    usersToBeSorted.set(i, second);
                    usersToBeSorted.set(j, first);
                }
            }
    }

    private static boolean doesHaveHigherRank(User second, User first, String difficulty) {
        int[] firstDifficultyInformation = first.difficultyInformation.get(difficulty);
        int[] secondDifficultyInformation = second.difficultyInformation.get(difficulty);
        if (secondDifficultyInformation[0] > firstDifficultyInformation[0])
            return true;
        else if (secondDifficultyInformation[0] < firstDifficultyInformation[0])
            return false;
        else if (secondDifficultyInformation[1] < firstDifficultyInformation[1])
            return true;
        else if (secondDifficultyInformation[1] > firstDifficultyInformation[1])
            return false;
        else if (second.getUsername().compareTo(first.getUsername()) < 0)
            return true;
        return false;
    }

    public HashMap<String, int[]> getDifficultyInformation() {
        return difficultyInformation;
    }

    public void setDifficultyInformation(String difficulty, int[] information) {
        difficultyInformation.put(difficulty, information);
    }
}
