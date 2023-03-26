package org.example;


import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.ControllerException;
import org.example.agents.ManagerAgent;
import org.example.agents.MenuAgent;
import org.example.agents.StorageAgent;
import org.example.agents.VisitorAgent;
import org.example.models.Menu;
import org.example.models.Visitor.Visitor;

import java.util.ArrayList;
import java.util.Date;


public class Main {
    public static void main(String[] args) throws ControllerException {
        final Runtime rt = Runtime.instance();
        final Profile p = new ProfileImpl();

        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "8080");
        p.setParameter(Profile.GUI, "true");


        var r = rt.createMainContainer(p);
        // TODO: считывание входных файлов
        // TODO: создание из фходных файлов агентов: Меню, Склад, Поваров, Обродудования, Посетителей
        r.createNewAgent("Storage", StorageAgent.class.getName(), new Object[]{r}).start();
        r.createNewAgent("Manager", ManagerAgent.class.getName(), new Object[]{r}).start();
        r.createNewAgent("Visitor1", VisitorAgent.class.getName(), new Object[] {new Visitor("Visitor1", new Date(), null, 0, new ArrayList<>())}).start();
        r.createNewAgent("Visitor2", VisitorAgent.class.getName(), new Object[] {new Visitor("Visitor2", new Date(), null, 0, new ArrayList<>())}).start();
        r.createNewAgent("Visitor3", VisitorAgent.class.getName(), new Object[] {new Visitor("Visitor3", new Date(), null, 0, new ArrayList<>())}).start();
    }
}