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

import client.services.DeleteService;
import client.services.JoinedBoardsService;
import commons.Board;
import commons.Card;
import commons.CardList;
import client.utils.ServerUtils;
import com.google.inject.Inject;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl implements EventHandler<KeyEvent>{

    private final ServerUtils server;
    private final DeleteService deleteService;

    private Stage primaryStage;

    private CardListOverviewCtrl listOverviewCtrl;
    private Scene listOverviewScene;

    private ServerLoginCtrl serverLoginCtrl;
    private Scene serverLoginScene;

    private AdminBoardOverviewCtrl adminBoardOverviewCtrl;
    private Scene adminBoardOverviewScene;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverviewScene;

    private CardTemplateCtrl draggableCardCtrl;
    private ListTemplateCtrl currentDraggedOverListCtrl;
    private int dragNewPosition;

    private HelpScreenCtrl helpScreenCtrl;
    private Scene helpScreenScene;

    private CardDeleteConfirmationCtrl cardDeleteConfirmationCtrl;
    private Scene cardDeleteConfirmationScene;

    private CardListDeleteConfirmationCtrl cardListDeleteConfirmationCtrl;
    private Scene cardListDeleteConfirmationScene;

    private BoardDeleteConfirmationCtrl boardDeleteConfirmationCtrl;
    private Scene boardDeleteConfirmationScene;

    private BoardJoinCtrl boardJoinCtrl;
    private Scene joinBoardScene;

    private AdminCardListOverviewCtrl adminCardListOverviewCtrl;
    private Scene adminCardListOverviewScene;

    private AdminBoardDeleteConfirmationCtrl adminBoardDeleteConfirmationCtrl;
    private Scene adminBoardDeleteConfirmationScene;

    private Stage popUpCardConfirmStage;
    private Stage popUpCardListConfirmStage;
    private Stage popUpBoardConfirmStage;

    private Stage adminPopUpBoardConfirmStage;

    private Stage popUpJoinBoardStage;

    private final JoinedBoardsService joinedBoardsService;

    /**
     * The constructor of this object
     * @param server Reference to ServerUtils
     * @param joinedBoardsService Reference to JoinedBoardsService
     * @param deleteService Reference to DeleteService
     */
    @Inject
    public MainCtrl(ServerUtils server, JoinedBoardsService joinedBoardsService,
                    DeleteService deleteService) {
        this.server = server;
        this.joinedBoardsService = joinedBoardsService;
        this.deleteService = deleteService;
    }

    /**
     * Starts the scene
     * @param primaryStage The primary scene
     * @param listOverview The CardListOverview scene
     * @param serverLogin  The ServerLogin scene
     * @param helpScreen    The HelpScreen scene
     */
    public void initialize(
            Stage primaryStage,
            Pair<CardListOverviewCtrl, Parent> listOverview,
            Pair<ServerLoginCtrl, Parent> serverLogin,
            Pair<HelpScreenCtrl, Parent> helpScreen
    ) {
        this.primaryStage = primaryStage;

        this.listOverviewCtrl = listOverview.getKey();
        this.listOverviewScene = new Scene(listOverview.getValue());

        this.serverLoginCtrl = serverLogin.getKey();
        this.serverLoginScene = new Scene(serverLogin.getValue());

        this.helpScreenCtrl = helpScreen.getKey();
        this.helpScreenScene = new Scene(helpScreen.getValue());

        setKeyShortcuts();
        showServerLogin();
        primaryStage.show();
    }

    public void loadBoardOverview (
        Pair<BoardOverviewCtrl, Parent> boardOverview){

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverviewScene = new Scene (boardOverview.getValue());
    }

    public void loadAdminBoardScene(
            Pair<AdminBoardOverviewCtrl, Parent> adminBoardOverview){

        this.adminBoardOverviewCtrl = adminBoardOverview.getKey();
        this.adminBoardOverviewScene = new Scene(adminBoardOverview.getValue());
    }

    public void loadAdminCardListOverview(
            Pair<AdminCardListOverviewCtrl, Parent> adminCardListOverview){
        this.adminCardListOverviewCtrl = adminCardListOverview.getKey();
        this.adminCardListOverviewScene = new Scene(adminCardListOverview.getValue());
    }

    public void loadCardDeleteConfirmationScene (
            Pair<CardDeleteConfirmationCtrl, Parent> cardDeleteConfirmation) {

        this.cardDeleteConfirmationCtrl = cardDeleteConfirmation.getKey();
        this.cardDeleteConfirmationScene = new Scene (cardDeleteConfirmation.getValue());
    }

    public void loadAdminBoardDeleteConfirmationScene (
            Pair<AdminBoardDeleteConfirmationCtrl, Parent> adminBoardDeleteConfirmation){

        this.adminBoardDeleteConfirmationCtrl = adminBoardDeleteConfirmation.getKey();
        this.adminBoardDeleteConfirmationScene = new Scene(adminBoardDeleteConfirmation.getValue());
    }

    public void loadBoardDeleteConfirmationScene (
            Pair<BoardDeleteConfirmationCtrl, Parent> boardDeleteConfirmation
    ) {
        this.boardDeleteConfirmationCtrl = boardDeleteConfirmation.getKey();
        this.boardDeleteConfirmationScene = new Scene (boardDeleteConfirmation.getValue());
    }

    public void loadCardListDeleteConfirmationScene (
            Pair<CardListDeleteConfirmationCtrl, Parent> cardListDeleteConfirmation) {

        this.cardListDeleteConfirmationCtrl = cardListDeleteConfirmation.getKey();
        this.cardListDeleteConfirmationScene = new Scene(cardListDeleteConfirmation.getValue());
    }

    public void loadJoinBoardScene (
            Pair<BoardJoinCtrl, Parent> joinBoard) {

        this.boardJoinCtrl = joinBoard.getKey();
        this.joinBoardScene = new Scene(joinBoard.getValue());
    }

    public void startClient() {
        server.setSession();
        joinedBoardsService.readJoinedBoardsFromFile();
        listOverviewCtrl.start();
        boardOverviewCtrl.start();
    }

    public void startAdmin() {
        server.setSession();
        adminBoardOverviewCtrl.start();
        adminCardListOverviewCtrl.start();
    }

     /**
     * Every time a key is pressed, go to the handle method
     *      and determine which shortcut it corresponds to.
     * As a note, certain shortcuts only correspond to certain Scenes.
     * All Scenes that should have shortcuts should be represented within this method.
     */
    public void setKeyShortcuts() {
        listOverviewScene.setOnKeyPressed(this);
        boardOverviewScene.setOnKeyPressed(this);
        adminBoardOverviewScene.setOnKeyPressed(this);
    }

    public void showListOverview() {
        primaryStage.setTitle("Card lists: overview");
        primaryStage.setScene(listOverviewScene);
        listOverviewCtrl.refresh();
    }

    public void showAdminListOverview(){
        primaryStage.setTitle("Admin Card List Overview");
        primaryStage.setScene(adminCardListOverviewScene);
        adminCardListOverviewCtrl.refresh();
    }

    public void showServerLogin() {
        primaryStage.setTitle("Login");
        primaryStage.setScene(serverLoginScene);
    }

    public void showAdminBoardOverview(){
        primaryStage.setTitle("Admin Board Overview");
        primaryStage.setScene(adminBoardOverviewScene);
        adminBoardOverviewCtrl.refresh();
    }

    public void showBoardOverview () {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(boardOverviewScene);
        boardOverviewCtrl.refresh();
    }

    public void showCardDeleteConfirmation (Card card) {
        deleteService.setObjectToDelete(card);

        popUpCardConfirmStage = new Stage();
        popUpCardConfirmStage.setScene(cardDeleteConfirmationScene);

        popUpCardConfirmStage.setTitle("Confirm Delete");
        popUpCardConfirmStage.initModality(Modality.APPLICATION_MODAL);

        popUpCardConfirmStage.showAndWait();
    }

    public void closeCardDeleteConfirmation() {
        popUpCardConfirmStage.close();
    }

    public void showCardListDeleteConfirmation (CardList cardList) {
        deleteService.setObjectToDelete(cardList);

        popUpCardListConfirmStage = new Stage();
        popUpCardListConfirmStage.setScene(cardListDeleteConfirmationScene);

        popUpCardListConfirmStage.setTitle("Confirm Delete");
        popUpCardListConfirmStage.initModality(Modality.APPLICATION_MODAL);

        popUpCardListConfirmStage.showAndWait();
    }

    public void closeCardListDeleteConfirmation () {
        popUpCardListConfirmStage.close();
    }

    public void showBoardDeleteConfirmation (Board board) {
        deleteService.setObjectToDelete(board);

        popUpBoardConfirmStage = new Stage();
        popUpBoardConfirmStage.setScene(boardDeleteConfirmationScene);

        popUpBoardConfirmStage.setTitle("Confirm Delete");
        popUpBoardConfirmStage.initModality(Modality.APPLICATION_MODAL);

        popUpBoardConfirmStage.showAndWait();
    }

    public void showAdminBoardDeleteConfirmation (Board board) {
        deleteService.setObjectToDelete(board);

        adminPopUpBoardConfirmStage = new Stage();
        adminPopUpBoardConfirmStage.setScene(adminBoardDeleteConfirmationScene);
        adminPopUpBoardConfirmStage.setTitle("Confirm Delete");
        adminPopUpBoardConfirmStage.initModality(Modality.APPLICATION_MODAL);
        adminPopUpBoardConfirmStage.showAndWait();
    }

    public void closeAdminBoardDeleteConfirmation(){adminPopUpBoardConfirmStage.close();}

    public void closeBoardDeleteConfirmation () {
        popUpBoardConfirmStage.close();
    }

    public void showJoinBoard(){
        popUpJoinBoardStage = new Stage();
        popUpJoinBoardStage.setScene(joinBoardScene);

        popUpJoinBoardStage.setTitle("Join Board");
        popUpJoinBoardStage.initModality(Modality.APPLICATION_MODAL);
        popUpJoinBoardStage.showAndWait();
    }

    public void closeJoinBoard(){popUpJoinBoardStage.close();}

    public void disconnectFromServer() {
        server.setServer(null);
        showServerLogin();
    }

    public CardTemplateCtrl getDraggableCardCtrl() {
        return draggableCardCtrl;
    }

    public void setDraggableCardCtrl(CardTemplateCtrl cardCtrl) {
        this.draggableCardCtrl = cardCtrl;
    }

    public ListTemplateCtrl getCurrentDraggedOverListCtrl() {
        return currentDraggedOverListCtrl;
    }

    public void setCurrentDraggedOverListCtrl(ListTemplateCtrl ctrl) {
        this.currentDraggedOverListCtrl = ctrl;
    }

    /**
     * Check which shortcut corresponds to the key combination
     *      that has just been pressed.
     * It overrides the handle method in the EventHandler interface.
     *
     * @param event the event which occurred
     */
    @Override
    public void handle(KeyEvent event) {
        KeyCombination questionMark = new KeyCharacterCombination("?");

        if (questionMark.match(event)) {
            showHelpScreen();
        }
    }

    /**
     * The method that deals specifically with the "?" shortcut.
     * Creates a new pop-up that contains the Help Screen and stays open
     *      until the user closes the window.
     */
    public void showHelpScreen() {
        Stage popUpHelpStage = new Stage();
        popUpHelpStage.setScene(helpScreenScene);

        popUpHelpStage.setTitle("Help Screen");
        popUpHelpStage.initModality(Modality.APPLICATION_MODAL);

        popUpHelpStage.showAndWait();
    }

    public int getDragNewPosition() {
        return dragNewPosition;
    }

    public void setDragNewPosition(int dragNewPosition) {
        this.dragNewPosition = dragNewPosition;
    }
}