package com.greatmancode.okb3.commands;

import com.greatmancode.okb3.OKB;

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
        for (BaseCommand OKBCommand : OKB.p.commands)
        {
            if (OKBCommand.hasPermission(sender))
            {
                sendMessage(OKBCommand.getUseageTemplate(true));
            }
        }
    }
}
