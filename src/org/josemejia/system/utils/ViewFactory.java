
package org.josemejia.system.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import org.josemejia.system.MainClass;


public class ViewFactory {
    private final String PATH_VIEWS = "/org/josemejia/system/view/";
    
    public Scene loadFileFXML(String nameFile, int width, int height){
        
        String pathOfFile = PATH_VIEWS + nameFile;
            try {
            //llamar al FXMLLoader
            FXMLLoader loadFXML = new FXMLLoader();
            //Obtener la URL del archivo, viene de la clase main
            
            URL urlFile = MainClass.class.getResource(pathOfFile);
            loadFXML.setBuilderFactory(new JavaFXBuilderFactory());
            loadFXML.setLocation(urlFile);
            
            return new Scene( loadFXML.load(), width, height);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        
    }
    
    public void loadScene(String nameFile){
        Scene scene = null;
        
        try { 
            
            switch (nameFile){
                case "login" -> scene = loadFileFXML("LoginView.fxml", 800, 500) ;
                case "registro" -> scene = loadFileFXML("CreateAccountView.fxml", 800, 616);
                case "dashboard" -> scene = loadFileFXML("DashboardView.fxml", 700, 500);
                case "productos" -> scene = loadFileFXML("ProductoView.fxml", 850, 600);
                case "auditoria" -> scene = loadFileFXML("AuditoriaView.fxml", 700, 500);
                case "catalogo" -> scene = loadFileFXML("CatalogoView.fxml", 800, 560);
                case "carrito" -> scene = loadFileFXML("CarritoView.fxml", 700, 520);
                default -> throw new IllegalArgumentException("Vista no reconocida: " + nameFile);
            }
            SceneManager.getInstanciaSceneManager().changeScene(scene);
            
        } catch (RuntimeException e){
            System.err.println("Error al cargar la vista '" + nameFile + "': " + e.getMessage());
            e.printStackTrace();
        }
    
    }
    
    public void viewLogin(){
        loadScene("login");
    }

    public void viewRegistro(){
        loadScene("registro");
    }

    public void viewDashboard(){
        loadScene("dashboard");
    }

    public void viewProductos(){
        loadScene("productos");
    }

    public void viewAuditoria(){
        loadScene("auditoria");
    }

    public void viewCatalogo(){
        loadScene("catalogo");
    }

    public void viewCarrito(){
        loadScene("carrito");
    }
}
