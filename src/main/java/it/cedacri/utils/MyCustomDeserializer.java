package it.cedacri.utils;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class MyCustomDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(String key, DeserializationContext deserializationContext) throws IOException {
        return key;
    }
}
