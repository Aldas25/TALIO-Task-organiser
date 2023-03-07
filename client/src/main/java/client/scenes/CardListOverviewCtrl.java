/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Card;
import commons.CardList;
//import commons.Quote;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.google.inject.Guice.createInjector;


public class CardListOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);


    @FXML
    private HBox listContainer;
    @FXML
    private TextField newListTextField;
    @FXML
    private TextField newCardIDForList;
    @FXML
    private TextField newCardTextField;

//    private ObservableList<Quote> data;

//    @FXML
//    private TableView<Quote> table;
//    @FXML
//    private TableColumn<Quote, String> colFirstName;
//    @FXML
//    private TableColumn<Quote, String> colLastName;
//    @FXML
//    private TableColumn<Quote, String> colQuote;

    @Inject
    public CardListOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        colFirstName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.firstName));
//        colLastName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.lastName));
//        colQuote.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().quote));
    }

//    public void addQuote() {
//        mainCtrl.showAdd();
//    }

    public void refresh() throws IOException {



        listContainer.getChildren().clear();
        listContainer.setSpacing(4);            //spacing between lists

        List<CardList> allLists = server.getCardLists();

        for (CardList cardList : allLists) {

            AnchorPane node = (AnchorPane) FXMLLoader.load(getLocation("client", "scenes", "ListTemplate.fxml"));
            Text text = (Text) node.getChildren().get(0);  //retrieving text from a copy of the file ListTemplate
            text.setText(cardList.title);                  //setting title to new node

            listContainer.getChildren().add(node);          //adding node to children of listContainer
        }
    }

    public void addNewList() throws IOException {
        CardList list = new CardList(newListTextField.getText());
        server.addCardList(list);
        refresh();
    }

    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

    public void addNewCard() throws IOException {
        CardList selectedList = null;
        for (CardList list : server.getCardLists()) {
            if (list.id == Long.parseLong(newCardIDForList.getText()))
                selectedList = list;
        }

        System.out.println("SELECTED LIST: " + selectedList);

        Card card = new Card(selectedList.id, newCardTextField.getText());
        server.addCard(card);

        refresh();
    }
}