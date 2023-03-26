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
import org.example.models.Cooks;
import org.example.models.Process;
import org.example.models.Visitor.VisOrdDishes;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProcessAgent extends Agent {
    private VisOrdDishes meal;
    private ArrayList<Process> necessaryForDish;


    @Override
    protected void setup() {
        meal = (VisOrdDishes) getArguments()[0];
        System.out.println(getLocalName() + ": setup");
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
                System.out.println(getLocalName() + ": sent message to Menu");
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
                    System.out.println(getLocalName() + ": received message");
                    necessaryForDish = (ArrayList<Process>) msg.getContentObject();
                    for (var necessary : necessaryForDish) {
                        for (var i : ParsingEquipment.equipments) {
                            if (i.equip_type == necessary.oper_equip_id) {
                                var messageToReserveEq = new ACLMessage(ACLMessage.INFORM);
                                messageToReserveEq.addReceiver(new AID(i.equip_name, AID.ISLOCALNAME));
                                necessary.oper_equip_id = i.equip_id;
                                messageToReserveEq.setContentObject(necessary);
                                send(messageToReserveEq);
                            }
                        }

                        var messageToReserve = new ACLMessage(ACLMessage.INFORM);
                        Cooks cook = null;
                        System.out.println(getLocalName() + ": finding the cook");
                        while (cook == null) {
                            for (var c : ParsingCooks.cooks) {
                                System.out.println(getLocalName() + ": " + c.cook_name + " " + c.cook_active);
                                if (c.cook_active) {
                                    c.cook_active = false;
                                    cook = c;

                                    necessary.oper_coocker_id = cook.cook_id;
                                    System.out.println(getLocalName() + ": trying to reserve cook named = " + cook.cook_name);
                                    messageToReserve.addReceiver(new AID(cook.cook_name, AID.ISLOCALNAME));
                                    messageToReserve.setContentObject(necessary);
                                    send(messageToReserve);
                                    break;
                                }
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
