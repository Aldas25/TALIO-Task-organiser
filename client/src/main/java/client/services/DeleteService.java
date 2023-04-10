package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;

public class DeleteService {

    private final ServerUtils server;
    private Object objectToDelete;

    /**
     * The constructor of DeleteService
     * @param server Reference to ServerUtils
     */
    @Inject
    public DeleteService (ServerUtils server) {
        this.server = server;
    }

    /**
     * Gets the object that needs to be deleted
     * @return The object to be deleted
     */
    public Object getObjectToDelete() {
        return objectToDelete;
    }

    /**
     * Sets the object that needs to be deleted
     * @param objectToDelete The object to be deleted
     */
    public void setObjectToDelete(Object objectToDelete) {
        this.objectToDelete = objectToDelete;
    }

    /**
     * Deletes selected object
     */
    public void deleteSelectedObject() {
        if (objectToDelete == null)
            return;

        switch (objectToDelete.getClass().getSimpleName()) {
            case "Card":
                server.removeCard((Card) objectToDelete);
                break;
            case "CardList":
                server.removeCardList((CardList) objectToDelete);
                break;
            case "Board":
                server.removeBoard((Board) objectToDelete);
                break;
            default:
                throw new RuntimeException("Unknown objectToDelete class in DeleteService.");
        }

        objectToDelete = null;
    }
}
