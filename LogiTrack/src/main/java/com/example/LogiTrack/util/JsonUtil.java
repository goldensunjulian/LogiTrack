package com.example.LogiTrack.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Convierte cualquier objeto Java a String JSON válido
    // Se usa en los services para guardar los valores antes/después en auditoría
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }
}
