package chat.server;

import chat.shared.IServerRemoteFacade;
import chat.shared.NetworkConstants;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Инициализирует и стартует сервер.
 * Created by alex on 12/6/14.
 */
public class Launcher {
    public static void main(String[] args){
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }

        try {

            IServerRemoteFacade server = new Server();
            IServerRemoteFacade stub = (IServerRemoteFacade) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(NetworkConstants.SERVER, stub);
            System.out.println("Server bound");
        } catch (Exception e) {
            System.err.println("Server exception:");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void onUserInput(String input){
        switch (input){
            case "exit": System.exit(0);
        }
    }
}
