package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;
import org.example.Parsing.ParsingDishCard;
import org.example.Parsing.ParsingMenu;
import org.example.models.DishCard.OperProduct;
import org.example.models.Process;
import org.example.models.Visitor.VisOrdDishes;
import org.example.models.Visitor.Visitor;

import java.io.IOException;
import java.util.ArrayList;

public class OrderAgent extends Agent {
    // Содержит id блюд

    private jade.wrapper.AgentContainer mainContainer;
    private ArrayList<VisOrdDishes> listOfDishes;
    private String nameOfVisitor;

    @Override
    protected void setup() {
        var args = getArguments();
        var vis = (Visitor) args[0];
        listOfDishes = vis.vis_ord_dishes;
        nameOfVisitor = vis.vis_name;

        addBehaviour(new CreateMeals());
    }

    private class CreateMeals extends Behaviour {

        @Override
        public void action() {
            if (!listOfDishes.isEmpty()) {
                var meal = listOfDishes.remove(0);
                System.out.println("Send message to StorageAgent about products for meal(" + meal.menu_dish + ")");
                // TODO: отправка продуктов для резервации.

                // отослали блюда для резервации продуктов.
                var msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("Storage", AID.ISLOCALNAME));
                int dish_card_id = 0;
                for (var i : ParsingMenu.dishesInMenu) {
                    if (i.menu_dish_id == meal.menu_dish) {
                        dish_card_id = i.menu_dish_card;
                        break;
                    }
                }
                ArrayList<OperProduct> necessaryProducts = new ArrayList<>();
                for (var i : ParsingDishCard.dishCards) {
                    if (i.card_id == dish_card_id) {
                        for (var j : i.operations) {
                            necessaryProducts.addAll(j.oper_products);
                        }
                    }
                }
                try {
                    msg.setContentObject(necessaryProducts);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                myAgent.send(msg);
                // создание процесса готовки.
                try {
                    mainContainer.createNewAgent(nameOfVisitor + " " + meal.menu_dish, ProcessAgent.class.getName(),
                            new Object[]{dish_card_id}).start();
                } catch (StaleProxyException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public boolean done() {
            return listOfDishes.isEmpty();
        }
    }


    private class NotifyAboutTime extends Behaviour {
        Integer step = 0;

        @Override
        public void action() {
            // TODO: send message to ask time left from operationAgent (step = 0)
            // TODO: receive message from operationAgent about time left (step = 1)
            // TODO: send message about time left to VisitorAgent (step = 2)
        }

        @Override
        public boolean done() {
            return step == 3;
        }
    }


}
