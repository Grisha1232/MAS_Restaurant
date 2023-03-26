package org.example;


import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.ControllerException;
import org.example.Parsing.*;
import org.example.agents.*;
import org.example.models.StorageList;
import org.example.models.Visitor.Visitor;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


public class Main {
    public static void main(String[] args) throws ControllerException, ParseException {
        final Runtime rt = Runtime.instance();
        final Profile p = new ProfileImpl();

        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "8080");
        p.setParameter(Profile.GUI, "true");


        var r = rt.createMainContainer(p);

        ParsingCooks.getCooks(new File("").getAbsolutePath() + "/src/main/java/org/example/input/cookers.txt");
        for (var cook : ParsingCooks.cooks) {
            r.createNewAgent(cook.cook_name, CookAgent.class.getName(), new Object[] {cook}).start();
        }

        ParsingEquipment.getDishesInMenu(new File("").getAbsolutePath() + "/src/main/java/org/example/input/equipment.txt");
        for (var eq : ParsingEquipment.equipments) {
            r.createNewAgent(eq.equip_name, EquipmentAgent.class.getName(), new Object[] {eq}).start();
        }

        ParsingMenu.getDishesInMenu(new File("").getAbsolutePath() + "/src/main/java/org/example/input/menu_dishes.txt");
        ParsingDishCard.getDishCards(new File("").getAbsolutePath() + "/src/main/java/org/example/input/dish_cards.txt");
        r.createNewAgent("Menu", MenuAgent.class.getName(), null).start();

        ParsingVisitor.getVisitors(new File("").getAbsolutePath() + "/src/main/java/org/example/input/visitor_order.txt");
        for (var vis : ParsingVisitor.visitors) {
            r.createNewAgent(vis.vis_name, VisitorAgent.class.getName(), new Object[]{vis}).start();
        }
        ParsingStorage.getStorageModelList(new File("").getAbsolutePath() + "/src/main/java/org/example/input/products.txt");
        r.createNewAgent("Storage", StorageAgent.class.getName(), new Object[]{ParsingStorage.storage}).start();
        r.createNewAgent("Manager", ManagerAgent.class.getName(), new Object[] {r}).start();
        // TODO: считывание входных файлов
        // TODO: создание из фходных файлов агентов: Меню, Склад, Поваров, Обродудования, Посетителей
    }
}