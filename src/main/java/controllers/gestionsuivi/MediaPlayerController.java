package controllers.gestionsuivi;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

import java.io.File;

public class MediaPlayerController {

    @FXML
    private Label chooseMusic;



    private static MediaPlayer mediaPlayer;

    @FXML
    void chooseMusic(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select your music");
        File file = chooser.showOpenDialog(null);
        if(file != null){
            String selectedFile = file.toURI().toString();
            Media media = new Media(selectedFile);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> chooseMusic.setText(file.getName()));
        }
    }

    @FXML
    void pause(MouseEvent event) {

        mediaPlayer.pause();
    }

    @FXML
    void play(MouseEvent event) {
        mediaPlayer.play();
    }

    @FXML
    void stop(MouseEvent event) {
        mediaPlayer.stop();
    }

}
