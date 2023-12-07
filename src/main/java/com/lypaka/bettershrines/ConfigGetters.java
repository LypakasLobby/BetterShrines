package com.lypaka.bettershrines;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

public class ConfigGetters {

    public static List<String> shrineFiles;

    public static void load() throws ObjectMappingException {

        shrineFiles = BetterShrines.configManager.getConfigNode(0, "Shrines").getList(TypeToken.of(String.class));

    }

}
