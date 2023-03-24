package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;

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
                System.out.println("Need to send message to StorageAgent about products " + meal);
                // TODO: запрос складу о наличии продуктов для данного блюда
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
