package sample.worksheet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.datamdodel.Access;
import sample.datamdodel.Employee;
import sample.datamdodel.EmployeeDAO;
import sample.datamdodel.StageManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageEmployeesController implements Initializable {

    @FXML
    private TextField textBoxName;

    @FXML
    private TextField textBoxSurname;

    @FXML
    private ChoiceBox<Access> choiceBoxAccountType;

    @FXML
    private TableView<Employee> tableViewEmployees;

    @FXML
    private TableColumn<Employee, Integer> columnID;

    @FXML
    private TableColumn<Employee, String> columnName;

    @FXML
    private TableColumn<Employee, String> columnSurname;

    @FXML
    private TableColumn<Employee, String> columnLogin;

    @FXML
    private TableColumn<Employee, String> columnPassword;

    @FXML
    private TableColumn<Employee, Integer> columnAccess;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBoxAccountType.setItems(FXCollections.observableArrayList(Access.values()));
        columnID.setCellValueFactory(new PropertyValueFactory<>("idEmployee"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        columnLogin.setCellValueFactory(new PropertyValueFactory<>("Login"));
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));
        columnAccess.setCellValueFactory(new PropertyValueFactory<>("Access"));
    }

    @FXML
    private void addEmployee(){
        if(validateFields()){
            try {
                Access value = choiceBoxAccountType.getValue();
                EmployeeDAO.insertEmployee(textBoxName.getText(), textBoxSurname.getText(), value.getValue());
                System.out.println("pracownik został dodadny pomyślnie");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void openWorkdayScene(ActionEvent event){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("Workday.fxml"));
            Parent root = loader.load();

            Stage stageWorkday = new Stage();
            stageWorkday.setScene(new Scene(root));
            stageWorkday.setResizable(false);
            stageWorkday.show();

            WorkdayController workdayController = loader.getController();
            stageWorkday.setOnCloseRequest(eventClose -> workdayController.send());

            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEditEmployee(){
        Employee toEditEmployee = tableViewEmployees.getSelectionModel().getSelectedItem();
        if(validateSelections()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditEmployee.fxml"));
                Parent root = loader.load();

                EditEmployeeController editEmployeeController = loader.getController();
                editEmployeeController.initializeToEdit(toEditEmployee);

                Stage editStage = new Stage();
                StageManager.stages.add(editStage);
                editStage.setScene(new Scene(root));

                editStage.initModality(Modality.APPLICATION_MODAL);

                editStage.setResizable(false);
                editStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void loadEmployees() throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Employee> empData = EmployeeDAO.searchEmployees();
            tableViewEmployees.setItems(empData);
        } catch (SQLException e){
            System.out.println("Error occurred while getting employees information from DB" + e);
            throw e;
        }
    }

    @FXML
    private void deleteEmployee(){
        if (validateSelections()) {
            int selectedRowID = tableViewEmployees.getSelectionModel().getSelectedItem().getIdEmployee();
            try {
                EmployeeDAO.deleteEmployeeWithId(selectedRowID);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Employee selectedEmployee = tableViewEmployees.getSelectionModel().getSelectedItem();
            tableViewEmployees.getItems().remove(selectedEmployee);
        }
    }
    private boolean validateSelections(){
        int selectedItems = tableViewEmployees.getSelectionModel().getSelectedIndex();
            if (selectedItems<0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No selection was made");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    boolean validateFields(){
        if (textBoxName.getText().isEmpty()|textBoxSurname.getText().isEmpty()|choiceBoxAccountType.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the required data to create new employee");
            alert.showAndWait();
            return false;
        }
        return true;
    }

}
