package ua.xsandl3x.api.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.command.*;
import java.lang.invoke.*;

@UtilityClass
public class CommandMapUtil {

    private final CommandMap COMMAND_MAP;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Server server = Bukkit.getServer();
            MethodType methodType = MethodType.methodType(SimpleCommandMap.class);
            COMMAND_MAP = (CommandMap) lookup.findVirtual(server.getClass(), "getCommandMap", methodType).invoke(server);
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public Command getCommandByName(String commandName) {
        return COMMAND_MAP.getCommand(commandName);
    }

    public CommandMap getCommandMap() {
        return COMMAND_MAP;
    }
}
