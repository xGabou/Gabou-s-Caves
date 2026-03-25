package com.github.alexmodguy.alexscaves.mixin;

import com.github.alexmodguy.alexscaves.server.config.BiomeContentConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlacedFeature.class)
public class PlacedFeatureMixin {

    @Inject(method = "placeWithBiomeCheck", at = @At("HEAD"), cancellable = true)
    private void ac_gateOreFeaturesByDimension(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        ResourceLocation placedFeatureId = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).getKey((PlacedFeature) (Object) this);
        if (!BiomeContentConfig.isOreFeatureAllowed(level, blockPos, placedFeatureId)) {
            cir.setReturnValue(false);
        }
    }
}
