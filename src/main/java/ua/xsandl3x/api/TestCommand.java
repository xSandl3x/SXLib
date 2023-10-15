package ua.xsandl3x.api;

import org.bukkit.entity.Player;
import ua.xsandl3x.api.commands.annotation.Completer;
import ua.xsandl3x.api.commands.annotation.SubCommand;
import ua.xsandl3x.api.commands.context.ICommandContext;
import ua.xsandl3x.api.commands.impl.ParentCommand;

public class TestCommand extends ParentCommand<Player> {
    public TestCommand() {
        super("jer");
    }

    @Completer({"apply", "issues"})
    @Override
    public void execute(ICommandContext<Player> context) {
        context.getSource().sendMessage("gogogogoggog");
    }

    @Completer({"hui"})
    @SubCommand(aliases = "hui")
    public void gg(ICommandContext<Player> context) {
        context.getSource().sendMessage("test subcommand 1!");
    }

    @Completer({"pop"})
    @SubCommand(aliases = "pop")
    public void jj(ICommandContext<Player> context) {
        context.getSource().sendMessage("test subcommand 2!!");
    }
}
