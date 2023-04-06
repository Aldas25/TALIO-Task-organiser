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

    /**
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     *
     * Whenever application is asked to stop we stop the thread.
     */
    @Override
    public void start(Stage primaryStage) {
        var listOverview = load(
                CardListOverviewCtrl.class, "client", "scenes", "CardListOverview.fxml"
        );
        var adminListOverview = load (
                AdminCardListOverviewCtrl.class, "client", "scenes", "AdminCardListOverview.fxml"
        );
        var serverLogin = load(
                ServerLoginCtrl.class, "client", "scenes", "ServerLogin.fxml"
        );
        var helpScreen = load (
                HelpScreenCtrl.class, "client", "scenes", "HelpScreen.fxml"
        );
        var boardOverview = load (
                BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml"
        );
        var adminBoardOverview = load(
                AdminBoardOverviewCtrl.class, "client", "scenes", "AdminBoardOverview.fxml");
        var cardDeleteConfirmation = load(
                CardDeleteConfirmationCtrl.class, "client", "scenes", "CardDeleteConfirmation.fxml"
        );
        var cardListDeleteConfirmation = load (
                CardListDeleteConfirmationCtrl.class, "client", "scenes",
                "CardListDeleteConfirmation.fxml");
        var boardDeleteConfirmation = load (
                BoardDeleteConfirmationCtrl.class, "client", "scenes",
                "BoardDeleteConfirmation.fxml");
        var adminBoardDeleteConfirmation = load (
                AdminBoardDeleteConfirmationCtrl.class, "client", "scenes",
                "AdminBoardDeleteConfirmation.fxml");
        var joinBoard = load (
                BoardJoinCtrl.class, "client", "scenes", "JoinBoard.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.loadBoardOverview(boardOverview);
        mainCtrl.loadAdminBoardScene(adminBoardOverview);
        mainCtrl.loadCardDeleteConfirmationScene (cardDeleteConfirmation);
        mainCtrl.loadCardListDeleteConfirmationScene (cardListDeleteConfirmation);
        mainCtrl.loadBoardDeleteConfirmationScene (boardDeleteConfirmation);
        mainCtrl.loadJoinBoardScene(joinBoard);
        mainCtrl.loadAdminBoardDeleteConfirmationScene(adminBoardDeleteConfirmation);
        mainCtrl.loadAdminCardListOverview(adminListOverview);

        mainCtrl.initialize(primaryStage, listOverview, serverLogin, helpScreen);
        primaryStage.setOnCloseRequest(e -> { adminBoardOverview.getKey().stop(); });
    }

    public static <T> Pair<T, Parent> load(Class<T> c, String... parts) {
        return FXML.load(c, parts);
    }

}