package chat.terminal_client;

import java.io.Console;

/**
 * Created by alex on 12/6/14.
 */
public class ConsoleWorker {
    private final Client client;
    private Console console;

    public ConsoleWorker(Client client, Console console) {
        this.client = client;
        this.console = console;
    }

    public void start(){
        String input;
        while(true){
            input = this.console.readLine();
            if(input.equals("/exit")){
                this.client.goOffline();
                return;
            }
            this.client.sendMessage(input);
        }
    }
}
