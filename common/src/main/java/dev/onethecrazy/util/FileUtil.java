package dev.onethecrazy.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.onethecrazy.Constants;
import dev.onethecrazy.JuxClient;
import dev.onethecrazy.config.JuxConfig;
import dev.onethecrazy.config.world.GsonIJuxWorldAdapter;
import dev.onethecrazy.config.world.IJuxWorld;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    private static final Path gameDir = Paths.get("").toAbsolutePath();

    public static String readJSONObjectFileContents(Path file) throws IOException{
        createFileIfNotPresent(file, "{}");
        return Files.readString(file);
    }

    public static void writeFile(Path file, String content) throws IOException{
        createFileIfNotPresent(file, "");

        Files.writeString(file, content);
    }

    public static JuxConfig loadConfig() {
        try{
            String saveContents = readJSONObjectFileContents(getConfigPath());
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(IJuxWorld.class, new GsonIJuxWorldAdapter())
                    .create();

            return gson.fromJson(saveContents, JuxConfig.class);
        }
        catch(Exception ex) {
            Constants.LOG.error("Error while loading config from disk: {0}", ex);
            return new JuxConfig();
        }
    }

    public static void createFileIfNotPresent(Path file, String emptyFileContent) throws IOException {
        Path parent = file.getParent();
        if (parent != null) Files.createDirectories(parent);

        try {
            Files.createFile(file);
            // Write Empty json, so we don't get an exception when reading the file contents and just parsing them via gson
            Files.writeString(file, emptyFileContent);
        }
        // File already exists
        catch(FileAlreadyExistsException e){ }
    }

    public static void createPaths() {
        try{
            Files.createDirectory(getDefaultPath());
            createFileIfNotPresent(getConfigPath(), "{}");
        } catch (IOException e) {
            // Ignore FileAlreadyExistsException
            if(e instanceof FileAlreadyExistsException)
                return;

            Constants.LOG.error("Ran into error while creating default path: {0}", e);
        }
    }

    public static Path getDefaultPath(){
        return JuxClient.getInstance().gameDir.resolve("." + Constants.MOD_ID);
    }

    public static Path getConfigPath(){
        return getDefaultPath().resolve(".config");
    }

    public static boolean doesFileExist(Path filePath){
        return Files.exists(filePath);
    }

    public static void writeConfig(JuxConfig save) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(IJuxWorld.class, new GsonIJuxWorldAdapter())
                .create();
        String json = gson.toJson(save);

        try {
            writeFile(getConfigPath(), json);
        } catch (IOException e) {
            Constants.LOG.error("I/O error while writing config: {0}", e);
        }
    }
}
