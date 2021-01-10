// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.farm.event;

import org.joml.Vector3i;
import org.terasology.entitySystem.event.Event;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class SeedPlanted implements Event {
    private Vector3i location;

    public SeedPlanted(Vector3i location) {
        this.location = location;
    }

    public Vector3i getLocation() {
        return location;
    }
}
