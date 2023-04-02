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

    private Stage primaryStage;

    private CardListOverviewCtrl listOverviewCtrl;
    private Scene listOverviewScene;

    private AddCardCtrl addCardCtrl;
    private Scene addCardScene;

    private ServerLoginCtrl serverLoginCtrl;
    private Scene serverLoginScene;

    private ServerSignUpCtrl serverSignUpCtrl;
    private Scene serverSignUpScene;

    private AdminBoardOverviewCtrl adminBoardOverviewCtrl;
    private Scene adminBoardOverviewScene;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverviewScene;

    private CardTemplateCtrl draggableCardCtrl;
    private ListTemplateCtrl currentDraggedOverListCtrl;

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


    private Stage popUpCardConfirmStage;
    private Stage popUpCardListConfirmStage;
    private Stage popUpBoardConfirmStage;

    private Stage popUpJoinBoardStage;

    private Board lastOpenedBoard;


    @Inject
    public MainCtrl(ServerUtils server) {
        this.server = server;
    }


    public void initialize(
            Stage primaryStage,
            Pair<CardListOverviewCtrl, Parent> listOverview,
            Pair<AddCardCtrl, Parent> addCard,
            Pair<ServerLoginCtrl, Parent> serverLogin,
            Pair<ServerSignUpCtrl, Parent> serverSignUp,
            Pair<HelpScreenCtrl, Parent> helpScreen
    ) {
        this.primaryStage = primaryStage;

        this.listOverviewCtrl = listOverview.getKey();
        this.listOverviewScene = new Scene(listOverview.getValue());

        this.addCardCtrl = addCard.getKey();
        this.addCardScene = new Scene(addCard.getValue());

        this.serverLoginCtrl = serverLogin.getKey();
        this.serverLoginScene = new Scene(serverLogin.getValue());

        this.serverSignUpCtrl = serverSignUp.getKey();
        this.serverSignUpScene = new Scene(serverSignUp.getValue());

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

    public void loadCardDeleteConfirmationScene (
            Pair<CardDeleteConfirmationCtrl, Parent> cardDeleteConfirmation) {

        this.cardDeleteConfirmationCtrl = cardDeleteConfirmation.getKey();
        this.cardDeleteConfirmationScene = new Scene (cardDeleteConfirmation.getValue());
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

    public void setLastOpenedBoard(Board board) {
        this.lastOpenedBoard = board;
    }

    public void start() {
        server.setSession();
        listOverviewCtrl.start();
        boardOverviewCtrl.start();
        addCardCtrl.start();
        showBoardOverview();
    }

     /**
     * Every time a key is pressed, go to the handle method
     *      and determine which shortcut it corresponds to.
     *
     * As a note, certain shortcuts only correspond to certain Scenes.
     * All Scenes that should have shortcuts should be represented within this method.
     */
    public void setKeyShortcuts() {
        listOverviewScene.setOnKeyPressed(this);
        addCardScene.setOnKeyPressed(this);
    }

    public void showListOverview() {
        primaryStage.setTitle("Card lists: overview");
        primaryStage.setScene(listOverviewScene);
        listOverviewCtrl.setBoard(lastOpenedBoard);
        listOverviewCtrl.refresh();
    }

    public void showAddCard(CardList list, Board board) {
        primaryStage.setTitle("Add new card");
        primaryStage.setScene(addCardScene);
        addCardCtrl.setList(list);
        addCardCtrl.setBoard(board);
        addCardCtrl.refresh();
    }

    public void showUpdateCard(CardList list, Card card, Board board) {
        primaryStage.setTitle("Edit a card");
        primaryStage.setScene(addCardScene);
        addCardCtrl.setList(list);
        addCardCtrl.setCard(card);
        addCardCtrl.setBoard(board);
        addCardCtrl.refresh();
    }

    public void showServerLogin() {
        primaryStage.setTitle("Login");
        primaryStage.setScene(serverLoginScene);
        // serverLoginCtrl.refresh();
    }

    public void showServerSignUp() {
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(serverSignUpScene);
    }

    public void showAdminBoardOverview(){
        primaryStage.setTitle("Admin Board Overview");
        primaryStage.setScene(adminBoardOverviewScene);
    }

    public void showBoardOverview () {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(boardOverviewScene);
        boardOverviewCtrl.refresh();
    }

    public void showCardDeleteConfirmation (Card card, Board board) {
        cardDeleteConfirmationCtrl.setCardToBeDeleted(card);
        cardDeleteConfirmationCtrl.setBoard(board);

        popUpCardConfirmStage = new Stage();
        popUpCardConfirmStage.setScene(cardDeleteConfirmationScene);

        popUpCardConfirmStage.setTitle("Confirm Delete");
        popUpCardConfirmStage.initModality(Modality.APPLICATION_MODAL);

        popUpCardConfirmStage.showAndWait();
    }

    public void closeCardDeleteConfirmation() {
        popUpCardConfirmStage.close();
    }

    public void showCardListDeleteConfirmation (CardList cardList, Board board) {
        cardListDeleteConfirmationCtrl.setCardListToBeDeleted(cardList);
        cardListDeleteConfirmationCtrl.setBoard(board);

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
        boardDeleteConfirmationCtrl.setBoardToBeDeleted(board);

        popUpBoardConfirmStage = new Stage();
        popUpBoardConfirmStage.setScene(boardDeleteConfirmationScene);

        popUpBoardConfirmStage.setTitle("Confirm Delete");
        popUpBoardConfirmStage.initModality(Modality.APPLICATION_MODAL);

        popUpBoardConfirmStage.showAndWait();
    }

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
     *
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
     *
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
}