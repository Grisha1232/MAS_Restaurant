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
    AID storage;


    @Override
    protected void setup() {
        Object[] args = getArguments();
        prodTypeId = (int) args[0];
        prodTypeName = (String) args[1];
        prodIsFood = (boolean) args[2];
        storage = (AID) args[3];
        amountOfProduct = (Double) args[4];

        addBehaviour(new WaitUntilReservation());
    }


    private class WaitUntilReservation extends CyclicBehaviour {
        @Override
        public void action() {
            // ждет сообщения с резервацией определенного колличества
            // если заканчивается кол-во продукта он должен удалиться
            var msg = myAgent.receive();
            if (msg != null) {
                System.out.println("Product: (" + myAgent.getLocalName() + ") received message for reservation");
                System.out.println("Reservation for amount: " + msg.getContent());
                try {
                    var amount = (Integer) msg.getContentObject();
                    if (amountOfProduct < amount) {
                        // TODO: нужно что-то делать при недопстатке
                    } else {
                        // TODO: отправить сообщение о резервации
                        amountOfProduct -= amount;
                        // Если не осталось продукта он удаляется (Хотя возможно стоит оставить??)
                        if (amountOfProduct <= 0) {
                            myAgent.doDelete();
                        }
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
