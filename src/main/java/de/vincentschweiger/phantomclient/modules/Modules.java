package de.vincentschweiger.phantomclient.modules;

import de.vincentschweiger.phantomclient.modules.impl.ModuleFPS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Modules {

    private static List<UIModule> registeredModules = new ArrayList<>();

    public static void registerModules(UIModule... modules) {
        registeredModules.addAll(Arrays.asList(modules));
    }

    public static List<UIModule> getRegisteredModules() {
        return registeredModules;
    }
}