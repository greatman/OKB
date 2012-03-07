package me.kalmanolah.okb3.commands;

public class BbbCommand extends BaseCommand
{

	public BbbCommand()
	{
		this.command.add("bbb");
		this.commandOnly = false;
		this.helpDescription = "Shows OKB3 help";
	}
}
