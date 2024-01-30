package com.store;

import com.store.dao.ClientDAO;
import com.store.entity.Client;
import com.store.util.FullAddress;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CrudController {

    @FXML private Pane readingUserPane;
    @FXML private Pane creatingUserPane;

    @FXML private TableView<Client> resultTable;
    @FXML private TableColumn<Client, Integer> columnId;
    @FXML private TableColumn<Client, String> columnName;
    @FXML private TableColumn<Client, String> columnEmail;
    @FXML private TableColumn<Client, String> columnSurname;
    @FXML private TableColumn<Client, LocalDate> columnBirthdate;
    @FXML private TableColumn<Client, Client> updateColumn;
    @FXML private TableColumn<Client, Client> deleteColumn;

    @FXML private TextField userId;
    @FXML private TextField userName;
    @FXML private TextField userSurname;
    @FXML private DatePicker userBirthdate;
    @FXML private TextField userCep;
    @FXML private TextField userAddress;
    @FXML private TextField userCity;
    @FXML private TextField userState;
    @FXML private TextField userEmail;
    @FXML private TextField searchingId;
    @FXML private TextField searchingName;
    @FXML private ToggleGroup toggleGroupSex;
    @FXML private Toggle toggleMasculine;
    @FXML private Toggle toggleFeminine;

    @Autowired private ClientDAO clientDAO;

    public CrudController() {
    }

    public void initialize(){
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Image icon = new Image("img/trash-bin.png");
            private final ImageView iconView = new ImageView(icon);
            private final ColorAdjust colorAdjust = new ColorAdjust();

            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                iconView.setFitHeight(16);
                iconView.setFitWidth(16);
                colorAdjust.setBrightness(1);
                iconView.setEffect(colorAdjust);
                Button deleteButton = new Button("", iconView);
                deleteButton.setStyle("-fx-background-color: #FF0000;");

                if (client == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> {
                            deleteUser(client);
                            getTableView().getItems().remove(client);
                        }
                );
            }
        });
        updateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Image icon = new Image("img/refresh.png");
            private final ImageView iconView = new ImageView(icon);
            private final ColorAdjust colorAdjust = new ColorAdjust();

            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                iconView.setFitHeight(16);
                iconView.setFitWidth(16);
                colorAdjust.setBrightness(1);
                iconView.setEffect(colorAdjust);
                Button updateButton = new Button("", iconView);
                updateButton.setStyle("-fx-background-color: #F3B95F;");

                if (client == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(actionEvent -> updateUser(client));
            }
        });
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        columnBirthdate.setCellFactory(param -> new TableCell<>(){
            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                if(empty){
                    setText(null);
                }else
                    setText(formatter.format(localDate));
            }
        });
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
    }

    @FXML
    protected void createUser(){
        Client client = new Client();
        client.setName(userName.getText());
        client.setSurname(userSurname.getText());
        client.setSex(toggleGroupSex.getSelectedToggle() == toggleMasculine? "Masculino" : "Feminino");
        client.setBirthdate(userBirthdate.getValue());
        client.setCep(userCep.getText());
        client.setAddress(userAddress.getText());
        client.setCity(userCity.getText());
        client.setState(userState.getText());
        client.setEmail(userEmail.getText());
        try{
            int id = Integer.parseInt(userId.getText());
            client.setId(id);
            clientDAO.update(client);
        }catch (NumberFormatException e){
            clientDAO.save(client);
        }
        resetForm();
    }

    protected void deleteUser(Client client){
        clientDAO.delete(client.getId());
    }

    protected void updateUser(Client client){
        toCreatingPane();
        userId.setText(String.valueOf(client.getId()));
        userName.setText(client.getName());
        userSurname.setText(client.getSurname());
        if(client.getSex().equals("Masculino"))
            toggleGroupSex.selectToggle(toggleMasculine);
        else if(client.getSex().equals("Feminino"))
            toggleGroupSex.selectToggle(toggleFeminine);
        LocalDate birthdate = client.getBirthdate();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedString = birthdate.format(formatter);
        userBirthdate.getEditor().setText(formattedString);
        userBirthdate.setValue(birthdate);
        userCep.setText(client.getCep());
        userAddress.setText(client.getAddress());
        userCity.setText(client.getCity());
        userState.setText(client.getState());
        userEmail.setText(client.getEmail());
    }


    @FXML
    protected void resetForm(){
        userName.clear();
        userSurname.clear();
        toggleGroupSex.selectToggle(null);
        userBirthdate.getEditor().clear();
        userCep.clear();
        userAddress.clear();
        userCity.clear();
        userState.clear();
        userEmail.clear();
    }

    @FXML
    protected void searchByNameOrId(){
        List<Client> clients = clientDAO.findByName(searchingName.getText());
        ObservableList<Client> data = FXCollections.observableList(clients);
        resultTable.setItems(data);
    }

    @FXML
    protected void getAddressByCep(){
        String cepRequest = "https://viacep.com.br/ws/" + userCep.getText().replace("-", "") + "/json/";
        try {
            URL url = URI.create(cepRequest).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream()), "utf-8"));
            StringBuilder json = new StringBuilder();
            String resposta;
            while ((resposta = bufferedReader.readLine()) != null) {
                json.append(resposta);
            }
            Gson gson = new Gson();
            FullAddress address = gson.fromJson(json.toString(), FullAddress.class);
            System.out.println(address);
            userCity.setText(address.getLocalidade());
            userState.setText(address.getUf());
            userAddress.setText(address.getLogradouro());
            userCep.setText(address.getCep());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void toReadingPane(){
        searchByNameOrId();
        readingUserPane.setVisible(true);
        creatingUserPane.setVisible(false);
    }

    @FXML
    protected void toCreatingPane(){
        resetForm();
        userId.clear();
        readingUserPane.setVisible(false);
        creatingUserPane.setVisible(true);
    }
}