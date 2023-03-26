package org.example.agents;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Loggers.OperationLogger;
import org.example.Parsing.ParsingCooks;
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
                    System.out.println(cook.cook_name + ": reserved from " + msg.getSender().getLocalName());
                    for (var c : ParsingCooks.cooks) {
                        if (c.cook_id == cook.cook_id) {
                            c.cook_active = false;
                        }
                    }
                    process = (Process)msg.getContentObject();

                    process.oper_started = new Date();
                    process.oper_active = false;

                    System.out.println(cook.cook_name + ": waiting for milliseconds = " + process.oper_time * 100000);

                    myAgent.doWait((int) (process.oper_time * 100000));

                    System.out.println(getLocalName() + ": finished the job");

                    process.oper_ended = new Date();

                    String json = OperationLogger.gson.toJson(process);
                    System.out.println("Logger writen a file");
                    OperationLogger.logger.fine(json);
                    for (var c : ParsingCooks.cooks) {
                        if (c.cook_id == cook.cook_id) {
                            c.cook_active = true;
                        }
                    }
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }
    }
}
