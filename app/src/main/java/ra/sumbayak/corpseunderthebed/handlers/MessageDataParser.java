package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ra.sumbayak.corpseunderthebed.datas.MessageData;
import ra.sumbayak.corpseunderthebed.datas.msg.Message;
import ra.sumbayak.corpseunderthebed.datas.msg.comment.CommentMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.info.InfoMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.info.InvitationMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.NormalMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.choices.Choices;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.choices.ChoicesMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.Post;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.PostMessage;

public class MessageDataParser {
    
    private int mDayCount;
    private Resources mResources;
    private String mPackageName;
    private IOHandler mIO;
    
    private MessageDataParser (Context context) {
        mResources = context.getResources ();
        mPackageName = context.getPackageName ();
        mDayCount = countMessageData ();
        mIO = new IOHandler (context);
    }
    
    public static MessageDataParser getParser (Context context) {
        return new MessageDataParser (context);
    }
    
    public void parseMessageData () {
        for (int day = 0; day <= mDayCount; day++) {
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
    
//    Dear me-in-the-future,
//    I told you already you won't remember how these codes work from this point.
//    So don't stress yourself and let the code as it is.
//    Just trust me. We had been thinking about it for days.
//    The only thing I can tell you is it does what the it says.
    
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
        
        return new ChoicesMessage (
            att.get ("room"), att.get ("saveAs"), choicesList
        );
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
            
            choicesList.add (new Choices (
                Integer.parseInt (att.get ("replies")), att.get ("label")
            ));
            parser.next ();
        }
        
        return choicesList;
    }
    
    private static PostMessage pullNoteMessage (XmlResourceParser parser) throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        parser.next ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            att.put (parser.getName (), parser.nextText ());
            parser.next ();
        }
        
        String body;
        body = att.get ("body").replace ("\\n", System.getProperty ("line.separator"));
        att.remove ("body");
        att.put ("body", body);
        
        return new PostMessage (
            att.get ("room"),
            att.get ("sender"),
            att.get ("time"),
            new Post (
                att.get ("author"), att.get ("originDate"), att.get ("originTime"), att.get ("body")
            ),
            Boolean.parseBoolean (att.get ("saveToNotes"))
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
    
        Log.d ("cutb_debug", att.get ("memberCount") + " " + att.get ("memberList"));
        Log.d ("cutb_debug", att.toString ());
        
        return new InvitationMessage (
            att.get ("room"), Integer.parseInt (att.get ("memberCount")), att.get ("memberList").split (",")
        );
    }
    
    private static Message pullCommentMessage (XmlResourceParser parser) throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        parser.next ();
        
        while (parser.getEventType () != XmlPullParser.END_TAG) {
            att.put (parser.getName (), parser.nextText ());
            parser.next ();
        }
        
        return new CommentMessage (att.get ("room"), att.get ("writer"), att.get ("text"));
    }
    
    private int countMessageData () {
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
