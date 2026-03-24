# Disable Mobs Analysis

## Scope

This document maps the parts of the mod that control custom mobs, their spawning, mob-linked items, and mob-tied data. It also records the concrete disable strategy used for this repository so the change is easy to audit and reverse.

## Relevant Project Map

### Bootstrap and registry entry points

- `src/main/java/com/github/alexmodguy/alexscaves/AlexsCaves.java`
  - Main mod bootstrap.
  - Registers `ACEntityRegistry`, `ACItemRegistry`, `ACBlockRegistry`, feature registries, loot registries, config, and event listeners.
- `src/main/java/com/github/alexmodguy/alexscaves/server/entity/ACEntityRegistry.java`
  - Registers all custom entity types.
  - Creates custom mob categories `alexscaves:cave_creature` and `alexscaves:deep_sea_creature`.
  - Registers attribute builders and spawn placement predicates for custom mobs.
- `src/main/java/com/github/alexmodguy/alexscaves/server/item/ACItemRegistry.java`
  - Registers all mod items.
  - Registers all spawn eggs in the static `spawnEgg(...)` block.
  - Registers fish buckets and other mob-adjacent items.
- `src/main/java/com/github/alexmodguy/alexscaves/server/misc/ACCreativeTabRegistry.java`
  - Adds spawn eggs, fish buckets, egg blocks, and other mob-tied content to creative tabs.

### Custom mob classes

- `src/main/java/com/github/alexmodguy/alexscaves/server/entity/living/*.java`
  - Contains all living mob implementations and their mob-specific AI, drops, spawn rules, and special behavior.
  - Includes the custom mobs that were targeted for disablement:
    - `TeletorEntity`
    - `MagnetronEntity`
    - `BoundroidEntity`
    - `BoundroidWinchEntity`
    - `FerrouslimeEntity`
    - `NotorEntity`
    - `SubterranodonEntity`
    - `VallumraptorEntity`
    - `GrottoceratopsEntity`
    - `TrilocarisEntity`
    - `TremorsaurusEntity`
    - `RelicheirusEntity`
    - `LuxtructosaurusEntity`
    - `AtlatitanEntity`
    - `NucleeperEntity`
    - `RadgillEntity`
    - `BrainiacEntity`
    - `GammaroachEntity`
    - `RaycatEntity`
    - `TremorzillaEntity`
    - `LanternfishEntity`
    - `SeaPigEntity`
    - `HullbreakerEntity`
    - `GossamerWormEntity`
    - `TripodfishEntity`
    - `DeepOneEntity`
    - `DeepOneKnightEntity`
    - `DeepOneMageEntity`
    - `MineGuardianEntity`
    - `GloomothEntity`
    - `UnderzealotEntity`
    - `WatcherEntity`
    - `CorrodentEntity`
    - `VesperEntity`
    - `ForsakenEntity`
    - `SweetishFishEntity`
    - `CaniacEntity`
    - `GumbeeperEntity`
    - `CandicornEntity`
    - `GumWormEntity`
    - `CaramelCubeEntity`
    - `GummyBearEntity`
    - `LicowitchEntity`
    - `GingerbreadManEntity`

### Biome and natural spawn data

- `src/main/resources/data/alexscaves/worldgen/biome/magnetic_caves.json`
- `src/main/resources/data/alexscaves/worldgen/biome/primordial_caves.json`
- `src/main/resources/data/alexscaves/worldgen/biome/toxic_caves.json`
- `src/main/resources/data/alexscaves/worldgen/biome/abyssal_chasm.json`
- `src/main/resources/data/alexscaves/worldgen/biome/forlorn_hollows.json`
- `src/main/resources/data/alexscaves/worldgen/biome/candy_cavity.json`
  - These JSONs contain biome spawn lists and spawn costs.
  - They are the primary data-layer source for biome spawning.
- `src/main/java/com/github/alexmodguy/alexscaves/mixin/NaturalSpawnerMixin.java`
  - Custom mixin that injects extra chunk-generation spawning for `alexscaves:cave_creature`.
  - This bypasses normal biome spawn behavior for prehistoric cave-creature mobs.
- `src/main/resources/alexscaves.mixins.json`
  - Enables `NaturalSpawnerMixin`.

### Runtime spawning and worldgen-driven mob creation

- `src/main/java/com/github/alexmodguy/alexscaves/server/block/blockentity/AmberMonolithBlockEntity.java`
  - Periodically spawns biome-matched creatures from nearby spawn tables.
  - Uses `ACEntityRegistry.CAVE_CREATURE` first, then vanilla `CREATURE`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/block/blockentity/GobthumperBlockEntity.java`
  - Finds or summons a temporary `GumWormEntity`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/block/blockentity/ConfectionOvenBlockEntity.java`
  - Spawns `GingerbreadManEntity`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/block/blockentity/VolcanicCoreBlockEntity.java`
  - Summons `LuxtructosaurusEntity` when activated with `ominous_catalyst`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/level/feature/SubterranodonRoostFeature.java`
  - Worldgen feature that places `subterranodon_egg` blocks and directly spawns a `SubterranodonEntity`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/block/DinosaurEggBlock.java`
  - Random ticks hatch egg blocks into dinosaur mobs.
  - Also used by `MultipleDinosaurEggsBlock` and `TremorzillaEggBlock`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/entity/ai/LicowitchAttackGoal.java`
  - Licowitch combat AI can summon possessed candy mobs at runtime.

### Mob-related items and item-driven mob creation

- `src/main/java/com/github/alexmodguy/alexscaves/server/item/ACItemRegistry.java`
  - `spawn_egg_*` items are registered here.
  - Fish buckets and sweetish fish buckets are registered here.
- `src/main/java/com/github/alexmodguy/alexscaves/server/item/ModFishBucketItem.java`
  - Bucket placement spawns the stored mob with `MobSpawnType.BUCKET`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/item/SweetishFishBucketItem.java`
  - Color-specific bucket subclass for `SweetishFishEntity`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/item/MagicConchItem.java`
  - Summons `DeepOne`, `DeepOneKnight`, and `DeepOneMage`.
- `src/main/java/com/github/alexmodguy/alexscaves/server/block/ACBlockRegistry.java`
  - Registers dinosaur egg blocks and `TremorzillaEggBlock`.

### Loot, drops, and mob-tied data

- `src/main/resources/data/alexscaves/loot_tables/entities/*.json`
  - Per-mob loot tables for almost every custom mob.
- `src/main/resources/data/alexscaves/loot_tables/gameplay/deep_one_barter.json`
- `src/main/resources/data/alexscaves/loot_tables/gameplay/deep_one_knight_barter.json`
- `src/main/resources/data/alexscaves/loot_tables/gameplay/deep_one_mage_barter.json`
- `src/main/resources/data/alexscaves/loot_tables/gameplay/sea_pig_digestion.json`
  - Special mob-linked loot systems used by Deep Ones and Sea Pigs.
- `src/main/java/com/github/alexmodguy/alexscaves/server/misc/ACTagRegistry.java`
  - Registers mob-related entity and item tags such as:
    - `dinosaurs`
    - `candy_mobs`
    - `amber_monolith_skips`
    - `seafloor_denizens`
    - `teletor_spawns_with`
    - `sea_pig_digests`
    - `deep_one_barters`
- `src/main/resources/data/alexscaves/tags/entity_types/*.json`
- `src/main/resources/data/alexscaves/tags/items/*.json`
  - Data backing those mob-related tags.

### Config files affecting spawn behavior

- `src/main/java/com/github/alexmodguy/alexscaves/server/config/ACServerConfig.java`
  - Relevant settings found:
    - `caveCreatureSpawnCountModifier`
    - `amberMonolithMeanTime`
    - `drownedDivingGearSpawnChance`
  - Important: there was no existing config toggle that globally disables all custom mob spawning.
- `alexscaves-general.toml`
  - Runtime file generated from `ACServerConfig`.

## What Each Relevant File/System Does

### Where mobs are registered

- `ACEntityRegistry`
  - Owns every custom entity `RegistryObject<EntityType<?>>`.
  - Binds attributes in `initializeAttributes(...)`.
  - Binds spawn placements in `spawnPlacements(...)`.

### Where automatic spawning is enabled

- Biome JSON files under `data/alexscaves/worldgen/biome`
  - Add normal biome spawns through `spawners`.
- `NaturalSpawnerMixin`
  - Adds extra chunk-generation spawning for `alexscaves:cave_creature`.
- `AmberMonolithBlockEntity`
  - Periodic autonomous creature repopulation.
- `GobthumperBlockEntity`
  - Runtime autonomous Gum Worm summon.
- `ConfectionOvenBlockEntity`
  - Runtime Gingerbread Man production.
- `VolcanicCoreBlockEntity`
  - Runtime boss summon.
- `SubterranodonRoostFeature`
  - Direct worldgen spawn and egg placement.
- `DinosaurEggBlock`
  - Runtime hatching into mobs.
- `MagicConchItem`
  - Player-driven mob summon item.
- `ModFishBucketItem`
  - Player-driven mob release item.
- `LicowitchAttackGoal`
  - Mob AI-driven summon path.

### Where biome spawn entries are added

- Only in the six biome JSON files listed above.
- `spawn_costs` also add biome spawn balancing for some custom mobs.

### Where mob items are registered

- `ACItemRegistry`
  - All spawn eggs.
  - All mob buckets.
  - Several mob drop items and combat items.
- `ACBlockRegistry`
  - Dinosaur eggs and Tremorzilla egg block items through block-item registration.

### Where loot and mob-tied content lives

- `data/alexscaves/loot_tables/entities/*.json`
  - Standard mob drops.
- `data/alexscaves/loot_tables/gameplay/*.json`
  - Special barter and digestion tables.
- Entity classes such as:
  - `SeaPigEntity` references `gameplay/sea_pig_digestion`
  - `DeepOneEntity`, `DeepOneKnightEntity`, `DeepOneMageEntity` reference barter tables

## What Must Change To Fully Disable Custom Mobs

1. Keep entity registration intact for technical stability.
2. Remove all custom biome spawn entries from the biome JSON files.
3. Force all custom spawn-placement predicates to return false so placement checks never succeed.
4. Stop the `NaturalSpawnerMixin` custom cave-creature path.
5. Add a global fail-safe so custom mobs cannot join the world even if some other path still tries to spawn them.
6. Disable runtime/worldgen summoners:
   - Amber Monolith
   - Gobthumper
   - Confection Oven
   - Volcanic Core
   - Subterranodon Roost direct spawn
   - Licowitch summon branch
7. Disable player-facing summon items:
   - spawn eggs
   - fish buckets
   - Magic Conch
8. Disable dinosaur and Tremorzilla egg hatching so worldgen egg blocks stay inert.

## Implementation Plan

1. Add a central helper in `ACEntityRegistry` that identifies all custom mob entity types targeted for disablement.
2. Use that helper as the canonical list for the disable pass.
3. Add a global `EntityJoinLevelEvent` fail-safe in `CommonEvents` to cancel any custom mob join.
4. Replace all custom spawn placement predicates in `ACEntityRegistry` with a shared false-returning gate.
5. Short-circuit `NaturalSpawnerMixin`.
6. Remove custom spawn entries and custom spawn costs from all six biome JSONs.
7. Short-circuit autonomous runtime spawners and worldgen summon paths.
8. Keep spawn egg item ids stable but make them inert items instead of `ForgeSpawnEggItem`.
9. Short-circuit fish bucket release and Magic Conch summon logic.
10. Short-circuit egg hatching.
11. Compile to confirm registry and signature stability.

## Implemented Disable Pass

### Code changes applied

- `ACEntityRegistry`
  - Added the central disabled-mob list.
  - Added helper methods to identify disabled custom mobs.
  - Replaced all custom spawn placement predicates with a shared disabled rule.
- `CommonEvents`
  - Added a global `EntityJoinLevelEvent` cancel path for all disabled custom mobs.
- `NaturalSpawnerMixin`
  - Added an early return when mob gameplay systems are disabled.
- `AmberMonolithBlockEntity`
  - Stops selecting and spawning mobs.
- `GobthumperBlockEntity`
  - Stops summoning Gum Worms.
- `ConfectionOvenBlockEntity`
  - Stops producing Gingerbread Men.
- `VolcanicCoreBlockEntity`
  - Stops summoning Luxtructosaurus.
- `SubterranodonRoostFeature`
  - Stops direct Subterranodon spawning from worldgen.
- `DinosaurEggBlock`
  - Stops egg hatching.
- `ACItemRegistry`
  - Spawn egg ids remain registered, but the registered item is inert.
- `ModFishBucketItem`
  - Bucket release no longer spawns mobs.
- `MagicConchItem`
  - Conch use no longer summons Deep Ones.
- `LicowitchAttackGoal`
  - Licowitch summon branch is disabled.

### Data changes applied

- Removed all Alex's Caves custom mob entries from:
  - `magnetic_caves.json`
  - `primordial_caves.json`
  - `toxic_caves.json`
  - `abyssal_chasm.json`
  - `forlorn_hollows.json`
  - `candy_cavity.json`
- Removed custom spawn costs from those same biome files.

## Technical Notes

- Custom entity types still exist in the registry.
- Client renderers, models, loot tables, tags, and mob classes were intentionally left in place.
- This keeps data generation, ids, saves, and references stable while making the mob layer inert.
- Existing custom mobs loaded from old worlds are also blocked by the join-level fail-safe and should not re-enter active play.

## Verification

- `./gradlew compileJava`
  - Result: success.
