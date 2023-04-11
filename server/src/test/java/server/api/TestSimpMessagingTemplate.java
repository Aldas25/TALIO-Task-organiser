package server.api;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

public class TestSimpMessagingTemplate extends SimpMessagingTemplate {
    public List<String> calledMethods = new ArrayList<>();

    public TestSimpMessagingTemplate() {
        super((message, timeout) -> false);
    }

    @Override
    public void convertAndSend(String destination, Object payload) {
        calledMethods.add("convertAndSend " + destination);
    }
}
