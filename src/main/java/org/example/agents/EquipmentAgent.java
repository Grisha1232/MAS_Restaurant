package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.models.KitchenEquipment;
import org.example.models.Process;

import java.util.Date;

public class EquipmentAgent extends Agent {
    Process proc;

    KitchenEquipment equipment;
    @Override
    protected void setup() {
        equipment = (KitchenEquipment) getArguments()[0];
        System.out.println(equipment.equip_name + ": setup");
        addBehaviour(new ReserveEquipment());
    }

    private class ReserveEquipment extends CyclicBehaviour {

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {
                try {
                    proc = (Process) msg.getContentObject();
                    proc.oper_started = new Date();
                    myAgent.wait((long)proc.oper_time * 60);
                    var message = new ACLMessage(ACLMessage.INFORM);
                    message.addReceiver(new AID(msg.getSender().getLocalName(), AID.ISLOCALNAME));
                    message.setContent("done");
                    proc.oper_ended = new Date();
                    send(message);
                } catch (UnreadableException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }
    }

}
