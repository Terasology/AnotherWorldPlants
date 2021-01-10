// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.farm.system;

import org.joml.RoundingMode;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.terasology.anotherWorldPlants.farm.component.FarmSoilComponent;
import org.terasology.anotherWorldPlants.farm.component.SeedComponent;
import org.terasology.anotherWorldPlants.farm.event.BeforeSeedPlanted;
import org.terasology.anotherWorldPlants.farm.event.SeedPlanted;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.gf.grass.GetGrowthChance;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.math.Side;
import org.terasology.registry.In;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.entity.placement.PlaceBlocks;

@RegisterSystem(RegisterMode.AUTHORITY)
public class FarmAuthoritySystem extends BaseComponentSystem {
    @In
    private WorldProvider worldProvider;
    @In
    private BlockEntityRegistry blockEntityRegistry;

    @ReceiveEvent
    public void plantingSeeds(ActivateEvent event, EntityRef item, SeedComponent seed) {
        boolean consume = true;
        EntityRef target = event.getTarget();
        // Clicked on top of soil
        if (Side.inDirection(event.getHitNormal()) == Side.TOP && target.hasComponent(FarmSoilComponent.class)) {
            BeforeSeedPlanted plantEvent = new BeforeSeedPlanted(event.getInstigator(), target);
            item.send(plantEvent);
            if (!plantEvent.isConsumed()) {
                Block blockPlaced = seed.blockPlaced;
                Vector3f location = event.getTargetLocation();
                Vector3i blockLocation = new Vector3i(new Vector3f(location.x + 0.5f, location.y + 1.5f, location.z + 0.5f), RoundingMode.FLOOR);
                PlaceBlocks placeBlocks = new PlaceBlocks(blockLocation, blockPlaced);
                worldProvider.getWorldEntity().send(placeBlocks);
                if (!placeBlocks.isConsumed()) {
                    item.send(new SeedPlanted(blockLocation));
                    consume = false;
                }
            }
        }

        if (consume) {
            event.consume();
        }
    }

    @ReceiveEvent
    public void soilGrowthImprovement(GetGrowthChance event, EntityRef plant, BlockComponent blockComponent) {
        Vector3i position = blockComponent.getPosition(new Vector3i());
        Vector3i soilLocation = new Vector3i(position.x, position.y - 1, position.z);
        if (worldProvider.isBlockRelevant(soilLocation)) {
            EntityRef soil = blockEntityRegistry.getEntityAt(soilLocation);
            FarmSoilComponent farmSoil = soil.getComponent(FarmSoilComponent.class);
            if (farmSoil != null) {
                event.multiply(farmSoil.growChanceMultiplier);
            }
        }
    }
}
