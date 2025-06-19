package ru.inettel.cassa;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import ru.inettel.cassa.card.Card;
import ru.inettel.cassa.dao.DAOHelper;
import ru.inettel.cassa.kkm.AtolKkm;
import ru.inettel.cassa.kkm.KkmExeption;
import ru.inettel.cassa.user.AtirraUser;
import ru.inettel.cassa.user.User;
import ru.inettel.cassa.user.UtmUser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by ksork on 29.06.17.
 */
public class MainFormController {

    //    private ObservableList<String> streets = ConfigHelper.getStreets();
    private List<String> streets = ConfigHelper.getStreets();
    private Set<String> names = DAOHelper.selectNames();
    private ObservableList<User> usersList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private static ObservableList<User> paymentList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private static User selectedUser;
    private static String sum;

    public static ObservableList<User> getPaymentList() {
        return paymentList;
    }

    public static User getSelectedUser() {
        return selectedUser;
    }

    @FXML
    private AnchorPane ap;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField houseTextField;
    @FXML
    private TextField flatTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField accountTextField;
    @FXML
    private CheckBox inetCheckBox;
    @FXML
    private CheckBox tvCheckBox;
    @FXML
    private Button findBtn;
    @FXML
    private Button clearCheckBtn;
    @FXML
    private Button cashBtn;
    @FXML
    private Button cardBtn;
    @FXML
    private TableView<User> mainTable;
    @FXML
    private TableColumn<User, String> serviceTableColumn;
    @FXML
    private TableColumn<User, String> accountTableColumn;
    @FXML
    private TableColumn<User, Double> balanceTableColumn;
    @FXML
    private TableColumn<User, String> streetTableColumn;
    @FXML
    private TableColumn<User, String> houseTableColumn;
    @FXML
    private TableColumn<User, String> flatTableColumn;
    @FXML
    private TableColumn<User, String> nameTableColumn;
    @FXML
    private TableColumn<User, String> tariffTableColumn;

    @FXML
    private TableView<User> paymentTable;
    @FXML
    private TableColumn<User, String> servicePaymentTableColumn;
    @FXML
    private TableColumn<User, String> accountPaymentTableColumn;
    @FXML
    private TableColumn<User, Double> balancePaymentTableColumn;
    @FXML
    private TableColumn<User, Double> cashInPaymentTableColumn;
    @FXML
    private TableColumn<User, String> streetPaymentTableColumn;
    @FXML
    private TableColumn<User, String> housePaymentTableColumn;
    @FXML
    private TableColumn<User, String> flatPaymentTableColumn;
    @FXML
    private TableColumn<User, String> namePaymentTableColumn;
    @FXML
    private TableColumn<User, String> tariffPaymentTableColumn;

    @FXML
    public void initialize() {
        TextFields.bindAutoCompletion(streetTextField, streets);
        TextFields.bindAutoCompletion(nameTextField, names);
        serviceTableColumn.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
        accountTableColumn.setCellValueFactory(cellData -> cellData.getValue().accountProperty());
        balanceTableColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());
        streetTableColumn.setCellValueFactory(cellData -> cellData.getValue().streetProperty());
        houseTableColumn.setCellValueFactory(cellData -> cellData.getValue().houseProperty());
        flatTableColumn.setCellValueFactory(cellData -> cellData.getValue().flatProperty());
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tariffTableColumn.setCellValueFactory(cellData -> cellData.getValue().tariffProperty());

        mainTable.setPlaceholder(new Label());
        mainTable.setItems(usersList);

        // Автовыделение всего текста при фокусе
        streetTextField.focusedProperty().addListener(new TextFieldFocusListener(streetTextField));
        houseTextField.focusedProperty().addListener(new TextFieldFocusListener(houseTextField));
        flatTextField.focusedProperty().addListener(new TextFieldFocusListener(flatTextField));
        accountTextField.focusedProperty().addListener(new TextFieldFocusListener(accountTextField));
        nameTextField.focusedProperty().addListener(new TextFieldFocusListener(nameTextField));

        mainTable.setRowFactory( tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    User user = row.getItem();
                    Dialog dialog = new TextInputDialog("");
                    dialog.setTitle("Сумма");
                    dialog.setHeaderText("Сумма оплаты");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()){
                        try {
                            double payment = Double.parseDouble(result.get());
                            if (payment <= 0 ) return;
                            user.setCashIn(payment);
                            paymentList.addAll(user);
                            switchBtn();
                        } catch (NumberFormatException e){
                            // Введена неверная сумма, ничего не делаем
                        }
                    }

                }
            });
            return row ;
        });

        //  Раскраска
        serviceTableColumn.setCellFactory(column -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                String style = "";
                setText(item);
                if (!empty) {
                    setTextFill(Color.BLACK);
                    TableRow<User> row = getTableRow();
                    User user = row.getItem();
                    if (user != null) {
                        if (user instanceof UtmUser)
                            style = user.getBalance() >= 0 ? "-fx-background-color: #61d7a4;" : "-fx-background-color: #ff7640;";
                        else if (user instanceof AtirraUser)
                            style = user.getBalance() >= 0 ? "-fx-background-color: #67e667;" : "-fx-background-color: #ff4040;";
                    }
                }
                setStyle(style);
            }
        });


        switchBtn();

        flatTableColumn.setComparator(new StringAsNumberComparator());
        houseTableColumn.setComparator(new StringAsNumberComparator());
        accountTableColumn.setComparator(new StringAsNumberComparator());
        nameTableColumn.setComparator((a, b) -> a.compareTo(b));

        // Таблица платежей
        servicePaymentTableColumn.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
        accountPaymentTableColumn.setCellValueFactory(cellData -> cellData.getValue().accountProperty());
        balancePaymentTableColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());
        cashInPaymentTableColumn.setCellValueFactory(cellDate -> cellDate.getValue().cashInProperty().asObject());
        streetPaymentTableColumn.setCellValueFactory(cellData -> cellData.getValue().streetProperty());
        housePaymentTableColumn.setCellValueFactory(cellData -> cellData.getValue().houseProperty());
        flatPaymentTableColumn.setCellValueFactory(cellData -> cellData.getValue().flatProperty());
        namePaymentTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tariffPaymentTableColumn.setCellValueFactory(cellData -> cellData.getValue().tariffProperty());

        paymentTable.setPlaceholder(new Label());
        paymentTable.setItems(paymentList);
    }

    public void disableFindBtn(boolean status) {
        findBtn.setDisable(status);
    }

    @FXML
    private void clear() {
        streetTextField.clear();
        houseTextField.clear();
        flatTextField.clear();
        nameTextField.clear();
        accountTextField.clear();
        paymentList.clear();
    }

    @FXML
    private void searchClient() {
        SearchData searchData = new SearchData();
        searchData.setStreet(streetTextField.getText().trim());
        searchData.setHouse(houseTextField.getText().trim());
        searchData.setFlat(flatTextField.getText().trim());
        searchData.setName(nameTextField.getText().trim());
        searchData.setAccount(accountTextField.getText().trim());
        searchData.setInet(inetCheckBox.isSelected());
        searchData.setTv(tvCheckBox.isSelected());
        usersList.clear();
        disableFindBtn(true);
        DAOHelper.searcUsers(searchData, usersList, this);
        findBtnSetFocus();
        mainTable.refresh();
        refreshNames();
    }

    @FXML
    private void findBtnSetFocus() {
        findBtn.requestFocus();
    }

    @FXML
    private void doPay(Event event) {
//        User user = mainTable.getSelectionModel().getSelectedItem();
//        MainFormController.selectedUser = user;
//        MainFormController.sum = sum;
//
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getClassLoader().getResource("payment_form.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Платеж");
            stage.setScene(new Scene(parent, 350, 240));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();
            Thread.sleep(500);
            clearCheck();
            searchClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void doPayCard(Event event){
        Card.pay();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearCheck();
        searchClient();
    }

    @FXML
    public void clearCheck(){
        paymentList.clear();
        switchBtn();
    }

    private void switchBtn(){
        clearCheckBtn.setDisable(paymentList.isEmpty());
        cardBtn.setDisable(paymentList.isEmpty());
        cashBtn.setDisable(paymentList.isEmpty());
    }

    @FXML
    private void xReport(){
        try {
            AtolKkm.xReport();
        } catch (KkmExeption e){
            ErrorMessage.show(e);
        }
    }

    @FXML
    private void closeAndShiftReport(){
        try {
            AtolKkm.closeAndShiftReport();
        } catch (KkmExeption e){
            ErrorMessage.show(e);
        }
    }

    @FXML
    private void cashIn(){
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("cash_in_form.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Касса");
            stage.setScene(new Scene(parent, 240, 180));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ap.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void cashOut(){
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("cash_out_form.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Касса");
            stage.setScene(new Scene(parent, 240, 180));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ap.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sellReturn(){
        User user = mainTable.getSelectionModel().getSelectedItem();
        if (user == null) return;
        MainFormController.selectedUser = user;
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("sell_return_form.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Возврат прихода");
            stage.setScene(new Scene(parent, 350, 400));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ap.getScene().getWindow());
            stage.showAndWait();
            Thread.sleep(500);
            searchClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sellCorrection(){}

    private void refreshNames(){
        names = DAOHelper.selectNames();
        System.out.println("Names refresh");
    }
}
