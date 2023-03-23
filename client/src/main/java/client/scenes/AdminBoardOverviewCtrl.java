package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class AdminBoardOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public AdminBoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    public void disconnectFromServer(){
        mainCtrl.disconnectFromServer();
    }
}
