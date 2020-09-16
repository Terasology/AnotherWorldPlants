// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.tree;

import com.google.common.collect.Maps;
import org.terasology.engine.utilities.random.Random;
import org.terasology.engine.world.generator.plugin.RegisterPlugin;
import org.terasology.growingflora.tree.PartOfTreeComponent;
import org.terasology.growingflora.tree.lsystem.AdvanceAxionElementGeneration;
import org.terasology.growingflora.tree.lsystem.AdvancedLSystemTreeDefinition;
import org.terasology.growingflora.tree.lsystem.AxionElementGeneration;
import org.terasology.growingflora.tree.lsystem.AxionElementReplacement;
import org.terasology.growingflora.tree.lsystem.DefaultAxionElementGeneration;
import org.terasology.growingflora.tree.lsystem.LSystemBasedTreeGrowthDefinition;
import org.terasology.growingflora.tree.lsystem.SimpleAxionElementReplacement;
import org.terasology.growingflora.tree.lsystem.SurroundAxionElementGeneration;
import org.terasology.growingflora.tree.lsystem.TreeBlockDefinition;

import java.util.Map;

@RegisterPlugin
public class LongPineGrowthDefinition extends LSystemBasedTreeGrowthDefinition {
    public static final String ID = "PlantPack:longPine";
    public static final String GENERATED_BLOCK = "AnotherWorldPlants:LongPineSaplingGenerated";

    private final AdvancedLSystemTreeDefinition treeDefinition;

    //TODO Make this look like real norther hemisphere pine
    public LongPineGrowthDefinition() {
        Map<Character, AxionElementReplacement> replacementMap = Maps.newHashMap();

        SimpleAxionElementReplacement sapling = new SimpleAxionElementReplacement("s");
        sapling.addReplacement(1f, "Tt");

        SimpleAxionElementReplacement trunkTop = new SimpleAxionElementReplacement("t");
        trunkTop.addReplacement(0.6f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // 137.5 degrees is a golden ratio
                        int deg = rnd.nextInt(100, 177);
                        return "N+(" + deg + ")[&Mb]Wt";
                    }
                });
        trunkTop.addReplacement(0.4f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // Always generate at least 2 branches
                        if (currentAxion.split("b").length < 8) {
                            // 137.5 degrees is a golden ratio
                            int deg = rnd.nextInt(100, 177);
                            return "N+(" + deg + ")[&Mb]Wt";
                        }
                        return "NWt";
                    }
                });

        SimpleAxionElementReplacement smallBranch = new SimpleAxionElementReplacement("b");
        smallBranch.addReplacement(0.8f, "Bb");

        SimpleAxionElementReplacement trunk = new SimpleAxionElementReplacement("T");
        trunk.addReplacement(0.7f, "TN");

        replacementMap.put('s', sapling);
        replacementMap.put('g', sapling);
        replacementMap.put('t', trunkTop);
        replacementMap.put('T', trunk);
        replacementMap.put('b', smallBranch);

        TreeBlockDefinition pineSapling = new TreeBlockDefinition("PlantPack:LongPineSapling",
                PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition pineSaplingGenerated = new TreeBlockDefinition(GENERATED_BLOCK,
                PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition greenLeaf = new TreeBlockDefinition("PlantPack:LongPineLeaf",
                PartOfTreeComponent.Part.LEAF);
        TreeBlockDefinition pineTrunk = new TreeBlockDefinition("PlantPack:LongPineTrunk",
                PartOfTreeComponent.Part.TRUNK);
        TreeBlockDefinition pineBranch = new TreeBlockDefinition("AnotherWorldPlants:LongPineBranch",
                PartOfTreeComponent.Part.BRANCH);

        float trunkAdvance = 0.5f;
        float branchAdvance = 0.45f;

        Map<Character, AxionElementGeneration> blockMap = Maps.newHashMap();
        blockMap.put('s', new DefaultAxionElementGeneration(pineSapling, trunkAdvance));
        blockMap.put('g', new DefaultAxionElementGeneration(pineSaplingGenerated, trunkAdvance));

        // Trunk building blocks
        blockMap.put('t', new SurroundAxionElementGeneration(greenLeaf, greenLeaf, trunkAdvance, 1.2f));
        blockMap.put('T', new DefaultAxionElementGeneration(pineTrunk, trunkAdvance));
        blockMap.put('N', new DefaultAxionElementGeneration(pineTrunk, trunkAdvance));
        blockMap.put('W', new SurroundAxionElementGeneration(pineBranch, greenLeaf, trunkAdvance, 1.2f));

        // Branch building blocks
        SurroundAxionElementGeneration smallBranchGeneration = new SurroundAxionElementGeneration(greenLeaf,
                greenLeaf, branchAdvance, 1.4f);
        smallBranchGeneration.setMaxZ(10);
        SurroundAxionElementGeneration largeBranchGeneration = new SurroundAxionElementGeneration(pineBranch,
                greenLeaf, branchAdvance, 0.8f, 1.8f);
        largeBranchGeneration.setMaxZ(10);
        blockMap.put('b', smallBranchGeneration);
        blockMap.put('B', largeBranchGeneration);
        blockMap.put('M', new AdvanceAxionElementGeneration(branchAdvance));

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
