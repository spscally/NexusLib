package tsp.mcnexus.mojang;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;
import tsp.mcnexus.player.info.NameHistory;
import tsp.mcnexus.player.info.SkinInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

/**
 * Class for async fetching non-authenticated info from mojang.
 *
 * @author TheSilentPro
 */
@SuppressWarnings("ClassCanBeRecord")
public class MojangAPI {

    private final JavaPlugin plugin;
    private final Executor executor;

    public MojangAPI(JavaPlugin plugin, Executor executor) {
        this.plugin = plugin;
        this.executor = executor;
    }

    /**
     * Retrieve the unique id of a player based on their name
     *
     * @param name The player name
     * @param timeout Connection timeout
     */
    public CompletableFuture<UUID> getUniqueId(String name, int timeout) {
        return getUniqueIdJson(name, timeout).thenApply(json -> UUID.fromString(json.get("id").toString()));
    }

    public CompletableFuture<UUID> getUniqueId(String name) {
        return getUniqueId(name, 5000);
    }

    /**
     * Retrieve skin information about a {@link UUID}
     *
     * @param uuid The unique id to check
     * @param timeout Connection timeout
     */
    public CompletableFuture<SkinInfo> getSkinInfo(UUID uuid, int timeout){
        return getSkinInfoJson(uuid, timeout).thenApply(json -> {
            JsonArray properties = json.get("properties").getAsJsonArray();
            JsonObject textures = properties.get(0).getAsJsonObject();

            String id = json.get("id").toString();
            String name = json.get("name").toString();
            String value = textures.get("value").toString();
            String signature = textures.get("signature").toString();
            return new SkinInfo(id, name, value, signature);
        });
    }

    public CompletableFuture<SkinInfo> getSkinInfo(UUID uuid) {
        return getSkinInfo(uuid, 5000);
    }

    /**
     * Retrieve name history of a {@link UUID}
     *
     * @param uuid The unique id
     * @param timeout Connection timeout
     */
    public CompletableFuture<NameHistory> getNameHistory(UUID uuid, int timeout) {
        return getNameHistoryJson(uuid, timeout).thenApply(json -> {
            Map<String, Long> history = new HashMap<>();
            for (JsonElement e : json) {
                JsonObject obj = e.getAsJsonObject();
                String name = obj.get("name").toString();
                long timestamp = obj.get("changedToAt") != null ? obj.get("changedToAt").getAsLong() : -1;
                history.put(name, timestamp);
            }

            return new NameHistory(uuid, history);
        });
    }

    public CompletableFuture<NameHistory> getNameHistory(UUID uuid) {
        return getNameHistory(uuid, 5000);
    }

    // Json
    public CompletableFuture<JsonObject> getUniqueIdJson(String name, int timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String req = "https://api.mojang.com/users/profiles/minecraft/" + name;

                URLConnection connection = new URL(req).openConnection();
                connection.setConnectTimeout(timeout);
                connection.setRequestProperty("User-Agent", plugin.getName() + "-UUIDFetcher");
                connection.setRequestProperty("Accept", "application/json");

                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                return (JsonParser.parseString(response.toString())).getAsJsonObject();
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    public CompletableFuture<JsonObject> getUniqueIdJson(String name) {
        return getUniqueIdJson(name, 5000);
    }

    public CompletableFuture<JsonObject> getSkinInfoJson(UUID uuid, int timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String req = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "") + "?unsigned=false";
                URLConnection connection = new URL(req).openConnection();
                connection.setConnectTimeout(timeout);
                connection.setRequestProperty("User-Agent", plugin.getName() + "-SkinFetcher");
                connection.setRequestProperty("Accept", "application/json");

                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }

                return JsonParser.parseString(response.toString()).getAsJsonObject();
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executor);
    }

    public CompletableFuture<JsonObject> getSkinInfoJson(UUID uuid) {
        return getSkinInfoJson(uuid, 5000);
    }

    public CompletableFuture<JsonArray> getNameHistoryJson(UUID uuid, int timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String req = "https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names";
                URLConnection connection = new URL(req).openConnection();
                connection.setConnectTimeout(timeout);
                connection.setRequestProperty("User-Agent", plugin.getName() + "-NameHistoryFetcher");
                connection.setRequestProperty("Accept", "application/json");

                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }

                return JsonParser.parseString(response.toString()).getAsJsonArray();
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executor);
    }

    public CompletableFuture<JsonArray> getNameHistoryJson(UUID uuid) {
        return getNameHistoryJson(uuid, 5000);
    }

    public CompletableFuture<JsonArray> getServiceStatusJson(int timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String req = "https://status.mojang.com/check";
                URLConnection connection = new URL(req).openConnection();
                connection.setConnectTimeout(timeout);
                connection.setRequestProperty("User-Agent", plugin.getName() + "-StatusFetcher");
                connection.setRequestProperty("Accept", "application/json");

                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }

                return JsonParser.parseString(response.toString()).getAsJsonArray();
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executor);
    }

    public CompletableFuture<JsonArray> getServiceStatusJson() {
        return getServiceStatusJson(5000);
    }

    public CompletableFuture<List<String>> getBlockedServersJson(int timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String req = "https://sessionserver.mojang.com/blockedservers";
                List<String> blockedServers = new ArrayList<>();
                URLConnection connection = new URL(req).openConnection();
                connection.setConnectTimeout(timeout);
                connection.setRequestProperty("User-Agent", plugin.getName() + "-BlockedServersFetcher");
                connection.setRequestProperty("Accept", "application/json");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while((line = in.readLine()) != null) {
                        blockedServers.add(line);
                    }
                }

                return blockedServers;
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executor);
    }

    public CompletableFuture<List<String>> getBlockedServersJson() {
        return getBlockedServersJson(5000);
    }

}
