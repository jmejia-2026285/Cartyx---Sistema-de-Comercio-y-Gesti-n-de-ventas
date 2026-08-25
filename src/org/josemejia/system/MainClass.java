/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.josemejia.system;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.josemejia.system.utils.SceneManager;
import org.josemejia.system.utils.ViewFactory;
/**
 *
 * @author mejia
 */

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
