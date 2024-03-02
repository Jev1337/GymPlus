package controllers.gestionequipement;

import animatefx.animation.*;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import entities.gestionequipements.Equipements_details;
import entities.gestionequipements.Maintenances;
import entities.gestionuser.Admin;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import services.gestionequipements.EquipementService;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import services.gestionequipements.MaintenancesService;
import services.gestionuser.AdminService;


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
    private VBox eqlist_vbox;

    @FXML
    private Button gotomain_btn;

    @FXML
    private VBox maintlist_vbox;

    @FXML
    private Button gotoequip_btn;

    private final EquipementService EquipementService = new EquipementService();

    private final MaintenancesService MaintenancesService = new MaintenancesService();



    @FXML
    void addEquipement(ActionEvent event) {
        try {
            String name = add_name.getText().trim();
            String description = add_desc.getText().trim();
            String lifeSpan = add_lp.getText().trim();

            // Check if name is empty
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name must not be empty");
            }

            // Check if description is empty
            if (description.isEmpty()) {
                throw new IllegalArgumentException("Description must not be empty");
            }

            // Check if life span is empty
            if (lifeSpan.isEmpty()) {
                throw new IllegalArgumentException("Life Span must not be empty");
            }

            EquipementService.add(new Equipements_details(name, description, lifeSpan, state_cb.getValue()));
            notify("Equipment added successfully!");
            getAllEquipements();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void deleteEquipment(ActionEvent event) {
        try {
            if (selectedEquipement == null) {
                throw new IllegalArgumentException("No equipment selected");
            }
            EquipementService.delete(selectedEquipement.getId());
            notify("Equipement deleted successfully!");
            getAllEquipements();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    void modifyEquipment(ActionEvent event) {

        if (selectedEquipement != null) {
            gotomain_btn.setDisable(true);
            add_name1.setText(selectedEquipement.getName());
            add_desc1.setText(selectedEquipement.getDescription());
            add_lp1.setText(selectedEquipement.getDuree_de_vie());
            statemod_cb.setValue(selectedEquipement.getEtat());
            if (statemod_cb.getValue().equals("Under Maintenance")) {
                statemod_cb.setDisable(true);
            }else {
                statemod_cb.setDisable(false);
            }
            var f = new FadeOutLeft();
            var f1 = new FadeInLeft();
            if (ModifyEquipPane.isVisible()) {
                f.setNode(ModifyEquipPane);
                f.setOnFinished((e) -> {
                    f1.setNode(ModifyEquipPane);
                    ModifyEquipPane.setVisible(true);
                    ModifyEquipPane.setOpacity(0);
                    f1.play();
                });
                f.play();
                return;
            }
            f.setNode(addpane);
            f.setOnFinished((e) -> {
                f1.setNode(ModifyEquipPane);
                ModifyEquipPane.setVisible(true);
                ModifyEquipPane.setOpacity(0);
                f1.play();
            });
            f.play();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No equipment selected");
            alert.showAndWait();
        }
    }

    private Equipements_details selectedEquipement;

    private Maintenances selectedMaint;
    void getAllEquipements() {
        selectedEquipement = null;
        try {
            List<Equipements_details> equipements = EquipementService.getAll();
            eqlist_vbox.getChildren().clear();
            for (Equipements_details equip : equipements) {
                //fill the vbox with the hboxes containing the equipements
                HBox hBox = new HBox();
                hBox.setCursor(Cursor.HAND);
                hBox.setSpacing(200);
                hBox.setPrefHeight(50);
                hBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: transparent  linear-gradient(to right, #00aaff, #00688b)  linear-gradient(to right, #00aaff, #00688b)  linear-gradient(to right, #00aaff, #00688b);");
                var id = new Label("ID: " + equip.getId());
                id.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                var name = new Label("Name: " + equip.getName());
                name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                var desc = new Label("Description: " + equip.getDescription());
                desc.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                var lifeSpan = new Label("Life Span: " + equip.getDuree_de_vie());
                lifeSpan.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                var state = new Label("State: " + equip.getEtat());
                state.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                hBox.getChildren().add(state);

                VBox vbox1 = new VBox();
                vbox1.getChildren().addAll(id, name);
                VBox vbox2 = new VBox();
                vbox2.getChildren().addAll(desc, lifeSpan);
                VBox vbox3 = new VBox();
                vbox3.getChildren().addAll(state);
                hBox.getChildren().addAll(vbox1, vbox2, vbox3);
                eqlist_vbox.getChildren().add(hBox);
                hBox.setOnMouseEntered(e -> {
                    if (selectedEquipement != null && selectedEquipement.getId() == equip.getId()) {
                        hBox.setStyle("-fx-background-color: #2196f3; -fx-border-color: transparent  linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b); -fx-border-radius: 5px;");
                    }else
                        hBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b); -fx-border-radius: 5px;");
                });
                hBox.setOnMouseExited(e -> {
                    if (selectedEquipement != null && selectedEquipement.getId() == equip.getId()) {
                        hBox.setStyle("-fx-background-color: #2196f3; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b);-fx-border-radius: 5px;");
                    }else
                        hBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b); -fx-border-radius: 5px;");
                });
                hBox.setOnMouseClicked(e -> {
                    selectedEquipement = equip;
                    for(Node node : eqlist_vbox.getChildren()){
                        if (node instanceof HBox && node != hBox){
                            node.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b);-fx-border-radius: 5px;");
                        }
                    }
                    hBox.setStyle("-fx-background-color: #2196f3; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b); -fx-border-radius: 5px;");
                });
            }
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
            Integer ide = add_ide.getValue();
            if (ide == null) {
                throw new IllegalArgumentException("Equipment ID must be selected.");
            }

            LocalDate date = add_date.getValue();
            if (date == null) {
                throw new IllegalArgumentException("Date must be selected.");
            }
            java.sql.Date sqlAddDate = java.sql.Date.valueOf(date);

            String status = add_status.getText().trim();
            if (status.isEmpty()) {
                throw new IllegalArgumentException("Status must not be empty.");
            }

            sendEquipMailAdmins(ide);
            MaintenancesService.add(new Maintenances(ide, sqlAddDate.toString(), status));
            notify("Maintenance added successfully!");
            getAllMaint();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    private void sendEquipMailAdmins(int ide) {
        try {
            String mail = "gymplus-noreply@grandelation.com";
            String password = "yzDvS_UoSL7b";
            List<String> emails = new AdminService().getAllAdminsEmails();
            String to = String.join(",", emails);
            String subject = "Maintenance Alert";
            //the body will be "index.html" inside src/assets/html
            File file = new File("src/assets/html/index.html");
            String body = "";
            body = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            body = body.replace("{E1}", String.valueOf(ide));
            String host = "mail.grandelation.com";

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.debug", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, null);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(mail);
            msg.setRecipients(Message.RecipientType.TO, to);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html");
            multipart.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile("src/assets/html/assets/image-5.png");
            attachment.setContentID("<image-5>");
            attachment.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(attachment);

            msg.setContent(multipart);

            Transport.send(msg, mail, password);

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void deleteMaint(ActionEvent event) {
        try {
            if (selectedMaint == null) {
                throw new IllegalArgumentException("No maintenance selected");
            }

            File file = new File("src/assets/html/attestation.html");
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            content = content.replace("{E1}", EquipementService.getEquipementById(selectedMaint.getIde()).getName());
            content = content.replace("{E2}", String.valueOf(java.time.temporal.ChronoUnit.DAYS.between(LocalDate.parse(selectedMaint.getDate_maintenance()), LocalDate.now())));
            content = content.replace("{E3}", String.valueOf(java.time.temporal.ChronoUnit.DAYS.between(LocalDate.parse(selectedMaint.getDate_maintenance()), LocalDate.now()) * 100));
            content = content.replace("{E4}", LocalDate.now().toString());
            content = content.replace("{E5}", String.valueOf(selectedMaint.getIdm()));

            File newFile = new File("src/assets/html/attestation" + selectedMaint.getIdm() + ".html");
            FileWriter writer = new FileWriter(newFile);
            writer.write(content);
            writer.close();
            ConverterProperties properties = new ConverterProperties();

            properties.setBaseUri("src/assets/html/");
            properties.setCharset("UTF-8");
            properties.setImmediateFlush(true);
            properties.setCreateAcroForm(true);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialFileName("attestation" + selectedMaint.getIdm());
            File file1 = fileChooser.showSaveDialog(gotoequip_btn.getScene().getWindow());
            if (file1 == null) {
                throw new IllegalArgumentException("No file selected");
            }
            HtmlConverter.convertToPdf(new FileInputStream("src/assets/html/attestation" + selectedMaint.getIdm() + ".html"), new FileOutputStream(file1), properties);
            java.awt.Desktop.getDesktop().open(file1);
            newFile.delete();



            MaintenancesService.delete(selectedMaint.getIdm());
            notify("Maintenance deleted successfully!");
            getAllMaint();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void modifyMaint(ActionEvent event) {
        if (selectedMaint != null) {
            gotoequip_btn.setDisable(true);
            add_date1.setValue(LocalDate.parse(selectedMaint.getDate_maintenance()));
            add_status1.setText(selectedMaint.getStatus());
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
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No maintenance selected");
            alert.showAndWait();
        }
    }

    void getAllMaint() {
        selectedMaint = null;
        try {
            List<Maintenances> maintenances = MaintenancesService.getAll();
            maintlist_vbox.getChildren().clear();

            for (Maintenances maint : maintenances) {
                //fill the vbox with the hboxes containing the equipements
                HBox hBox = new HBox();
                hBox.setCursor(Cursor.HAND);
                hBox.setSpacing(200);
                hBox.setPrefHeight(50);
                hBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: transparent  linear-gradient(to right, #00aaff, #00688b)  linear-gradient(to right, #00aaff, #00688b)  linear-gradient(to right, #00aaff, #00688b);");
                var idm = new Label("ID: " + maint.getIdm());
                idm.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                var ide = new Label("Equipment ID: " + maint.getIde());
                ide.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                var date = new Label("Date: " + maint.getDate_maintenance());
                date.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                var status = new Label("Status: " + maint.getStatus());
                status.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
                hBox.getChildren().add(status);

                VBox vbox1 = new VBox();
                vbox1.getChildren().addAll(idm, ide);
                VBox vbox2 = new VBox();
                vbox2.getChildren().addAll(date, status);
                hBox.getChildren().addAll(vbox1, vbox2);
                maintlist_vbox.getChildren().add(hBox);
                hBox.setOnMouseEntered(e -> {
                    if (selectedMaint != null && selectedMaint.getIdm() == maint.getIdm()) {
                        hBox.setStyle("-fx-background-color: #2196f3; -fx-border-color: transparent  linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b);-fx-border-radius: 5px;");
                    }else
                        hBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b); -fx-border-radius: 5px;");
                });
                hBox.setOnMouseExited(e -> {
                    if (selectedMaint != null && selectedMaint.getIdm() == maint.getIdm()){
                        hBox.setStyle("-fx-background-color: #2196f3; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b);-fx-border-radius: 5px;");
                    }else
                        hBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b); -fx-border-radius: 5px;");
                });
                hBox.setOnMouseClicked(e -> {
                    selectedMaint = maint;
                    for(Node node : maintlist_vbox.getChildren()){
                        if (node instanceof HBox && node != hBox){
                            node.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b);-fx-border-radius: 5px;");
                        }
                    }
                    hBox.setStyle("-fx-background-color: #2196f3; -fx-border-color: transparent linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b) linear-gradient(to right, #00aaff, #00688b); -fx-border-radius: 5px;");
                });
            }
        } catch (Exception e) {
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

            if (selectedEquipement != null) {
                String name = add_name1.getText().trim();
                String description = add_desc1.getText().trim();
                String lifeSpan = add_lp1.getText().trim();

                // Check if name is empty
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Name must not be empty");
                }

                // Check if description is empty
                if (description.isEmpty()) {
                    throw new IllegalArgumentException("Description must not be empty");
                }

                // Check if life span is empty
                if (lifeSpan.isEmpty()) {
                    throw new IllegalArgumentException("Life Span must not be empty");
                }

                if (!selectedEquipement.getEtat().equals("Bad") && statemod_cb.getValue().equals("Bad")){
                    sendEquipMailAdmins(selectedEquipement.getId());
                }
                EquipementService eventDetailsService = new EquipementService();
                Equipements_details equipementsDetails = new Equipements_details();
                equipementsDetails.setId(selectedEquipement.getId());
                equipementsDetails.setName(name);
                equipementsDetails.setDescription(description);
                equipementsDetails.setDuree_de_vie(lifeSpan);
                equipementsDetails.setEtat(statemod_cb.getValue());
                eventDetailsService.update(equipementsDetails);
                getAllEquipements();
                var f = new FadeOutLeft(ModifyEquipPane);
                f.setOnFinished((e) -> {
                    ModifyEquipPane.setVisible(false);
                    FadeInRight f2 = new FadeInRight(addpane);
                    addpane.setVisible(true);
                    addpane.setOpacity(0);
                    gotomain_btn.setDisable(false);
                    f2.play();
                });
                f.play();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }



    }

    @FXML
    void editMaint_btn_act(ActionEvent event) {
        try {
            if (selectedMaint != null) {
                LocalDate maintenanceDate = add_date1.getValue();
                String status = add_status1.getText().trim();

                // Check if maintenance date is empty
                if (maintenanceDate == null) {
                    throw new IllegalArgumentException("Maintenance Date must not be empty");
                }

                // Check if status is empty
                if (status.isEmpty()) {
                    throw new IllegalArgumentException("Status must not be empty");
                }

                MaintenancesService maintenancesService = new MaintenancesService();
                Maintenances maintenances = new Maintenances();
                maintenances.setIdm(selectedMaint.getIdm());
                maintenances.setIde(selectedMaint.getIde());
                maintenances.setDate_maintenance(maintenanceDate.toString());
                maintenances.setStatus(status);
                maintenancesService.update(maintenances);

                FadeOutRight f = new FadeOutRight(ModifyMaintPane);
                f.setOnFinished((e) -> {
                    ModifyMaintPane.setVisible(false);
                    FadeInRight f2 = new FadeInRight(addmaintpane);
                    addmaintpane.setOpacity(0);
                    addmaintpane.setVisible(true);
                    gotoequip_btn.setDisable(false);
                    f2.play();

                });
                f.play();
                getAllMaint();
            } else {
                System.out.println("No maintenance selected");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }


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







