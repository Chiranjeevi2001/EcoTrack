package com.example.ecotrack_v1;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class StorageReferenceFetcher implements DataFetcher<InputStream> {

    private final StorageReference storageReference;

    StorageReferenceFetcher(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        try {
            // Use Tasks.await to get the result synchronously
            InputStream stream = Tasks.await(storageReference.getStream()).getStream();
            callback.onDataReady(stream);
        } catch (ExecutionException | InterruptedException e) {
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {
        // No cleanup required for the InputStream
    }

    @Override
    public void cancel() {
        // Cancelling is not supported
    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
