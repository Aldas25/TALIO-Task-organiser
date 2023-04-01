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
package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        var listOverview = load(
                CardListOverviewCtrl.class, "client", "scenes", "CardListOverview.fxml"
        );
        var addCard = load(
                AddCardCtrl.class, "client", "scenes", "AddCard.fxml"
        );
        var serverLogin = load(
                ServerLoginCtrl.class, "client", "scenes", "ServerLogin.fxml"
        );
        var serverSignUp = load (
                ServerSignUpCtrl.class, "client", "scenes", "ServerSignUp.fxml"
        );
        var helpScreen = load (
                HelpScreenCtrl.class, "client", "scenes", "HelpScreen.fxml"
        );
        var boardOverview = load (
                BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml"
        );
        var adminBoardOverview = load(
                AdminBoardOverviewCtrl.class, "client", "scenes", "AdminBoardOverview.fxml"
        );
        var cardDeleteConfirmation = load(
                CardDeleteConfirmationCtrl.class, "client", "scenes", "CardDeleteConfirmation.fxml"
        );
        var cardListDeleteConfirmation = load (
                CardListDeleteConfirmationCtrl.class, "client", "scenes",
                "CardListDeleteConfirmation.fxml"
        );
        var boardDeleteConfirmation = load (
                BoardDeleteConfirmationCtrl.class, "client", "scenes",
                "BoardDeleteConfirmation.fxml"
        );

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.loadBoardOverview(boardOverview);
        mainCtrl.loadAdminBoardScene(adminBoardOverview);
        mainCtrl.loadCardDeleteConfirmationScene (cardDeleteConfirmation);
        mainCtrl.loadCardListDeleteConfirmationScene (cardListDeleteConfirmation);
        mainCtrl.loadBoardDeleteConfirmationScene (boardDeleteConfirmation);

        mainCtrl.initialize(primaryStage, listOverview, addCard, serverLogin, serverSignUp,
                            helpScreen);
    }

    public static <T> Pair<T, Parent> load(Class<T> c, String... parts) {
        return FXML.load(c, parts);
    }

}