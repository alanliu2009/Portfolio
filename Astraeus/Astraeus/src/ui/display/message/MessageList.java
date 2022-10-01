package ui.display.message;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

public class MessageList 
{
	protected ArrayList<Message> messages;
	
	public MessageList()
	{
		messages = new ArrayList<Message>();
	}
	
	public void render(Graphics g)
	{
		for(int i = 0; i < messages.size(); i++)
		{
			Message m = messages.get(i);
			m.updateID(i);
			m.render(g);
		}
	}
	
	public void addMessage(Message m)
	{
		messages.add(m);
	}
	
	public void update()
	{
		for(int i = 0; i < messages.size(); i++)
		{
			Message m = messages.get(i);
			
			if(m.isDone())
			{
				messages.remove(m);
				i--;
			}
			else
			{
				m.updateID(i);
				m.update();
				
			}
		}		
	}
	
	public void clear()
	{
		messages.clear();
	}
}
