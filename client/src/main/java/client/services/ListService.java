package client.services;

import client.utils.ServerUtils;
import commons.CardList;

import javax.inject.Inject;


public class ListService {

    public ServerUtils server;


    /**
     * Constructor
     * @param server ServerUtils reference
     */
    @Inject
    public ListService(ServerUtils server){
        this.server = server;
    }

    /**
     * Updates title of CardList
     * @param list the CardList
     * @param title the new title
     */
    public void updateListTitle(CardList list, String title){
        list.title = title;
        server.updateListTitle(list);
    }


}
