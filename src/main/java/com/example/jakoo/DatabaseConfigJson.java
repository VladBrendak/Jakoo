package com.example.jakoo;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DatabaseConfigJson {
    private JSONObject jsonConfig;
    private String configFile = "src/database.json";

    public DatabaseConfigJson() {
        try {
            if (!fileExists(configFile)) {
                // Створення файлу, якщо він не існує
                createFile(configFile);
            }
            String jsonContent = readFile(configFile);
            jsonConfig = new JSONObject(jsonContent);
        } catch (IOException e) {
            e.printStackTrace();
            // Обробка помилки, якщо файл JSON не знайдено або не може бути завантажено.
        }
    }

    public void reloadConfig() {
        try {
            String jsonContent = readFile(configFile);
            jsonConfig = new JSONObject(jsonContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getSkipLogin() {
        return jsonConfig.getJSONObject("db").getBoolean("skipLogin");
    }

    public void setSkipLogin(boolean skipLogin) throws FileNotFoundException {
        jsonConfig.getJSONObject("db").put("skipLogin", skipLogin);
        saveJsonToFile(jsonConfig.toString());
    }

    public String getUrl() {
        return jsonConfig.getJSONObject("db").getString("url");
    }

    public void setUrl(String url) throws FileNotFoundException {
        jsonConfig.getJSONObject("db").put("url", url);
        saveJsonToFile(jsonConfig.toString());
    }

    public String getUser() {
        return jsonConfig.getJSONObject("db").getString("user");
    }

    public void setUser(String user) throws FileNotFoundException {
        jsonConfig.getJSONObject("db").put("user", user);
        saveJsonToFile(jsonConfig.toString());
    }

    public String getPassword() {
        return jsonConfig.getJSONObject("db").getString("password");
    }

    public void setPassword(String password) throws FileNotFoundException {
        jsonConfig.getJSONObject("db").put("password", password);
        saveJsonToFile(jsonConfig.toString());
    }

    private String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    private void saveJsonToFile(String json) throws FileNotFoundException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
            // Обробка помилки, якщо не вдається зберегти JSON в файл.
        }
    }

    private boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private void createFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.createNewFile()) {
            // Файл створено успішно
        } else {
            throw new IOException("Не вдалося створити файл: " + filePath);
        }
    }
}
