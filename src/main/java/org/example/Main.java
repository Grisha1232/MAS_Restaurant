package org.example;


import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.example.agents.ProductAgent;

public class Main {
    public static void main(String[] args) throws StaleProxyException {
        final Runtime rt = Runtime.instance();
        final Profile p = new ProfileImpl();

        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "8080");
        p.setParameter(Profile.GUI, "true");
        var r = rt.createMainContainer(p);
        r.createNewAgent("ag", ProductAgent.class.getName(),null ).start();

    }
}