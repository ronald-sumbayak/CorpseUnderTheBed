package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ra.sumbayak.corpseunderthebed.datas.MessageData;
import ra.sumbayak.corpseunderthebed.datas.msg.*;
import ra.sumbayak.corpseunderthebed.datas.msg.comment.CommentMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.info.InfoMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.info.InvitationMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.choices.Choices;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.choices.ChoicesMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.NormalMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.NoteMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.Post;

public class MessageDataParser {
    
    private int mDayCount;
    private Resources mResources;
    private String mPackageName;
    private FileIOHandler mIO;
    
    private MessageDataParser (Context context) {
        mResources = context.getResources ();
        mDayCount = countMessageData ();
        mPackageName = context.getPackageName ();
        mIO = new FileIOHandler (context);
    }
    
    public void parseMessageData () {
        for (int day = 1; day <= mDayCount; day++) {
            String fileName = "day" + String.valueOf (day);
            int id = mResources.getIdentifier (fileName, "xml", mPackageName);
            XmlResourceParser parser = mResources.getXml (id);
            MessageData messageData;
            
            try {
                messageData = parseXml (parser);
                mIO.saveMessageData (messageData, day);
                parser.close ();
            }
            catch (IOException | XmlPullParserException e) {
                e.printStackTrace ();
            }
        }
    }
    
    private MessageData parseXml (XmlResourceParser parser) throws IOException, XmlPullParserException {
        while (parser.getEventType () == XmlPullParser.START_DOCUMENT) {
            parser.next ();
        }
        
        parser.next ();
        parser.nextText ();
        parser.next ();
        
        String date;
        date = parser.nextText ();
        parser.next ();
        
        List<Message> messages;
        messages = new ArrayList<> ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            parser.next ();
            
            switch (parser.nextText ()) {
                case Message.TYPE_NORMAL: messages.add (pullNormalMessage (parser)); break;
                case Message.TYPE_CHOICES: messages.add (pullChoicesMessage (parser)); break;
                case Message.TYPE_NOTE: messages.add (pullNoteMessage (parser)); break;
                case Message.TYPE_INFO: messages.add (pullInfoMessage (parser)); break;
                case Message.TYPE_COMMENT: messages.add (pullCommentMessage (parser)); break;
            }
            
            parser.next ();
        }
        
        return new MessageData (date, messages);
    }
    
    private NormalMessage pullNormalMessage (XmlResourceParser parser) throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        parser.next ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            att.put (parser.getName (), parser.nextText ());
            parser.next ();
        }
        
        return new NormalMessage (
            att.get ("room"), att.get ("sender"), att.get ("time"), att.get ("text")
        );
    }
    
    private static ChoicesMessage pullChoicesMessage (XmlResourceParser parser) throws IOException, XmlPullParserException {
        List<Choices> choicesList = new ArrayList<> ();
        Map<String, String> att = new HashMap<> ();
        parser.next ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            if (parser.getName ().equals ("choices")) choicesList = pullChoices (parser);
            else att.put (parser.getName (), parser.nextText ());
            
            parser.next ();
        }
        
        return new ChoicesMessage (att.get ("room"), att.get ("time"), att.get ("saveAs"), choicesList);
    }
    
    private static List<Choices> pullChoices (XmlResourceParser parser) throws IOException, XmlPullParserException {
        List<Choices> choicesList = new ArrayList<> ();
        parser.next ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            Map<String, String> att = new HashMap<> ();
            parser.next ();
            
            while (parser.getEventType () != XmlPullParser.END_TAG) {
                att.put (parser.getName (), parser.nextText ());
                parser.next ();
            }
            
            choicesList.add (new Choices (Integer.parseInt (att.get ("replies")), att.get ("text")));
            parser.next ();
        }
        
        return choicesList;
    }
    
    private static NoteMessage pullNoteMessage (XmlResourceParser parser) throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        parser.next ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            att.put (parser.getName (), parser.nextText ());
            parser.next ();
        }
        
        Post post;
        post = new Post (
            att.get ("author"), att.get ("originDate"), att.get ("originTime"), att.get ("text")
        );
        
        return new NoteMessage (
            att.get ("room"), att.get ("sender"), att.get ("time"), post, Boolean.valueOf (att.get ("saveToNotes"))
        );
    }
    
    private static InfoMessage pullInfoMessage (XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.next ();
        
        switch (parser.nextText ()) {
            case InfoMessage.CATEGORY_INVITATION: return pullInvitationMessage (parser);
            default: return null;
        }
    }
    
    private static InvitationMessage pullInvitationMessage (XmlResourceParser parser) throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        parser.next ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            att.put (parser.getName (), parser.nextText ());
            parser.next ();
        }
        
        return new InvitationMessage (
            att.get ("room"), Integer.parseInt (att.get ("members")), att.get ("memberList").split (",")
        );
    }
    
    private static Message pullCommentMessage (XmlResourceParser parser) throws IOException, XmlPullParserException {
        String room = null;
        parser.next ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            room = parser.nextText ();
            parser.next ();
        }
        
        return new CommentMessage (room);
    }
    
    private Integer countMessageData () {
        int dayCount = 0, id;
        
        do {
            String fileName;
            fileName = "day" + String.valueOf (++dayCount);
            id = mResources.getIdentifier (fileName, "xml", mPackageName);
        }
        while (id != 0);
        
        return dayCount - 1;
    }
}
