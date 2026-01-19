package com.project.consumer.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.project.global.exception.ApplicationException;
import com.project.global.exception.code.domain.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class LuaScriptLoader {

    public static String load(String path) {
        try (InputStream is = LuaScriptLoader.class.getClassLoader().getResourceAsStream(path)) {

            if (is == null) {
                throw new IllegalStateException("Lua script not found: " + path);
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to load lua script");
            throw new ApplicationException(GlobalErrorCode.LUA_SCRIPT_LOAD_INVALID);
        }
    }

    private LuaScriptLoader() {}
}
