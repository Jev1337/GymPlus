package controllers.gestionsuivi;

import atlantafx.base.controls.Message;
import com.github.javafaker.Faker;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectifController  implements Initializable {


    @FXML
    private VBox WarningVbox;

    @FXML
    private VBox pnl_scroll1;
    @FXML
    public VBox pnl_scroll2;

    @FXML
    private VBox pnl_scorllChatBot;


    private static Stage stage ;


    private ObjectifController objectifController ;



    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ObjectifController.stage = stage;
    }



    private void refreshNodes()
    {
        pnl_scroll1.getChildren().clear();

        Node[] nodes = new Node[15];

        for(int i = 0; i<1; i++)
        {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/gestionSuivi/AddingObjectif.fxml"));
                pnl_scroll1.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }




   /* public void animation () {
        var rectangle = new Rectangle(100,100) ;
        rectangle.setFill(Color.RED);
        var animation= Animations.fadeIn(rectangle, Duration.seconds(1));
        animation.playFromStart();
        pnl_scroll2.getChildren().add(animation);
    }
    */





    public void refreshNodesListeItems() throws IOException {
        pnl_scroll2.getChildren().clear();
        ObjectifListController objectifListController = new ObjectifListController();
        ObservableList<Objectif> objectifList = objectifListController.getObjectifStatusList();



        for (Objectif objecitf : objectifList){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/Item.fxml"));
                Node node = loader.load();
                ObjectifListController itemController = loader.getController();
                itemController.setObjectif(objecitf);
                itemController.buton();
                itemController.buttonPanel();
                itemController.scrollButton(objecitf);
                pnl_scroll2.getChildren().add(node);
                FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/gestionSuivi/AddingObjectif.fxml"));
                Node node3 = loader3.load();
                AddObjectifController addoj = loader3.getController();
                itemController.setObjectifController(this);
                addoj.setObjectifController(this);

                Button itemButton = (Button) node.lookup("#itemButton");
                if (itemButton != null) {
                    itemButton.setOnAction(event -> {
                        pnl_scroll1.getChildren().clear();
                        pnl_scroll1.getChildren().add(node3);
                        addoj.sendingDataToForm(objecitf);

                    });
                }
            }catch (IOException ex){
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }




    private void refreshNodesChatBot() throws IOException {
        pnl_scorllChatBot.getChildren().clear();
        // Node[] nodes = new Node[1];
        //FXMLLoader loader = FXMLLoader.load(getClass().getResource("/gestionSuivi/chatBot.fxml"));



            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/chatBot.fxml"));
            Node node = loader.load();
            pnl_scorllChatBot.getChildren().add(node);
            ChatControler chatControler = loader.getController() ;
           // chatControler.chatGPT();

        }



    @FXML
    private VBox vboxWeb;

    private void refreshNodesWeb()
    {
        vboxWeb.getChildren().clear();

        Node[] nodes = new Node[1];

        for(int i = 0; i<1; i++)
        {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/gestionSuivi/WebView.fxml"));
                vboxWeb.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshNodesWeb();
            refreshNodes();

        try {
            refreshNodesListeItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            refreshNodesChatBot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
