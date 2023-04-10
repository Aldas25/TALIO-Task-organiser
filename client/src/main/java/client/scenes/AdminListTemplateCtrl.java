package client.scenes;

import commons.CardList;

public class AdminListTemplateCtrl {

    private CardList list;

    public void start(CardList list){
        setList(list);
    }

    public void setList(CardList list) {
        this.list = list;
    }

    public CardList getList(){
        return list;
    }

}