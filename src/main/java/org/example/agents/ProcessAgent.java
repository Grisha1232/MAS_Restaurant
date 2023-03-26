package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Loggers.OperationLogger;
import org.example.Loggers.ProcessLogger;
import org.example.Pair;
import org.example.Parsing.ParsingCooks;
import org.example.Parsing.ParsingEquipment;
import org.example.models.Cooks;
import org.example.models.Operation.OperProc;
import org.example.models.Operation.Operation;
import org.example.models.Process;
import org.example.models.Visitor.VisOrdDishes;
import org.example.models.Visitor.Visitor;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ProcessAgent extends Agent {
    private VisOrdDishes meal;
    private ArrayList<Process> necessaryForDish;
    private static int id = 0;
    private int timeLeft = 0;


    @Override
    protected void setup() {
        meal = (VisOrdDishes) getArguments()[0];
        System.out.println(getLocalName() + ": setup");
        addBehaviour(new AskOperations());
//        addBehaviour(new ReplyOnTimeLeft());
        ++id;
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
                System.out.println("\u001B[32m PROCESS" + getLocalName() + "\u001B[0m" + ": received from " + msg.getSender().getLocalName() + "  content = " + msg.getContent());

                var message = new ACLMessage(ACLMessage.INFORM);
                message.addReceiver(new AID(msg.getSender().getLocalName(), AID.ISLOCALNAME));
                try {
                    Double time = (double) timeLeft;
                    message.setContentObject(time);
                    send(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }
    }

    private class ReceiveDishCard extends Behaviour {
        private boolean isDone = false;

        @Override
        public void action() {
            var msg = receive();
            if (msg != null && msg.getSender().getLocalName().equals("Menu")) {
                try {
                    Date start = new Date();
                    System.out.println(getLocalName() + ": received message");
                    necessaryForDish = ((Pair<ArrayList<Process>, VisOrdDishes>) msg.getContentObject()).getFirst();
                    var visOrDish = ((Pair<ArrayList<Process>, VisOrdDishes>) msg.getContentObject()).getSecond();
                    for (var necessary : necessaryForDish) {
                        necessary.oper_proc = id;
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
                    ArrayList<OperProc> operProcs = new ArrayList<>();
                    int secs = 0;
                    for (var i : necessaryForDish) {
                        operProcs.add(new OperProc(i.oper_id));
                        secs += i.oper_time * 100;
                    }
                    timeLeft = secs;
                    Date end = start;
                    Calendar gcal = new GregorianCalendar();
                    gcal.setTime(end);
                    gcal.add(Calendar.SECOND, secs);
                    end = gcal.getTime();
                    var operation = new Operation(necessaryForDish.get(0).oper_proc, visOrDish.ord_dish_id, start,
                            end, false, operProcs);
                    String json = ProcessLogger.gson.toJson(operation);
                    ProcessLogger.logger.fine(json);
                    isDone = true;
                } catch (UnreadableException | IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return isDone;
        }
    }
}
