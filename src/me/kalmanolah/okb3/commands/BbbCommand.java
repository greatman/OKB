package me.kalmanolah.okb3.commands;

import me.kalmanolah.okb3.OKmain;

public class BbbCommand extends BaseCommand
{

    public BbbCommand()
    {
        this.command.add("bbb");
        this.commandOnly = false;
        this.helpDescription = "Shows OKB3 help";
        this.senderMustBePlayer = false;
    }
    
    public void perform()
    {
        for (BaseCommand OKBCommand : OKmain.p.commands)
        {
            if (OKBCommand.hasPermission(sender))
            {
                sendMessage(OKBCommand.getUseageTemplate(true));
            }
        }
    }
}
