// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.tree;

import com.google.common.collect.Maps;
import org.terasology.engine.utilities.random.Random;
import org.terasology.engine.world.generator.plugin.RegisterPlugin;
import org.terasology.gf.tree.PartOfTreeComponent;
import org.terasology.gf.tree.lsystem.AdvanceAxionElementGeneration;
import org.terasology.gf.tree.lsystem.AdvancedLSystemTreeDefinition;
import org.terasology.gf.tree.lsystem.AxionElementGeneration;
import org.terasology.gf.tree.lsystem.AxionElementReplacement;
import org.terasology.gf.tree.lsystem.DefaultAxionElementGeneration;
import org.terasology.gf.tree.lsystem.LSystemBasedTreeGrowthDefinition;
import org.terasology.gf.tree.lsystem.SimpleAxionElementReplacement;
import org.terasology.gf.tree.lsystem.SurroundAxionElementGeneration;
import org.terasology.gf.tree.lsystem.TreeBlockDefinition;

import java.util.Map;

@RegisterPlugin
public class MithrootGrowthDefinition extends LSystemBasedTreeGrowthDefinition {
    public static final String ID = "PlantPack:mithroot";
    public static final String GENERATED_BLOCK = "AnotherWorldPlants:MithrootSaplingGenerated";

    private final AdvancedLSystemTreeDefinition treeDefinition;

    public MithrootGrowthDefinition() {
        Map<Character, AxionElementReplacement> replacementMap = Maps.newHashMap();

        SimpleAxionElementReplacement sapling = new SimpleAxionElementReplacement("s");
        sapling.addReplacement(1f, "Tt");

        SimpleAxionElementReplacement trunkTop = new SimpleAxionElementReplacement("t");
        trunkTop.addReplacement(0.6f,
                new AxionElementReplacement() {
                    @Override
                    public String getReplacement(Random rnd, String parameter, String currentAxion) {
                        // 137.5 degrees is a golden ratio
                        int deg = rnd.nextInt(105, 172);
                        return "+(" + deg + ")[&Mb]Wt";
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
                            return "+(" + deg + ")[&Mb]Wt";
                        }
                        return "Wt";
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

        TreeBlockDefinition mithrootSapling = new TreeBlockDefinition("PlantPack:MithrootSapling",
                PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition mithrootSaplingGenerated = new TreeBlockDefinition(GENERATED_BLOCK,
                PartOfTreeComponent.Part.SAPLING);
        TreeBlockDefinition greenLeaf = new TreeBlockDefinition("PlantPack:MithrootLeaf",
                PartOfTreeComponent.Part.LEAF);
        TreeBlockDefinition mithrootTrunk = new TreeBlockDefinition("PlantPack:MithrootTrunk",
                PartOfTreeComponent.Part.TRUNK);
        TreeBlockDefinition mithrootBranch = new TreeBlockDefinition("AnotherWorldPlants:MithrootBranch",
                PartOfTreeComponent.Part.BRANCH);

        float trunkAdvance = 0.3f;
        float branchAdvance = 0.5f;

        Map<Character, AxionElementGeneration> blockMap = Maps.newHashMap();
        blockMap.put('s', new DefaultAxionElementGeneration(mithrootSapling, trunkAdvance));
        blockMap.put('g', new DefaultAxionElementGeneration(mithrootSaplingGenerated, trunkAdvance));

        // Trunk building blocks
        blockMap.put('t', new SurroundAxionElementGeneration(greenLeaf, greenLeaf, trunkAdvance, 2f));
        blockMap.put('T', new DefaultAxionElementGeneration(mithrootTrunk, trunkAdvance));
        blockMap.put('N', new DefaultAxionElementGeneration(mithrootTrunk, trunkAdvance));
        blockMap.put('W', new SurroundAxionElementGeneration(mithrootBranch, greenLeaf, trunkAdvance, 2f));

        // Branch building blocks
        SurroundAxionElementGeneration smallBranchGeneration = new SurroundAxionElementGeneration(greenLeaf,
                greenLeaf, branchAdvance, 2.6f);
        smallBranchGeneration.setMaxZ(0);
        SurroundAxionElementGeneration largeBranchGeneration = new SurroundAxionElementGeneration(mithrootBranch,
                greenLeaf, branchAdvance, 1.1f, 3.5f);
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
