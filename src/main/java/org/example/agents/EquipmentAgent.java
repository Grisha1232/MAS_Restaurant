package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Loggers.OperationLogger;
import org.example.Parsing.ParsingCooks;
import org.example.Parsing.ParsingEquipment;
import org.example.models.KitchenEquipment;
import org.example.models.Process;

import java.util.Date;

public class EquipmentAgent extends Agent {
    Process process;

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
                    System.out.println(equipment.equip_name + ": reserved from " + msg.getSender().getLocalName());
                    for (var c : ParsingEquipment.equipments) {
                        if (c.equip_id == equipment.equip_id) {
                            c.equip_active = false;
                        }
                    }
                    process = (Process) msg.getContentObject();

                    System.out.println(getLocalName() + ": finished the job");

                    for (var c : ParsingEquipment.equipments) {
                        if (c.equip_id == equipment.equip_id) {
                            c.equip_active = true;
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
