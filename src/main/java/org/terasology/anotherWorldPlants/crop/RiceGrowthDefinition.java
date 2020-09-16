// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.crop;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.terasology.anotherWorld.LocalParameters;
import org.terasology.engine.world.block.BlockUri;
import org.terasology.engine.world.generator.plugin.RegisterPlugin;
import org.terasology.growingflora.grass.AdvancedStagesGrowthDefinition;

import java.util.Arrays;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterPlugin
public class RiceGrowthDefinition extends AdvancedStagesGrowthDefinition {
    public static final String ID = "PlantPack:rice";

    public RiceGrowthDefinition() {
        super(ID,
                new Predicate<LocalParameters>() {
                    @Override
                    public boolean apply(LocalParameters input) {
                        return input.getHumidity() > 0.4f && input.getTemperature() > 15f;
                    }
                },
                new Function<LocalParameters, Long>() {
                    @Override
                    public Long apply(LocalParameters input) {
                        // Corn growth depends on humidity
                        float humidity = input.getHumidity();
                        long yearLength = 24 * 150000;
                        float minGrowthLength = yearLength / 3f;
                        int stageCount = 4;
                        float minStageGrowthLength = minGrowthLength / (stageCount - 1);
                        float maxTemperature = 0.9f;
                        float minTemperature = 0.6f;
                        float maxMultiplier = 1.5f;
                        if (humidity >= maxTemperature) {
                            return (long) minStageGrowthLength;
                        } else if (humidity <= minTemperature) {
                            return (long) (minStageGrowthLength * maxMultiplier);
                        } else {
                            return (long) ((1 + 1f * (maxMultiplier - 1) * (maxTemperature - humidity) / (maxTemperature - minTemperature)) * minStageGrowthLength);
                        }
                    }
                },
                Arrays.asList(
                        new BlockUri("PlantPack:Rice1"), new BlockUri("PlantPack:Rice2"), new BlockUri("PlantPack" +
                                ":Rice3"),
                        new BlockUri("PlantPack:Rice4")),
                new Predicate<LocalParameters>() {
                    @Override
                    public boolean apply(LocalParameters input) {
                        return input.getHumidity() < 0.4f || input.getTemperature() < 5f;
                    }
                }, new BlockUri("PlantPack:DeadBush"));
    }
}
