package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Pair;
import org.example.Parsing.ParsingDishCard;
import org.example.Parsing.ParsingMenu;
import org.example.models.DishCard.OperProduct;
import org.example.models.Storage;
import org.example.models.StorageList;
import org.example.models.Visitor.Visitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageAgent extends Agent {


    private jade.wrapper.AgentContainer mainContainer;
    private ArrayList<Storage> availableProducts;
//    private ArrayList<StorageList> allPossibleProducts;

    @Override
    protected void setup() {
        var args = getArguments();
        availableProducts = (ArrayList<Storage>) args[0];
//        allPossibleProducts = (ArrayList<StorageList>) args[1];
        System.out.println("Storage has been set");
        addBehaviour(new WaitForReceive());
    }

    private class WaitForReceive extends CyclicBehaviour {
        @Override
        public void action() {
            var msg = myAgent.receive();
            Map<Integer, ArrayList<OperProduct>> dishes = new HashMap<>();
            if (msg != null) {
                try {
                    var response = (Visitor) msg.getContentObject();
                    Boolean flag = false;
                    // Проходимся по всем блюдам пользователя
                    for (var meal : response.vis_ord_dishes) {
                        // Проходимся по всем блюдам в меню
                        for (var i : ParsingMenu.dishesInMenu) {
                            // Если id совпали то
                            if (i.menu_dish_id == meal.menu_dish) {
                                // Проходимся по карточкам
                                for (var k : ParsingDishCard.dishCards) {
                                    // Если совпали id карточек то
                                    if (k.card_id == i.menu_dish_card) {
                                        // Проходимся по операциям
                                        for (var j : k.operations) {
                                            // добавляем в мапу
                                            dishes.put(i.menu_dish_card, j.oper_products);
                                        }
                                    }
                                }
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            break;
                        }
                    }

                    ArrayList<Integer> unavailable = new ArrayList<>();
                    for (var entry : dishes.entrySet()) {
                        for (var product : entry.getValue()) {
                            for (var available : availableProducts) {
                                if (product.prod_type == available.prod_item_type) {
                                    if (msg.getSender().getLocalName().equals("Menu")) {
                                        if (available.prod_item_quantity - product.prod_quantity <= 0) {
                                            // Добавляем в недоступные menu_dish_card (menu_dish)
                                            unavailable.add(entry.getKey());
                                        }
                                    } else {
                                        if (product.prod_type == available.prod_item_type) {
                                            available.prod_item_quantity -= product.prod_quantity;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (msg.getSender().getLocalName().equals("Menu")) {
                        var message = new ACLMessage(ACLMessage.INFORM);
                        message.addReceiver(new AID("Menu", AID.ISLOCALNAME));
                        try {
                            message.setContentObject(new Pair<>(response, unavailable));
                            send(message);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
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
