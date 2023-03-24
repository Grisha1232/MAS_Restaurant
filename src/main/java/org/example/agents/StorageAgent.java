package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
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
                mainContainer.createNewAgent("product " + i, ProductAgent.class.getName(), new Object[]{i, "product " + i, true, 10.0}).start();
                nicknamesOfProducts.add("product " + i);
            } catch (StaleProxyException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.print("Storage: ");
        for (var prod : nicknamesOfProducts) {
            System.out.print(prod + " ");
        }
        System.out.println();
        addBehaviour(new WaitForReceive());
    }

    private class WaitForReceive extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Storage: waiting " + nicknamesOfProducts.isEmpty());
            var msg = myAgent.receive();
            if (msg != null) {
                // TODO: отправить сообщение продукту о его резервации в колличестве msg.getContentObject()
                // Название продукта будет в msg.getContent() => найти его в nicknameOfProduct
                Object[] args = {"none", 0};
                try {
                    args = (Object[]) msg.getContentObject();
                } catch (UnreadableException e) {
                    System.out.println("Storage: " + e.getMessage());
                }
                String content = (String) args[0];
                Integer amount = (Integer) args[1];

                // TODO: если продукт исчерпан или недоступен и есть еще один такой же продукт нужно взять следующий
                var sendMsg = new ACLMessage(ACLMessage.INFORM);
                sendMsg.addReceiver(new AID(content, AID.ISLOCALNAME));
                try {
                    sendMsg.setContentObject(amount);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Storage: sending message to " + content);
                myAgent.send(sendMsg);

            } else {
                block();
            }
        }
    }
}
