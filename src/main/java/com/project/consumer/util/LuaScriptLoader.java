package com.project.consumer.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class LuaScriptLoader {

  public static String load(String path) {
    try (InputStream is = LuaScriptLoader.class.getClassLoader().getResourceAsStream(path)) {

      if (is == null) {
        throw new IllegalStateException("Lua script not found: " + path);
      }

      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load lua script", e);
    }
  }
}
