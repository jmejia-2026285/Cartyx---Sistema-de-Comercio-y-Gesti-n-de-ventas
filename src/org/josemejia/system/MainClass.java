package org.josemejia.system;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.josemejia.system.utils.SceneManager;
import org.josemejia.system.utils.ViewFactory;

public class MainClass extends Application {

    private static HostServices hostServices;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stageRoot) {
        hostServices = getHostServices();
        SceneManager.getInstanciaSceneManager().setStagePrincipal(stageRoot);
        ViewFactory viewFacto = new ViewFactory();
        viewFacto.viewLogin();
    }
    
    public static HostServices getAppHostServices() {
        return hostServices;
    }
}
