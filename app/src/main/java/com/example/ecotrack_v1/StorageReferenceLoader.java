package com.example.ecotrack_v1;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class StorageReferenceLoader implements ModelLoader<StorageReference, InputStream> {

    @Override
    public LoadData<InputStream> buildLoadData(StorageReference model, int width, int height, Options options) {
        return new LoadData<>(new ObjectKey(model.getPath()), new StorageReferenceFetcher(model));
    }

    @Override
    public boolean handles(StorageReference model) {
        return true;
    }
    public static class Factory implements ModelLoaderFactory<StorageReference, InputStream>, com.example.ecotrack_v1.Factory {

        @Override
        public ModelLoader<StorageReference, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new StorageReferenceLoader();
        }

        @Override
        public void teardown() {

        }

        @Override
        public Class<InputStream> getDataClass() {
            return InputStream.class;
        }
    }
}
