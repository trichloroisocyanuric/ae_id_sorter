package com.example.ae_id_sorter;

import me.ramidzkh.mekae2.ae2.MekanismKey;

import static mekanism.api.MekanismAPI.CHEMICAL_REGISTRY;

public class RegistryMekSorter {
    public static void registry() {
        IdSorter.registry(MekanismKey.class, (chemical) -> CHEMICAL_REGISTRY.getId(chemical.getStack().getChemical()));
    }
}
