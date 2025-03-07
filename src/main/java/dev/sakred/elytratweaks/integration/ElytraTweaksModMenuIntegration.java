package dev.sakred.elytratweaks.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.sakred.elytratweaks.config.ElytraTweaksConfigScreen;

public class ElytraTweaksModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ElytraTweaksConfigScreen.create(parent);
    }
}