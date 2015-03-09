package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.net.ActionSender;

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
    public static final int SNAKE_WEED_G = 1525;
    public static final int SNAKE_WEED_C = 1526;
    public static final int ARDRIGAL_G = 1527;
    public static final int ARDRIGAL_C = 1528;
    public static final int SITO_FOIL_G = 1529;
    public static final int SITO_FOIL_C = 1530;
    public static final int VOLENCIA_MOSS_G= 1531;
    public static final int VOLENCIA_MOSS_C = 1532;
    public static final int ROGUES_PURSE_G = 1533;
    public static final int ROGUES_PURSE_C = 1534;
    
    //Positions
    //public static final Position POSITION = new Position(0, 0, 0);
    public static final Position ROGUES_PURSE_CAVE= new Position(2830, 9522, 0);
    public static final Position ROGUES_PURSE_CAVE_EXIT= new Position(2823, 3119, 0);
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
    public static final int ROUGES_CAVE_FUNGUS = 2583; 
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
    public void handleDeath(final Player player, final Npc died) {
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
	player.getActionSender().sendItemOnInterface(12145, 250, SNAKE_WEED_C); //zoom, then itemId
	player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
	player.getActionSender().sendString("You are rewarded: ", 12146);
	player.getActionSender().sendString("1 Quest Point", 12150);
	player.getActionSender().sendString("Some herb exp.", 12151);
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
        int lastIndex = 0;
	switch (questStage) {
	    case QUEST_STARTED:
		lastIndex = 1;
		break;
	    case GET_SNAKE:
		lastIndex= 3;
		break;
	    case GET_ARDRIGAL:
		lastIndex= 6;
		break;
	    case GET_SITO:
		lastIndex= 9;
		break;
	    case GET_PURSE:
		lastIndex= 12;
		break;
	    case QUEST_COMPLETE:
		lastIndex = 17;
		break;
		
	}
	ActionSender a = player.getActionSender();
                a.sendQuestLogString("Talk to Trufits in Tai wa bow village to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Trufits wants some Snake weed that grows near", 3, this.getQuestID(),GET_SNAKE);
		a.sendQuestLogString("The river to the south west of the village", 4, this.getQuestID(),GET_SNAKE);
		a.sendQuestLogString("Now he wants some Ardrigal, which grows", 6, this.getQuestID(), GET_ARDRIGAL);
		a.sendQuestLogString("on some palm trees to the north east of the village.", 7, this.getQuestID(), GET_ARDRIGAL);
		a.sendQuestLogString("I am supposed to get Sito Foil, which likes", 9, this.getQuestID(), GET_SITO);
		a.sendQuestLogString("burned ground, maybe there is some around the village.", 10, this.getQuestID(), GET_SITO);
		a.sendQuestLogString("I have to get some Volencia Moss, sounds like it grows on", 12, this.getQuestID(), GET_MOSS);
		a.sendQuestLogString("rocks that have a high metal content, there is a mine near me I should check.", 13, this.getQuestID(), GET_MOSS);
		a.sendQuestLogString("The last herb is Rouges Purse, and it only grows in a cave to the north of the village", 15, this.getQuestID(), GET_PURSE);
		a.sendQuestLogString("I have completed the quest and helped make the jungle potion.", 17, this.getQuestID(), QUEST_COMPLETE);
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
		case SNAKE_WEED_G :
			player.getInventory().replaceItemWithItem(new Item(SNAKE_WEED_G), new Item(SNAKE_WEED_C));
			return true;
	   	case ARDRIGAL_G :
			player.getInventory().replaceItemWithItem(new Item(ARDRIGAL_G), new Item(ARDRIGAL_C));
                        return true;
                case SITO_FOIL_G :
                        player.getInventory().replaceItemWithItem(new Item(SITO_FOIL_G), new Item(SITO_FOIL_C));
                        return true;
                case ROGUES_PURSE_G :
                        player.getInventory().replaceItemWithItem(new Item(ROGUES_PURSE_G), new Item(ROGUES_PURSE_C));
                        return true;
                case VOLENCIA_MOSS_G :
                        player.getInventory().replaceItemWithItem(new Item(VOLENCIA_MOSS_G), new Item(VOLENCIA_MOSS_C));
                        return true;

 
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
	  /**  case  SNAKE_WEED_VINE : //snake weed pick
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
		return true; **/
	   case  ROUGES_CAVE_HANDHOLDS :
		Ladders.climbLadder(player, ROGUES_PURSE_CAVE_EXIT);
		//player.fadeTeleport(ROUGES_PURSE_CAVE_EXIT);
		return true;
	}
	return false;
    }

    public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
	switch (object) {
	   case  SNAKE_WEED_VINE : //snake weed pick
                        player.getInventory().addItemOrDrop(new Item(SNAKE_WEED_G));
                        player.getActionSender().sendMessage("You grab a bit of Snake weed", true ); //Spare controls crate
                return true;
            case  ARDRIGAL_TREE ://palm tree pick
                        player.getInventory().addItemOrDrop(new Item(ARDRIGAL_G));
                        player.getActionSender().sendMessage("You manage to grab some Ardrigal from the tree", true );
                return true;
           case  SITO_GROUND : //burnt ground search
                        player.getInventory().addItemOrDrop(new Item(SITO_FOIL_G));
                        player.getActionSender().sendMessage("You sweep away some dirt and find some Sito Foil", true );
                return true;
           case  ROUGES_CAVE_FUNGUS ://cave fungus
                        player.getInventory().addItemOrDrop(new Item(ROGUES_PURSE_G));
                        player.getActionSender().sendMessage("You pull some Rogues purse from the wall", true );
                return true;
           case  VOLENCIA_ROCK : //rocks that have herbs
                        player.getInventory().addItemOrDrop(new Item(VOLENCIA_MOSS_G));
                        player.getActionSender().sendMessage("You find some Moss on the rock.", true );
                return true;
           case  ROUGE_CAVE_ROCKS :
                //player.fadeTeleport(ROUGES_PURSE_CAVE);
                Ladders.climbLadder(player, ROGUES_PURSE_CAVE);
                return true;

	}
	return false;
    }

    public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
	DialogueManager d = player.getDialogue();
	switch (WITCHDOCTA) { //Npc ID
	    case WITCHDOCTA:
		switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
		    case 0:
			switch (d.getChatId()) {
				case 1:
					if (!QuestHandler.questCompleted(player, 10)){
						d.sendNpcChat("Go away, I need to work on this potion", Dialogues.ANNOYED);
						d.endDialogue();
						return true;
					}else {
						d.sendNpcChat("Greetings Bwana, I am Trufius Shakaya", "of the Tai Bow Wannai village", Dialogues.CONTENT);
						return true;
					}
				case 2:
						d.sendNpcChat("Welcome to my humble village.", Dialogues.CONTENT);
						return true;
						
					
				case 3:
					d.sendOption("What does Bwana mean?", "Tai Bow Wannai, what does that mean?", "It's a nice village were is everyone?");
					return true;
				case 4:
					switch(optionId) {
						case 1:
							d.sendNpcChat("It means outsider.", Dialogues.CONTENT);
							d.setNextChatId(3);
							return true;
						case 2:
							d.sendNpcChat("It means were da white women at", Dialogues.CONTENT);
                                                        d.setNextChatId(3);
							return true;
						case 3:
							d.sendNpcChat("My people are afraid to stay in the village. They have", "returned to the jungle. I need to commune with the", "gods to see what fate befalls us.", Dialogues.CONTENT);
							d.setNextChatId(5);
							return true;
					}
					return false;
				case 5:
					d.sendNpcChat("You may be able to help with this.", Dialogues.CONTENT);
					return true;
				case 6:
					d.sendOption("Me? How can I help?", "I'm sorry but I'm very busy");
					return true;
				case 7:
					switch(optionId) {
						case 1:
							d.sendPlayerChat("Me? How can I help?", Dialogues.CONTENT);
							d.setNextChatId(8);
							return true;
						case 2:
							d.sendPlayerChat("I'm much too busy to help a savage.", Dialogues.CONTENT);
							d.endDialogue();
							return true;
					}
					return false;
				case 8:
					d.sendNpcChat("I need to make a special brew! A potion that helps me", "to commune with the gods. For this potion, I need very", "special herbs, that are only found in the deep jungle.", Dialogues.CONTENT);
					return true;
				case 9:
					d.sendNpcChat("I can only guide you so far as the herbs are not easy", "to find. With some luck, you will find each herb in turn", "and bring it to me. I will then give you details of where", "to find the next herb.", Dialogues.CONTENT);
					return true;
				case 10:
					d.sendNpcChat("In return for this favor I will give you training", "in Herblore.", CONTENT);
					return true;
				case 11:
					d.sendOption("hmmm sounds difficult, I don't know if I'm ready for the challenge", "It sounds just like the challenge for me.");
					return true;
				case 12:
					switch(optionId) {
						case 1:
							d.sendNpcChat("Well come back to me when you git gud then.", CONTENT);
							d.endDialogue();
							return true;
						case 2:
							d.sendNpcChat("That is excellent Bwana! The first herb you need", "to gather is called Snake Weed.", CONTENT);
							d.setNextChatId(13);
							return true;
					}
					return false;						
				case 13:
					d.sendNpcChat("It grows near vines in an area to the south west were", "the ground turns soft and the water kisses your feet.", CONTENT);
					player.setQuestStage(this.getQuestID(), GET_SNAKE);
					d.endDialogue();
					return true;
			}
			return false;
		    case GET_SNAKE:
			switch (d.getChatId()) {
				case 1:
					d.sendNpcChat("Did you bring me the Snake Weed?", CONTENT);
					return true;
				case 2:
					d.sendOption("Yes it's right here.", "Oh that's what i need");
				case 3:
					switch(optionId) {
						case 1:
						if (player.getInventory().playerHasItem(SNAKE_WEED_C)) {
							d.sendNpcChat("Great you have the Snake Weed! Many thanks. Ok,", "the next herb is called Ardrigal. It is related to the palm.", "and grows to the east in it's brothers shady profusion.", CONTENT);
							player.getInventory().removeItem(new Item(SNAKE_WEED_C));
							player.setQuestStage(this.getQuestID(), GET_ARDRIGAL);
							return true;
						} else {
							d.sendNpcChat("Don't lie to me, return to me when you have the Snake weed.", CONTENT);
							d.endDialogue();
							return true;
						}
						case 2:
							d.sendNpcChat("Well return to me when you have it then", CONTENT);
							return true;
					}
					return false;
			}
			return false;
		    case GET_ARDRIGAL:
			switch (d.getChatId()) {
				case 1:
				d.sendNpcChat("Did you bring me the Ardrigal?", CONTENT);
                                        return true;
				case 2:
                                        d.sendOption("Yes it's right here.", "Oh that's what I need");
                                case 3:
                                        switch(optionId) {
                                                case 1:
                                                if (player.getInventory().playerHasItem(ARDRIGAL_C)) {
						d.sendNpcChat("Great you have the Ardrigal! Many thanks.", CONTENT);
						player.getInventory().removeItem(new Item(ARDRIGAL_C));
						d.setNextChatId(4);
                                                return true;
						} else {
						d.sendNpcChat("Don't lie to me, return to me when you have the Ardrigal.", CONTENT);
                                                        d.endDialogue();
                                                        return true;
						}
						case 2:
							d.sendNpcChat("Well return to me when you have it then", CONTENT);
                                                        return true;
						}
					return false;
				case 4:
					d.sendNpcChat("You are doing well Bwana. The next herb is called Sito", "Foil, and it grows best were the ground has been", "blacked by living flame.", CONTENT);
					player.setQuestStage(this.getQuestID(), GET_SITO);
			}
			return false;
		   case GET_SITO:
                        switch (d.getChatId()) {
                                case 1:
                                d.sendNpcChat("Did you bring me the Sito Foil?", CONTENT);
                                        return true;
                                case 2:
                                        d.sendOption("Yes it's right here.", "Oh that's what I need");
                                case 3:
                                        switch(optionId) {
                                                case 1:
                                                if (player.getInventory().playerHasItem(ARDRIGAL_C)) {
                                                d.sendNpcChat("Well done Bwana, only two more herbs to collect.", CONTENT);
                                                player.getInventory().removeItem(new Item(ARDRIGAL_C));
                                                player.setQuestStage(this.getQuestID(), GET_MOSS);
						d.setNextChatId(4);
                                                return true;
                                                } else {
                                                d.sendNpcChat("Don't lie to me, return to me when you have the Sito Foil.", CONTENT);
                                                        d.endDialogue();
                                                        return true;
                                                }
                                                case 2:
                                                        d.sendNpcChat("Well return to me when you have it then", CONTENT);
                                                        return true;
                                                }
                                        return false;
				case 4:
					d.sendNpcChat("The next herb is called Volencia Moss. It clings to", "rocks for its existence. It is difficult to see, so you", "must search for it well.", CONTENT);
					return true;
				case 5:
					d.sendNpcChat("It prefers rocks of high metal content and a frequently", "disturbed enviroment. There is some, I belive to the", "south east of this village.", CONTENT);
					d.endDialogue();
					return true;
                        }
                        return false;
		    case GET_MOSS:
                        switch (d.getChatId()) {
                                case 1:
                                d.sendNpcChat("Did you bring me the Volencia Moss?", CONTENT);
                                        return true;
                                case 2:
                                        d.sendOption("Yes it's right here.", "Oh that's what I need");
                                case 3:
                                        switch(optionId) {
                                                case 1:
                                                if (player.getInventory().playerHasItem(VOLENCIA_MOSS_C)) {
                                                d.sendNpcChat("Ah Volencia Moss, beautiful. One final herb and the", "potion will be complete", CONTENT);
                                                player.getInventory().removeItem(new Item(VOLENCIA_MOSS_C));
                                                player.setQuestStage(this.getQuestID(), GET_PURSE);
                                                d.setNextChatId(4);
                                                return true;
                                                } else {
                                                d.sendNpcChat("Don't lie to me, return to me when you have the Volencia Moss.", CONTENT);
                                                        d.endDialogue();
                                                        return true;
                                                }
                                                case 2:
                                                        d.sendNpcChat("Well return to me when you have it then", CONTENT);
                                                        return true;
                                                }
                                        return false;
                                case 4:
                                        d.sendNpcChat("This is the most difficult to find as it inhabits the", "darkness of the underground. It is called Rogues", "Purse, and is only to be found in the caverns", CONTENT);
					return true;
				case 5:
					d.sendNpcChat("in the nothern part of the island. A secret entrance to", "the cavern is set into the nothern cliffs of this island.", "Take care Bwana as it may be dangerous", CONTENT);
                                        d.endDialogue();
                        }
                        return false;
		case GET_PURSE:
                        switch (d.getChatId()) {
                                case 1:
                                d.sendNpcChat("Did you bring me the Rouges Purse?", CONTENT);
                                        return true;
                                case 2:
                                        d.sendOption("Yes it's right here.", "Oh that's what I need");
                                case 3:
                                        switch(optionId) {
                                                case 1:
                                                if (player.getInventory().playerHasItem(ROGUES_PURSE_C)) {
                                                d.sendNpcChat("Ah Volencia Moss, beautiful. One final herb and the", "potion will be complete", CONTENT);
                                                player.getInventory().removeItem(new Item(ROGUES_PURSE_C));
                                                player.setQuestStage(this.getQuestID(), QUEST_COMPLETE);
                                                d.setNextChatId(4);
                                                return true;
                                                } else {
                                                d.sendNpcChat("Don't lie to me, return to me when you have the Rouges Purse.", CONTENT);
                                                        d.endDialogue();
                                                        return true;
                                                }
                                                case 2:
                                                        d.sendNpcChat("Well return to me when you have it then", CONTENT);
                                                        return true;
                                                }
                                        return false;
                                case 4:
					d.sendNpcChat("Many blessings on you! I must prepare, please", "excuse me while I make the arangements.", CONTENT);
					d.endDialogue();
					return true;
			}
			return false;

		    case QUEST_COMPLETE:
			switch (d.getChatId()) {
			    case 1:
				d.sendNpcChat("I can see forever.", Dialogues.HAPPY);
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
