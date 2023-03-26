package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Pair;
import org.example.Parsing.ParsingDishCard;
import org.example.Parsing.ParsingMenu;
import org.example.models.Process;
import org.example.models.Visitor.VisOrdDishes;
import org.example.models.Visitor.Visitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MenuAgent extends Agent {

    private static int oper_id = 0;
    @Override
    protected void setup() {
        System.out.println("Menu has been setup");
        addBehaviour(new ProcessTheReceivedMessages());
    }


    private class ProcessTheReceivedMessages extends CyclicBehaviour {
        @Override
        public void action() {
            var msg = receive();
            if (msg != null) {
                if (msg.getSender().getLocalName().equals("Storage")) {
                    try {
                        var pair = (Pair<Visitor, ArrayList<Integer>>)msg.getContentObject();
                        var vis = pair.getFirst();
                        var unv = pair.getSecond();
                        // Удаляем блюда, которые не можем приготовить
                        for (var i : unv) {
                            vis.vis_ord_dishes.removeIf((x) -> x.menu_dish == i);
                        }
                        var message = new ACLMessage(ACLMessage.INFORM);
                        message.addReceiver(new AID("Manager", AID.ISLOCALNAME));
                        message.setContentObject(vis);
                        send(message);
                    } catch (UnreadableException | IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (msg.getSender().getLocalName().equals("Manager")) {
                    try {
                        Visitor vis = (Visitor) msg.getContentObject();
                        var message = new ACLMessage(ACLMessage.INFORM);
                        message.addReceiver(new AID("Storage", AID.ISLOCALNAME));
                        message.setContentObject(vis);
                        send(message);
                    } catch (UnreadableException | IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    ArrayList<Process> necessary = new ArrayList<>();
                    try {
                        var response = (VisOrdDishes) msg.getContentObject();
                        for (var menu : ParsingMenu.dishesInMenu) {
                            if (menu.menu_dish_id == response.menu_dish) {
                                for (var dish_card : ParsingDishCard.dishCards) {
                                    for (var oper : dish_card.operations) {
                                        var operation = new Process(oper_id++, 0, dish_card.card_id,
                                                null, null, oper.equip_type, 0,
                                                true, oper.oper_time);
                                        necessary.add(operation);
                                    }
                                }
                            }
                        }
                        var message = new ACLMessage(ACLMessage.INFORM);
                        message.addReceiver(new AID(msg.getSender().getLocalName(), AID.ISLOCALNAME));
                        message.setContentObject(new Pair<>(necessary,response));
                        send(message);
                    } catch (UnreadableException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                block();
            }
        }
    }
}
