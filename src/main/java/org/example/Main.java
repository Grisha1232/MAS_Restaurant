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
        ArrayList<Menu> menus = new ArrayList<>();
        menus.add(new Menu(0, 0, 20, true));
        menus.add(new Menu(1, 1, 50, true));
        menus.add(new Menu(2, 2, 100, true));
        menus.add(new Menu(3, 3, 100, true));
        menus.add(new Menu(4, 4, 100, true));
        menus.add(new Menu(5, 5, 100, true));

        var r = rt.createMainContainer(p);
        // TODO: считывание входных файлов
        // TODO: Содание StorageAgent из введенных данных (заменить null на параметры для Storage)
        r.createNewAgent("Storage", StorageAgent.class.getName(), new Object[]{r}).start();
        r.createNewAgent("Menu", MenuAgent.class.getName(), new Object[]{menus}).start();
        r.createNewAgent("Manager", ManagerAgent.class.getName(), new Object[]{r}).start();
        r.createNewAgent("Visitor1", VisitorAgent.class.getName(), new Object[] {new Visitor("Visitor1", new Date(), null, 0, new ArrayList<>())}).start();
        r.createNewAgent("Visitor2", VisitorAgent.class.getName(), new Object[] {new Visitor("Visitor2", new Date(), null, 0, new ArrayList<>())}).start();
        r.createNewAgent("Visitor3", VisitorAgent.class.getName(), new Object[] {new Visitor("Visitor3", new Date(), null, 0, new ArrayList<>())}).start();
    }
}