package org.example;


import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.ControllerException;
import org.example.agents.ManagerAgent;
import org.example.agents.VisitorAgent;


public class Main {
    public static void main(String[] args) throws ControllerException {
        final Runtime rt = Runtime.instance();
        final Profile p = new ProfileImpl();

        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "8080");
        p.setParameter(Profile.GUI, "true");
        var r = rt.createMainContainer(p);
        Object[] arg = {r};
        var manager = r.createNewAgent("Manager", ManagerAgent.class.getName(), arg);
        manager.start();
        r.createNewAgent("Visitor1", VisitorAgent.class.getName(), null).start();
        r.createNewAgent("Visitor2", VisitorAgent.class.getName(), null).start();
        r.createNewAgent("Visitor3", VisitorAgent.class.getName(), null).start();
    }
}