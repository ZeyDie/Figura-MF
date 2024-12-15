package com.zeydie.figura.permissions;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.figuramc.figura.FiguraMod;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class FiguraModelPermissions {
    private static final @NotNull List<String> allowedModels = new ArrayList<>();
    private static long delayed;

    public static @NotNull LocalPlayer getSession() {
        return Minecraft.getInstance().player;
    }

    public static @NotNull UUID getUUID() {
        return getSession().getUUID();
    }

    public static @NotNull String getUsername() {
        return getSession().getName().getString();
    }

    @SneakyThrows
    public static void updateAllowedModels() {
        if (delayed >= System.currentTimeMillis()) return;
        else
            delayed = System.currentTimeMillis() + 1000 * 10;

        allowedModels.clear();

        @NonNull final String site = String.format(
                FiguraMod.getFiguraConfig().getConfigMainSection().getPermissionsUuidURL(),
                getUUID()
        );

        @NonNull final URL url = new URL(site);
        @NonNull final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        @Cleanup @NonNull final InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
        @Cleanup @NonNull final BufferedReader reader = new BufferedReader(inputStreamReader);

        for (String line; (line = reader.readLine()) != null; )
            addModel(line);
    }

    private static void addModel(@NonNull final String line) {
        if (line.contains(" ")) {
            final String[] strings = line.split(" ");

            for (final String string : strings) {
                final String file = string
                        .replaceAll("\\r", "")
                        .replaceAll("\\n", "");
                allowedModels.add(file);
            }
        } else {
            final String file = line
                    .replaceAll("\\r", "")
                    .replaceAll("\\n", "");
            allowedModels.add(file);
        }
    }

    public static boolean hasPermission(@NonNull final File file) {
        return hasPermission(file.getParentFile().getName()) || hasPermission(file.getName());
    }

    public static boolean hasPermission(@NonNull final String model) {
        return allowedModels.contains(model);
    }
}