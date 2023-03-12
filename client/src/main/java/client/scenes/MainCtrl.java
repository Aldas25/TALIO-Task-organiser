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

import commons.CardList;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private final ServerUtils server;

    private Stage primaryStage;

    private CardListOverviewCtrl listOverviewCtrl;
    private Scene listOverviewScene;

    private AddCardCtrl addCardCtrl;
    private Scene addCardScene;

    private ServerLoginCtrl serverLoginCtrl;
    private Scene serverLoginScene;

    @Inject
    public MainCtrl(ServerUtils server) {
        this.server = server;
    }

    public void initialize(
            Stage primaryStage,
            Pair<CardListOverviewCtrl, Parent> listOverview,
            Pair<AddCardCtrl, Parent> addCard,
            Pair<ServerLoginCtrl, Parent> serverLogin
    ) {
        this.primaryStage = primaryStage;

        this.listOverviewCtrl = listOverview.getKey();
        this.listOverviewScene = new Scene(listOverview.getValue());

        this.addCardCtrl = addCard.getKey();
        this.addCardScene = new Scene(addCard.getValue());

        this.serverLoginCtrl = serverLogin.getKey();
        this.serverLoginScene = new Scene(serverLogin.getValue());

        showServerLogin();
        primaryStage.show();
    }

    public void showListOverview() {
        primaryStage.setTitle("Card lists: overview");
        primaryStage.setScene(listOverviewScene);
        listOverviewCtrl.refresh();
    }

    public void showAddCard(CardList list) {
        primaryStage.setTitle("Add new card");
        primaryStage.setScene(addCardScene);
        addCardCtrl.setList(list);
    }

    public void showServerLogin() {
        primaryStage.setTitle("Login");
        primaryStage.setScene(serverLoginScene);
        serverLoginCtrl.refresh();
    }

    public void disconnectFromServer() {
        server.setServer(null);
        showServerLogin();
    }
}