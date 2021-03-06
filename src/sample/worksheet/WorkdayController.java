package sample.worksheet;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Platform;
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
import sample.datamdodel.*;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class WorkdayController implements Initializable {

    @FXML
    private Label loggedUserName;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelToday;

    @FXML
    private Button buttonChange;

    @FXML
    private Button buttonReportDaily;

    @FXML
    private Button buttonTasks;

    @FXML
    private Button buttonEmployees;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonRemove;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnCheck;

    @FXML
    private TableView<Event> tableEvents;

    @FXML
    private TableColumn<Event,String> columnName;

    @FXML
    private TableColumn<Event,Timestamp> columnStart;

    @FXML
    private TableColumn<Event,Timestamp> columnEndDate;

    @FXML
    private TableColumn<Event,Integer> columnTime;

    @FXML
    private TableColumn<Event, Integer> columnIDEv;

    @FXML
    private TableView<Task> tableViewTasks;

    @FXML
    private TableColumn<Task, Integer> columnID;

    @FXML
    private TableColumn<Task, String> columnTaskName;

    @FXML
    private TableColumn<Task, String> columnDescirption;

    @FXML
    private JFXTimePicker timePickerStartTime;

    @FXML
    private JFXTimePicker timePickerEndTime;

    @FXML
    private JFXDatePicker datePickerAnotherDay;

    @FXML
    private TextArea textAreaDisplayDescription;
    private static LocalDate localDate;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        columnIDEv.setCellValueFactory(new PropertyValueFactory<>("idEvent"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("task"));
        columnStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        columnEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        columnID.setCellValueFactory(new PropertyValueFactory<>("idTask"));
        columnTaskName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnDescirption.setCellValueFactory(new PropertyValueFactory<>("Descirption"));

        timePickerStartTime.setIs24HourView(true);
        timePickerStartTime.editableProperty().setValue(false);
        timePickerEndTime.setIs24HourView(true);
        timePickerEndTime.editableProperty().setValue(false);

        localDate = LocalDate.now();
        System.out.println(localDate);
        buttonsController(EmployeeDAO.validateEmployeeAcount(Employee.loggedEmployee.getIdEmployee()));
        loadEventData(localDate);
        loadTasks();
        initializeLoggedEmployeeData();
    }

    private void loadEventData(LocalDate localDate) {
        try {
            ObservableList<Event> eventData = EventDAO.searchEvents(localDate);
            tableEvents.setItems(eventData);
        }catch(SQLException e){
                e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try {
            ObservableList<Task> taskData = TaskDAO.searchTasks();
            tableViewTasks.setItems(taskData);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("Login.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stageLogin = new Stage();

            stageLogin.setTitle("Login");
            stageLogin.setScene(new Scene(root1));
            stageLogin.setResizable(false);
            stageLogin.show();

            StageManager.stages.add(stageLogin);
            StageManager.closeStages(stageLogin);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openEmployeesWindow(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManageEmployees.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stageEmployee = new Stage();
            StageManager.stages.add(stageEmployee);

            stageEmployee.setTitle("Employees");
            stageEmployee.setScene(new Scene(root1));
            stageEmployee.show();
            stageEmployee.setResizable(false);

            ManageEmployeesController manageEmployeesController = fxmlLoader.getController();
            stageEmployee.setOnCloseRequest(eventClose -> manageEmployeesController.openWorkdayScene(event));

            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openTasksWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManageTasks.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stageKeywords = new Stage();
            StageManager.stages.add(stageKeywords);

            stageKeywords.setTitle("Tasks");
            stageKeywords.setScene(new Scene(root1));
            stageKeywords.show();
            stageKeywords.setResizable(false);

            ManageTasksController manageTasksController = fxmlLoader.getController();

            stageKeywords.setOnCloseRequest(eventClose -> manageTasksController.openWorkdayScene(event));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeLoginData(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChangeLoginData.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stageChangeLoginData = new Stage();
            stageChangeLoginData.setTitle("Data");
            stageChangeLoginData.setScene(new Scene(root1));

            stageChangeLoginData.initModality(Modality.APPLICATION_MODAL);

            stageChangeLoginData.show();
            stageChangeLoginData.setResizable(false);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void add() {
        if (validateFields()){
            LocalTime startTime = timePickerStartTime.getValue();
            String insertStart = localDate.toString() + " " + startTime.toString() + ":" + startTime.getSecond();
            Timestamp start = Timestamp.valueOf(insertStart);

            LocalTime endTime = timePickerEndTime.getValue();
            String insertEnd = localDate.toString() + " " + endTime.toString() + ":" + endTime.getSecond();
            Timestamp end = Timestamp.valueOf(insertEnd);

            Timestamp lastAdded = start;

            if (!tableEvents.getItems().isEmpty()) {
                int lastIndex = tableEvents.getItems().size()-1;
                lastAdded = tableEvents.getItems().get(lastIndex).getEndDate();
                System.out.println(lastIndex);
            }

            if ( start.before(end)&&(start.after(lastAdded)||start.equals(lastAdded))&&(start.after(Timestamp.valueOf(localDate + " 00:00:00"))||end.before(Timestamp.valueOf(localDate + " 23:59:59")))) {
                int elapsedMinutes = (int) Duration.between(startTime, endTime).toMinutes();

                Task selectedTask = tableViewTasks.getSelectionModel().getSelectedItem();

                Event eventToInsert = new Event(start, end, elapsedMinutes, 0, Employee.loggedEmployee.getIdEmployee(), selectedTask);

                tableEvents.getItems().add(eventToInsert);

            }
            else{Actions.showAlert("Wrong time input");}
        }
    }

    @FXML
    private boolean showDescription() {
        if (Actions.validateSelections(tableViewTasks.getSelectionModel().getSelectedIndex())) {
            Task description = tableViewTasks.getSelectionModel().getSelectedItem();
            textAreaDisplayDescription.setText(description.getDescirption());
            return true;
        }
        return false;
    }

    @FXML
    private void deleteEvent() {
        int idEventToDelete = tableEvents.getSelectionModel().getSelectedItem().getIdEvent();
        try {
            System.out.println(idEventToDelete);
            EventDAO.deleteEvent(idEventToDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Event selectedEvent = tableEvents.getSelectionModel().getSelectedItem();
        tableEvents.getItems().remove(selectedEvent);
    }

    public void initializeLoggedEmployeeData() {
        String loggedUser = "Logged as: " + Employee.loggedEmployee.getName() + " " + Employee.loggedEmployee.getSurname();
        String today = "Today is: ";
        String date = String.valueOf(localDate);
        loggedUserName.setText(loggedUser);
        labelToday.setText(today);
        labelDate.setText(date);
    }

    private boolean validateFields(){
        if (timePickerStartTime.getValue()!=null&&timePickerEndTime.getValue()!=null&&showDescription()){
            return true;
        }
        Actions.showAlert("Fill the required data");
        return false;
    }

    @FXML
    private void checkEvents(){
        EventDAO.checkCurrentEvents(tableEvents);
    }

    @FXML
    private void loadAnotherDay(){
        localDate = datePickerAnotherDay.getValue();
        loadEventData(localDate);
    }

    @FXML
    public void send(){
        Platform.runLater(() -> {
            if (!tableEvents.getItems().isEmpty()) {
                try {
                    ObservableList<Event> toSend = tableEvents.getItems();
                    EventDAO.sendEvents(toSend);
                } catch (Exception e) {
                    Actions.showAlert(e.toString());
                }
                loadEventData(localDate);
            }
            else{
                Actions.showInfo("No events to send");
            }
        });

    }

    @FXML
    private void openDailyReport(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportDaily.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stageReportDaily = new Stage();
            stageReportDaily.setTitle("Daily report");
            stageReportDaily.setScene(new Scene(root1));
            stageReportDaily.show();
            stageReportDaily.setResizable(false);

            ReportDailyController reportDailyController = fxmlLoader.getController();

            stageReportDaily.setOnCloseRequest(eventClose -> reportDailyController.openWorkdayScene(event));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openDateReport(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportDate.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stageReportDaily = new Stage();
            stageReportDaily.setTitle("Periodical report");
            stageReportDaily.setScene(new Scene(root1));
            stageReportDaily.show();
            stageReportDaily.setResizable(false);

            ReportDateController reportDateController = fxmlLoader.getController();

            stageReportDaily.setOnCloseRequest(eventClose -> reportDateController.openWorkdayScene(event));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonsController(int id){
        switch (id) {
            case 1:
                buttonReportDaily.setVisible(false);
                buttonReportDaily.setDisable(true);
                buttonTasks.setVisible(false);
                buttonTasks.setDisable(true);
                buttonEmployees.setVisible(false);
                buttonEmployees.setDisable(true);
                break;
            case 2:
                buttonReportDaily.setVisible(false);
                buttonReportDaily.setDisable(true);
                buttonEmployees.setVisible(false);
                buttonEmployees.setDisable(true);
                break;
            case 3:
                break;
        }
    }

    private boolean validateTime(Timestamp start, Timestamp end){


        return false;
    }
}
