package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;
import org.example.Parsing.ParsingDishCard;
import org.example.Parsing.ParsingMenu;
import org.example.Parsing.ParsingStorage;
import org.example.models.DishCard.OperProduct;
import org.example.models.Storage;
import org.example.models.StorageList;
import org.example.models.Visitor.VisOrdDishes;
import org.example.models.Visitor.Visitor;

import java.util.ArrayList;

public class StorageAgent extends Agent {


    private jade.wrapper.AgentContainer mainContainer;
    private ArrayList<Storage> availableProducts;
    private ArrayList<StorageList> allPossibleProducts;

    @Override
    protected void setup() {
        var args = getArguments();
        availableProducts = (ArrayList<Storage>) args[0];
        allPossibleProducts = (ArrayList<StorageList>) args[1];

        addBehaviour(new WaitForReceive());
    }

    private class WaitForReceive extends CyclicBehaviour {
        @Override
        public void action() {
            var msg = myAgent.receive();
            ArrayList<OperProduct> necessaryProducts = new ArrayList<>();
            if (msg != null) {
                try {
                    int dish_card_id = 0;
                    var response = (Visitor) msg.getContentObject();
                    for (var meal : response.vis_ord_dishes) {
                        for (var i : ParsingMenu.dishesInMenu) {
                            if (i.menu_dish_id == meal.menu_dish) {
                                dish_card_id = i.menu_dish_card;
                                break;
                            }
                        }
                        for (var i : ParsingDishCard.dishCards) {
                            if (i.card_id == dish_card_id) {
                                for (var j : i.operations) {
                                    necessaryProducts.addAll(j.oper_products);
                                }
                            }
                        }
                    }
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                for (var product : necessaryProducts) {
                    for (var available : availableProducts) {
                        if (product.prod_type == available.prod_item_type) {
                            if (msg.getSender().getLocalName().equals("Menu")) {
                                if (available.prod_item_quantity - product.prod_quantity <= 0) {
                                    // TODO: отправить меню о недоступности блюда
                                }
                            } else {
                                if (product.prod_type == available.prod_item_type) {
                                    available.prod_item_quantity -= product.prod_quantity;
                                }
                            }
                        }
                    }
                }

            } else {
                block();
            }
        }
    }
}
