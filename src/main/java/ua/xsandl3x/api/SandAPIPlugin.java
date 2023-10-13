package ua.xsandl3x.api;

import org.bukkit.plugin.java.JavaPlugin;
import ua.xsandl3x.api.commands.service.impl.SimpleCommandService;

public final class SandAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new SimpleCommandService().register(new TestCommand());
    }

    @Override
    public void onDisable() {

    }
}
