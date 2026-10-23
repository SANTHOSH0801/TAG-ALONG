package com.tagalong.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static String serialize(Object o) {
        try {
            return JSON_MAPPER.writeValueAsString(o);
        } catch (JacksonException e) {
            log.error("Error serializing {} object to json, error: {}", o.getClass(), e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <E, T> T deepMerge(T baseNode, E updateNode, List<String> ignoreFields)
            throws Exception {
        // Create copy of baseNode
        T baseNodeCopy = JSON_MAPPER.readValue(JSON_MAPPER.writeValueAsString(baseNode),
                (Class<T>) baseNode.getClass());
        // Merge copy of baseNode and updatedNode
        return getPOJO(deepMerge(getJsonNode(baseNodeCopy), getJsonNode(updateNode), ignoreFields),
                (Class<T>) baseNode.getClass());
    }

    public static <E, T> T deepMerge(T baseNode, E updateNode) throws Exception {
        return deepMerge(baseNode, updateNode, new ArrayList<>());
    }

    /**
     * Get POJO from JsonNode
     */
    public static <T> T getPOJO(JsonNode node, Class<T> tClass) throws Exception {
        return JSON_MAPPER.treeToValue(node, tClass);
    }

    /**
     * Convert an object to JSONNode
     */
    public static <T> JsonNode getJsonNode(T node) {
        return JSON_MAPPER.valueToTree(node);
    }

}
