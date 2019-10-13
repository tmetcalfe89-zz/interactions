package us.timinc.interactions;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import us.timinc.interactions.command.CommandInteract;
import us.timinc.interactions.event.InteractionHandler;

/**
 * The core mod file. Not to be confused with coremods.
 * 
 * @author Tim
 *
 */
@Mod(modid = Interactions.MODID, name = Interactions.NAME, version = Interactions.VERSION)
public class Interactions {
	/**
	 * The internal ID of the mod.
	 */
	public static final String MODID = "interactions";
	/**
	 * The human-readable name of the mod.
	 */
	public static final String NAME = "Interactions";
	/**
	 * The version of the mod. I'm sure I won't forget to update this. Probably.
	 */
	public static final String VERSION = "1.7";

	/**
	 * An instance of the mod. I forget why this is here.
	 */
	@Mod.Instance(Interactions.MODID)
	public static Interactions instance;

	public InteractionHandler interactionHandler;

	/**
	 * The logger for the mod. Output stuff here, not to System.
	 */
	public Logger logger;

	/**
	 * Handles preinitialization stuff. Fires up the real event handler.
	 * 
	 * @param event
	 *            The preinit event.
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		interactionHandler = new InteractionHandler();
	}

	/**
	 * Handles initialization stuff. Registers the interaction handler to the
	 * interaction event.
	 * 
	 * @param event
	 *            The init event.
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(interactionHandler);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandInteract());
	}
}
