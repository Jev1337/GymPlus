package controllers.gestionsuivi;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MacrosController {


    @FXML
    private Label CarbsLabel;

    @FXML
    private Label FibreLabel;

    @FXML
    private Label PotassuimLabel;

    @FXML
    private Label SoduimLabel;

    @FXML
    private Label caloriesLabel;

    @FXML
    private Label cholesterolLabel;

    @FXML
    private Label fatSaturatedLabel;

    @FXML
    private Label fatTotalLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label servingLabel;

    @FXML
    private Label proteinLabel;

    @FXML
    private Label sugarLabel;

public void fillMacros(String name,double calories,double serving_size_g,double fat_total_g,double fat_saturated_g,double protein_g,double sodium_mg,double potassium_mg,double cholesterol_mg,double carbohydrates_total_g,double fiber,double sugar_g){

    nameLabel.setText(name);
    caloriesLabel.setText(String.valueOf(calories));
    servingLabel.setText(String.valueOf(serving_size_g));
    fatTotalLabel.setText(String.valueOf(fat_total_g));
    fatSaturatedLabel.setText(String.valueOf(fat_saturated_g));
    proteinLabel.setText(String.valueOf(protein_g));
    SoduimLabel.setText(String.valueOf(sodium_mg));
    PotassuimLabel.setText(String.valueOf(potassium_mg));
    cholesterolLabel.setText(String.valueOf(cholesterol_mg));
    CarbsLabel.setText(String.valueOf(carbohydrates_total_g));
    FibreLabel.setText(String.valueOf(fiber));
    sugarLabel.setText(String.valueOf(sugar_g));




}



}
