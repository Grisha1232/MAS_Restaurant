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

import java.util.ArrayList;

public class StorageAgent extends Agent {


    private jade.wrapper.AgentContainer mainContainer;
    private ArrayList<Storage> availableProducts;
    private ArrayList<StorageList> allPossibleProducts;

    @Override
    protected void setup() {
        var args = getArguments();
        availableProducts = (ArrayList<Storage>) args[0];
        allPossibleProducts = (ArrayList<StorageList>)args[1];

        addBehaviour(new WaitForReceive());
    }

    private class WaitForReceive extends CyclicBehaviour {
        @Override
        public void action() {

            var msg = myAgent.receive();
            if (msg != null && msg.getSender().getLocalName().equals("Menu")) {

            } else if (msg != null) {
                try {
                    var necessaryProducts = (ArrayList<OperProduct>)msg.getContentObject();
                    for (var product : necessaryProducts) {
                        for (var available : availableProducts) {
                            if (product.prod_type == available.prod_item_type) {
                                if (available.prod_item_quantity - product.prod_quantity <= 0) {
                                    // TODO: что делать если не хватает продуктов??
                                } else {
                                    available.prod_item_quantity -= product.prod_quantity;
                                }
                            }
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
