import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        try {
            //Scanner
            InputReader inputReader = InputReader.getInstance();
            //Separate input based on blocks and store each block in arrayList
            ArrayList<Command> commands = inputReader.getCommands();
            //Get each command in commands
            Iterator<Command> currentCommand = commands.iterator();

            //set up a database to store info from commands
            CommandHandler commandHandler = new CommandHandler(new Database());

            //run each command
            while (currentCommand.hasNext()) {
                commandHandler.run(currentCommand.next());
            }


        } catch (ParseException e) {
            System.out.println(e.getMessage());
        } catch (BadCommandException e) {
            System.out.println(e.getMessage());
        }
    }
}
