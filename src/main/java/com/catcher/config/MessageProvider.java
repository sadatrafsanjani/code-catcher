package com.catcher.config;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class MessageProvider {

    private static final String MESSAGES_FILE = "messages.properties";
    private final Properties messages = new Properties();

    public MessageProvider() {

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(MESSAGES_FILE)) {

            if (inputStream == null) {

                throw new IllegalStateException("Could not load " + MESSAGES_FILE);
            }

            messages.load(inputStream);

        }
        catch (IOException e) {
            throw new IllegalStateException("Could not load " + MESSAGES_FILE, e);
        }
    }

    public String get(String key) {

        return messages.getProperty(key);
    }

    public String get(String key, Object... arguments) {

        return MessageFormat.format(messages.getProperty(key), arguments);
    }
}