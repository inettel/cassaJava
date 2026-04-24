package ru.inettel.cassa;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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

public class MainFormController {
    private List<String> streets = ConfigHelper.getStreets();
    private Set<String> names = DAOHelper.selectNames();
    private ObservableList<User> usersList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private static ObservableList<User> paymentList = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private static User selectedUser;
    private static String sum;
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

    public MainFormController() {
    }

    public static ObservableList<User> getPaymentList() {
        return paymentList;
    }

    public static User getSelectedUser() {
        return selectedUser;
    }

    @FXML
    public void initialize() {
        TextFields.bindAutoCompletion(this.streetTextField, this.streets);
        TextFields.bindAutoCompletion(this.nameTextField, this.names);
        this.serviceTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).serviceProperty();
        });
        this.accountTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).accountProperty();
        });
        this.balanceTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).balanceProperty().asObject();
        });
        this.streetTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).streetProperty();
        });
        this.houseTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).houseProperty();
        });
        this.flatTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).flatProperty();
        });
        this.nameTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).nameProperty();
        });
        this.tariffTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).tariffProperty();
        });
        this.mainTable.setPlaceholder(new Label());
        this.mainTable.setItems(this.usersList);
        this.streetTextField.focusedProperty().addListener(new TextFieldFocusListener(this.streetTextField));
        this.houseTextField.focusedProperty().addListener(new TextFieldFocusListener(this.houseTextField));
        this.flatTextField.focusedProperty().addListener(new TextFieldFocusListener(this.flatTextField));
        this.accountTextField.focusedProperty().addListener(new TextFieldFocusListener(this.accountTextField));
        this.nameTextField.focusedProperty().addListener(new TextFieldFocusListener(this.nameTextField));
        this.mainTable.setRowFactory((tv) -> {
            TableRow<User> row = new TableRow();
            row.setOnMouseClicked((event) -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    User user = (User)row.getItem();
                    Dialog dialog = new TextInputDialog("");
                    ((Dialog)dialog).setTitle("Сумма");
                    ((Dialog)dialog).setHeaderText("Сумма оплаты");
                    Optional<String> result = ((Dialog)dialog).showAndWait();
                    if (result.isPresent()) {
                        try {
                            double payment = Double.parseDouble((String)result.get());
                            if (payment <= 0.0) {
                                return;
                            }

                            user.setCashIn(payment);
                            paymentList.addAll(new User[]{user});
                            this.switchBtn();
                        } catch (NumberFormatException var8) {
                        }
                    }
                }

            });
            return row;
        });
        this.serviceTableColumn.setCellFactory((column) -> {
            return new TableCell<User, String>() {
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    String style = "";
                    this.setText(item);
                    if (!empty) {
                        this.setTextFill(Color.BLACK);
                        TableRow row = this.getTableRow();
                        User user = (User)row.getItem();
                        if (user != null) {
                            if (user instanceof UtmUser) {
                                style = user.getBalance() >= 0.0 ? "-fx-background-color: #61d7a4;" : "-fx-background-color: #ff7640;";
                            } else if (user instanceof AtirraUser) {
                                style = user.getBalance() >= 0.0 ? "-fx-background-color: #67e667;" : "-fx-background-color: #ff4040;";
                            }
                        }
                    }

                    this.setStyle(style);
                }
            };
        });
        this.switchBtn();
        this.flatTableColumn.setComparator(new StringAsNumberComparator());
        this.houseTableColumn.setComparator(new StringAsNumberComparator());
        this.accountTableColumn.setComparator(new StringAsNumberComparator());
        this.nameTableColumn.setComparator((a, b) -> {
            return a.compareTo(b);
        });
        this.servicePaymentTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).serviceProperty();
        });
        this.accountPaymentTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).accountProperty();
        });
        this.balancePaymentTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).balanceProperty().asObject();
        });
        this.cashInPaymentTableColumn.setCellValueFactory((cellDate) -> {
            return ((User)cellDate.getValue()).cashInProperty().asObject();
        });
        this.streetPaymentTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).streetProperty();
        });
        this.housePaymentTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).houseProperty();
        });
        this.flatPaymentTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).flatProperty();
        });
        this.namePaymentTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).nameProperty();
        });
        this.tariffPaymentTableColumn.setCellValueFactory((cellData) -> {
            return ((User)cellData.getValue()).tariffProperty();
        });
        this.paymentTable.setPlaceholder(new Label());
        this.paymentTable.setItems(paymentList);
    }

    public void disableFindBtn(boolean status) {
        this.findBtn.setDisable(status);
    }

    @FXML
    private void clear() {
        this.streetTextField.clear();
        this.houseTextField.clear();
        this.flatTextField.clear();
        this.nameTextField.clear();
        this.accountTextField.clear();
        paymentList.clear();
    }

    @FXML
    private void searchClient() {
        SearchData searchData = new SearchData();
        searchData.setStreet(this.streetTextField.getText().trim());
        searchData.setHouse(this.houseTextField.getText().trim());
        searchData.setFlat(this.flatTextField.getText().trim());
        searchData.setName(this.nameTextField.getText().trim());
        searchData.setAccount(this.accountTextField.getText().trim());
        searchData.setInet(this.inetCheckBox.isSelected());
        searchData.setTv(this.tvCheckBox.isSelected());
        this.usersList.clear();
        this.disableFindBtn(true);
        DAOHelper.searcUsers(searchData, this.usersList, this);
        this.findBtnSetFocus();
        this.mainTable.refresh();
        this.refreshNames();
    }

    @FXML
    private void findBtnSetFocus() {
        this.findBtn.requestFocus();
    }

    @FXML
    private void doPay(Event event) {
        Parent parent = null;

        try {
            parent = (Parent)FXMLLoader.load(this.getClass().getClassLoader().getResource("payment_form.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Платеж");
            stage.setScene(new Scene(parent, 350.0, 240.0));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());
            stage.showAndWait();
            Thread.sleep(500L);
            this.clearCheck();
            this.searchClient();
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
        } catch (InterruptedException var5) {
            InterruptedException e = var5;
            e.printStackTrace();
        }

    }

    @FXML
    private void doPayCard(Event event) {
        Card.pay();

        try {
            Thread.sleep(500L);
        } catch (InterruptedException var3) {
            InterruptedException e = var3;
            e.printStackTrace();
        }

        this.clearCheck();
        this.searchClient();
    }

    @FXML
    public void clearCheck() {
        paymentList.clear();
        this.switchBtn();
    }

    private void switchBtn() {
        this.clearCheckBtn.setDisable(paymentList.isEmpty());
        this.cardBtn.setDisable(paymentList.isEmpty());
        this.cashBtn.setDisable(paymentList.isEmpty());
    }

    @FXML
    private void xReport() {
        try {
            AtolKkm.xReport();
        } catch (KkmExeption var2) {
            KkmExeption e = var2;
            ErrorMessage.show(e);
        }

    }

    @FXML
    private void closeAndShiftReport() {
        try {
            AtolKkm.closeAndShiftReport();
        } catch (KkmExeption var2) {
            KkmExeption e = var2;
            ErrorMessage.show(e);
        }

    }

    @FXML
    private void cashIn() {
        try {
            Parent parent = (Parent)FXMLLoader.load(this.getClass().getClassLoader().getResource("cash_in_form.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Касса");
            stage.setScene(new Scene(parent, 240.0, 180.0));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.ap.getScene().getWindow());
            stage.show();
        } catch (IOException var3) {
            IOException e = var3;
            e.printStackTrace();
        }

    }

    @FXML
    private void cashOut() {
        try {
            Parent parent = (Parent)FXMLLoader.load(this.getClass().getClassLoader().getResource("cash_out_form.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Касса");
            stage.setScene(new Scene(parent, 240.0, 180.0));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.ap.getScene().getWindow());
            stage.show();
        } catch (IOException var3) {
            IOException e = var3;
            e.printStackTrace();
        }

    }

    @FXML
    private void sellReturn() {
        User user = (User)this.mainTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            selectedUser = user;

            try {
                Parent parent = (Parent)FXMLLoader.load(this.getClass().getClassLoader().getResource("sell_return_form.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Возврат прихода");
                stage.setScene(new Scene(parent, 350.0, 400.0));
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(this.ap.getScene().getWindow());
                stage.showAndWait();
                Thread.sleep(500L);
                this.searchClient();
            } catch (IOException var4) {
                IOException e = var4;
                e.printStackTrace();
            } catch (InterruptedException var5) {
                InterruptedException e = var5;
                e.printStackTrace();
            }

        }
    }

    @FXML
    private void sellCorrection() {
    }

    private void refreshNames() {
        this.names = DAOHelper.selectNames();
        System.out.println("Names refresh");
    }
}
