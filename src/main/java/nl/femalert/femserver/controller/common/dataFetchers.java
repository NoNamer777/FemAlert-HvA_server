package nl.femalert.femserver.controller.common;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class dataFetchers {

    public static String getStringValue(ObjectNode data, String fieldName) {
        if (data.get(fieldName) == null || !data.get(fieldName).isTextual()) return null;

        return data.get(fieldName).textValue();
    }

    public static Boolean getBooleanValue(ObjectNode data, String fieldName) {
        if (data.get(fieldName) == null || !data.get(fieldName).isBoolean()) return null;

        return data.get(fieldName).booleanValue();
    }

    public static String getObjectStringValue(ObjectNode data, String objectName, String fieldName) {
        if (data.get(objectName) == null || !data.get(objectName).isObject()) return null;

        return getStringValue((ObjectNode) data.get(objectName), fieldName);
    }
}
