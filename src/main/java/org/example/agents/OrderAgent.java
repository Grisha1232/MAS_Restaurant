package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;

public class OrderAgent extends Agent {
    // Содержит id блюд
    ArrayList<Integer> meals;
    Integer orderID;

    private jade.wrapper.AgentContainer mainContainer;


    @Override
    protected void setup() {
        var args = getArguments();
        this.meals = (ArrayList<Integer>) args[0];
        this.orderID = (Integer) args[1];
        this.mainContainer = (jade.wrapper.AgentContainer) args[2];

        addBehaviour(new CreateMeals());
        System.out.println("Start new order number: " + orderID);
    }

    private class CreateMeals extends Behaviour {

        @Override
        public void action() {
            if (!meals.isEmpty()) {
                var meal = meals.remove(0);
                System.out.println("Send message to StorageAgent about products for meal(" + meal +")");
                // TODO: получение списка продуктов для приготовления блюда из файла
                // TODO: запрос складу о наличии продуктов для данного блюда
                var msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("Storage", AID.ISLOCALNAME));
                String content = "product " + meal.toString();
                Integer i = 1;
                if (meal == 1) {
                    try {
                        msg.setContentObject(new Object[]{content, i});

                    } catch (IOException e) {
                        System.out.println("Something went wrong with setting content for product 1\n" + e.getMessage());
                    }
                } else {
                    try {
                        msg.setContentObject(new Object[]{content, i});
                    } catch (IOException e) {
                        System.out.println("Something went wrong with setting content for product 2\n" + e.getMessage());
                    }
                }
                System.out.println("OrderAgent SEND MESSAGE WITH CONTENT: " + content);
                myAgent.send(msg);
            }
        }

        @Override
        public boolean done() {
            return meals.isEmpty();
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
