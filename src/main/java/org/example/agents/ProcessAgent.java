package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Parsing.ParsingCooks;
import org.example.Parsing.ParsingEquipment;
import org.example.models.Process;
import org.example.models.Visitor.VisOrdDishes;

import java.io.IOException;
import java.util.ArrayList;

public class ProcessAgent extends Agent {
    private VisOrdDishes meal;
    private ArrayList<Process> necessaryForDish;

    private Double timeLeft;

    @Override
    protected void setup() {
        meal = (VisOrdDishes) getArguments()[0];
        addBehaviour(new AskOperations());
    }


    private class AskOperations extends OneShotBehaviour {
        @Override
        public void action() {
            var msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Menu", AID.ISLOCALNAME));
            try {
                msg.setContentObject(meal);
                send(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            addBehaviour(new ReceiveDishCard());
        }
    }

    private class ReplyOnTimeLeft extends CyclicBehaviour {
        @Override
        public void action() {
            var msg = receive();
            if (msg != null) {

            }
        }
    }

    private class ReceiveDishCard extends Behaviour {
        @Override
        public void action() {
            var msg = receive();
            if (msg != null) {
                try {
                    necessaryForDish = (ArrayList<Process>) msg.getContentObject();
                    for (var necessary : necessaryForDish) {
                        for (var i : ParsingCooks.cooks) {
                            if (i.cook_id == necessary.oper_coocker_id) {
                                var messageToReserve = new ACLMessage(ACLMessage.INFORM);
                                messageToReserve.addReceiver(new AID(i.cook_name, AID.ISLOCALNAME));
                                messageToReserve.setContentObject(necessary.oper_time);
                                send(messageToReserve);
                            }
                        }
                        for (var i : ParsingEquipment.equipments) {
                            if (i.equip_id == necessary.oper_equip_id) {
                                var messageToReserve = new ACLMessage(ACLMessage.INFORM);
                                messageToReserve.addReceiver(new AID(i.equip_name, AID.ISLOCALNAME));
                                messageToReserve.setContentObject(necessary.oper_time);
                                send(messageToReserve);
                            }
                        }
                    }
                } catch (UnreadableException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return necessaryForDish != null;
        }
    }
}
