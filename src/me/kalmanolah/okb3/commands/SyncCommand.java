package me.kalmanolah.okb3.commands;

import org.bukkit.ChatColor;

import me.kalmanolah.okb3.OKFunctions;

public class SyncCommand extends BaseCommand
{
	public SyncCommand()
	{
		this.command.add("sync");
		this.helpDescription = "Sync your account with the forum";
		this.optionalParameters.add("Username");
		this.optionalParameters.add("Password");
	}
	
	public void perform()
	{
		if (((Integer) OKFunctions.getConfig("mode") == 1) && this.parameters.size() == 2)
		{
			String encpass = OKFunctions.getEncPass(this.parameters.get(0), this.parameters.get(1));
			if (encpass.equals("nope")) {
				sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "Incorrect username or password.");
			} else {
				OKFunctions.updateSecure(sender, player, player.getName(), this.parameters.get(0), this.parameters.get(1), false);
			}
        }
        else if (((Integer) OKFunctions.getConfig("mode") == 0) && (this.parameters.size() == 0))
        {
            OKFunctions.updateNormal(sender, player, player.getName(), false);
		} else {
			sendMessage(this.getUseageTemplate(false));
		}
	}

}
