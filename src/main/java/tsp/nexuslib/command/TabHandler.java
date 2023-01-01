package tsp.nexuslib.command;

import org.bukkit.command.CommandSender;

import java.util.List;

@FunctionalInterface
public interface TabHandler {

    List<String> getCompletions(CommandSender sender, String[] args);

}
