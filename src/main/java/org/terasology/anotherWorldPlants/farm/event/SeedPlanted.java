// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.farm.event;

import org.terasology.engine.entitySystem.event.Event;
import org.terasology.math.geom.Vector3i;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class SeedPlanted implements Event {
    private final Vector3i location;

    public SeedPlanted(Vector3i location) {
        this.location = location;
    }

    public Vector3i getLocation() {
        return location;
    }
}
