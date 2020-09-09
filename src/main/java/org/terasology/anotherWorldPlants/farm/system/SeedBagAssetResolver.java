// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.anotherWorldPlants.farm.system;

import com.google.common.collect.ImmutableSet;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureData;
import org.terasology.engine.rendering.assets.texture.TextureRegionAsset;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.gestalt.assets.AssetDataProducer;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.gestalt.assets.management.AssetManager;
import org.terasology.gestalt.assets.module.annotations.RegisterAssetDataProducer;
import org.terasology.gestalt.naming.Name;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RegisterAssetDataProducer
public class SeedBagAssetResolver implements AssetDataProducer<TextureData> {
    private static final Name PLANT_PACK_MODULE = new Name("anotherworldplants");

    private final AssetManager assetManager;

    public SeedBagAssetResolver(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Set<ResourceUrn> getAvailableAssetUrns() {
        return Collections.emptySet();
    }

    @Override
    public Set<Name> getModulesProviding(Name resourceName) {
        if (!resourceName.toLowerCase().startsWith("seedbag(")) {
            return Collections.emptySet();
        }
        return ImmutableSet.of(PLANT_PACK_MODULE);
    }

    @Override
    public ResourceUrn redirect(ResourceUrn urn) {
        return urn;
    }

    @Override
    public Optional<TextureData> getAssetData(ResourceUrn urn) throws IOException {
        final String assetName = urn.getResourceName().toString().toLowerCase();
        if (!PLANT_PACK_MODULE.equals(urn.getModuleName())
                || !assetName.startsWith("seedbag(")) {
            return Optional.empty();
        }
        String[] split = assetName.split("\\(", 2);

        Optional<TextureRegionAsset> resultImageAsset = assetManager.getAsset("AnotherWorldPlants:farming#Pouch",
                TextureRegionAsset.class);
        BufferedImage resultImage = TextureUtil.convertToImage(resultImageAsset.get());
        Optional<TextureRegionAsset> seedTextureAsset = assetManager.getAsset(
                split[1].substring(0, split[1].length() - 1), TextureRegionAsset.class);
        BufferedImage seedTexture = TextureUtil.convertToImage(seedTextureAsset.get());

        Graphics2D gr = (Graphics2D) resultImage.getGraphics();
        try {
            int resultWidth = resultImage.getWidth();
            int resultHeight = resultImage.getHeight();
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            gr.drawImage(seedTexture, resultWidth / 4, 3 * resultHeight / 8, resultWidth / 2, resultWidth / 2, null);
        } finally {
            gr.dispose();
        }

        final ByteBuffer byteBuffer = TextureUtil.convertToByteBuffer(resultImage);
        return Optional.of(new TextureData(resultImage.getWidth(), resultImage.getHeight(),
                new ByteBuffer[]{byteBuffer}, Texture.WrapMode.REPEAT, Texture.FilterMode.NEAREST));
    }


}