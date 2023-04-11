package client.scenes;

import commons.CardList;

public class AdminListTemplateCtrl {

    private CardList list;

    /**
     * Starts a CardList
     * @param list The CardList
     */
    public void start(CardList list){
        setList(list);
    }

    /**
     * Sets list
     * @param list the list
     */
    public void setList(CardList list) {
        this.list = list;
    }

    /**
     * Gets list
     * @return returns list
     */
    public CardList getList(){
        return list;
    }

}