package com.zeydie.figura.configuration;

import lombok.Getter;

@Getter
public final class ConfigMainSection {
    private final String permissionsUsernameURL = "https://admin.minefite.net/fs/figura-models.php?username=%s";
    private final String permissionsUuidURL = "https://admin.minefite.net/fs/figura-models.php?uuid=%s";

    private final ConfigServerSection serverSection = new ConfigServerSection();
    private final ConfigButtonSection buttonSection = new ConfigButtonSection();
    private final ConfigCommandSection commandSection = new ConfigCommandSection();
}