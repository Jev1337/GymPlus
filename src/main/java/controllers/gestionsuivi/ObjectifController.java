package controllers.gestionsuivi;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectifController  implements Initializable {





    @FXML
    private VBox pnl_scroll2;

    @FXML
    private VBox pnl_scroll1;

    private static Stage stage ;



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
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/AddingObjectif.fxml"));
                pnl_scroll1.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void refreshNodesListeItems()
    {
        pnl_scroll2.getChildren().clear();

        Node[] nodes = new Node[15];

        for(int i = 0; i<9; i++)
        {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/Item.fxml"));
                pnl_scroll2.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshNodes();
        refreshNodesListeItems();
    }
}
