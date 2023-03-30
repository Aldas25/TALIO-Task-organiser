package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;

public class TagTemplateCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    private Tag tag;

    @Inject
    public TagTemplateCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }
}
