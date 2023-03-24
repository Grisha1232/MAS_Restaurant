package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ProductAgent extends Agent {
    int prodTypeId;
    String prodTypeName;
    boolean prodIsFood;
    Double amountOfProduct;


    @Override
    protected void setup() {
        Object[] args = getArguments();
        prodTypeId = (int) args[0];
        prodTypeName = (String) args[1];
        prodIsFood = (boolean) args[2];
        amountOfProduct = (Double) args[3];

        addBehaviour(new WaitUntilReservation());
        System.out.println(getLocalName() + ": " + "has been setup");
    }


    private class WaitUntilReservation extends CyclicBehaviour {
        @Override
        public void action() {
            // ждет сообщения с резервацией определенного колличества
            // если заканчивается кол-во продукта он должен удалиться
            var msg = myAgent.receive();
            if (msg != null) {
                System.out.println("Product: (" + myAgent.getLocalName() + ") received message for reservation");
                try {
                    System.out.println("Product: Reservation for amount: " + (Integer) msg.getContentObject());
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                try {
                    var amount = (Integer) msg.getContentObject();
                    if (amountOfProduct < amount) {
                        // TODO: нужно что-то делать при недопстатке
                        System.out.println("Product: not enough product amount");
                    } else {
                        // TODO: отправить сообщение о резервации
                        amountOfProduct -= amount;
                        // Если не осталось продукта он удаляется (Хотя возможно стоит оставить??)

                    }
                } catch (UnreadableException e) {
                    System.out.println("Do not specified amount of product");
                }
            } else {
                block();
            }
        }
    }
}
