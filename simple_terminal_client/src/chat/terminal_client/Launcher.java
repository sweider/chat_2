package chat.terminal_client;

import chat.shared.Contact;
import chat.shared.IClientRemoteFacade;
import chat.shared.IServerRemoteFacade;
import chat.shared.NetworkConstants;

import java.io.Console;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by alex on 12/6/14.
 */
public class Launcher {
    public static void main(String[] args){
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);
            IServerRemoteFacade serverRemoteFacade = (IServerRemoteFacade) registry.lookup(NetworkConstants.SERVER);
            System.out.println("Server getted");

            Console console = System.console();
            String nickName;Contact self;Client client;boolean loginSuccess;
            IClientRemoteFacade remoteProxy;

            do{
                System.out.println("Enter your nickname please:");
                nickName = console.readLine();
                self = new Contact(nickName);
                client = new Client(serverRemoteFacade, self);
                remoteProxy = (IClientRemoteFacade) UnicastRemoteObject.exportObject(client, 0);
                loginSuccess = serverRemoteFacade.loginContact(self, remoteProxy);
                if(!loginSuccess) {
                    System.out.println("This nickname already use. Try other name.");
                }
            }while(!loginSuccess);
            ConsoleWorker worker = new ConsoleWorker(client, console);
            System.out.println("start chat");
            worker.start();
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }
}
