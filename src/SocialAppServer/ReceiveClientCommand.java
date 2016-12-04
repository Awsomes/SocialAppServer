package SocialAppServer;

import FileManagment.FilesManager;
import FileManagment.FilesPath;
import FileManagment.Saver;
import SocialAppGeneral.*;

import java.net.Socket;


/**
 * Created by kemo on 25/10/2016.
 */
class ReceiveClientCommand extends ReceiveCommand implements FilesPath {
    private HalfDuplexConnection connection;
    private String loggedUserId;
    ReceiveClientCommand(Socket remote, HalfDuplexConnection connection) {
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
            command1.setSharableObject("#000");
            //lastly send new command to the client
            connection.sendCommand(command1);
        }
       else if(command.getKeyWord().equals(RegisterInfo.KEYWORD)){
         // h3ml constrain el fe saver 7alyin
            RegisterInfo reg =RegisterInfo.fromJsonString(command.getObjectStr());
            reg.getUserInfo().setProfileImage("default");
            Saver s=new Saver(reg,connection);

            Admin a=new Admin();
          a.convertIntoPermnantUser(reg.getLoginInfo().getEMAIL());
            //System.out.println("in");
        }
       else if(command.getKeyWord().equals(LoginInfo.KEYWORD)){
            LoginInfo log=LoginInfo.fromJsonString(command.getObjectStr());
            loggedUserId = UserFinder.validate(log.getEMAIL(),log.getPassword());
            command.setSharableObject(loggedUserId);
            connection.sendCommand(command);
        }
        else if (command.getKeyWord().equals(Group.CREATE_GROUP))
        {
            String name = command.getObjectStr();
            Group group=new Group(name);
           group.setId(Integer.parseInt(Generator.GenerateUnigueId(FilesPath.GROUPS)));
//            group.setAdminId(1);
//            group.setMember(1);
            GroupfileMangement g=new GroupfileMangement();
            g.create(group);
            Command command1 = new Command();
            command1.setKeyWord(Group.CREATE_GROUP);
            command1.setSharableObject(group);
            connection.sendCommand(command1);
        }

       else if(command.getKeyWord().equals(Post.SAVE_POST_USER)){
            Post post=Post.fromJsonString(command.getObjectStr());
           PostManger.SavePost(post,FilesPath.USERS+post.getPostPos());
            Command command1 = new Command();
            command1.setKeyWord(Post.SAVE_POST_USER);
            command1.setSharableObject(post.convertToJsonString());
            connection.sendCommand(command1);

        }

       else if(command.getKeyWord().equals(Post.SAVE_POST_GROUP)) {
            Post post = Post.fromJsonString(command.getObjectStr());
            PostManger.SavePost(post, FilesPath.GROUPS + post.getPostPos());
            Command command1 = new Command();
            command1.setKeyWord(Post.SAVE_POST_USER);
            command1.setSharableObject(post.convertToJsonString());
            connection.sendCommand(command1);
        }

        else if(command.getKeyWord().equals(Post.LOAD_POST_USERS)){
            ArraylistPost posts=new ArraylistPost();
            posts=(ArraylistPost.fromJsonString(command.getObjectStr()));
            posts.setPosts(PostManger.PickPosts(FilesPath.USERS+posts.getOwnerPosts()));
            System.out.println(posts.getPosts());
            Command command1 = new Command();
            command1.setKeyWord(Post.LOAD_POST_USERS);
            command1.setSharableObject(posts.convertToJsonString());
            connection.sendCommand(command1);

        }
       else if(command.getKeyWord().equals(Post.Add_COMMENT)){
        Post post=Post.fromJsonString(command.getObjectStr());
           // post =PostManger.addComment(FilesPath.GROUPS+post.getPostPos(),post.getId(),post);
            PostManger.savePostWithoutId(post,FilesPath.GROUPS+post.getPostPos());
            Command command1 = new Command();
            command1.setKeyWord(Post.Add_COMMENT);
            command1.setSharableObject(post.convertToJsonString());
            connection.sendCommand(command1);
            SecondaryConnection.sendNotification("0",command1);
        }
           else if (command.getKeyWord().equals(UserInfo.PICK_INFO))
        {
            command.setSharableObject(UserPicker.pickUserInfo(command.getObjectStr()));
            connection.sendCommand(command);
        }
        else if (command.getKeyWord().equals(UserInfo.EDIT_INFO))
        {

            FilesManager.Removefile(FilesPath.USERS + loggedUserId+"\\" + FilesPath.INFO + ".txt", command.getObjectStr());
            command.setSharableObject("true");
            connection.sendCommand(command);


        }else if(command.getKeyWord().equals(LoggedUser.ADD_FRIEND)){

            String  id =command.getObjectStr();
            FilesManager.AddLine(USERS+id+"\\"+FriendRequest+".txt",loggedUserId);
            command.setSharableObject("true");
            connection.sendCommand(command);
            command.setKeyWord(LoggedUser.FRIEND_REQ);
            command.setSharableObject(loggedUserId);
            System.out.println(id);
            SecondaryConnection.sendNotification(id,command);
        }
    }
}
