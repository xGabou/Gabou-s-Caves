package com.github.alexmodguy.alexscaves.server.config;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.BiomeSourceAccessor;
import com.github.alexthe666.citadel.Citadel;
import com.google.common.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class BiomeContentConfig {

    private static final String DEFAULT_DIMENSION = "minecraft:overworld";
    private static final LinkedHashMap<ResourceKey<Biome>, BiomeContentEntry> DEFAULTS = new LinkedHashMap<>();
    public static final LinkedHashMap<ResourceKey<Biome>, BiomeContentEntry> BIOME_CONTENT = new LinkedHashMap<>();

    static {
        DEFAULTS.put(ACBiomeRegistry.MAGNETIC_CAVES, new BiomeContentEntry(
                "alexscaves:magnetic_caves",
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of("alexscaves:notor", "alexscaves:magnetron", "alexscaves:teletor", "alexscaves:boundroid", "alexscaves:ferrouslime"),
                List.of("alexscaves:galena_iron_ore", "alexscaves:metal_swarf"),
                List.of("alexscaves:ferrocave")
        ));
        DEFAULTS.put(ACBiomeRegistry.PRIMORDIAL_CAVES, new BiomeContentEntry(
                "alexscaves:primordial_caves",
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of("alexscaves:trilocaris", "alexscaves:subterranodon", "alexscaves:vallumraptor", "alexscaves:grottoceratops", "alexscaves:tremorsaurus", "alexscaves:relicheirus", "alexscaves:atlatitan"),
                List.of(),
                List.of("alexscaves:volcano", "alexscaves:dino_bowl")
        ));
        DEFAULTS.put(ACBiomeRegistry.TOXIC_CAVES, new BiomeContentEntry(
                "alexscaves:toxic_caves",
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of("alexscaves:gammaroach", "alexscaves:raycat", "alexscaves:brainiac", "alexscaves:nucleeper", "alexscaves:radgill"),
                List.of("alexscaves:metal_swarf_replace_mud", "alexscaves:unrefined_waste_replace_mud", "alexscaves:radrock_uranium_ore"),
                List.of("alexscaves:acid_pit")
        ));
        DEFAULTS.put(ACBiomeRegistry.ABYSSAL_CHASM, new BiomeContentEntry(
                "alexscaves:abyssal_chasm",
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of("alexscaves:deep_one_mage", "alexscaves:deep_one_knight", "alexscaves:deep_one", "alexscaves:hullbreaker", "alexscaves:lanternfish", "alexscaves:tripodfish", "alexscaves:sea_pig", "alexscaves:gossamer_worm"),
                List.of(),
                List.of("alexscaves:ocean_trench", "alexscaves:abyssal_ruins")
        ));
        DEFAULTS.put(ACBiomeRegistry.FORLORN_HOLLOWS, new BiomeContentEntry(
                "alexscaves:forlorn_hollows",
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of("alexscaves:gloomoth", "alexscaves:underzealot", "alexscaves:corrodent", "alexscaves:vesper"),
                List.of("alexscaves:coprolith_coal_ore", "alexscaves:guanostone_redstone_ore"),
                List.of("alexscaves:forlorn_canyon", "alexscaves:forlorn_bridge")
        ));
        DEFAULTS.put(ACBiomeRegistry.CANDY_CAVITY, new BiomeContentEntry(
                "alexscaves:candy_cavity",
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of(DEFAULT_DIMENSION),
                List.of("alexscaves:caniac", "alexscaves:gumbeeper", "alexscaves:gum_worm", "alexscaves:caramel_cube", "alexscaves:sweetish_fish", "alexscaves:candicorn", "alexscaves:gummy_bear"),
                List.of(),
                List.of("alexscaves:cake_cave", "alexscaves:soda_bottle", "alexscaves:donut_arch", "alexscaves:licowitch_tower", "alexscaves:gingerbread_town")
        ));
    }

    public static void reloadConfig() {
        BIOME_CONTENT.clear();
        for (Map.Entry<ResourceKey<Biome>, BiomeContentEntry> entry : DEFAULTS.entrySet()) {
            String fileName = entry.getKey().location().getPath();
            BIOME_CONTENT.put(entry.getKey(), getConfigData(fileName, entry.getValue()));
        }
    }

    public static boolean isMobSpawnAllowed(LevelAccessor level, BlockPos pos, EntityType<?> entityType) {
        if (entityType == null) {
            return true;
        }
        ResourceKey<Biome> biomeKey = getBiomeKey(level, pos);
        if (biomeKey == null) {
            return true;
        }
        BiomeContentEntry config = BIOME_CONTENT.get(biomeKey);
        if (config == null || !config.mobs.contains(toStringId(entityType))) {
            return true;
        }
        ResourceLocation dimensionId = getDimensionId(level);
        return dimensionId == null || config.mobSpawnDimensions.contains(toStringId(dimensionId));
    }

    public static boolean isBiomeMobSpawnAllowed(LevelAccessor level, BlockPos pos) {
        ResourceKey<Biome> biomeKey = getBiomeKey(level, pos);
        if (biomeKey == null) {
            return true;
        }
        BiomeContentEntry config = BIOME_CONTENT.get(biomeKey);
        ResourceLocation dimensionId = getDimensionId(level);
        return config == null || dimensionId == null || config.mobSpawnDimensions.contains(toStringId(dimensionId));
    }

    public static boolean isOreFeatureAllowed(WorldGenLevel level, BlockPos pos, @Nullable ResourceLocation featureId) {
        if (featureId == null) {
            return true;
        }
        ResourceKey<Biome> biomeKey = getBiomeKey(level, pos);
        if (biomeKey == null) {
            return true;
        }
        BiomeContentEntry config = BIOME_CONTENT.get(biomeKey);
        if (config == null || !config.oreFeatures.contains(toStringId(featureId))) {
            return true;
        }
        return config.oreFeatureDimensions.contains(toStringId(level.getLevel().dimension().location()));
    }

    public static boolean isStructureAllowed(BiomeSource biomeSource, ResourceKey<Biome> biomeKey, @Nullable ResourceLocation structureId) {
        if (structureId == null) {
            return true;
        }
        BiomeContentEntry config = BIOME_CONTENT.get(biomeKey);
        if (config == null || !config.structures.contains(toStringId(structureId))) {
            return true;
        }
        if (!(biomeSource instanceof BiomeSourceAccessor accessor)) {
            return true;
        }
        ResourceLocation dimensionId = accessor.getAssignedDimension();
        return dimensionId == null || config.structureDimensions.contains(toStringId(dimensionId));
    }

    @Nullable
    public static ResourceKey<Biome> getBiomeKey(LevelAccessor level, BlockPos pos) {
        return level.getBiome(pos).unwrapKey().orElse(null);
    }

    private static String toStringId(ResourceLocation id) {
        return id.toString();
    }

    @Nullable
    private static ResourceLocation getDimensionId(LevelAccessor level) {
        if (level instanceof Level actualLevel) {
            return actualLevel.dimension().location();
        }
        if (level instanceof ServerLevelAccessor serverLevelAccessor) {
            return serverLevelAccessor.getLevel().dimension().location();
        }
        return null;
    }

    @Nullable
    private static String toStringId(EntityType<?> entityType) {
        ResourceLocation id = EntityType.getKey(entityType);
        return id == null ? null : id.toString();
    }

    private static BiomeContentEntry getConfigData(String fileName, BiomeContentEntry defaultConfigData) {
        return getOrCreateConfigFile(getConfigDirectory(), fileName, defaultConfigData, new TypeToken<BiomeContentEntry>() {
        }.getType(), BiomeContentEntry::isInvalid);
    }

    private static <T> T getOrCreateConfigFile(File configDir, String configName, T defaults, Type type, Predicate<T> isInvalid) {
        File configFile = new File(configDir, configName + ".json");
        if (!configDir.exists() && !configDir.mkdirs()) {
            Citadel.LOGGER.error("Biome Content Config: Could not create {}", configDir);
        }
        if (!configFile.exists()) {
            try {
                FileUtils.write(configFile, BiomeGenerationConfig.GSON.toJson(defaults));
            } catch (IOException e) {
                Citadel.LOGGER.error("Biome Content Config: Could not write " + configFile, e);
            }
        }
        try {
            T found = BiomeGenerationConfig.GSON.fromJson(FileUtils.readFileToString(configFile), type);
            if (isInvalid.test(found)) {
                Citadel.LOGGER.warn("Biome Content Config: Invalid format found for {}, rewriting defaults.", configName);
                FileUtils.write(configFile, BiomeGenerationConfig.GSON.toJson(defaults));
            } else {
                return found;
            }
        } catch (Exception e) {
            Citadel.LOGGER.error("Biome Content Config: Could not load " + configFile, e);
        }
        return defaults;
    }

    private static File getConfigDirectory() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path jsonPath = Paths.get(configPath.toAbsolutePath().toString(), "alexscaves_biome_content");
        return jsonPath.toFile();
    }

    public static class BiomeContentEntry {
        public String biome;
        public List<String> mobSpawnDimensions = new ArrayList<>();
        public List<String> oreFeatureDimensions = new ArrayList<>();
        public List<String> structureDimensions = new ArrayList<>();
        public List<String> mobs = new ArrayList<>();
        public List<String> oreFeatures = new ArrayList<>();
        public List<String> structures = new ArrayList<>();

        public BiomeContentEntry() {
        }

        public BiomeContentEntry(String biome, List<String> mobSpawnDimensions, List<String> oreFeatureDimensions, List<String> structureDimensions, List<String> mobs, List<String> oreFeatures, List<String> structures) {
            this.biome = biome;
            this.mobSpawnDimensions = new ArrayList<>(mobSpawnDimensions);
            this.oreFeatureDimensions = new ArrayList<>(oreFeatureDimensions);
            this.structureDimensions = new ArrayList<>(structureDimensions);
            this.mobs = new ArrayList<>(mobs);
            this.oreFeatures = new ArrayList<>(oreFeatures);
            this.structures = new ArrayList<>(structures);
        }

        public boolean isInvalid() {
            return biome == null || mobSpawnDimensions == null || oreFeatureDimensions == null || structureDimensions == null || mobs == null || oreFeatures == null || structures == null;
        }
    }
}
