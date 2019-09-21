package us.timinc.interactions.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import us.timinc.interactions.Interactions;
import us.timinc.interactions.util.MinecraftUtil;

/**
 * Holds the commands for this mod.
 * 
 * @author Tim
 *
 */
public class CommandInteract implements ICommand {

	/**
	 * Holds the aliases for the root command.
	 */
	private final ArrayList aliases;
	/**
	 * Holds the tab complets.
	 */
	private final ArrayList tabCompletes;

	/**
	 * Creates a new instance of this class.
	 */
	public CommandInteract() {
		aliases = new ArrayList();
		aliases.add("ia");
		aliases.add("inter");

		tabCompletes = new ArrayList();
		tabCompletes.add("reload");
	}

	@Override
	public int compareTo(ICommand command) {
		return 0;
	}

	@Override
	public String getName() {
		return "interactions";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "interactions <reload>";
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			switch (args[0]) {
			case "reload":
				Interactions.instance.interactionHandler.reload();
				MinecraftUtil.sayTo(sender, String.join(" ", "Reloaded",
						Interactions.instance.interactionHandler.interactRecipes.getRecipeCount() + "", "recipes."));
				break;
			default:
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return tabCompletes;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
