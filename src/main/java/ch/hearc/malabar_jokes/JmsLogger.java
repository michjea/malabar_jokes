package ch.hearc.malabar_jokes;

import java.lang.System.Logger;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import ch.hearc.malabar_jokes.jokes.model.Log;

@Component
public class JmsLogger {

    @Autowired
    JmsTemplate jmsTemplate;

    public void log(Object log) {
        jmsTemplate.convertAndSend("json-q", log);
    }

}
