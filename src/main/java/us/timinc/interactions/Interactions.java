package us.timinc.interactions;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import us.timinc.interactions.event.InteractionHandler;

@Mod(modid = Interactions.MODID, name = Interactions.NAME, version = Interactions.VERSION)
public class Interactions {
	public static final String MODID = "interactions";
	public static final String NAME = "Interactions";
	public static final String VERSION = "1.0";

	@Mod.Instance(Interactions.MODID)
	public static Interactions instance;
	
	private InteractionHandler interactionHandler;

	public static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		interactionHandler = new InteractionHandler();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		interactionHandler.loadRecipes();
		MinecraftForge.EVENT_BUS.register(interactionHandler);
	}
}
