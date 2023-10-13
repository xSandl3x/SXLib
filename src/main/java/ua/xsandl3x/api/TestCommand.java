package ua.xsandl3x.api;

import org.bukkit.entity.Player;
import ua.xsandl3x.api.commands.annotation.Completer;
import ua.xsandl3x.api.commands.context.ICommandContext;
import ua.xsandl3x.api.commands.impl.SimpleCommand;

public class TestCommand extends SimpleCommand<Player> {
    public TestCommand() {
        super("jer");
    }

    @Completer({"apply", "issues"})
    @Override
    public void execute(ICommandContext<Player> context) {
        context.getSource().sendMessage("gogogogoggog");
    }
}
