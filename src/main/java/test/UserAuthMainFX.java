package test;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.sun.jna.platform.win32.Win32Exception;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import org.opencv.core.Core;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
public class UserAuthMainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        System.load(System.getProperty("user.dir")+"\\opencv\\x64\\" + Core.NATIVE_LIBRARY_NAME + ".dll");
        try{
            String theme = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "theme");
            if(theme.equals("dark")){
                Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
            }else if(theme.equals("light")){
                Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            }
        }catch (Win32Exception e){
            Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus");
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "theme", "light");
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "tts", "false");
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/authInterface.fxml")); //change me
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
