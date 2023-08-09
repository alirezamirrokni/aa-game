package view;

import controller.AppCenter;
import controller.SettingsController;
import javafx.beans.binding.IntegerBinding;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Settings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsMenuController implements Initializable {
    @FXML
    private CheckBox map1CheckBox;
    @FXML
    private CheckBox map2CheckBox;
    @FXML
    private CheckBox map3CheckBox;
    @FXML
    private ImageView map1;
    @FXML
    private ImageView map2;
    @FXML
    private ImageView map3;
    @FXML
    private Label freeze;
    @FXML
    private CheckBox soundOn;
    @FXML
    private Label moveBallLeft;
    @FXML
    private Label moveBallRight;
    @FXML
    private Label shootBall;
    @FXML
    private CheckBox blackAndWhite;
    @FXML
    private ComboBox numberOfBalls;
    @FXML
    private ComboBox difficulty;
    @FXML
    private Label menuName;
    private Stage keyBindings;

    {
        keyBindings = new Stage();
        keyBindings.setTitle("key binding");
        keyBindings.getIcons().add(new Image(SettingsController.class.getResource("/Images/logo.png").toString()));
    }

    private final SettingsController controller = new SettingsController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuName.setStyle("-fx-font-size: 30; -fx-font-family: '2  Baran';");
        if (!AppCenter.getSettings().isBlackAndWhite())
            menuName.setTextFill(Color.ORANGE);
        setDifficulty();
        setNumberOfBalls();
        setMapImages();
        setMap();
        setBindings();
        soundOn.setSelected(AppCenter.getSettings().isSoundOn());
        blackAndWhite.setSelected(AppCenter.getSettings().isBlackAndWhite());

    }

    private void setMap() {
        switch (AppCenter.getSettings().getMap()) {
            case 1:
                map1CheckBox.setSelected(true);
                break;
            case 2:
                map2CheckBox.setSelected(true);
                break;
            case 3:
                map3CheckBox.setSelected(true);
                break;
        }
    }

    private void setMapImages() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.BLACK);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        map1.setImage(new Image(Settings.class.getResource("/Images/map1.png").toString(), 200, 200, false, false));
        map2.setImage(new Image(Settings.class.getResource("/Images/map2.png").toString(), 200, 200, false, false));
        map3.setImage(new Image(Settings.class.getResource("/Images/map3.png").toString(), 200, 200, false, false));
        map1.setEffect(borderGlow);
        map2.setEffect(borderGlow);
        map3.setEffect(borderGlow);
    }

    private void setDifficulty() {
        difficulty.getItems().clear();
        difficulty.getItems().addAll("Easy", "Medium", "Hard");
        difficulty.setPromptText(AppCenter.getSettings().getDifficulty().toString());
    }

    private void setNumberOfBalls() {
        numberOfBalls.getItems().clear();
        for (int i = 2; i <= 8; i++)
            numberOfBalls.getItems().add(String.valueOf(4 * i));
        numberOfBalls.setPromptText(String.valueOf(AppCenter.getSettings().getNumberOfBalls()));
    }

    private void setBindings() {
        shootBall.setText("shoot ball: " + AppCenter.getSettings().getShootBall());
        freeze.setText("freeze: " + AppCenter.getSettings().getFreeze());
        moveBallRight.setText("move ball right: " + AppCenter.getSettings().getMoveBallRight());
        moveBallLeft.setText("move ball left: " + AppCenter.getSettings().getMoveBallLeft());
    }

    public void changeDifficulty(ActionEvent actionEvent) throws IOException {
        controller.changeDifficulty(difficulty.getValue().toString());
    }

    public void changeNumberOfBalls(ActionEvent actionEvent) throws IOException {
        controller.changeNumberOfBalls(Integer.parseInt(numberOfBalls.getValue().toString()));
    }


    public void changeSoundOn(ActionEvent actionEvent) throws IOException {
        controller.changeSoundOn(soundOn.isSelected());
    }

    public void changeBlackAndWhite(ActionEvent actionEvent) throws IOException {
        controller.changeBlackAndWhite(blackAndWhite.isSelected());
        if (blackAndWhite.isSelected()) {
            menuName.setTextFill(Color.BLACK);
            App.settingsBorderPane.setStyle("-fx-background-color: white;");
        } else {
            menuName.setTextFill(Color.ORANGE);
            App.settingsBorderPane.setStyle("-fx-background-color: #6e6ee8");
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        App.mainMenu.start(App.stage);
    }

    public void changeMoveBallLeft(MouseEvent mouseEvent) {
        keyBindings.setScene(getScene(3));
        keyBindings.show();
    }


    public void changeMoveBallRight(MouseEvent mouseEvent) {
        keyBindings.setScene(getScene(2));
        keyBindings.show();
    }

    public void changeShootBall(MouseEvent mouseEvent) {
        keyBindings.setScene(getScene(1));
        keyBindings.show();
    }

    public void changeFreeze(MouseEvent mouseEvent) {
        keyBindings.setScene(getScene(4));
        keyBindings.show();
    }

    private Scene getScene(int state) {
        BorderPane borderPane = new BorderPane();
        Label label = new Label("enter the key you want...");
        borderPane.setCenter(label);
        Scene scene = new Scene(borderPane, 300, 100);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().toString();
                keyBindings.close();
                switch (state) {
                    case 1 -> {
                        try {
                            controller.changeShootBall(keyName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case 2 -> {
                        try {
                            controller.changeMoveBallRight(keyName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case 3 -> {
                        try {
                            controller.changeMoveBallLeft(keyName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case 4 -> {
                        try {
                            controller.changeFreeze(keyName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                setBindings();
            }
        });
        return scene;
    }


    public void chooseMap1(ActionEvent actionEvent) {
        cancelAllOthers(map2CheckBox, map3CheckBox, map1CheckBox);
        AppCenter.getSettings().setMap(1);
    }

    public void chooseMap2(ActionEvent actionEvent) {
        cancelAllOthers(map1CheckBox, map3CheckBox, map2CheckBox);
        AppCenter.getSettings().setMap(2);
    }

    public void chooseMap3(ActionEvent actionEvent) {
        cancelAllOthers(map1CheckBox, map2CheckBox, map3CheckBox);
        AppCenter.getSettings().setMap(3);
    }

    private void cancelAllOthers(CheckBox checkBox1, CheckBox checkBox2, CheckBox checkBox3) {
        checkBox1.setSelected(false);
        checkBox2.setSelected(false);
        checkBox3.setSelected(true);
    }
}
