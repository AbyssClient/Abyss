package de.vincentschweiger.phantomclient.modules;

import de.vincentschweiger.phantomclient.modules.impl.ModuleFPS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Modules {

    public static UIModule moduleFPS = new ModuleFPS();

    private static List<UIModule> registeredModules = new ArrayList<>();

    public static void registerModules(UIModule... modules) {
        registeredModules.addAll(Arrays.asList(modules));
    }

    public static List<UIModule> getRegisteredModules() {
        return registeredModules;
    }
}