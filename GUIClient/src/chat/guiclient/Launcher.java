package chat.guiclient;

import chat.guiclient.controllers.chatscontrollsystem.ChatsController;
import chat.guiclient.controllers.contactpanelscontroller.ContactPanelsController;
import chat.shared.Contact;
import chat.shared.IClientRemoteFacade;
import chat.shared.IServerRemoteFacade;
import chat.shared.NetworkConstants;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by alex on 12/6/14.
 */
public class Launcher {
    public static void main(String[] args){
        try{
           // int port = args == null || args[0] == null ? 1099 : Integer.parseInt(args[0]);
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            IServerRemoteFacade serverRemoteFacade = (IServerRemoteFacade) registry.lookup(NetworkConstants.SERVER);
            final ClientFacade clientFacade = new ClientFacade(serverRemoteFacade);
            IClientRemoteFacade clientFacadeProxy = (IClientRemoteFacade) UnicastRemoteObject.exportObject(clientFacade,0);

            Launcher.setLaF();

            String nickName;Contact self = null;boolean loginSuccess = false;
            while(!loginSuccess){
                nickName = JOptionPane.showInputDialog("Enter you nick,please");
                if(nickName == null){System.exit(0);}
                while(nickName.equals("")){
                    JOptionPane.showMessageDialog(null, "Nick should not be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    nickName = JOptionPane.showInputDialog("Enter you nick,please", nickName);
                }
                self = new Contact(nickName);
                loginSuccess = serverRemoteFacade.loginContact(self, clientFacadeProxy);
                if(!loginSuccess) {JOptionPane.showMessageDialog(null, "Nick is already in use", "Error", JOptionPane.ERROR_MESSAGE);}
            }
            clientFacade.setCurrentContact(self);

            final Contact finalSelf = self;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ClientGUI clientGUI = new ClientGUI(clientFacade);
                    Launcher.initializeControllerAndLocator(clientGUI, finalSelf);
                    clientFacade.injectDependencies();
                    clientGUI.setVisible(true);
                }
            });
        }
        catch (Exception ex){

        }

    }

    private static void initializeControllerAndLocator(ClientGUI clientGUI, Contact finalSelf) {
        ChatsController chatsController = new ChatsController(clientGUI, finalSelf);
        ContactPanelsController panelsController = new ContactPanelsController(clientGUI.getRightSidePanel());
        ServiceLocator.initialize(chatsController, panelsController);

        chatsController.injectDependencies();
        panelsController.injectDependencies();
    }

    private static void setLaF(){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
