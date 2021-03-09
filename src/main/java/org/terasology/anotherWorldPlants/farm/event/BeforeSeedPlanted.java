/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.anotherWorldPlants.farm.event;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.AbstractConsumableEvent;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class BeforeSeedPlanted extends AbstractConsumableEvent {
    private EntityRef instigator;
    private EntityRef soil;

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
