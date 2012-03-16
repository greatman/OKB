package me.kalmanolah.okb3;

public interface OKBSync
{
    /**
     * Check if a account exist on the website
     * @param username The player account username on the website
     * @param password The player account password on the website
     * @return True if it exists, else false
     */
    public boolean accountExist(String username, String password);
    
    /**
     * Change a player rank on the website
     * @param username The player account username on the website
     * @param forumGroupId The group ID on the forum to promote to
     */
    public void changeRank(String username, int forumGroupId);
    
    /**
     * Ban a player on the website
     * @param username The player account username on the website
     * @param forumGroupId The group ID on the forum for the banned group
     * @return True if success, else false
     */
    public void ban(String username, int forumGroupId);
    
    /**
     * Ban a player on the website
     * @param username The player account username on the website
     * @param forumGroupId The group ID on the website to set the user back to
     * @return True if success, else false
     */
    public void unban(String username, int forumGroupId);
    
    /**
     * Get the website group ID of a user
     * @param username The player account username on the website
     * @return The group ID. If none found return -1
     */
    public int getGroup(String username);
    
}
