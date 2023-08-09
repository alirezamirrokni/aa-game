package view;

import controller.ProfileController;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameAvatars implements Initializable {
    public ImageView img1;
    public ImageView img2;
    public ImageView img3;
    public ImageView img4;
    public ImageView img5;
    public ImageView img6;
    public ImageView img7;
    public ImageView img8;
    public ImageView img9;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image img1 = new Image(GameAvatars.class.getResource("/Images/1.png").toString(), 100, 100, false, false);
        this.img1.setImage(img1);
        Image img2 = new Image(GameAvatars.class.getResource("/Images/2.png").toString(), 100, 100, false, false);
        this.img2.setImage(img2);
        Image img3 = new Image(GameAvatars.class.getResource("/Images/3.png").toString(), 100, 100, false, false);
        this.img3.setImage(img3);
        Image img4 = new Image(GameAvatars.class.getResource("/Images/4.png").toString(), 100, 100, false, false);
        this.img4.setImage(img4);
        Image img5 = new Image(GameAvatars.class.getResource("/Images/5.png").toString(), 100, 100, false, false);
        this.img5.setImage(img5);
        Image img6 = new Image(GameAvatars.class.getResource("/Images/6.png").toString(), 100, 100, false, false);
        this.img6.setImage(img6);
        Image img7 = new Image(GameAvatars.class.getResource("/Images/7.png").toString(), 100, 100, false, false);
        this.img7.setImage(img7);
        Image img8 = new Image(GameAvatars.class.getResource("/Images/8.png").toString(), 100, 100, false, false);
        this.img8.setImage(img8);
        Image img9 = new Image(GameAvatars.class.getResource("/Images/9.png").toString(), 100, 100, false, false);
        this.img9.setImage(img9);

    }

    public void image1(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img1.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }

    public void image2(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img2.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }

    public void image3(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img3.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }

    public void image4(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img4.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }

    public void image5(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img5.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }

    public void image6(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img6.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }

    public void image7(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img7.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }

    public void image8(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img8.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }

    public void image9(MouseEvent mouseEvent) throws Exception {
        App.gameAvatarStage.close();
        ProfileController.changeAvatar(img9.getImage().getUrl().toString());
        App.changeProfileMenu.start(App.stage);
    }


}
