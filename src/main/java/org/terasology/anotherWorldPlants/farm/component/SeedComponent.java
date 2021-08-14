// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.farm.component;

import org.terasology.engine.world.block.Block;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class SeedComponent implements Component<SeedComponent> {
    public Block blockPlaced;

    @Override
    public void copyFrom(SeedComponent other) {
        this.blockPlaced = other.blockPlaced;
    }
}
