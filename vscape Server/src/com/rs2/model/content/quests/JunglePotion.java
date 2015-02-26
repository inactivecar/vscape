package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.objects.functions.Ladders;

public class JunglePotion implements Quest {
    public static final int questIndex = 7365; //Used in player's quest log interface, id is in Player.java //Change
    //Quest stages
    public static final int QUEST_STARTED = 1;
    public static final int GET_SNAKE = 2;
    public static final int GET_ARDRIGAL = 3;
    public static final int GET_SITO = 4;
    public static final int GET_MOSS = 5;
    public static final int GET_PURSE = 6;
    public static final int QUEST_COMPLETE = 7;

    //Items
    public static final int SNAKE_WEED = 1526;
    public static final int ARDRIGAL = 1528;
    public static final int SITO_FOIL = 1530;
    public static final int VOLENCIA_MOSS = 1532;
    public static final int ROGUES_PURSE = 1534;
    
    //Positions
    //public static final Position POSITION = new Position(0, 0, 0);
    public static final Position ROGUES_PURSE_CAVE= new Position(2830, 9522, 0);
    public static final Position ROGUES_PURSE_CAVE_EXIT= new Position(2823, 3319, 0);
    //Interfaces
    //public static final int INTERFACE = -1;

    //Npcs
    //public static final int NPC = -1;
    public static final int WITCHDOCTA = 740;

    //Objects
    public static final int SNAKE_WEED_VINE = 2575;
    public static final int ARDRIGAL_TREE = 2577;
    public static final int SITO_GROUND = 2579;
    public static final int VOLENCIA_ROCK = 2581;
    public static final int ROUGE_CAVE_ROCKS = 2584;
    public static final int ROUGES_CAVE_HANDHOLDS = 2585;
    public static final int ROUGES_CAVE_FUNGUS = 2683; 
    public int dialogueStage = 0;

    private int reward[][] = { //{itemId, count},

    };

    private int expReward[][] = { 
	{15, 775}
    };

    private static final int questPointReward = 1; //Change

    public int getQuestID() { //Change
	return 39;
    }

    public String getQuestName() { //Change
	return "Jungle Potion";
    }

    public String getQuestSaveName() { //Change
	return "junglepotion";
    }

    public boolean canDoQuest(final Player player) {
	return true;
    }

    public void getReward(Player player) {
	for (int[] rewards : reward) {
	    player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
	}
	for (int[] expRewards : expReward) {
	    player.getSkill().addExp(expRewards[0], (expRewards[1]));
	}
	player.addQuestPoints(questPointReward);
	player.getActionSender().QPEdit(player.getQuestPoints());
    }

    public void completeQuest(Player player) {
	getReward(player);
	player.getActionSender().sendInterface(12140);
	player.getActionSender().sendItemOnInterface(12145, 250, SNAKE_WEED); //zoom, then itemId
	player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
	player.getActionSender().sendString("You are rewarded: ", 12146);
	player.getActionSender().sendString("1 Quest Point", 12150);
	player.getActionSender().sendString("", 12151);
	player.getActionSender().sendString("", 12152);
	player.getActionSender().sendString("", 12153);
	player.getActionSender().sendString("", 12154);
	player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
	player.getActionSender().sendString(" ", 12147);
	player.setQuestStage(getQuestID(), QUEST_COMPLETE);
	player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
    }

    public void sendQuestRequirements(Player player) {
	int questStage = player.getQuestStage(getQuestID());
	switch (questStage) {
	    case QUEST_STARTED:
		player.getActionSender().sendString("@str@" + "Talk to Trufitus in Tai Bwo Wannai to begin this quest.", 8147);
		//Change
		player.getActionSender().sendString("", 8149);
		break;
	    case QUEST_COMPLETE:
		player.getActionSender().sendString("@str@" + "", 8147);
		//Change
		player.getActionSender().sendString("@red@" + "You have completed this quest!", 8177);
		break;
	    default:
		player.getActionSender().sendString("Talk to @dre@ x @bla@in the @dre@ x @bla@to begin.", 8147);
		player.getActionSender().sendString("@dre@Requirements:", 8148);
		if (QuestHandler.questCompleted(player, -1)) {
		    player.getActionSender().sendString("@str@-Template Quest.", 8150);
		} else {
		    player.getActionSender().sendString("@dbl@-Template Quest.", 8150);
		}
		//Change
		break;
	}
    }

    public void sendQuestInterface(Player player) {
	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }

    public void startQuest(Player player) {
	player.setQuestStage(getQuestID(), QUEST_STARTED);
	player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
    }

    public boolean questCompleted(Player player) {
	int questStage = player.getQuestStage(getQuestID());
	if (questStage >= QUEST_COMPLETE) {
	    return true;
	}
	return false;
    }

    public void sendQuestTabStatus(Player player) {
	int questStage = player.getQuestStage(getQuestID());
	if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
	    player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
	} else if (questStage == QUEST_COMPLETE) {
	    player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	} else {
	    player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
	}
    }

    public int getQuestPoints() {
	return questPointReward;
    }

    public void showInterface(Player player) {
	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	player.getActionSender().sendString(getQuestName(), 8144);
    }

    public void dialogue(Player player, Npc npc) {
	//Don't even need this anymore really
    }

    public int getDialogueStage(Player player) {
	return dialogueStage;
    }

    public void setDialogueStage(int in) {
	dialogueStage = in;
    }
    
    public boolean itemHandling(final Player player, int itemId) {
	switch(itemId) {
	    
	}
	return false;
    }

    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
	return false; 
    }
    
    public boolean doItemOnObject(final Player player, int object, int item) {
	return false;
    }

    public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
	return false;
    }

    public boolean doNpcClicking(Player player, Npc npc) {
	return false;
    }

    public boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch (object) {
	    case  SNAKE_WEED_VINE : //snake weed pick
			player.getInventory().addItemOrDrop(new Item(SNAKE_WEED));
			player.getActionSender().sendMessage("You grab a bit of Snake weed", true ); //Spare controls crate
		return true;
	    case  ARDRIGAL_TREE ://palm tree pick
                        player.getInventory().addItemOrDrop(new Item(ARDRIGAL));
                        player.getActionSender().sendMessage("You manage to grab some Ardrigal from the tree", true );
		return true;
	   case  SITO_GROUND : //burnt ground search
			player.getInventory().addItemOrDrop(new Item(SITO_FOIL));
                        player.getActionSender().sendMessage("You sweep away some dirt and find some Sito Foil", true );
		return true;
	   case  ROUGES_CAVE_FUNGUS ://cave fungus
                        player.getInventory().addItemOrDrop(new Item(ROGUES_PURSE));
                        player.getActionSender().sendMessage("You pull some Rogues purse from the wall", true );
                return true;
	   case  VOLENCIA_ROCK : //rocks that have herbs
			player.getInventory().addItemOrDrop(new Item(VOLENCIA_MOSS));
                        player.getActionSender().sendMessage("You find some Moss on the rock.", true );
                return true;
	   case  ROUGE_CAVE_ROCKS :
		//player.fadeTeleport(ROUGES_PURSE_CAVE);
		Ladders.climbLadder(player, ROGUES_PURSE_CAVE);
		return true;
	   case  ROUGES_CAVE_HANDHOLDS :
		Ladders.climbLadder(player, ROGUES_PURSE_CAVE_EXIT);
		//player.fadeTeleport(ROUGES_PURSE_CAVE_EXIT);
		return true;
	}
	return false;
    }

    public static boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
	switch (object) {

	}
	return false;
    }

    public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
	DialogueManager d = player.getDialogue();
	switch (WITCHDOCTA) { //Npc ID
	    case WITCHDOCTA:
		switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
		    case QUEST_COMPLETE:
			switch (d.getChatId()) {
			    case 1:
				d.sendNpcChat("Thank you again!", Dialogues.HAPPY);
				d.endDialogue();
				return true;
			}
			return false;
		}
		return false;
	}
	return false;
    }

}
