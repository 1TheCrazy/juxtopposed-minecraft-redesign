package dev.onethecrazy.config.world;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GsonIJuxWorldAdapter implements JsonDeserializer<IJuxWorld>, JsonSerializer<IJuxWorld> {
    @Override
    public IJuxWorld deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        // Deserialize into correct class
        JsonObject obj = json.getAsJsonObject();
        String t = obj.get("type").getAsString();

        return switch (t) {
            case "SERVER" -> ctx.deserialize(obj, JuxServer.class);
            case "WORLD" -> ctx.deserialize(obj, JuxWorld.class);
            default -> throw new JsonParseException("Unknown IJuxWorld type: " + t);
        };
    }

    @Override
    public JsonElement serialize(IJuxWorld src, Type typeOfSrc, JsonSerializationContext ctx) {
        JsonObject obj = ctx.serialize(src, src.getClass()).getAsJsonObject();

        // Attach custom field to be able to know what class to deserialize to
        String typeValue = src instanceof JuxWorld ? "WORLD" : "SERVER";
        obj.addProperty("type", typeValue);

        return obj;
    }
}
