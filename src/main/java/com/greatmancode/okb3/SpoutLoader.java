package com.greatmancode.okb3;

import java.io.File;
import java.io.IOException;

import org.spout.api.plugin.CommonPlugin;

import com.greatmancode.okb3.utils.MetricsSpout;

/**
 * Class used when the plugin is loaded from Spout.
 * @author greatman
 * 
 */
public class SpoutLoader extends CommonPlugin implements Loader{

	private MetricsSpout metrics;

	@Override
	public void onEnable() {
		this.loadLibrary(new File("natives" + File.separator + "sqlite-jdbc-3.7.2.jar"));
		this.loadLibrary(new File("natives" + File.separator + "mysql-connector-java-5.1.14.jar"));
		try {
			metrics = new MetricsSpout(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
		new Common(this, getLogger()).initialize();
	}

	@Override
	public void onDisable() {
		Common.getInstance().disable();
	}

	public MetricsSpout getMetrics() {
		return metrics;
	}

	public boolean isBukkit() {
		return false;
	}
}
