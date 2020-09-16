// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.tree;

import com.google.common.collect.Maps;
import org.terasology.engine.utilities.random.Random;
import org.terasology.engine.world.generator.plugin.RegisterPlugin;
import org.terasology.growingflora.tree.PartOfTreeComponent;
import org.terasology.growingflora.tree.lsystem.AdvancedLSystemTreeDefinition;
import org.terasology.growingflora.tree.lsystem.AxionElementGeneration;
import org.terasology.growingflora.tree.lsystem.AxionElementReplacement;
import org.terasology.growingflora.tree.lsystem.BlockLengthElementGeneration;
import org.terasology.growingflora.tree.lsystem.DefaultAxionElementGeneration;
import org.terasology.growingflora.tree.lsystem.GrowthAxionElementReplacement;
import org.terasology.growingflora.tree.lsystem.LSystemBasedTreeGrowthDefinition;
import org.terasology.growingflora.tree.lsystem.SimpleAxionElementReplacement;
import org.terasology.growingflora.tree.lsystem.SurroundAxionElementGeneration;
import org.terasology.growingflora.tree.lsystem.SurroundLengthAxionElementGeneration;
import org.terasology.growingflora.tree.lsystem.TreeBlockDefinition;

import java.util.Map;

@RegisterPlugin
public class BirchGrowthDefinition extends LSystemBasedTreeGrowthDefinition {
    public static final String ID = "PlantPack:birch";
    public static final String GENERATED_BLOCK = "AnotherWorldPlants:BirchSaplingGenerated";

    private final AdvancedLSystemTreeDefinition treeDefinition;

    public BirchGrowthDefinition() {
        Map<Character, AxionElementReplacement> replacementMap = Maps.newHashMap();

        SimpleAxionElementReplacement sapling = new SimpleAxionElementReplacement("s");
        sapling.addReplacement(1f, "T(0.5)t");

        SimpleAxionElementReplacement trunkTop = new SimpleAxionElementReplacement("t");
        trunkTop.addReplacement(0.6f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // 137.5 degrees is a golden ratio
                        int deg = rnd.nextInt(120, 157);
                        return "+(" + deg + ")[&B(0.5)b]T(0.5)t";
                    }
                });
        trunkTop.addReplacement(0.4f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // Always generate at least 2 branches
                        if (currentAxion.split("b").length < 2) {
                            // 137.5 degrees is a golden ratio
                            int deg = rnd.nextInt(130, 147);
                            return "+(" + deg + ")[&B(0.5)b]T(0.5)t";
                        }
                        return "t";
                    }
                });

        replacementMap.put('s', sapling);
        replacementMap.put('g', sapling);
        replacementMap.put('t', trunkTop);
        replacementMap.put('T', new GrowthAxionElementReplacement("T", 1.2f));
        replacementMap.put('B', new GrowthAxionElementReplacement("B", 1.1f));

        TreeBlockDefinition birchSapling = new TreeBlockDefinition("PlantPack:BirchSapling",
                PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition birchSaplingGenerated = new TreeBlockDefinition(GENERATED_BLOCK,
                PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition greenLeaf = new TreeBlockDefinition("PlantPack:BirchLeaf", PartOfTreeComponent.Part.LEAF);
        TreeBlockDefinition birchTrunk = new TreeBlockDefinition("PlantPack:BirchTrunk",
                PartOfTreeComponent.Part.TRUNK);
        TreeBlockDefinition birchBranch = new TreeBlockDefinition("AnotherWorldPlants:BirchBranch",
                PartOfTreeComponent.Part.BRANCH);

        float trunkAdvance = 0.6f;
        float branchAdvance = 0.1f;

        Map<Character, AxionElementGeneration> blockMap = Maps.newHashMap();
        blockMap.put('s', new DefaultAxionElementGeneration(birchSapling, trunkAdvance));
        blockMap.put('g', new DefaultAxionElementGeneration(birchSaplingGenerated, trunkAdvance));

        // Trunk building blocks
        blockMap.put('t', new SurroundAxionElementGeneration(greenLeaf, greenLeaf, trunkAdvance, 2f));
        blockMap.put('T', new BlockLengthElementGeneration(birchTrunk, trunkAdvance));

        // Branch building blocks
        SurroundAxionElementGeneration smallBranchGeneration = new SurroundAxionElementGeneration(greenLeaf,
                greenLeaf, branchAdvance, 2.6f);
        smallBranchGeneration.setMaxZ(0);
        blockMap.put('b', smallBranchGeneration);

        SurroundLengthAxionElementGeneration largeBranchGeneration =
                new SurroundLengthAxionElementGeneration(birchBranch, greenLeaf, branchAdvance, 1.1f, 3.5f);
        largeBranchGeneration.setMaxZ(0);
        blockMap.put('B', largeBranchGeneration);

        treeDefinition = new AdvancedLSystemTreeDefinition(ID, "g", replacementMap, blockMap, 1.5f);
    }

    @Override
    public String getPlantId() {
        return ID;
    }

    @Override
    protected String getGeneratedBlock() {
        return GENERATED_BLOCK;
    }

    @Override
    protected AdvancedLSystemTreeDefinition getTreeDefinition() {
        return treeDefinition;
    }
}
