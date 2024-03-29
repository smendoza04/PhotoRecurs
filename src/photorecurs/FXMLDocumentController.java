/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photorecurs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author User
 */
public class FXMLDocumentController implements Initializable {

    private File currentLocation = new File(Paths.get("./src/photorecurs/gfx").toAbsolutePath().normalize().toString());
    private File locationTree = new File(Paths.get("./src/photorecurs").toAbsolutePath().normalize().toString());
    private ButtonDirectory buttonDirectory;
    private ImageView imageDirectory;
    private ArrayList<File> imageSlider;
    private int slider;
    private int sliderMax = -1;
    private boolean uiChange = true;
    private ImageView favouriteImage;

    private Favourite favourites;
    private Button buttonFav;

    @FXML
    private VBox vBoxFav;

    @FXML
    private TextField pathFinder;
    @FXML
    private ToggleButton favouriteButton;
    @FXML
    private FlowPane centerFlowPane;

    //Tree
    @FXML
    private ImageView imageTree;
    @FXML
    private Button buttonTree;
    @FXML
    private VBox vBoxTree;

    @FXML
    private VBox vBoxCenter;
    @FXML
    private VBox vBoxImageDirectory;
    @FXML
    private ImageView toggledImage;
    @FXML
    private Text toggledName;
    @FXML
    private Text toggledType;
    @FXML
    private Text toggledDate;
    @FXML
    private Text toggledPath;

    @FXML
    private Text pathLocation;

    @FXML
    private MenuButton menuButton;

    @FXML
    private void undo(ActionEvent event) throws FileNotFoundException {

        if (locationTree.getParentFile() != null) {
            currentLocation = locationTree;
            locationTree = new File(locationTree.getParentFile().getAbsolutePath());

            if (uiChange == true) {
                showDirectoryIcon();
            } else {
                showDirectoryList();
            }
            showNavigationTree();
            showFavourites();
            pathLocation.setText(currentLocation.getAbsolutePath());
            System.out.println(locationTree);
        }
    }

    @FXML
    private void list(ActionEvent event) throws FileNotFoundException {
        uiChange = false;
        if (uiChange == true) {
            showDirectoryIcon();
        } else {
            showDirectoryList();
        }
        showNavigationTree();
        showFavourites();
        menuButton.setText("List");
    }

    @FXML
    private void icon(ActionEvent event) throws FileNotFoundException {
        uiChange = true;
        if (uiChange == true) {
            showDirectoryIcon();
        } else {
            showDirectoryList();
        }
        showNavigationTree();
        showFavourites();
        menuButton.setText("Icon");
    }

    @FXML
    private void favourite(ActionEvent event) throws TransformerException, FileNotFoundException {

        if (favouriteButton.isSelected()) {
            System.out.println("check");
            favourites.addElement(imageSlider.get(slider).getAbsolutePath());
            event.consume();
        } else {
            System.out.println("uncheck");
            favourites.deleteElement(imageSlider.get(slider).getAbsolutePath());
            event.consume();
        }

        if (uiChange == true) {
            showDirectoryIcon();
        } else {
            showDirectoryList();
        }
        showNavigationTree();
        showFavourites();
    }

    private void showFavourites() throws FileNotFoundException {
        vBoxFav.getChildren().clear();
        for (int i = 0; i < favourites.getAllImages().size(); i++) {
            Path path = Paths.get(favourites.getAllImages().get(i));
            if (Files.exists(path)) {
                favouriteImage = new ImageView(new Image(new FileInputStream(path.toString())));
                favouriteImage.setFitHeight(46.0);
                favouriteImage.setFitWidth(36.0);

                buttonFav = new ButtonDirectory(path.toFile());
                buttonFav.setText(path.getFileName().toString());
                buttonFav.setGraphic(favouriteImage);
                buttonFav.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                buttonFav.setContentDisplay(ContentDisplay.LEFT);

                buttonFav.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ButtonDirectory btn = (ButtonDirectory) event.getSource();
                        ArrayList<File> newSlider = new ArrayList<>();
                        slider = 0;
                        for (int y = 0; y < favourites.getAllImages().size(); y++) {
                            newSlider.add(new File(favourites.getAllImages().get(y)));
                            if (favourites.getAllImages().get(y).equals(btn.getFile().getAbsolutePath())) {
                                slider = y;
                            }
                        }
                        imageSlider = newSlider;
                        sliderMax = imageSlider.size();
                        File newFile = btn.getFile();

                        locationTree = newFile.getParentFile();
                        toggledImage.setFitWidth(197.0);
                        toggledImage.setFitHeight(192.0);
                        try {
                            toggledImage.setImage(new Image(new FileInputStream(imageSlider.get(slider))));
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        toggledName.setText(imageSlider.get(slider).getName());
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        toggledDate.setText(df.format(currentLocation.lastModified()));
                        toggledPath.setText(imageSlider.get(slider).getAbsolutePath());
                        pathLocation.setText(currentLocation.getAbsolutePath());
                        try {
                            if (uiChange == true) {
                                showDirectoryIcon();
                            } else {
                                showDirectoryList();
                            }
                            showNavigationTree();
                            showFavourites();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                vBoxFav.getChildren().addAll(buttonFav);
            }

        }

    }

    @FXML
    private void sliderLeft(ActionEvent event) throws FileNotFoundException {
        slider--;
        System.out.println(slider);
        if (slider < 0) {
            slider = sliderMax - 1;
        }
        toggledImage.setImage(new Image(new FileInputStream(imageSlider.get(slider))));
        toggledImage.setImage(new Image(new FileInputStream(imageSlider.get(slider))));
        toggledName.setText(imageSlider.get(slider).getName());
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        toggledDate.setText(df.format(currentLocation.lastModified()));
        toggledPath.setText(imageSlider.get(slider).getAbsolutePath());
        pathLocation.setText(currentLocation.getAbsolutePath());

        if (uiChange == true) {
            showDirectoryIcon();
        } else {
            showDirectoryList();
        }
        showNavigationTree();
        showFavourites();

    }

    @FXML
    private void sliderRight(ActionEvent event) throws FileNotFoundException {

        slider++;
        System.out.println(slider);

        if (slider == sliderMax) {
            slider = 0;
        }
        toggledImage.setImage(new Image(new FileInputStream(imageSlider.get(slider))));
        toggledName.setText(imageSlider.get(slider).getName());
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        toggledDate.setText(df.format(currentLocation.lastModified()));
        toggledPath.setText(imageSlider.get(slider).getAbsolutePath());
        pathLocation.setText(currentLocation.getAbsolutePath());

        if (uiChange == true) {
            showDirectoryIcon();
        } else {
            showDirectoryList();
        }
        showNavigationTree();
        showFavourites();

    }

    @FXML
    private void pathFinder(ActionEvent event) throws FileNotFoundException {
        currentLocation = new File(pathFinder.getText());
        locationTree = new File(pathFinder.getText()).getParentFile();
        Stage primStage = (Stage) pathFinder.getScene().getWindow();
        primStage.setTitle(pathFinder.getText());
        if (uiChange == true) {
            showDirectoryIcon();
        } else {
            showDirectoryList();
        }
        showNavigationTree();
        showFavourites();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pathLocation.setText(currentLocation.getAbsolutePath());
        try {
            favourites = new Favourite();
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (uiChange == true) {
                showDirectoryIcon();
            } else {
                showDirectoryList();
            }
            showNavigationTree();
            showFavourites();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showDirectoryIcon() throws FileNotFoundException {
        centerFlowPane.getChildren().clear();
        pathFinder.setText(currentLocation.getAbsolutePath());
        if (Files.exists(this.currentLocation.toPath())) {

            for (int i = 0; i < currentLocation.listFiles().length; i++) {
                if (currentLocation.listFiles()[i].isDirectory()) {

                    imageDirectory = new ImageView(new Image(new FileInputStream("./src/photorecurs/gfx/dir.png")));

                    imageDirectory.setFitHeight(95.0);
                    imageDirectory.setFitWidth(90.0);

                    buttonDirectory = new ButtonDirectory(currentLocation.listFiles()[i]);
                    buttonDirectory.setText(currentLocation.listFiles()[i].getName());
                    buttonDirectory.setGraphic(imageDirectory);
                    buttonDirectory.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                    buttonDirectory.setContentDisplay(ContentDisplay.TOP);

                    buttonDirectory.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ButtonDirectory btn = (ButtonDirectory) event.getSource();

                            File newFile = btn.getFile();

                            locationTree = newFile.getParentFile();
                            currentLocation = newFile;
                            if (uiChange == true) {
                                try {
                                    showDirectoryIcon();
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                try {
                                    showDirectoryList();
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            try {
                                showNavigationTree();
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                showFavourites();
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });

                    centerFlowPane.getChildren().addAll(buttonDirectory);
                } else if (currentLocation.listFiles()[i].getName().endsWith(".jpg")
                        || currentLocation.listFiles()[i].getName().endsWith(".png")) {
                    imageDirectory = new ImageView(new Image(new FileInputStream(currentLocation.listFiles()[i].getAbsolutePath())));

                    imageDirectory.setFitHeight(95.0);
                    imageDirectory.setFitWidth(90.0);

                    buttonDirectory = new ButtonDirectory(currentLocation.listFiles()[i]);
                    buttonDirectory.setText(currentLocation.listFiles()[i].getName());
                    buttonDirectory.setGraphic(imageDirectory);
                    buttonDirectory.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                    buttonDirectory.setContentDisplay(ContentDisplay.TOP);

                    if (sliderMax > 0) {
                        if (favourites.contains(imageSlider.get(slider).getAbsolutePath())) {
                            favouriteButton.setSelected(true);
                        } else {
                            favouriteButton.setSelected(false);
                        }
                    }

                    buttonDirectory.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ButtonDirectory btn = (ButtonDirectory) event.getSource();
                            System.out.println("btn: " + btn);
                            try {
                                imageSlider = new ArrayList<>();
                                int sliderNum = 0;
                                for (int i = 0; i < btn.getFile().getParentFile().listFiles().length; i++) {
                                    if (currentLocation.listFiles()[i].getName().endsWith(".jpg")
                                            || currentLocation.listFiles()[i].getName().endsWith(".png")) {
                                        imageSlider.add(btn.getFile().getParentFile().listFiles()[i]);

                                        if (currentLocation.listFiles()[i].getName().endsWith(".jpg")) {
                                            toggledType.setText(".jpg");
                                        } else if (currentLocation.listFiles()[i].getName().endsWith(".png")) {
                                            toggledType.setText(".png");
                                        }

                                        if (btn.getFile().getParentFile().listFiles()[i].equals(btn.getFile())) {
                                            slider = sliderNum;
                                            System.out.println(i);
                                        }
                                        sliderNum++;
                                    }
                                }

                                sliderMax = imageSlider.size();

                                vBoxImageDirectory.getChildren().removeAll(toggledImage);

                                toggledImage.setFitWidth(197.0);
                                toggledImage.setFitHeight(192.0);

                                try {
                                    if (sliderMax > 0) {
                                        System.out.println("slider " + slider);
                                        toggledImage.setImage(new Image(new FileInputStream(imageSlider.get(slider))));
                                    }
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                toggledName.setText(imageSlider.get(slider).getName());
                                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                                toggledDate.setText(df.format(currentLocation.lastModified()));
                                toggledPath.setText(imageSlider.get(slider).getAbsolutePath());
                                pathLocation.setText(currentLocation.getAbsolutePath());

                                if (uiChange == true) {
                                    showDirectoryIcon();
                                } else {
                                    showDirectoryList();
                                }
                                showNavigationTree();
                                showFavourites();

                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });

                    centerFlowPane.getChildren().addAll(buttonDirectory);
                }

            }
        } else {
            centerFlowPane.getChildren().clear();
            Text noPathFound = new Text("Path not Found");
            noPathFound.setStyle("-fx-font-size: 18px;");
            noPathFound.setTextAlignment(TextAlignment.CENTER);
            centerFlowPane.setAlignment(Pos.CENTER);
            centerFlowPane.getChildren().add(noPathFound);
        }

    }

    public void showDirectoryList() throws FileNotFoundException {
        centerFlowPane.getChildren().clear();
        vBoxCenter.getChildren().clear();

        pathFinder.setText(currentLocation.getAbsolutePath());
        if (Files.exists(this.currentLocation.toPath())) {

            for (int i = 0; i < currentLocation.listFiles().length; i++) {
                if (currentLocation.listFiles()[i].isDirectory()) {

                    imageDirectory = new ImageView(new Image(new FileInputStream("./src/photorecurs/gfx/dir.png")));

                    imageDirectory.setFitHeight(46.0);
                    imageDirectory.setFitWidth(36.0);

                    buttonDirectory = new ButtonDirectory(currentLocation.listFiles()[i]);
                    buttonDirectory.setText(currentLocation.listFiles()[i].getName());
                    buttonDirectory.setGraphic(imageDirectory);
                    buttonDirectory.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                    buttonDirectory.setContentDisplay(ContentDisplay.LEFT);

                    buttonDirectory.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ButtonDirectory btn = (ButtonDirectory) event.getSource();

                            File newFile = btn.getFile();

                            locationTree = newFile.getParentFile();
                            currentLocation = newFile;
                            toggledImage.setFitWidth(197.0);
                            toggledImage.setFitHeight(192.0);
                            try {
                                toggledImage.setImage(new Image(new FileInputStream(imageSlider.get(slider))));
                                System.out.println("img slider: " + imageSlider.get(slider));
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            toggledName.setText(imageSlider.get(slider).getName());
                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                            toggledDate.setText(df.format(currentLocation.lastModified()));
                            toggledPath.setText(imageSlider.get(slider).getAbsolutePath());
                            pathLocation.setText(currentLocation.getAbsolutePath());
                            try {
                                if (uiChange == true) {
                                    showDirectoryIcon();
                                } else {
                                    showDirectoryList();
                                }
                                showNavigationTree();
                                showFavourites();
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    vBoxCenter.getChildren().add(buttonDirectory);

                } else if (currentLocation.listFiles()[i].getName().endsWith(".jpg")
                        || currentLocation.listFiles()[i].getName().endsWith(".png")) {
                    imageDirectory = new ImageView(new Image(new FileInputStream(currentLocation.listFiles()[i].getAbsolutePath())));

                    imageDirectory.setFitHeight(46.0);
                    imageDirectory.setFitWidth(36.0);

                    buttonDirectory = new ButtonDirectory(currentLocation.listFiles()[i]);
                    buttonDirectory.setText(currentLocation.listFiles()[i].getName());
                    buttonDirectory.setGraphic(imageDirectory);
                    buttonDirectory.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                    buttonDirectory.setContentDisplay(ContentDisplay.LEFT);

                    vBoxCenter.getChildren().add(buttonDirectory);

                    buttonDirectory.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ButtonDirectory btn = (ButtonDirectory) event.getSource();
                            System.out.println("btn: " + btn);
                            try {
                                imageSlider = new ArrayList<>();
                                int sliderNum = 0;
                                for (int i = 0; i < btn.getFile().getParentFile().listFiles().length; i++) {
                                    if (currentLocation.listFiles()[i].getName().endsWith(".jpg")
                                            || currentLocation.listFiles()[i].getName().endsWith(".png")) {
                                        imageSlider.add(btn.getFile().getParentFile().listFiles()[i]);

                                        if (currentLocation.listFiles()[i].getName().endsWith(".jpg")) {
                                            toggledType.setText(".jpg");
                                        } else if (currentLocation.listFiles()[i].getName().endsWith(".png")) {
                                            toggledType.setText(".png");
                                        }

                                        if (btn.getFile().getParentFile().listFiles()[i].equals(btn.getFile())) {
                                            slider = sliderNum;
                                            System.out.println(i);
                                        } 
                                        sliderNum++;
                                    }
                                    
                                }

                                sliderMax = imageSlider.size();

                                toggledImage.setFitWidth(197.0);
                                toggledImage.setFitHeight(192.0);
                                toggledImage.setImage(new Image(new FileInputStream(imageSlider.get(slider))));
                                toggledName.setText(btn.getText());
                                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                                toggledDate.setText(df.format(currentLocation.lastModified()));
                                toggledPath.setText(imageSlider.get(slider).getAbsolutePath());
                                pathLocation.setText(currentLocation.getAbsolutePath());

                                if (uiChange == true) {
                                    showDirectoryIcon();
                                } else {
                                    showDirectoryList();
                                }
                                showNavigationTree();
                                showFavourites();

                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });

                }

            }
            centerFlowPane.getChildren().add(vBoxCenter);
        } else {
            centerFlowPane.getChildren().clear();
            Text noPathFound = new Text("Path not Found");
            noPathFound.setStyle("-fx-font-size: 18px;");
            noPathFound.setTextAlignment(TextAlignment.CENTER);
            centerFlowPane.setAlignment(Pos.CENTER);
            centerFlowPane.getChildren().add(noPathFound);
        }

    }

    public void showNavigationTree() throws FileNotFoundException {
        vBoxTree.getChildren().clear();

        if (Files.exists(this.currentLocation.toPath())) {
            for (int j = 0; j < locationTree.listFiles().length; j++) {
                if (locationTree.listFiles()[j].isDirectory()) {
                    imageTree = new ImageView(new Image(new FileInputStream("./src/photorecurs/gfx/dir.png")));
                    imageTree.setFitHeight(46.0);
                    imageTree.setFitWidth(36.0);
                    buttonTree = new ButtonDirectory(locationTree.listFiles()[j]);
                    buttonTree.setText(locationTree.listFiles()[j].getName());
                    buttonTree.setGraphic(imageTree);
                    buttonTree.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                    buttonTree.setContentDisplay(ContentDisplay.LEFT);

                    vBoxTree.getChildren().addAll(buttonTree);

                }

                buttonTree.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ButtonDirectory btn = (ButtonDirectory) event.getSource();

                        File newFile = btn.getFile();

                        locationTree = newFile.getParentFile();
                        currentLocation = newFile;
                        pathLocation.setText(newFile.toString());
                        try {
                            if (uiChange == true) {
                                showDirectoryIcon();
                            } else {
                                showDirectoryList();
                            }
                            showNavigationTree();
                            showFavourites();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            }
        } else {
            centerFlowPane.getChildren().clear();
            Text noPathFound = new Text("Path not Found");
            noPathFound.setStyle("-fx-font-size: 18px;");
            noPathFound.setTextAlignment(TextAlignment.CENTER);
            centerFlowPane.setAlignment(Pos.CENTER);
            centerFlowPane.getChildren().add(noPathFound);
        }

    }

}
