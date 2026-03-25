# Biome Content Dimension Config

Gabous Caves now generates one JSON file per cave biome in:

`config/alexscaves_biome_content/`

Files:

- `magnetic_caves.json`
- `primordial_caves.json`
- `toxic_caves.json`
- `abyssal_chasm.json`
- `forlorn_hollows.json`
- `candy_cavity.json`

Each file controls three dimension whitelists for that biome:

- `mob_spawn_dimensions`
- `ore_feature_dimensions`
- `structure_dimensions`

Each file also lists the content currently controlled by that biome:

- `mobs`
- `ore_features`
- `structures`

Example:

```json
{
  "biome": "alexscaves:magnetic_caves",
  "mob_spawn_dimensions": [
    "minecraft:overworld"
  ],
  "ore_feature_dimensions": [
    "minecraft:overworld"
  ],
  "structure_dimensions": [
    "minecraft:overworld"
  ],
  "mobs": [
    "alexscaves:notor",
    "alexscaves:magnetron",
    "alexscaves:teletor",
    "alexscaves:boundroid",
    "alexscaves:ferrouslime"
  ],
  "ore_features": [
    "alexscaves:galena_iron_ore",
    "alexscaves:metal_swarf"
  ],
  "structures": [
    "alexscaves:ferrocave"
  ]
}
```

Behavior:

- If a cave biome appears in a dimension listed under `mob_spawn_dimensions`, its configured biome mob spawns are allowed there.
- If that dimension is missing from `mob_spawn_dimensions`, biome-based mob spawning for that cave biome is blocked there.
- `ore_feature_dimensions` does the same for the listed placed ore features.
- `structure_dimensions` does the same for the biome-linked Alex's Caves structures.

Notes:

- These files are created automatically on config load if they do not exist yet.
- The lists under `mobs`, `ore_features`, and `structures` are the content currently governed by that biome file.
- An empty dimension list disables that content type for the biome everywhere.
