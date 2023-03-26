package org.example.agents;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Loggers.OperationLogger;
import org.example.models.Cooks;
import org.example.models.Process;

import java.util.Date;

public class CookAgent extends Agent {
    Process process;
    Cooks cook;

    @Override
    protected void setup() {
        cook = (Cooks) getArguments()[0];
        System.out.println(cook.cook_name + ": setup");
        addBehaviour(new ReserveCook());
    }

    private class ReserveCook extends CyclicBehaviour {

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {
                try {
                    process = (Process)msg.getContentObject();

                    process.oper_started = new Date();

                    myAgent.wait((long) process.oper_time * 60);
                    var message = new ACLMessage(ACLMessage.INFORM);
                    message.addReceiver(new AID(msg.getSender().getLocalName(), AID.ISLOCALNAME));
                    message.setContent("done");

                    process.oper_ended = new Date();

                    String json = OperationLogger.gson.toJson(process);
                    OperationLogger.logger.fine(json);

                    send(message);
                } catch (UnreadableException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }
    }
}
