// Alex Nguyen
// atn170001
import java.util.*;
import java.io.*;
public class Main {
    static final double THRESHOLD = 0.00000001;
	public static void main(String[] args) throws IOException
	{
		Scanner fileReader = null;
		Scanner input = new Scanner(System.in);
        String inputFilename;
        
        inputFilename = input.nextLine();
        File inFile = new File(inputFilename);

        try {
            fileReader = new Scanner(inFile);
        }
        catch (FileNotFoundException e)
        {
            input.close();
            throw e;
        }
        LinkList<Player> list = new LinkList<>();

        while(fileReader.hasNextLine()) // Read through file and get all players
        {
            Player player = null;
            boolean multipleEntries = false;
            String line = fileReader.nextLine();
            String name = line.substring(0, line.indexOf(' '));
            if(list.getHead() == null)  // If no node in linked list
            {
                player = new Player();
                player.setName(name);
            }
            else
            {
                for(Node<Player> n = list.getHead(); n != null; n = n.getNext())    // Checks for duplicate names in file
                {
                    if(n.getData().getName().equals(name)) // If duplicate names in files, combine into 1 player by adding stats to first occurence of player
                    {   
                        player = n.getData();
                        multipleEntries = true;
                    }
                    if(multipleEntries)
                        break;
                    else if(n.getNext() == null) // If no duplicate names in file, create new player
                    {
                        player = new Player();
                        player.setName(name);
                    }
                    else
                        continue;
                }
            }
            String battingRecord = line.substring(line.indexOf(' ') + 1, line.length()); // Gets player's batting record as a string

            int battingRecordSize = 0;

            for(int i = 0; i < battingRecord.length(); i++)
                battingRecordSize++;
            char[] battingRecordArray = new char[battingRecordSize]; // Create array of characters to store player's batting record in

            for(int i = 0; i < battingRecordSize; i++)
                battingRecordArray[i] = battingRecord.charAt(i); //Put character of batting record into char array
            for(int i = 0; i < battingRecordArray.length; i++) // Loop through batting record array of characters
            {
                switch(battingRecordArray[i])
                {
                    case 'H': // If character is H increase hits by 1
                        player.setHits(player.getHits() + 1);
                        break;
                    case 'O': // If character is O increase outs by 1
                        player.setOuts(player.getOuts() + 1);
                        break;
                    case 'K': // If character is K increase strikeouts by 1
                        player.setStrikeOuts(player.getStrikeouts() + 1);
                        break;
                    case 'W': // If character is W increase walks by 1
                        player.setWalks(player.getWalks() + 1);
                        break;
                    case 'P': // If character is P increase hit by pitch by 1
                        player.setHitByPitches(player.getHitByPitches() + 1);
                        break;
                    case 'S': // If character is S increase sacrifice by 1
                        player.setSacrifices(player.getSacrifices() + 1);
                        break;
                    default:
                        break;
                }
            }
            if(!multipleEntries) // If player read is not a duplicate create a new node and add to list, else do not create new node
            {
                Node<Player> node = new Node<>(player);
                list.appendList(node);
            }
        }
        // Outputs to file in append mode to prevent overwriting lines
        FileWriter output = new FileWriter("leaders.txt");
        BufferedWriter bWriter = new BufferedWriter(output);

        // Sort list alphabetically
        list.sortAlphabetically(list.getHead());

        // Display all players and their respective stats
        list.displayPlayerData(list.getHead(), bWriter);

        
        int totalNumLeaders;
        int firstCounter, secondCounter;
        int listSize;

        /*
        LinkList<Player> names = new LinkList<>();
        Node<Player> test = new Node<>(list.getHead().getData());
        Node<Player> cnt = list.getHead();
        System.out.println("list before: " + list.getListSize(list.getHead()));
        System.out.println("names before: " + names.getListSize(names.getHead()));
        while(cnt != null)
        {
            System.out.println(cnt.getData().getName());
            cnt = cnt.getNext();
        }

        names.appendList(test);
        System.out.println("list after: " + list.getListSize(list.getHead()));
        System.out.println("names after: " + names.getListSize(names.getHead()));
        cnt = names.getHead();
        while(cnt != null)
        {
            System.out.println(cnt.getData().getName());
            cnt = cnt.getNext();
        }
        names.destroyLinkList();
        System.out.println("names after 2: " + names.getListSize(names.getHead()));
        //System.out.println(names.getListSize(names.getHead()));
        */
       // Print league header
        bWriter.write("\n");
        bWriter.write("LEAGUE LEADERS");
       
        listSize = list.getListSize(list.getHead());
        Node<Player> head;
        Node<Player> newNode;
        
        // Loop through each leader stat and display the leaders for that stat
        for(int i = 0; i <= 5; i++)
        {
            totalNumLeaders = 0;
            firstCounter = 1;
            secondCounter = 1;
            head = list.getHead(); // resets head of list after every category
            
            switch(i)
            {
                /* 
                    STEPS FOR DISPLAYING LEADERS AND DEALING WITH TIES     
                    1) Create a counter for total number of leaders
                    2) Create a counter for each place on leaderboard (1st, 2nd)
                    3) Add head of list to first leaders array list b/c 1st leader will always be displayed
                    4) Loop through list and compare iter to head
                        a) If iter = head, then add iter.name to array list for first leaders and increment 1st place counter
                        b) Stop iter at node where iter != head, set head to iter
                    5) Add 1st place counter to total number of leaders counter
                    6) Loop through first leaders array list and display each name
                    7) If total number of leaders counter <= 2
                        a) Loop through list starting at new head
                        b) Add new head to second leaders array list
                        c) If iter = head, then add iter to second leaders array list and increment 2nd place counter
                        d) Stop iter at node where iter != head, set head to iter
                        Else do not display anything (b/c 1st place leaders >= 3)
                    8) Add 2nd place counter to total number of leaders counter
                    9) Loop through second leaders array list and display each name
                    8) If total number of leaders counter <= 2
                        a) Loop through list starting at new head
                        b) Add new head to third leaders array list
                        c) If iter = head, then add iter to third leaders array list (Don't need 3rd place counter b/c there can be unlimited ties for 3rd)
                        d) Stop iter at node where iter = null or doesn't equal new head                
                
                /*
                    NEED TO DISPLAY TIES IN ALPHABETICAL
                    1) Create a new linked list at beginning of case to contain all tied players for 1st
                    2) Don't print out first person as leader
                        a) Put first person as leader into linked list and append following tied players to list
                    3) Alphabetically sort the list
                    4) Loop through list and print out the tied players for first
                    6) Repeat for 2nd ties and 3rd ties
                */
                case 0: // Case for displaying batting average leaders
                    {
                        // Sorts the list in descending order based on batting average
                        list.sortDescendingBattingAverage(head);    // Sort list in descending order
                        LinkList<Player> firstLeaders = new LinkList<>();
                        //ArrayList<String> firstLeaders = new ArrayList<>(); // Create arraylist for first leaders names
                        bWriter.write("\nBATTING AVERAGE\n");   // Display leader stat header
                        int numAtBats = head.getData().calculateNumAtBats(head.getData().getHits(), head.getData().getOuts(), head.getData().getStrikeouts());
                        
                        // Add first leader to list
                        //firstLeaders.add(head.getData().getName());
                        newNode = new Node<>(head.getData());
                        firstLeaders.appendList(newNode);
                        // Display first leader stat
                        bWriter.write(String.format("%.3f", head.getData().calculateBattingAverage(head.getData().getHits(), numAtBats)) + "\t");
                        //bWriter.write(String.format("%.3f", head.getData().calculateBattingAverage(head.getData().getHits(), numAtBats)) + "\t" + head.getData().getName());
                        Node<Player> iter = head.getNext();
                        
                        int iterNumAtBats;
                        double iterBattingAverage, headBattingAverage;
                        boolean tie = false;
                        // Loop through list to get all batting averages for first
                        while(iter != null)
                        {
                            // Calculates the batting average of 1st place leader and all other players
                            iterNumAtBats = iter.getData().calculateNumAtBats(iter.getData().getHits(), iter.getData().getOuts(), iter.getData().getStrikeouts());
                            iterBattingAverage = iter.getData().calculateBattingAverage(iter.getData().getHits(), iterNumAtBats);
                            headBattingAverage = head.getData().calculateBattingAverage(head.getData().getHits(), numAtBats);

                            // If batting average of other players is equal to 1st place leader print out player's name
                            if(iterBattingAverage == headBattingAverage)
                            {
                                // Add tied leaders to list
                                newNode = new Node<>(iter.getData());
                                firstLeaders.appendList(newNode);
                                //firstLeaders.add(iter.getData().getName());
                                //bWriter.write(", " + iter.getData().getName()); // <------ Append node to new ties linked list instead of printing name
                                firstCounter++;
                                tie = true;
                            }

                            // If not equal stop looping and set head to player where batting average was not equal (a.k.a 2nd place leader)
                            else
                            {
                                head = iter; // Move head to 2nd place leader
                                iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                break;
                            }
                            iter = iter.getNext(); // Iterates to next player if there is a tie
                        }
                        totalNumLeaders += firstCounter; // Increase total number of leaders
                        firstLeaders.sortAlphabetically(firstLeaders.getHead());
                        //list.sortArrayListNames(firstLeaders);  // Sort array list of first leader names
                        for(Node<Player> j = firstLeaders.getHead(); j != null; j = j.getNext()) // Print names of first leaders
                        {
                            if(j == firstLeaders.getHead())
                                bWriter.write(j.getData().getName());
                            else
                                bWriter.write(", " + j.getData().getName());
                        }
                        /*for(int j = 0; j < firstLeaders.size(); j++)    // Display names of all first leaders
                        {
                            if(j == 0)
                                bWriter.write(firstLeaders.get(j));
                            else
                                bWriter.write(", " + firstLeaders.get(j));
                        }*/
                        if(totalNumLeaders <= 2) // Check if there are 3 or more leaders already
                        {
                            // Create arraylist for second leaders
                            LinkList<Player> secondLeaders = new LinkList<>();
                            //ArrayList<String> secondLeaders = new ArrayList<>();
                           
                            numAtBats = head.getData().calculateNumAtBats(head.getData().getHits(), head.getData().getOuts(), head.getData().getStrikeouts());
                            headBattingAverage = head.getData().calculateBattingAverage(head.getData().getHits(), numAtBats);

                            newNode = new Node<>(head.getData());
                            if(listSize > 2)    // If there are enough players in list
                                secondLeaders.appendList(newNode);    
                            //secondLeaders.add(head.getData().getName());
                               // bWriter.write("\n" + String.format("%.3f", headBattingAverage) + "\t" + head.getData().getName()); // <-------- Append Node to ties linked list instead of printing
                            else if(!tie && listSize > 1) // if at least 2 players in list and not a tie
                                secondLeaders.appendList(newNode); 
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + String.format("%.3f", headBattingAverage) + "\t" + head.getData().getName());
                            else
                            { 
                                bWriter.write("\n");
                                break;
                            }
                            
                            // Print out 2nd leader and their batting average
                            bWriter.write("\n" + String.format("%.3f", headBattingAverage) + "\t");
                            //bWriter.write("\n" + String.format("%.3f", headBattingAverage) + "\t" + head.getData().getName());
                            while(iter != null)
                            {
                                // Calculates the batting average of 2nd place leader and all other players
                                iterNumAtBats = iter.getData().calculateNumAtBats(iter.getData().getHits(), iter.getData().getOuts(), iter.getData().getStrikeouts());
                                iterBattingAverage = iter.getData().calculateBattingAverage(iter.getData().getHits(), iterNumAtBats);
                                headBattingAverage = head.getData().calculateBattingAverage(head.getData().getHits(), numAtBats);
                                
                                // If batting average of other players is equal to 2st place leader
                                if(iterBattingAverage == headBattingAverage)
                                {
                                    // Add player's name to second leaders list
                                    newNode = new Node<>(iter.getData());
                                    secondLeaders.appendList(newNode);
                                    //bWriter.write(", " + iter.getData().getName()); // <------ Append node to new ties linked list instead of printing name
                                    secondCounter++;
                                }

                                // If not equal stop looping and set head to player where batting average was not equal (a.k.a 3rd place leader)
                                else
                                {
                                    head = iter; // Move head to 3rd place leader
                                    iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                    break;
                                }
                                iter = iter.getNext(); // Iterates to next player if there is a tie
                            }
                            totalNumLeaders += secondCounter; // Increase total number of leaders
                            secondLeaders.sortAlphabetically(secondLeaders.getHead());
                            //list.sortArrayListNames(secondLeaders); // Sort second leaders array list alphabetically
                            
                            for(Node<Player> j = secondLeaders.getHead(); j != null; j = j.getNext())
                            {
                                if(j == secondLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            /*for(int j = 0; j < secondLeaders.size(); j++)   // Display second leaders names
                            {
                                if(j == 0)
                                    bWriter.write(secondLeaders.get(j));
                                else
                                    bWriter.write(", " + secondLeaders.get(j));
                            }*/
                        }
                        else // If ties for 1st were >= 3 do not print out anything and end loop for that stat
                        { 
                            bWriter.write("\n");
                            break;
                        }
                        
                        if(totalNumLeaders <= 2) // Check if there are already 3 or more leaders already
                        {
                            // Create third leaders list
                            LinkList<Player> thirdLeaders = new LinkList<>();
                            //ArrayList<String> thirdLeaders = new ArrayList<>();
                            // -----------> Deconstruct list to clear it of 1st place tied players by calling deconstruct function
                            numAtBats = head.getData().calculateNumAtBats(head.getData().getHits(), head.getData().getOuts(), head.getData().getStrikeouts());
                            headBattingAverage = head.getData().calculateBattingAverage(head.getData().getHits(), numAtBats);

                            // Add head of third leaders to third leaders array list
                            newNode = new Node<>(head.getData());
                            thirdLeaders.appendList(newNode);
                            //thirdLeaders.add(head.getData().getName());
                            // Print out 3rd leader and their batting average
                            bWriter.write("\n" + String.format("%.3f", headBattingAverage) + "\t");
                            //bWriter.write("\n" + String.format("%.3f", headBattingAverage) + "\t" + head.getData().getName()); // <------ Append node to new ties linked list instead of printing name
                            while(iter != null)
                            {

                                // Calculates the batting average of 3rd place leader and all other players
                                iterNumAtBats = iter.getData().calculateNumAtBats(iter.getData().getHits(), iter.getData().getOuts(), iter.getData().getStrikeouts());
                                iterBattingAverage = iter.getData().calculateBattingAverage(iter.getData().getHits(), iterNumAtBats);
                                headBattingAverage = head.getData().calculateBattingAverage(head.getData().getHits(), numAtBats);
                                
                                // If batting average of other players is equal to 3rd place
                                if(iterBattingAverage == headBattingAverage)
                                {
                                    // Add player's name to third leaders list
                                    newNode = new Node<>(iter.getData());
                                    thirdLeaders.appendList(newNode);
                                    //thirdLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName()); // <------ Append node to new ties linked list instead of printing name
                                }

                                // If not equal stop looping and print out nothing because there are no more leaders after 3rd
                                else
                                { 
                                    //bWriter.write("\n");
                                    break;
                                }
                                iter = iter.getNext();
                            }
                            //list.sortArrayListNames(thirdLeaders); // Sort third leaders array list alphabetically
                            thirdLeaders.sortAlphabetically(thirdLeaders.getHead());
                            for(Node<Player> j = thirdLeaders.getHead(); j != null; j = j.getNext())
                            {
                                if(j == thirdLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            /*for(int j = 0; j < thirdLeaders.size(); j++)    // Display third leaders names
                            {
                                if(j == 0)
                                    bWriter.write(thirdLeaders.get(j));
                                else
                                    bWriter.write(", " + thirdLeaders.get(j));
                            }*/
                            //bWriter.write("\n");
                        }
                        bWriter.write("\n");
                        break;
                    }
                
                case 1: // Case for displaying on base percentage leaders
                    {
                        list.sortDescendingOnBasePercentage(head); // Sort list of players descending
                        LinkList<Player> firstLeaders = new LinkList<>();
                        //ArrayList<String> firstLeaders = new ArrayList<>(); // arraylist for first leaders
                        bWriter.write("\nON-BASE PERCENTAGE\n"); // leader stat header
                        int numPlateAppearances = head.getData().calculatePlateAppearances(head.getData().getHits(), head.getData().getOuts(), head.getData().getStrikeouts(), head.getData().getWalks(), head.getData().getHitByPitches(), head.getData().getSacrifices());
                        
                        // Add head to first leaders list
                        newNode = new Node<>(head.getData());
                        firstLeaders.appendList(newNode);
                        //firstLeaders.add(head.getData().getName());
                        // Print out first leader stat
                        bWriter.write(String.format("%.3f", head.getData().calculateOnBasePercentage(head.getData().getHits(), head.getData().getWalks(), head.getData().getHitByPitches(), numPlateAppearances)) + "\t");
                        //bWriter.write(String.format("%.3f", head.getData().calculateOnBasePercentage(head.getData().getHits(), head.getData().getWalks(), head.getData().getHitByPitches(), numPlateAppearances)) + "\t" + head.getData().getName());
                        Node<Player> iter = head.getNext();
                        int iterNumPlateAppearances;
                        double iterOnBasePercentage, headOnBasePercentage;
                        boolean tie = false;
                        // Loop through list to get all on-base percentage for first
                        while(iter != null)
                        {
                            // Calculates the on-base percentage of 1st place leader and all other players
                            iterNumPlateAppearances = iter.getData().calculatePlateAppearances(iter.getData().getHits(), iter.getData().getOuts(), iter.getData().getStrikeouts(), iter.getData().getWalks(), iter.getData().getHitByPitches(), iter.getData().getSacrifices());
                            iterOnBasePercentage = iter.getData().calculateOnBasePercentage(iter.getData().getHits(), iter.getData().getWalks(), iter.getData().getHitByPitches(), iterNumPlateAppearances);
                            headOnBasePercentage = head.getData().calculateOnBasePercentage(head.getData().getHits(), head.getData().getWalks(), head.getData().getHitByPitches(), numPlateAppearances);

                            // If on-base percentage of other players is equal to 1st place leader
                            if(Math.abs(iterOnBasePercentage - headOnBasePercentage) <= THRESHOLD)
                            {
                                // Add player's name to first leaders list
                                newNode = new Node<>(iter.getData());
                                firstLeaders.appendList(newNode);
                                //firstLeaders.add(iter.getData().getName());
                                //bWriter.write(", " + iter.getData().getName());
                                firstCounter++;
                                tie = true;
                            }

                            // If not equal stop looping and set head to player where on-place percentage was not equal (a.k.a 2nd place leader)
                            else
                            {
                                head = iter; // Move head to 2nd place leader
                                iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                break;
                            }
                            iter = iter.getNext(); // Iterates to next player if there is a tie
                        }
                        firstLeaders.sortAlphabetically(firstLeaders.getHead()); // Sort first leaders list alphabetically
                        //list.sortArrayListNames(firstLeaders);
                        for(Node<Player> j = firstLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                        {
                            if(j == firstLeaders.getHead())
                                bWriter.write(j.getData().getName());
                            else
                                bWriter.write(", " + j.getData().getName());
                        }
                        /*for(int j = 0; j < firstLeaders.size(); j++) // Print out first leaders names
                        {
                            if(j == 0)
                                bWriter.write(firstLeaders.get(j));
                            else
                                bWriter.write(", " + firstLeaders.get(j));
                        }*/
                        totalNumLeaders += firstCounter; // Increase total number of leaders
                        if(totalNumLeaders <= 2) // Check if there are 3 or more leaders already
                        {
                            // Create arraylist for second leaders
                            LinkList<Player> secondLeaders = new LinkList<>();
                            //ArrayList<String> secondLeaders = new ArrayList<>();
                            numPlateAppearances = head.getData().calculatePlateAppearances(head.getData().getHits(), head.getData().getOuts(), head.getData().getStrikeouts(), head.getData().getWalks(), head.getData().getHitByPitches(), head.getData().getSacrifices());
                            headOnBasePercentage = head.getData().calculateOnBasePercentage(head.getData().getHits(), head.getData().getWalks(), head.getData().getHitByPitches(), numPlateAppearances);
                            newNode = new Node<>(head.getData());
                            if(listSize > 2)
                                secondLeaders.appendList(newNode);
                                //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + String.format("%.3f", headOnBasePercentage) + "\t" + head.getData().getName());
                            else if(!tie && listSize > 1)
                                secondLeaders.appendList(newNode);  
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + String.format("%.3f", headOnBasePercentage) + "\t" + head.getData().getName());
                            else
                            { 
                                bWriter.write("\n");
                                break;
                            }

                            bWriter.write("\n" + String.format("%.3f", headOnBasePercentage) + "\t");
                            // Print out 2nd leader and their on-base percentage
                            while(iter != null)
                            {
                                // Calculates the on-base percentage of 2nd place leader and all other players
                                iterNumPlateAppearances = iter.getData().calculatePlateAppearances(iter.getData().getHits(), iter.getData().getOuts(), iter.getData().getStrikeouts(), iter.getData().getWalks(), iter.getData().getHitByPitches(), iter.getData().getSacrifices());
                                iterOnBasePercentage = iter.getData().calculateOnBasePercentage(iter.getData().getHits(), iter.getData().getWalks(), iter.getData().getHitByPitches(), iterNumPlateAppearances);
                                headOnBasePercentage = head.getData().calculateOnBasePercentage(head.getData().getHits(), head.getData().getWalks(), head.getData().getHitByPitches(), numPlateAppearances);
                                
                                // If on-base percentage of other players is equal to 2nd place leader
                                if(Math.abs(iterOnBasePercentage - headOnBasePercentage) <= THRESHOLD)
                                {
                                    // Add player to second leaders list
                                    newNode = new Node<>(iter.getData());
                                    secondLeaders.appendList(newNode);
                                    //secondLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                    secondCounter++;
                                }

                                // If not equal stop looping and set head to player where on-base percentage was not equal (a.k.a 3rd place leader)
                                else
                                {
                                    head = iter; // Move head to 3rd place leader
                                    iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                    break;
                                }
                                iter = iter.getNext(); // Iterates to next player if there is a tie
                            }
                            totalNumLeaders += secondCounter; // Increase total number of leaders
                            //list.sortArrayListNames(secondLeaders); // Sort second leaders names alphabetically
                            secondLeaders.sortAlphabetically(secondLeaders.getHead());
                            for(Node<Player> j = secondLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                            if(j == secondLeaders.getHead())
                                bWriter.write(j.getData().getName());
                            else
                                bWriter.write(", " + j.getData().getName());
                            }
                            /*for(int j = 0; j < secondLeaders.size(); j++)   // Print out second leaders names
                            {
                                if(j == 0)
                                    bWriter.write(secondLeaders.get(j));
                                else
                                    bWriter.write(", " + secondLeaders.get(j));
                            }*/
                        }
                        else // If ties for 1st were >= 3 do not print out anything and end loop for that stat
                        { 
                            bWriter.write("\n");
                            break;
                        }
                        if(totalNumLeaders <= 2) // Check if there are already 3 or more leaders already
                        {
                            numPlateAppearances = head.getData().calculatePlateAppearances(head.getData().getHits(), head.getData().getOuts(), head.getData().getStrikeouts(), head.getData().getWalks(), head.getData().getHitByPitches(), head.getData().getSacrifices());
                            headOnBasePercentage = head.getData().calculateOnBasePercentage(head.getData().getHits(), head.getData().getWalks(), head.getData().getHitByPitches(), numPlateAppearances);

                            // Create third leaders array list
                            LinkList<Player> thirdLeaders = new LinkList<>();
                            //ArrayList<String> thirdLeaders = new ArrayList<>();
                            // Add head to third leaders list
                            newNode = new Node<>(head.getData());
                            thirdLeaders.appendList(newNode);
                            //thirdLeaders.add(head.getData().getName());
                            // Print out third leaders stat
                            bWriter.write("\n" + String.format("%.3f", headOnBasePercentage) + "\t");
                            //bWriter.write("\n" + String.format("%.3f", headOnBasePercentage) + "\t" + head.getData().getName());
                            while(iter != null)
                            {

                                // Calculates the on-base percentage of 3rd place leader and all other players
                                iterNumPlateAppearances = iter.getData().calculatePlateAppearances(iter.getData().getHits(), iter.getData().getOuts(), iter.getData().getStrikeouts(), iter.getData().getWalks(), iter.getData().getHitByPitches(), iter.getData().getSacrifices());
                                iterOnBasePercentage = iter.getData().calculateOnBasePercentage(iter.getData().getHits(), iter.getData().getWalks(), iter.getData().getHitByPitches(), iterNumPlateAppearances);
                                headOnBasePercentage = head.getData().calculateOnBasePercentage(head.getData().getHits(), head.getData().getWalks(), head.getData().getHitByPitches(), numPlateAppearances);
                                
                                // If on-base percentage of other players is equal to 3rd place leader
                                if(Math.abs(iterOnBasePercentage - headOnBasePercentage) <= THRESHOLD)
                                {
                                    // Add player's name to third leaders array list
                                    newNode = new Node<>(iter.getData());
                                    thirdLeaders.appendList(newNode);
                                    //thirdLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                }

                                // If not equal stop looping and print out nothing because there are no more leaders after 3rd
                                else
                                { 
                                    //bWriter.write("\n");
                                    break;
                                }
                                iter = iter.getNext();
                            }
                            //list.sortArrayListNames(thirdLeaders); // Sort third leaders names alphabetically
                            thirdLeaders.sortAlphabetically(thirdLeaders.getHead());
                            for(Node<Player> j = thirdLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == thirdLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            /*for(int j = 0; j < thirdLeaders.size(); j++)    // Print out third leaders names
                            {
                                if(j == 0)
                                    bWriter.write(thirdLeaders.get(j));
                                else
                                    bWriter.write(", " + thirdLeaders.get(j));
                            }*/
                            //bWriter.write("\n");
                        }
                        bWriter.write("\n");
                        break;
                    }
                
                case 2: // Case for displaying hits leaders
                    {
                        list.sortDescendingHits(head);  // Sort list of players
                        // Create first leaders array list
                        LinkList<Player> firstLeaders = new LinkList<>();
                        //ArrayList<String> firstLeaders = new ArrayList<>();
                        bWriter.write("\nHITS\n");  // Display hits leaders header
                        
                        // Add head to first leaders array list
                        newNode = new Node<>(head.getData());
                        firstLeaders.appendList(newNode);
                        //firstLeaders.add(head.getData().getName());
                        // Print hits for first leaders
                        bWriter.write(head.getData().getHits() + "\t");
                        Node<Player> iter = head.getNext();
                        int iterNumHits, headNumHits;
                        boolean tie = false;
                        // Loop through list to get all hits for first
                        while(iter != null)
                        {
                            // Gets the number of hits of 1st place leader and all other players
                            iterNumHits = iter.getData().getHits();
                            headNumHits = head.getData().getHits();

                            // If number of hits of other players is equal to 1st place leader
                            if(iterNumHits == headNumHits)
                            {
                                // Add players' names to first leaders array list
                                newNode = new Node<>(iter.getData());
                                firstLeaders.appendList(newNode);
                                //firstLeaders.add(iter.getData().getName());
                                //bWriter.write(", " + iter.getData().getName());
                                firstCounter++;
                                tie = true;
                            }
                            // If not equal stop looping and set head to player where number of hits was not equal (a.k.a 2nd place leader)
                            else
                            {
                                head = iter; // Move head to 2nd place leader
                                iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                break;
                            }
                            iter = iter.getNext(); // Iterates to next player if there is a tie
                        }
                        totalNumLeaders += firstCounter; // Increase total number of leaders
                        firstLeaders.sortAlphabetically(firstLeaders.getHead());
                        for(Node<Player> j = firstLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                        {
                            if(j == firstLeaders.getHead())
                                bWriter.write(j.getData().getName());
                            else
                                bWriter.write(", " + j.getData().getName());
                        }
                        //list.sortArrayListNames(firstLeaders); // Sort first leaders names alphabetically
                        /*for(int j = 0; j < firstLeaders.size(); j++) // Print first leaders names
                        {
                            if(j == 0)
                                bWriter.write(firstLeaders.get(j));
                            else
                                bWriter.write(", " + firstLeaders.get(j));
                        }*/
                        if(totalNumLeaders <= 2) // Check if there are 3 or more leaders already
                        {
                            headNumHits = head.getData().getHits();
                            // Create second leaders list
                            LinkList<Player> secondLeaders = new LinkList<>();
                            newNode = new Node<>(head.getData());
                            //ArrayList<String> secondLeaders = new ArrayList<>();
                            if(listSize > 2)    // If enough players in list
                                secondLeaders.appendList(newNode); 
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + head.getData().getHits() + "\t" + head.getData().getName());
                            else if(!tie && listSize > 1)   // If at least 2 players in list and not tied
                                secondLeaders.appendList(newNode);   
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + head.getData().getHits() + "\t" + head.getData().getName());
                            else
                            { 
                                bWriter.write("\n");
                                break;
                            }
                            // Print 2nd leaders hit stat
                            bWriter.write("\n" + head.getData().getHits() + "\t");
                            //bWriter.write("\n" + head.getData().getHits() + "\t" + head.getData().getName());
                            while(iter != null)
                            {
                                // Gets the number of 2nd place leader and all other players
                                iterNumHits = iter.getData().getHits();
                                headNumHits = head.getData().getHits();
                                
                                // If number of hits of other players is equal to 2nd place leader
                                if(iterNumHits == headNumHits)
                                {
                                    // Add player's name to second leaders array list
                                    newNode = new Node<>(iter.getData());
                                    secondLeaders.appendList(newNode);
                                    //secondLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                    secondCounter++;
                                }

                                // If not equal stop looping and set head to player where number of hits was not equal (a.k.a 3rd place leader)
                                else
                                {
                                    head = iter; // Move head to 3rd place leader
                                    iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                    break;
                                }
                                iter = iter.getNext(); // Iterates to next player if there is a tie
                            }
                            totalNumLeaders += secondCounter; // Increase total number of leaders
                            secondLeaders.sortAlphabetically(secondLeaders.getHead());
                            for(Node<Player> j = secondLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == secondLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            //list.sortArrayListNames(secondLeaders); // Sort second leaders names alphabetically
                            /*for(int j = 0; j < secondLeaders.size(); j++)   // Print second leaders names
                            {
                                if(j == 0)
                                    bWriter.write(secondLeaders.get(j));
                                else
                                    bWriter.write(", " + secondLeaders.get(j));
                            }*/
                        }
                        else // If ties for 1st were >= 3 do not print out anything and end loop for that stat
                        { 
                            bWriter.write("\n");
                            break;
                        }
                        if(totalNumLeaders <= 2) // Check if there are already 3 or more leaders already
                        {
                            headNumHits = head.getData().getHits();
                            // Create third leaders list
                            LinkList<Player> thirdLeaders = new LinkList<>();
                            //ArrayList<String> thirdLeaders = new ArrayList<>();
                            // Add head to third leaders array list
                            newNode = new Node<>(head.getData());
                            thirdLeaders.appendList(newNode);
                            //thirdLeaders.add(head.getData().getName());
                            // Print out 3rd leader and their number of hits
                            bWriter.write("\n" + headNumHits + "\t");

                            //bWriter.write("\n" + headNumHits + "\t" + head.getData().getName());
                            while(iter != null)
                            {

                                // Gets the number of hits of 3rd place leader and all other players
                                iterNumHits = iter.getData().getHits();
                                headNumHits = head.getData().getHits();
                                
                                // If the number of hits of other players is equal to 3rd place leader
                                if(iterNumHits == headNumHits)
                                {
                                    // Add player's name to third leaders list
                                    newNode = new Node<>(iter.getData());
                                    thirdLeaders.appendList(newNode);
                                    //thirdLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                }

                                // If not equal stop looping and print out nothing because there are no more leaders after 3rd
                                else
                                { 
                                    //bWriter.write("\n");
                                    break;
                                }
                                iter = iter.getNext();
                            }
                            thirdLeaders.sortAlphabetically(thirdLeaders.getHead());
                            for(Node<Player> j = thirdLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == thirdLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            //list.sortArrayListNames(thirdLeaders); // Sort third leaders names alphabetically
                            /*for(int j = 0; j < thirdLeaders.size(); j++)    // Print third leaders names
                            {
                                if(j == 0)
                                    bWriter.write(thirdLeaders.get(j));
                                else
                                    bWriter.write(", " + thirdLeaders.get(j));
                            }*/
                            //bWriter.write("\n");
                        }
                        bWriter.write("\n");
                        break;
                    }
                
                case 3: // Case for displaying walks leaders
                    {
                        list.sortDescendingWalks(head); // Sort list descending by walks
                        LinkList<Player> firstLeaders = new LinkList<>();
                        // Create arraylist for first leaders
                        //ArrayList<String> firstLeaders = new ArrayList<>();
                        bWriter.write("\nWALKS\n");

                        // Add head to first leaders list
                        newNode = new Node<>(head.getData());
                        firstLeaders.appendList(newNode);
                        //firstLeaders.add(head.getData().getName());
                        // Print out 1st leader and their walks
                        bWriter.write(head.getData().getWalks() + "\t");
                        //bWriter.write(head.getData().getWalks() + "\t" + head.getData().getName());
                        Node<Player> iter = head.getNext();
                        int iterNumWalks, headNumWalks;
                        boolean tie = false;
                        // Loop through list to get all walks for first
                        while(iter != null)
                        {
                            // Gets the number of walks of 1st place leader and all other players
                            iterNumWalks = iter.getData().getWalks();
                            headNumWalks = head.getData().getWalks();

                            // If number of walks of other players is equal to 1st place leader
                            if(iterNumWalks == headNumWalks)
                            {
                                // Add player's name to first leaders list
                                newNode = new Node<>(iter.getData());
                                firstLeaders.appendList(newNode);
                                //firstLeaders.add(iter.getData().getName());
                                //bWriter.write(", " + iter.getData().getName());
                                firstCounter++;
                                tie = true;
                            }
                            // If not equal stop looping and set head to player where number of walks was not equal (a.k.a 2nd place leader)
                            else
                            {
                                head = iter; // Move head to 2nd place leader
                                iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                break;
                            }
                            iter = iter.getNext(); // Iterates to next player if there is a tie
                        }
                        totalNumLeaders += firstCounter; // Increase total number of leaders
                        firstLeaders.sortAlphabetically(firstLeaders.getHead());
                        for(Node<Player> j = firstLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                        {
                            if(j == firstLeaders.getHead())
                                bWriter.write(j.getData().getName());
                            else
                                bWriter.write(", " + j.getData().getName());
                        }
                        //list.sortArrayListNames(firstLeaders);  // Sort first leaders arraylist alphabetically
                        /*for(int j = 0; j < firstLeaders.size(); j++) // Print first leaders names
                        {
                            if(j == 0)
                                bWriter.write(firstLeaders.get(j));
                            else
                                bWriter.write(", " + firstLeaders.get(j));
                        }*/
                        if(totalNumLeaders <= 2) // Check if there are 3 or more leaders already
                        {
                            // Create second leaders list
                            LinkList<Player> secondLeaders = new LinkList<>();
                            //ArrayList<String> secondLeaders = new ArrayList<>();
                            headNumWalks = head.getData().getWalks();
                            newNode = new Node<>(head.getData());
                            if(listSize > 2) // If list has enough players
                                secondLeaders.appendList(newNode);  
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + head.getData().getWalks() + "\t" + head.getData().getName());
                            else if(!tie && listSize > 1) // If at least 2 players and not tied
                                secondLeaders.appendList(newNode);
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + head.getData().getWalks() + "\t" + head.getData().getName());
                            else
                            { 
                                bWriter.write("\n");
                                break;
                            }
                            // Print out 2nd leader and their number of walks
                            bWriter.write("\n" + head.getData().getWalks() + "\t");
                            //bWriter.write("\n" + head.getData().getWalks() + "\t" + head.getData().getName());
                            while(iter != null)
                            {
                                // Gets the number of 2nd place leader and all other players
                                iterNumWalks = iter.getData().getWalks();
                                headNumWalks = head.getData().getWalks();
                                
                                // If number of walks of other players is equal to 2nd place leader
                                if(iterNumWalks == headNumWalks)
                                {
                                    // Add player's name to second leaders list
                                    newNode = new Node<>(iter.getData());
                                    secondLeaders.appendList(newNode);
                                    //secondLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                    secondCounter++;
                                }

                                // If not equal stop looping and set head to player where number of walks was not equal (a.k.a 3rd place leader)
                                else
                                {
                                    head = iter; // Move head to 3rd place leader
                                    iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                    break;
                                }
                                iter = iter.getNext(); // Iterates to next player if there is a tie
                            }
                            totalNumLeaders += secondCounter; // Increase total number of leaders
                            secondLeaders.sortAlphabetically(secondLeaders.getHead());
                            for(Node<Player> j = secondLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == secondLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            //list.sortArrayListNames(secondLeaders); // Sort second eladers names alphabetically
                            /*for(int j = 0; j < secondLeaders.size(); j++)   // Display second leader's names
                            {
                                if(j == 0)
                                    bWriter.write(secondLeaders.get(j));
                                else
                                    bWriter.write(", " + secondLeaders.get(j));
                            }*/
                        }
                        else // If ties for 1st were >= 3 do not print out anything and end loop for that stat
                        { 
                            bWriter.write("\n");
                            break;
                        }
                        if(totalNumLeaders <= 2) // Check if there are already 3 or more leaders already
                        {
                            // Create third leaders array list
                            LinkList<Player> thirdLeaders = new LinkList<>();
                            //ArrayList<String> thirdLeaders = new ArrayList<>();
                            headNumWalks = head.getData().getWalks();

                            // Add head to third leaders list
                            newNode = new Node<>(head.getData());
                            thirdLeaders.appendList(newNode);
                            //thirdLeaders.add(head.getData().getName());
                            // Print out 3rd leader number of walks
                            bWriter.write("\n" + headNumWalks + "\t");
                            //bWriter.write("\n" + headNumWalks + "\t" + head.getData().getName());
                            while(iter != null)
                            {

                                // Gets the number of walks of 3rd place leader and all other players
                                iterNumWalks = iter.getData().getWalks();
                                headNumWalks = head.getData().getWalks();
                                
                                // If the number of walks of other players is equal to 3rd place leader
                                if(iterNumWalks == headNumWalks)
                                {
                                    // Add player's name to third leaders list
                                    newNode = new Node<>(iter.getData());
                                    thirdLeaders.appendList(newNode);
                                    //thirdLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                }

                                // If not equal stop looping and print out nothing because there are no more leaders after 3rd
                                else
                                { 
                                    //bWriter.write("\n");
                                    break;
                                }
                                iter = iter.getNext();
                            }
                            //list.sortArrayListNames(thirdLeaders); // Sort third leaders array list alphabetically
                            thirdLeaders.sortAlphabetically(thirdLeaders.getHead());
                            for(Node<Player> j = thirdLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == thirdLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            /*for(int j = 0; j < thirdLeaders.size(); j++) // Print third leaders array list
                            {
                                if(j == 0)
                                    bWriter.write(thirdLeaders.get(j));
                                else
                                    bWriter.write(", " + thirdLeaders.get(j));
                            }*/
                            //bWriter.write("\n");
                        }
                        bWriter.write("\n");
                        break;
                    }
                
                case 4: // Case for displaying strikeouts leaders
                    {
                        list.sortAscendingStrikeouts(head); // Sort list ascending by strikeouts
                        // Create first leaders array list
                        LinkList<Player> firstLeaders = new LinkList<>();
                        //ArrayList<String> firstLeaders = new ArrayList<>();
                        // Print strikeouts leader header
                        bWriter.write("\nSTRIKEOUTS\n");

                        // Add head to first leaders array list
                        newNode = new Node<>(head.getData());
                        firstLeaders.appendList(newNode);
                        //firstLeaders.add(head.getData().getName());
                        // Print out 1st leader number of strikeouts
                        bWriter.write(head.getData().getStrikeouts() + "\t");
                        //bWriter.write(head.getData().getStrikeouts() + "\t" + head.getData().getName());
                        Node<Player> iter = head.getNext();
                        int iterNumStrikeouts, headNumStrikeouts;
                        boolean tie = false;
                        // Loop through list to get all strikeouts for first
                        while(iter != null)
                        {
                            // Gets the number of strikeouts of 1st place leader and all other players
                            iterNumStrikeouts = iter.getData().getStrikeouts();
                            headNumStrikeouts = head.getData().getStrikeouts();

                            // If number of strikeouts of other players is equal to 1st place leader
                            if(iterNumStrikeouts == headNumStrikeouts)
                            {
                                // Add player to list
                                newNode = new Node<>(iter.getData());
                                firstLeaders.appendList(newNode);
                                //firstLeaders.add(iter.getData().getName());
                                //bWriter.write(", " + iter.getData().getName());
                                firstCounter++;
                                tie = true;
                            }
                            // If not equal stop looping and set head to player where number of strikeouts was not equal (a.k.a 2nd place leader)
                            else
                            {
                                head = iter; // Move head to 2nd place leader
                                iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                break;
                            }
                            iter = iter.getNext(); // Iterates to next player if there is a tie
                        }
                        totalNumLeaders += firstCounter; // Increase total number of leaders
                        firstLeaders.sortAlphabetically(firstLeaders.getHead());
                        for(Node<Player> j = firstLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                        {
                            if(j == firstLeaders.getHead())
                                bWriter.write(j.getData().getName());
                            else
                                bWriter.write(", " + j.getData().getName());
                        }
                        //list.sortArrayListNames(firstLeaders); // Sort first leaders array list alphabetically
                        /*for(int j = 0; j < firstLeaders.size(); j++)    // Print first leaders names
                        {
                            if(j == 0)
                                bWriter.write(firstLeaders.get(j));
                            else
                                bWriter.write(", " + firstLeaders.get(j));
                        }*/
                        if(totalNumLeaders <= 2) // Check if there are 3 or more leaders already
                        {
                            LinkList<Player> secondLeaders = new LinkList<>();
                            //ArrayList<String> secondLeaders = new ArrayList<>();
                            headNumStrikeouts = head.getData().getStrikeouts();
                            
                            newNode = new Node<>(head.getData());
                            if(listSize > 2) // If there are enough players
                                secondLeaders.appendList(newNode);  
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + head.getData().getStrikeouts() + "\t" + head.getData().getName());
                            else if(!tie && listSize > 1) // If at least 2 players and not a tie
                                secondLeaders.appendList(newNode);
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + head.getData().getStrikeouts() + "\t" + head.getData().getName());
                            else
                            {
                                bWriter.write("\n");
                                break;
                            }
                            // Print out 2nd leader number of strikeouts
                            bWriter.write("\n" + head.getData().getStrikeouts() + "\t");
                            //bWriter.write("\n" + head.getData().getStrikeouts() + "\t" + head.getData().getName());
                            while(iter != null)
                            {
                                // Gets the number of 2nd place leader and all other players
                                iterNumStrikeouts = iter.getData().getStrikeouts();
                                headNumStrikeouts = head.getData().getStrikeouts();
                                
                                // If number of strikeouts of other players is equal to 1st place leader
                                if(iterNumStrikeouts == headNumStrikeouts)
                                {
                                    // Add player's name to second leaders list
                                    newNode = new Node<>(iter.getData());
                                    secondLeaders.appendList(newNode);
                                    //secondLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                    secondCounter++;
                                }

                                // If not equal stop looping and set head to player where number of strikeouts was not equal (a.k.a 3rd place leader)
                                else
                                {
                                    head = iter; // Move head to 3rd place leader
                                    iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                    break;
                                }
                                iter = iter.getNext(); // Iterates to next player if there is a tie
                            }
                            totalNumLeaders += secondCounter; // Increase total number of leaders
                            secondLeaders.sortAlphabetically(secondLeaders.getHead());
                            for(Node<Player> j = secondLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == secondLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            //list.sortArrayListNames(secondLeaders); // Sort second leaders alphabetically
                            /*for(int j = 0; j < secondLeaders.size(); j++)   // Print second leaders
                            {
                                if(j == 0)
                                    bWriter.write(secondLeaders.get(j));
                                else
                                    bWriter.write(", " + secondLeaders.get(j));
                            }*/
                        }
                        else // If ties for 1st were >= 3 do not print out anything and end loop for that stat
                        { 
                            bWriter.write("\n");
                            break;
                        }
                        if(totalNumLeaders <= 2) // Check if there are already 3 or more leaders already
                        {
                            // Create third leaders array list
                            LinkList<Player> thirdLeaders = new LinkList<>();
                            //ArrayList<String> thirdLeaders = new ArrayList<>();
                            headNumStrikeouts = head.getData().getStrikeouts();

                            // Add head to third leaders array list
                            newNode = new Node<>(head.getData());
                            thirdLeaders.appendList(newNode);
                            //thirdLeaders.add(head.getData().getName());
                            // Print out 3rd leader number of strikeouts
                            bWriter.write("\n" + headNumStrikeouts + "\t");
                            //bWriter.write("\n" + headNumStrikeouts + "\t" + head.getData().getName());
                            while(iter != null)
                            {

                                // Gets the number of strikeouts of 3rd place leader and all other players
                                iterNumStrikeouts = iter.getData().getStrikeouts();
                                headNumStrikeouts = head.getData().getStrikeouts();
                                
                                // If the number of strikeouts of other players is equal to 3rd place leader
                                if(iterNumStrikeouts == headNumStrikeouts)
                                {
                                    // Add player's name to list
                                    newNode = new Node<>(iter.getData());
                                    thirdLeaders.appendList(newNode);
                                    //thirdLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                }

                                // If not equal stop looping and print out nothing because there are no more leaders after 3rd
                                else
                                { 
                                    //bWriter.write("\n");
                                    break;
                                }
                                iter = iter.getNext();
                            }
                            //list.sortArrayListNames(thirdLeaders); // Sort third leaders alphabetically
                            thirdLeaders.sortAlphabetically(thirdLeaders.getHead());
                            for(Node<Player> j = thirdLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == thirdLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            /*for(int j = 0; j < thirdLeaders.size(); j++) // Print third leaders names
                            {
                                if(j == 0)
                                    bWriter.write(thirdLeaders.get(j));
                                else
                                    bWriter.write(", " + thirdLeaders.get(j));
                            }*/
                            //bWriter.write("\n");
                        }
                        bWriter.write("\n");
                        break;
                    }
                
                case 5: // case for displaying hit by pitch leaders
                    {
                        // Sort list descending by hit by pitches
                        list.sortDescendingHitByPitches(head);
                        // Create first leaders list
                        LinkList<Player> firstLeaders = new LinkList<>();

                        //ArrayList<String> firstLeaders = new ArrayList<>();
                        bWriter.write("\nHIT BY PITCH\n");

                        // Add head to first leader list
                        newNode = new Node<>(head.getData());
                        firstLeaders.appendList(newNode);
                        //firstLeaders.add(head.getData().getName());
                        // Print out 1st leader hit by pitches
                        bWriter.write(head.getData().getHitByPitches() + "\t");
                        //bWriter.write(head.getData().getHitByPitches() + "\t" + head.getData().getName());
                        Node<Player> iter = head.getNext();
                        int iterNumHitByPitches, headNumHitByPitches;
                        boolean tie = false;
                        // Loop through list to get all hit by pitches for first
                        while(iter != null)
                        {
                            // Gets the number of hit by pitches of 1st place leader and all other players
                            iterNumHitByPitches = iter.getData().getHitByPitches();
                            headNumHitByPitches = head.getData().getHitByPitches();

                            // If number of hit by pitches of other players is equal to 1st place leader
                            if(iterNumHitByPitches == headNumHitByPitches)
                            {
                                // Add players to first leader list
                                newNode = new Node<>(iter.getData());
                                firstLeaders.appendList(newNode);
                                //firstLeaders.add(iter.getData().getName());
                                //bWriter.write(", " + iter.getData().getName());
                                firstCounter++;
                                tie = true;
                            }
                            // If not equal stop looping and set head to player where number of hit by pitches was not equal (a.k.a 2nd place leader)
                            else
                            {
                                head = iter; // Move head to 2nd place leader
                                iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                break;
                            }
                            iter = iter.getNext(); // Iterates to next player if there is a tie
                        }
                        totalNumLeaders += firstCounter; // Increase total number of leaders
                        firstLeaders.sortAlphabetically(firstLeaders.getHead());
                        for(Node<Player> j = firstLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                        {
                            if(j == firstLeaders.getHead())
                                bWriter.write(j.getData().getName());
                            else
                                bWriter.write(", " + j.getData().getName());
                        }
                        //list.sortArrayListNames(firstLeaders); // Sort first leaders list alphabetically
                        /*for(int j = 0; j < firstLeaders.size(); j++) // Print first leaders names
                        {
                            if(j == 0)
                                bWriter.write(firstLeaders.get(j));
                            else
                                bWriter.write(", " + firstLeaders.get(j));
                        }*/
                        if(totalNumLeaders <= 2) // Check if there are 3 or more leaders already
                        {
                            // Create second leaders list
                            LinkList<Player> secondLeaders = new LinkList<>();
                            //ArrayList<String> secondLeaders = new ArrayList<>();
                            headNumHitByPitches = head.getData().getHitByPitches();
                            newNode = new Node<>(head.getData());
                            if(listSize > 2) // If enough players
                                secondLeaders.appendList(newNode);
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + head.getData().getHitByPitches() + "\t" + head.getData().getName());
                            else if(!tie && listSize > 1) // If at least 2 players and not tied
                                secondLeaders.appendList(newNode);  
                            //secondLeaders.add(head.getData().getName());
                                //bWriter.write("\n" + head.getData().getHitByPitches() + "\t" + head.getData().getName());
                            else
                            {
                                bWriter.write("\n\n");
                                break;
                            }
                            // Print out 2nd leader number of hit by pithes
                            bWriter.write("\n" + head.getData().getHitByPitches() + "\t");
                            //bWriter.write("\n" + head.getData().getHitByPitches() + "\t" + head.getData().getName());
                            while(iter != null)
                            {
                                // Gets the number of 2nd place leader and all other players
                                iterNumHitByPitches = iter.getData().getHitByPitches();
                                headNumHitByPitches = head.getData().getHitByPitches();
                                
                                // If number of hit by pitches of other players is equal to 2nd place leader
                                if(iterNumHitByPitches == headNumHitByPitches)
                                {

                                    // Add player to second leaders list
                                    newNode = new Node<>(iter.getData());
                                    secondLeaders.appendList(newNode);
                                    //secondLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                    secondCounter++;
                                }

                                // If not equal stop looping and set head to player where number of hit by pitches was not equal (a.k.a 3rd place leader)
                                else
                                {
                                    head = iter; // Move head to 3rd place leader
                                    iter = iter.getNext(); // Move iterator to next player after 2nd place leader
                                    break;
                                }
                                iter = iter.getNext(); // Iterates to next player if there is a tie
                            }
                            totalNumLeaders += secondCounter; // Increase total number of leaders
                            secondLeaders.sortAlphabetically(secondLeaders.getHead());
                            for(Node<Player> j = secondLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == secondLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            //list.sortArrayListNames(secondLeaders); // Sort second leaders list
                            /*for(int j = 0; j < secondLeaders.size(); j++) // Print second leaders names
                            {
                                if(j == 0)
                                    bWriter.write(secondLeaders.get(j));
                                else
                                    bWriter.write(", " + secondLeaders.get(j));
                            }*/
                            
                        }
                        else // If ties for 1st were >= 3 do not print out anything and end loop for that stat
                        { 
                            bWriter.write("\n");
                            break;
                        }
                        if(totalNumLeaders <= 2) // Check if there are already 3 or more leaders already
                        {
                            // Create third leaders list
                            LinkList<Player> thirdLeaders = new LinkList<>();
                            //ArrayList<String> thirdLeaders = new ArrayList<>();
                            headNumHitByPitches = head.getData().getHitByPitches();

                            // Add head to third leaders list
                            newNode = new Node<>(head.getData());
                            thirdLeaders.appendList(newNode);
                            //thirdLeaders.add(head.getData().getName());
                            // Print out 3rd leader and their number of hit by pitches
                            bWriter.write("\n" + headNumHitByPitches + "\t");
                            //bWriter.write("\n" + headNumHitByPitches + "\t" + head.getData().getName());
                            while(iter != null)
                            {

                                // Gets the number of hit by pitches of 3rd place leader and all other players
                                iterNumHitByPitches = iter.getData().getHitByPitches();
                                headNumHitByPitches = head.getData().getHitByPitches();
                                
                                // If the number of hit by pitches of other players is equal to 3rd place leader
                                if(iterNumHitByPitches == headNumHitByPitches)
                                {
                                    // Add player to third leaders list
                                    newNode = new Node<>(iter.getData());
                                    thirdLeaders.appendList(newNode);
                                    //thirdLeaders.add(iter.getData().getName());
                                    //bWriter.write(", " + iter.getData().getName());
                                }

                                // If not equal stop looping and print out nothing because there are no more leaders after 3rd
                                else
                                { 
                                    //bWriter.write("\n");
                                    break;
                                }
                                iter = iter.getNext();
                            }
                            thirdLeaders.sortAlphabetically(thirdLeaders.getHead());
                            for(Node<Player> j = thirdLeaders.getHead(); j != null; j = j.getNext()) // Print first leaders
                            {
                                if(j == thirdLeaders.getHead())
                                    bWriter.write(j.getData().getName());
                                else
                                    bWriter.write(", " + j.getData().getName());
                            }
                            //list.sortArrayListNames(thirdLeaders); // Sort third players list
                            //System.out.println(head.getData().getName());
                            /*for(int j = 0; j < thirdLeaders.size(); j++) // Print third players' names
                            {
                                if(j == 0)
                                    bWriter.write(thirdLeaders.get(j));
                                else
                                    bWriter.write(", " + thirdLeaders.get(j));
                            }*/
                            //bWriter.write("\n");
                        }
                        bWriter.write("\n\n");
                        break;
                    }
                default:
                    {
                        System.out.println("\n\nCounter is too high!\n");
                        break;
                    }
                    
            }
        }
        bWriter.close();
        // Close files
        bWriter.close();
        fileReader.close();
        input.close();

    }
    
}   