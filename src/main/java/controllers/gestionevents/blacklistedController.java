package controllers.gestionevents;

import entities.gestionevents.Black_Listed;
import entities.gestionuser.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import services.gestionevents.BlackListedService;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class blacklistedController {
    private Connection connection;
    @FXML
    private TableView<Black_Listed> black_listedpeople;
    @FXML
    private TableColumn<Black_Listed, Integer> id_user_col;
    @FXML
    private TableColumn<Black_Listed, Date> start_ban_col;
    @FXML
    private TableColumn<Black_Listed, Date> end_ban_col;
    @FXML
    private Button ban_btn;
    @FXML
    private TableView<User> table_all_users;
    @FXML
    private TableColumn<User, Integer> idcol;
    @FXML
    private TableColumn<User, String> usernamecol;
    @FXML
    private TableColumn<User, String> firstnamecol;
    @FXML
    private TableColumn<User, String> lastnamecol;
    @FXML
    private final BlackListedService blackListedService = new BlackListedService();
    @FXML
    private Button show_users_btn;
    @FXML
    private Button show_blacklisted_btn;
    @FXML
    private Pane blacklisted_users;
    @FXML
    private Pane All_users;
    @FXML
    private Button unban_btn;
    @FXML
    private Button back_tomanag;

    @FXML
    void initialize() {
        afficherBlackListed();
        afficher_all_users();
        unbanUser();

    }
    public User getUserDetails(int userId) throws SQLException {

        String query = "SELECT id, username, firstname, lastname FROM user WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setFirstname(rs.getString("firstname"));
            user.setLastname(rs.getString("lastname"));
            return user;
        }
        return null;
    }

    void afficher_all_users(){

        try {
            List<Integer> userIds = blackListedService.getusersids();
            ObservableList<User> data = FXCollections.observableArrayList();

            for (Integer userId : userIds) {
                User user = blackListedService.getUserDetails(userId);
                if (user != null) {
                    // Check if the user is banned
                    boolean isBanned = blackListedService.search(userId);
                    // If the user is not banned, add them to the table
                    if (!isBanned) {
                        data.add(user);
                    }
                }
            }

            table_all_users.getItems().clear();
            table_all_users.getItems().addAll(data);
            idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            usernamecol.setCellValueFactory(new PropertyValueFactory<>("username"));
            firstnamecol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            lastnamecol.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void afficherBlackListed(){
        try{
            List<Black_Listed> blackListed = blackListedService.getAll();

            if(blackListed.isEmpty()){
                System.out.println("Empty list");
            }


            black_listedpeople.getItems().clear();
            black_listedpeople.getItems().addAll(blackListed);
            id_user_col.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            start_ban_col.setCellValueFactory(new PropertyValueFactory<>("start_ban"));
            end_ban_col.setCellValueFactory(new PropertyValueFactory<>("end_ban"));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }





    }
    @FXML
    void ban_user(ActionEvent event) {
        // Get the selected user from the TableView
        User selectedUser = table_all_users.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Show an alert dialog to get the start ban date
            long currentTime = System.currentTimeMillis();
            Date startBan = new Date(currentTime); // replace with start ban date from alert dialog

            // Create a DatePicker dialog to get the end ban date
            Dialog<LocalDate> dialog = new Dialog<>();
            dialog.setTitle("Select End Ban Date");
            DatePicker datePicker = new DatePicker();
            datePicker.setEditable(false); // make DatePicker non-editable
            datePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();

                    // Disable past dates and the current date
                    setDisable(empty || date.compareTo(today.plusDays(1)) < 0);
                }
            });
            dialog.getDialogPane().setContent(datePicker);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK); // add confirmation button
            dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? datePicker.getValue() : null);
            Optional<LocalDate> result = dialog.showAndWait();

            if (result.isPresent()) {
                LocalDate localDate = result.get();
                Date endBan = Date.valueOf(localDate);

                // Create a new Black_Listed object with the user's id and the ban dates
                Black_Listed blackListedUser = new Black_Listed(selectedUser.getId(), startBan, endBan);

                try {
                    // Use the BlackListedService to add the new Black_Listed object to the database
                    blackListedService.add(blackListedUser);

                    // Call the afficherBlackListed method to refresh the TableView
                    afficherBlackListed();
                    afficher_all_users();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @FXML
    void show_n_users(ActionEvent event) {
        blacklisted_users.setVisible(false);
        All_users.setVisible(true);
        afficher_all_users();
        unbanUser();
    }
    @FXML
    void show_blacklisted(ActionEvent event) {
        All_users.setVisible(false);
        blacklisted_users.setVisible(true);
        afficherBlackListed();
        unbanUser();
    }
    @FXML
    void unban_user(ActionEvent event) {
        // Get the selected user from the TableView
        Black_Listed selectedUser = black_listedpeople.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            try {
                // Use the BlackListedService to delete the selected Black_Listed object from the database
                blackListedService.delete(selectedUser.getId_user());

                // Call the afficherBlackListed method to refresh the TableView
                afficherBlackListed();
                afficher_all_users();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //when ban date ends the user is automatically unbanned
    public void unbanUser() {
        try {
            List<Black_Listed> blackListed = blackListedService.getAll();
            long currentTime = System.currentTimeMillis();
            Date currentDate = new Date(currentTime);

            for (Black_Listed blackListed1 : blackListed) {
                if (blackListed1.getEnd_ban().before(currentDate)) {
                    blackListedService.delete(blackListed1.getId_user());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
