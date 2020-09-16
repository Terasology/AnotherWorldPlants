// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.crop;

import org.terasology.anotherWorldPlants.farm.event.SeedPlanted;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.growingflora.LivingPlantComponent;
import org.terasology.growingflora.PlantedSaplingComponent;
import org.terasology.math.geom.Vector3i;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class CropAuthoritySystem extends BaseComponentSystem {
    @In
    private BlockEntityRegistry blockEntityRegistry;

    @ReceiveEvent
    public void cropPlanted(SeedPlanted event, EntityRef plant) {
        Vector3i location = event.getLocation();
        EntityRef plantedEntity = blockEntityRegistry.getEntityAt(location);
        if (plantedEntity.hasComponent(LivingPlantComponent.class)) {
            plantedEntity.addComponent(new PlantedSaplingComponent());
        }
    }
}
