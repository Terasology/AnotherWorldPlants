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
public class CypressGrowthDefinition extends LSystemBasedTreeGrowthDefinition {
    public static final String ID = "PlantPack:cypress";
    public static final String GENERATED_BLOCK = "AnotherWorldPlants:CypressSaplingGenerated";

    private final AdvancedLSystemTreeDefinition treeDefinition;

    public CypressGrowthDefinition() {
        Map<Character, AxionElementReplacement> replacementMap = Maps.newHashMap();

        SimpleAxionElementReplacement sapling = new SimpleAxionElementReplacement("s");
        sapling.addReplacement(1f, "Tt");

        SimpleAxionElementReplacement trunkTop = new SimpleAxionElementReplacement("t");
        trunkTop.addReplacement(0.6f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // 137.5 degrees is a golden ratio
                        int deg = rnd.nextInt(132, 143);
                        return "N+(" + deg + ")[&Mb]Wt";
                    }
                });
        trunkTop.addReplacement(0.4f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // Always generate at least 2 branches
                        if (currentAxion.split("b").length < 1) {
                            // 137.5 degrees is a golden ratio
                            int deg = rnd.nextInt(135, 145);
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

        TreeBlockDefinition cypressSapling = new TreeBlockDefinition("PlantPack:CypressSapling",
                PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition cypressSaplingGenerated = new TreeBlockDefinition(GENERATED_BLOCK,
                PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition greenLeaf = new TreeBlockDefinition("PlantPack:CypressLeaf", PartOfTreeComponent.Part.LEAF);
        TreeBlockDefinition cypressTrunk = new TreeBlockDefinition("PlantPack:CypressTrunk",
                PartOfTreeComponent.Part.TRUNK);
        TreeBlockDefinition cypressBranch = new TreeBlockDefinition("AnotherWorldPlants:CypressBranch",
                PartOfTreeComponent.Part.BRANCH);

        float trunkAdvance = 0.4f;
        float branchAdvance = 0.25f;

        Map<Character, AxionElementGeneration> blockMap = Maps.newHashMap();
        blockMap.put('s', new DefaultAxionElementGeneration(cypressSapling, trunkAdvance));
        blockMap.put('g', new DefaultAxionElementGeneration(cypressSaplingGenerated, trunkAdvance));

        // Trunk building blocks
        blockMap.put('t', new SurroundAxionElementGeneration(greenLeaf, greenLeaf, trunkAdvance, 1.2f));
        blockMap.put('T', new DefaultAxionElementGeneration(cypressTrunk, trunkAdvance));
        blockMap.put('N', new DefaultAxionElementGeneration(cypressTrunk, trunkAdvance));
        blockMap.put('W', new SurroundAxionElementGeneration(cypressBranch, greenLeaf, trunkAdvance, 1.2f));

        // Branch building blocks
        SurroundAxionElementGeneration smallBranchGeneration = new SurroundAxionElementGeneration(greenLeaf,
                greenLeaf, branchAdvance, 1.4f);
        smallBranchGeneration.setMaxZ(0);
        SurroundAxionElementGeneration largeBranchGeneration = new SurroundAxionElementGeneration(cypressBranch,
                greenLeaf, branchAdvance, 0.8f, 1.8f);
        largeBranchGeneration.setMaxZ(0);
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
