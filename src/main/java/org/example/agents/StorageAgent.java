package org.example.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

public class StorageAgent extends Agent {

    private ArrayList<String> nicknamesOfProducts;
    private jade.wrapper.AgentContainer mainContainer;
    @Override
    protected void setup() {
        var args = getArguments();
        System.out.println("StorageAgent " + getName() + " is set");
        mainContainer = (jade.wrapper.AgentContainer) args[0];
        nicknamesOfProducts = new ArrayList<>();
        // TODO: изменить на агрументы переданые при создании.
        for (int i = 0; i < 10; i++) {
            try {
                mainContainer.createNewAgent("product" + i, ProductAgent.class.getName(), null).start();
                nicknamesOfProducts.add("product" + i);
            } catch (StaleProxyException e) {
                throw new RuntimeException(e);
            }
        }
        addBehaviour(new WaitForReceive());
    }

    private class WaitForReceive extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Storage waiting");
            var msg = myAgent.receive();
            if (msg != null) {
                System.out.println("Received reservation for product" + msg.getContent());
                // TODO: отправить сообщение продукту о его резервации в колличестве msg.getContentObject()
                // Название продукта будет в msg.getContent() => найти его в nicknameOfProduct
                for (var prod : nicknamesOfProducts) {
                    if (prod.equals(msg.getContent())) {
                        // найден продукт
                        // TODO: если продукт исчерпан или недоступен и есть еще один такой же продукт нужно взять следующий
                    }
                }
            } else {
                block();
            }
        }
    }
}
