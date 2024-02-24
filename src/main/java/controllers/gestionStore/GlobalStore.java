package controllers.gestionStore;

import javafx.scene.Scene;
import javafx.stage.Stage;

public final class GlobalStore
{
    static InterfacePaneController Ipc = new InterfacePaneController();

    static Stage stage = new Stage();
    static Scene scene = new Scene(Ipc.getGestionStore());



}
