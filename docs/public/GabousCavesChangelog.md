# GabousCaves Changelog

## Current Version

- Added a working Mandalorian travel chain for `Mandalore System`, including the missing space dimension, corrected UI travel targets, and fixed atmospheric return from Mandalore back into `cosmos:mandalore_system`.
- Reworked Mandalore crystal content with `green_crystal_block`, `green_crystal_block_floor`, `green_crystal_block_floor_cracked`, and `green_crystal_cluster` support wired into block/item registration, tags, loot, and placement rules.
- Updated Mandalore hazard behavior so toxic contamination applies at level I across the planet and intensifies to level II near green crystal nodes.
- Rebalanced Mandalore resources by making green crystal terrain/features far more common, drastically reducing GabousCaves ore generation, and replacing the old Mandalore iron feature with coal ore.
- Added Mandalore-specific surface worldgen using custom noise settings, custom Mandalore noise layers, debris/scar/remnant features, and cluster patch biome modifiers to create a more glassed, fractured wasteland surface.
- Added a full mob-system analysis document in `DISABLE_MOBS_ANALYSIS.md` mapping entity registration, biome spawn data, natural spawning hooks, runtime summoners, mob-linked items, loot, tags, and config touchpoints before gameplay changes were applied.
- Disabled Alex's Caves custom mob gameplay by removing custom biome spawn entries, forcing custom spawn placement checks to fail, stopping custom chunk-generation spawning, and canceling custom mob world joins as a global fail-safe.
- Disabled mob creation from runtime and worldgen systems including Amber Monoliths, Gobthumpers, Confection Ovens, Volcanic Cores, Subterranodon roost direct spawns, Licowitch summon behavior, fish buckets, Magic Conch usage, and dinosaur egg hatching.
- Kept custom mob registries, loot tables, renderers, and related data intact for stability, while making spawn eggs inert so mob ids remain present but inactive.
- Added per-biome dimension config generation under `config/alexscaves_biome_content`, restoring default cave biome mob spawns and allowing biome mob spawns, custom ore features, and cave-linked structures to be whitelisted by dimension through JSON.
