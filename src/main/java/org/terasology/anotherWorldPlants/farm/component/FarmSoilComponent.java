// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.farm.component;

import org.terasology.gestalt.entitysystem.component.Component;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class FarmSoilComponent implements Component<FarmSoilComponent> {
    public float growChanceMultiplier;

    @Override
    public void copy(FarmSoilComponent other) {
        this.growChanceMultiplier = other.growChanceMultiplier;
    }
}
