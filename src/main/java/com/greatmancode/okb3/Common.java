/*
 * This file is part of OKB3.
 *
 * Copyright (c) 2011-2012, Greatman <http://github.com/greatman/>
 *
 * OKB3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OKB3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with OKB3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.okb3;

import java.util.logging.Logger;

import com.greatmancode.okb3.commands.CommandManager;
import com.greatmancode.okb3.database.DatabaseHandler;

public class Common {
	private Logger logger;
	private static Common instance;
	private Caller caller;
	private DatabaseHandler databaseHandler;
	private CommandManager commandManager;

	public Common(Loader loader, Logger logger) {
		instance = this;
		this.logger = logger;
		if (loader instanceof BukkitLoader) {
			caller = new BukkitCaller(loader);
		} else if (loader instanceof SpoutLoader) {
			caller = new SpoutCaller(loader);
		}
		getServerCaller().startMetrics();
		commandManager = new CommandHandler();
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public Caller getServerCaller() {

		return caller;
	}

	public static Common getInstance() {
		return instance;
	}

	public void disable() {
		// TODO Auto-generated method stub

	}

	public Logger getLogger() {
		return logger;
	}

	public DatabaseHandler getDatabaseHandler() {
		return databaseHandler;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

}
