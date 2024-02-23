package controllers.gestionequipement;

import animatefx.animation.*;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import entities.gestionequipements.Equipements_details;
import entities.gestionequipements.Maintenances;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import services.gestionequipements.EquipementService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.scene.layout.Pane;
import services.gestionequipements.MaintenancesService;

public class EquipementController {

    final Notification msg = new Notification();

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Pane addpane;

    @FXML
    private DatePicker add_date1;

    @FXML
    private TextField add_status1;

    @FXML
    private Pane ModifyMaintPane;

    @FXML
    private Pane EquipPane;

    @FXML
    private Pane maintPane;

    @FXML
    private Pane ModifyEquipPane;


    @FXML
    private DatePicker add_date;

    @FXML
    private TextField add_desc;


    @FXML
    private ComboBox<Integer> add_ide;

    @FXML
    private TextField add_lp;

    @FXML
    private TextField add_name;

    @FXML
    private ComboBox<String> state_cb;

    @FXML
    private TextField add_status;


    @FXML
    private TableColumn<?, ?> show_datem;

    @FXML
    private TableColumn<?, ?> show_desc;

    @FXML
    private TableColumn<?, ?> show_id;

    @FXML
    private TableColumn<?, ?> show_ide;

    @FXML
    private TableColumn<?, ?> show_idm;

    @FXML
    private TableColumn<?, ?> show_lp;

    @FXML
    private TableColumn<?, ?> show_name;

    @FXML
    private TableColumn<?, ?> show_state;

    @FXML
    private TableColumn<?, ?> show_status;

    @FXML
    private TextField add_lp1;

    @FXML
    private TextField add_name1;

    @FXML
    private Pane addmaintpane;


    @FXML
    private ComboBox<String> statemod_cb;

    @FXML
    private TextField add_desc1;

    @FXML
    private TableView<Equipements_details> tableviewEq;

    @FXML
    private TableView<Maintenances> tableviewMaint;

    private final EquipementService EquipementService = new EquipementService();

    private final MaintenancesService MaintenancesService = new MaintenancesService();

    @FXML
    void addEquipement(ActionEvent event) {
        try {
            EquipementService.add(new Equipements_details(add_name.getText(), add_desc.getText(), add_lp.getText(), state_cb.getValue()));
            notify("Equipement added successfully!");
            getAllEquipements();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    @FXML
    void deleteEquipment(ActionEvent event) {
        try {
            EquipementService.delete(tableviewEq.getSelectionModel().getSelectedItem().getId());
            notify("Equipement deleted successfully!");
            getAllEquipements();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    void modifyEquipment(ActionEvent event) {
        Equipements_details equipment = tableviewEq.getSelectionModel().getSelectedItem();
        if (equipment != null) {
            add_name1.setText(equipment.getName());
            add_desc1.setText(equipment.getDescription());
            add_lp1.setText(equipment.getDuree_de_vie());
            statemod_cb.setValue(equipment.getEtat());
            if (statemod_cb.getValue().equals("Under Maintenance")) {
                statemod_cb.setDisable(true);
            }else {
                statemod_cb.setDisable(false);
            }
            var f = new FadeOutLeft();
            var f1 = new FadeInLeft();
            f.setNode(addpane);
            f.setOnFinished((e) -> {
                f1.setNode(ModifyEquipPane);
                ModifyEquipPane.setVisible(true);
                ModifyEquipPane.setOpacity(0);
                f1.play();
            });
            f.play();
        }
    }

    void getAllEquipements() {
        try {
            List<Equipements_details> equipements = EquipementService.getAll();
            ObservableList<Equipements_details> equipementsObservableList = FXCollections.observableArrayList(equipements);
            show_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            show_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            show_desc.setCellValueFactory(new PropertyValueFactory<>("description"));
            show_lp.setCellValueFactory(new PropertyValueFactory<>("duree_de_vie"));
            show_state.setCellValueFactory(new PropertyValueFactory<>("etat"));
            tableviewEq.setItems(equipementsObservableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void initialize() {
        getAllEquipements();
        getAllMaint();
        updateCb();
        state_cb.getItems().addAll("Good", "Bad");
        state_cb.setValue("Good");
        statemod_cb.getItems().addAll("Good", "Bad");
    }

    private void updateCb() {
        add_ide.getItems().clear();
        try {
            List<Equipements_details> listEquip = EquipementService.getAll();
            for (Equipements_details equip : listEquip) {
                add_ide.getItems().add(equip.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addMaint(ActionEvent event) {
        try {
            if (MaintenancesService.isUnderMaintenance(add_ide.getValue())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("This equipment is already under maintenance!");
                alert.showAndWait();
                return;
            }
            String mydate = String.valueOf(add_date.getValue());
            java.sql.Date sqlAddDate = java.sql.Date.valueOf(mydate);
            MaintenancesService.add(new Maintenances( add_ide.getValue(), mydate, add_status.getText()));
            notify("Maintenance added successfully!");
            getAllMaint();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    void deleteMaint(ActionEvent event) {
        try {
            MaintenancesService.delete(tableviewMaint.getSelectionModel().getSelectedItem().getIdm());
            notify("Maintenance deleted successfully!");
            getAllMaint();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void modifyMaint(ActionEvent event) {
        Maintenances maintenances = tableviewMaint.getSelectionModel().getSelectedItem();
        if (maintenances != null) {
            add_date1.setValue(LocalDate.parse(maintenances.getDate_maintenance()));
            add_status1.setText(maintenances.getStatus());
            FadeOutRight f = new FadeOutRight();
            FadeInRight f1 = new FadeInRight();
            f.setNode(addmaintpane);
            f.setOnFinished((e) -> {
                f1.setNode(ModifyMaintPane);
                ModifyMaintPane.setVisible(true);
                ModifyMaintPane.setOpacity(0);
                addmaintpane.setVisible(false);
                f1.play();
            });
            f.play();
        }
    }

    void getAllMaint() {
        try {
            List<Maintenances> maintenances = MaintenancesService.getAll();
            ObservableList<Maintenances> equipementsObservableList = FXCollections.observableArrayList(maintenances);
            show_idm.setCellValueFactory(new PropertyValueFactory<>("idm"));
            show_ide.setCellValueFactory(new PropertyValueFactory<>("ide"));
            show_datem.setCellValueFactory(new PropertyValueFactory<>("date_maintenance"));
            show_status.setCellValueFactory(new PropertyValueFactory<>("status"));
            tableviewMaint.setItems(equipementsObservableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void gotomaint_btn_act(ActionEvent event) {
        getAllEquipements();
        getAllMaint();
        updateCb();
        FadeOutRight f = new FadeOutRight();
        FadeInRight f1 = new FadeInRight();
        f.setNode(EquipPane);
        f.setOnFinished((e) -> {
            f1.setNode(maintPane);
            maintPane.setVisible(true);
            maintPane.setOpacity(0);
            EquipPane.setVisible(false);
            f1.play();
        });
        f.play();
    }

    @FXML
    void gotoequip_btn_act(ActionEvent event) {
        getAllEquipements();
        getAllMaint();
        FadeOutLeft f = new FadeOutLeft();
        FadeInLeft f1 = new FadeInLeft();
        f.setNode(maintPane);
        f.setOnFinished((e) -> {
            f1.setNode(EquipPane);
            EquipPane.setVisible(true);
            EquipPane.setOpacity(0);
            maintPane.setVisible(false);
            f1.play();
        });
        f.play();

    }

    @FXML
    void editEquip(ActionEvent event) {
        try {
            Equipements_details selectedEvent = tableviewEq.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                EquipementService eventDetailsService = new EquipementService();
                Equipements_details equipementsDetails = new Equipements_details();
                equipementsDetails.setId(selectedEvent.getId());
                equipementsDetails.setName(add_name1.getText());
                equipementsDetails.setDescription(add_desc1.getText());
                equipementsDetails.setDuree_de_vie(add_lp1.getText());
                equipementsDetails.setEtat(statemod_cb.getValue());
                eventDetailsService.update(equipementsDetails);
                getAllEquipements();
                var f = new FadeOutLeft(ModifyEquipPane);
                f.setOnFinished((e) -> {
                    ModifyEquipPane.setVisible(false);
                    FadeInRight f2 = new FadeInRight(addpane);
                    addpane.setVisible(true);
                    addpane.setOpacity(0);
                    f2.play();
                });
                f.play();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void editMaint_btn_act(ActionEvent event) {
        try {
            Maintenances selectedEvent = tableviewMaint.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                MaintenancesService maintenancesService = new MaintenancesService();
                Maintenances maintenances = new Maintenances();
                maintenances.setIdm(selectedEvent.getIdm());
                maintenances.setIde(selectedEvent.getIde());
                maintenances.setDate_maintenance(add_date1.getValue().toString());
                maintenances.setStatus(add_status1.getText());
                maintenancesService.update(maintenances);
                getAllMaint();
                FadeOutRight f = new FadeOutRight(ModifyMaintPane);
                f.setOnFinished((e) -> {
                    ModifyMaintPane.setVisible(false);
                    FadeInRight f2 = new FadeInRight(maintPane);
                    maintPane.setOpacity(0);
                    maintPane.setVisible(true);
                    f2.play();
                });
                f.play();
            } else {
                System.out.println("No maintenance selected");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        FadeOutLeft f = new FadeOutLeft();
        FadeInLeft f1 = new FadeInLeft();
        f.setNode(ModifyMaintPane);
        f.setOnFinished((e) -> {
            f1.setNode(maintPane);
            maintPane.setVisible(true);
            maintPane.setOpacity(0);
            ModifyMaintPane.setVisible(false);
            f1.play();
        });
        f.play();
        getAllMaint();
    }

    private void notify(String message) {
        msg.setMessage(message);
        msg.getStyleClass().addAll(
                Styles.SUCCESS, Styles.ELEVATED_1
        );
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);
        msg.setLayoutX(750);
        msg.setLayoutY(80);

        msg.setOnClose(e -> {
            var out = Animations.slideOutRight(msg, Duration.millis(250));
            out.setOnFinished(f -> mainPane.getChildren().remove(msg));
            out.playFromStart();
        });
        var in = Animations.slideInRight(msg, Duration.millis(250));
        if (!mainPane.getChildren().contains(msg)) {
            mainPane.getChildren().add(msg);
        }
        in.playFromStart();
    }
}







