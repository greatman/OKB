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

import java.util.List;

public interface OKBSync {
	/**
	 * Check if a account exist on the website
	 * 
	 * @param username The player account username on the website
	 * @param password The player account password on the website
	 * @return True if it exists, else false
	 */
	public boolean accountExist(String username, String password);

	/**
	 * Change a player rank on the website
	 * 
	 * @param username The player account username on the website
	 * @param forumGroupId The group ID on the forum to promote to
	 */
	public void changeRank(String username, int forumGroupId);

	/**
	 * Ban a player on the website
	 * 
	 * @param username The player account username on the website
	 * @param forumGroupId The group ID on the forum for the banned group
	 * @return True if success, else false
	 */
	public void ban(String username, int forumGroupId);

	/**
	 * Ban a player on the website
	 * 
	 * @param username The player account username on the website
	 * @param forumGroupId The group ID on the website to set the user back to
	 * @return True if success, else false
	 */
	public void unban(String username, int forumGroupId);

	/**
	 * Get the website group ID of a user
	 * 
	 * @param username The player account username on the website
	 * @return A list of group ID
	 */
	public List<Integer> getGroup(String username);
}
