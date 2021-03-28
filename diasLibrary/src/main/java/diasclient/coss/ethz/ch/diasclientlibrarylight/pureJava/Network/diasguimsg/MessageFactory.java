package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

import com.google.gson.Gson;

public class MessageFactory 
{


	private final Gson 			gson;		// Google's JSON converter  
	
	private String OriginalMessage,
								MessageType;
	
	private Object CrackedMessage;
	
	private GUIMessage			guiMessage;
	
	public MessageFactory()
	{
		gson = new Gson();
	}
	 
	public MessageFactory(String msg)
	{
		gson = new Gson();
		
		CrackMessage(msg);
	}
	
	
	public GUIMessage CrackMessage( String Message )
	{
		OriginalMessage = Message;
		CrackedMessage = null;
		guiMessage = null;
		
		// convert to GUI message
		guiMessage = gson.fromJson(Message, GUIMessage.class);
	
		
		return GetGUIMessage();
	}
	
	public Object CastMessage(Class TargetClass )
	{
		return gson.fromJson(OriginalMessage, TargetClass);
	}
	
	public GUIMessage GetGUIMessage()
	{
		return guiMessage;
	}
	
	
	
}
