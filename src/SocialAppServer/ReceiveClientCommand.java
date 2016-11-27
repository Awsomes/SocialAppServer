package SocialAppServer;

import FileManagment.Saver;
import SocialAppGeneral.Command;
import SocialAppGeneral.Group;
import SocialAppGeneral.ReceiveCommand;
import SocialAppGeneral.RegisterInfo;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.attribute.GroupPrincipal;

/**
 * Created by kemo on 25/10/2016.
 */
public class ReceiveClientCommand extends ReceiveCommand {
    HalfDuplexConnection connection;
    public ReceiveClientCommand(Socket remote, HalfDuplexConnection connection) {
        super(remote);
        this.connection = connection;
    }
    @Override
    public void Analyze(Command command) {
        //TODO #AllTeam mem
        //our code starts Here HF
        //TODO #Server Command prototype
        if(command.getKeyWord().equals("changeColor"))
        {
            //DO ur algorithm
            Command command1 = new Command();
            command1.setKeyWord("changeColor");
            command1.setSharableObject("#023345");
            //lastly send new command to the client
            connection.sendCommand(command1);
        }
        if(command.getKeyWord().equals("new register")){
         // h3ml constrain el fe saver 7alyin

            Saver s=new Saver();


        }
        if (command.getKeyWord().equals(Group.CREATE_GROUP))
        {
            Group group = Group.fromJsonString(command.getObjectStr());
            GroupfileMangement g=new GroupfileMangement();
            try {
                g.create(group);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
