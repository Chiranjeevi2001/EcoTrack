package com.example.ecotrack_v1;

import java.io.InputStream;

public interface Factory {
    Class<InputStream> getDataClass();
}
