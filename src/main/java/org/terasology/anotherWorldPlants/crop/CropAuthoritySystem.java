// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.crop;

import org.joml.Vector3i;
import org.terasology.anotherWorldPlants.farm.event.SeedPlanted;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.gf.LivingPlantComponent;
import org.terasology.gf.PlantedSaplingComponent;

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
