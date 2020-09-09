// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.farm.event;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.AbstractConsumableEvent;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class BeforeSeedPlanted extends AbstractConsumableEvent {
    private final EntityRef instigator;
    private final EntityRef soil;

    public BeforeSeedPlanted(EntityRef instigator, EntityRef soil) {
        this.instigator = instigator;
        this.soil = soil;
    }

    public EntityRef getInstigator() {
        return instigator;
    }

    public EntityRef getSoil() {
        return soil;
    }
}
