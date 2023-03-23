package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import java.util.Date;
import java.util.Deque;

public class MealsBeveragesAgent extends Agent {


    @Override
    protected void setup() {
        // TODO: get from arguments what need to cook
    }

    private class CookMeal extends Behaviour {

        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
