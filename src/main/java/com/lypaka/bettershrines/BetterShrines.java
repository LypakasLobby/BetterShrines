package com.lypaka.bettershrines;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettershrines.ShrineRegistry.Shrine;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("bettershrines")
public class BetterShrines {

    public static final String MOD_ID = "bettershrines";
    public static final String MOD_NAME = "BetterShrines";
    public static Logger logger = LogManager.getLogger(MOD_NAME);
    public static BasicConfigManager configManager;
    public static Map<String, Shrine> shrineMap = new HashMap<>();

    public BetterShrines() throws IOException, ObjectMappingException {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/bettershrines"));
        String[] files = new String[]{"bettershrines.conf"};
        configManager = new BasicConfigManager(files, dir, BetterShrines.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        ConfigGetters.load();
        loadShrines();

    }

    public static void loadShrines() throws ObjectMappingException, IOException {

        shrineMap = new HashMap<>();
        String[] file = new String[]{"shrine.conf"};
        for (int i = 0; i < ConfigGetters.shrineFiles.size(); i++) {

            Path shrineDir = ConfigUtils.checkDir(Paths.get("./config/bettershrines/" + ConfigGetters.shrineFiles.get(i)));
            BasicConfigManager bcm = new BasicConfigManager(file, shrineDir, BetterShrines.class, MOD_NAME, MOD_ID, logger);
            bcm.init();
            String mode = bcm.getConfigNode(0, "Basic-Settings", "Mode").getString();
            List<String> triggerExecution = bcm.getConfigNode(0, "Basic-Settings", "Trigger-Execution").getList(TypeToken.of(String.class));
            List<String> triggerLocations = bcm.getConfigNode(0, "Basic-Settings", "Trigger-Locations").getList(TypeToken.of(String.class));
            String triggerType = bcm.getConfigNode(0, "Basic-Settings", "Trigger-Type").getString();
            int maxActivationAmount = bcm.getConfigNode(0, "Max-Activation-Amount").getInt();
            Map<String, String> requirementsMap = bcm.getConfigNode(0, "Requirements").getValue(new TypeToken<Map<String, String>>() {});
            Shrine shrine = new Shrine(ConfigGetters.shrineFiles.get(i), bcm, mode, triggerExecution, triggerLocations, triggerType, maxActivationAmount, requirementsMap);
            shrine.create();

        }

    }

}
