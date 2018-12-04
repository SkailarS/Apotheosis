package shadows;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class ApotheosisCore implements IFMLLoadingPlugin {

	public static boolean enableAnvil = true;
	public static boolean enableEnch = true;

	public static String updateRepair;
	public static String capsIsCreative;
	public static String empty;
	public static String drawForeground;
	public static String format;
	public static String calcStackEnch;
	static String pCapClass = "net/minecraft/entity/player/PlayerCapabilities";
	static String pStackClass = "net/minecraft/item/ItemStack";

	public static final Logger LOG = LogManager.getLogger("Apotheosis Core");

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "shadows.anvil.AnvilCapRemover", "shadows.ench.EnchCapRemover" };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		boolean dev = !(Boolean) data.get("runtimeDeobfuscationEnabled");
		updateRepair = dev ? "updateRepairOutput" : "e";
		capsIsCreative = dev ? "isCreativeMode" : "d";
		empty = dev ? "EMPTY" : "a";
		drawForeground = dev ? "drawGuiContainerForegroundLayer" : "c";
		calcStackEnch = dev ? "calcItemStackEnchantability" : "a";
		if (!dev) {
			pCapClass = FMLDeobfuscatingRemapper.INSTANCE.unmap(pCapClass);
			pStackClass = FMLDeobfuscatingRemapper.INSTANCE.unmap(pStackClass);
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	public static boolean isRepairOutput(MethodNode m) {
		return m.name.equals(updateRepair) && m.desc.equals("()V");
	}

	public static boolean isCapIsCreative(FieldInsnNode fn) {
		return fn.owner.equals(pCapClass) && fn.name.equals(capsIsCreative);
	}

	public static boolean isEmptyStack(FieldInsnNode fn) {
		return fn.owner.equals(pStackClass) && fn.name.equals(empty);
	}

	public static boolean isDrawForeground(MethodNode m) {
		return m.name.equals(drawForeground) && m.desc.equals("(II)V");
	}

	public static boolean isCalcStackEnch(MethodNode m) {
		return m.name.equals(calcStackEnch) && m.desc.equals("(Ljava/util/Random;IILnet/minecraft/item/ItemStack;)I");
	}

}