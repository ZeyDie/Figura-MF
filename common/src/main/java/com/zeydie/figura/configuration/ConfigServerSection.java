package com.zeydie.figura.configuration;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class ConfigServerSection {
    private final boolean localhost = false;

    @Getter
    private final @NotNull String serverIP = this.localhost ? "localhost" : "d1.minefite.net:25565";

    @Getter
    private final @NotNull String backendIP = this.localhost ? "localhost" : "d1.minefite.net:25566";
}