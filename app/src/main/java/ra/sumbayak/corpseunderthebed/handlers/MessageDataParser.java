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
import ra.sumbayak.corpseunderthebed.datas.messages.*;

class MessageDataParser {
    
    private final int mFileCount;
    private final IOHandler mIO;
    private final Resources mResources;
    private final String mPackageName;
    private XmlResourceParser mParser;
    
    private MessageDataParser (Context context) {
        mIO = new IOHandler (context);
        mPackageName = context.getPackageName ();
        mResources = context.getResources ();
        mFileCount = countMessageData ();
    }
    
    static MessageDataParser getParser (Context context) {
        return new MessageDataParser (context);
    }
    
    void parseMessageData () {
        for (int i = 0; i < mFileCount; i++) {
            String filename = "day" + String.valueOf (i);
            int id = mResources.getIdentifier (filename, "xml", mPackageName);
            mParser = mResources.getXml (id);
            MessageData messageData;
            
            try {
                messageData = parseXml ();
                mIO.saveMessageData (messageData, i);
                mParser.close ();
            }
            catch (IOException | XmlPullParserException e) {
                e.printStackTrace ();
            }
        }
    }
    
//    Dear me-in-the-future,
//    I told you already you won't remember how these codes work from this point.
//    So don't stress yourself and let the code as it is.
//    Just trust me. We had been thinking about it for weeks.
//    The only thing I can tell you is it does what it says.
    
    private MessageData parseXml () throws IOException, XmlPullParserException {
        while (mParser.getEventType () == XmlPullParser.START_DOCUMENT) {
            mParser.next ();
        }
        
        mParser.next ();
        mParser.nextText ();
        mParser.next ();
        
        String date;
        date = mParser.nextText ();
        mParser.next ();
        
        List<Message> messages;
        messages = new ArrayList<> ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            mParser.next ();
            
            switch (mParser.nextText ()) {
                case Message.TYPE_NORMAL: messages.add (pullNormalMessage ()); break;
                case Message.TYPE_CHOICES: messages.add (pullChoicesMessage ()); break;
                case Message.TYPE_NOTE: messages.add (pullNoteMessage ()); break;
                case Message.TYPE_SHARE: messages.add (pullShareMessage ()); break;
                case Message.TYPE_INFO: messages.add (pullInfoMessage ()); break;
                case Message.TYPE_COMMENT: messages.add (pullCommentMessage ()); break;
            }
            
            mParser.next ();
        }
        
        return new MessageData (date, messages);
    }
    
    private NormalMessage pullNormalMessage () throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        mParser.next ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            att.put (mParser.getName (), mParser.nextText ());
            mParser.next ();
        }
        
        return new NormalMessage (
            att.get ("room"), att.get ("sender"), att.get ("time"), att.get ("text")
        );
    }
    
    private ChoicesMessage pullChoicesMessage () throws IOException, XmlPullParserException {
        List<Choice> choices = new ArrayList<> ();
        Map<String, String> att = new HashMap<> ();
        mParser.next ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            if (mParser.getName ().equals ("choices")) choices = pullChoices ();
            else att.put (mParser.getName (), mParser.nextText ());
            mParser.next ();
        }
        
        return new ChoicesMessage (
            att.get ("room"), att.get ("save"), choices
        );
    }
    
    private List<Choice> pullChoices () throws IOException, XmlPullParserException {
        List<Choice> choices = new ArrayList<> ();
        mParser.next ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            Map<String, String> att = new HashMap<> ();
            mParser.next ();
            
            while (mParser.getEventType () != XmlPullParser.END_TAG) {
                att.put (mParser.getName (), mParser.nextText ());
                mParser.next ();
            }
            
            Choice choice;
            choice = new Choice (Integer.parseInt (att.get ("replies")), att.get ("label"));
            choices.add (choice);
            mParser.next ();
        }
        
        return choices;
    }
    
    private NoteMessage pullNoteMessage () throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        mParser.next ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            att.put (mParser.getName (), mParser.nextText ());
            mParser.next ();
        }
        
        return new NoteMessage (
            att.get ("room"),
            att.get ("sender"),
            att.get ("time"),
            att.get ("author"),
            att.get ("body").replace ("\\n", System.getProperty ("line.separator"))
        );
    }
    
    private ShareMessage pullShareMessage () throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        Post post = null;
        mParser.next ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            if (mParser.getName ().equals ("post")) post =  pullPost ();
            else att.put (mParser.getName (), mParser.nextText ());
            mParser.next ();
        }
        
        return new ShareMessage (
            att.get ("room"),
            att.get ("sender"),
            att.get ("time"),
            post
        );
    }
    
    private Post pullPost () throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        mParser.next ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            att.put (mParser.getName (), mParser.nextText ());
            mParser.next ();
        }
        
        return new Post (
            att.get ("author"),
            att.get ("date"),
            att.get ("time"),
            att.get ("body").replace ("\\n", System.getProperty ("line.separator"))
        );
    }
    
    private InfoMessage pullInfoMessage () throws IOException, XmlPullParserException {
        mParser.next ();
        
        switch (mParser.nextText ()) {
            case InfoMessage.CATEGORY_INVITATION: return pullInvitationMessage ();
            default: return null;
        }
    }
    
    private InvitationMessage pullInvitationMessage () throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        mParser.next ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            att.put (mParser.getName (), mParser.nextText ());
            mParser.next ();
        }
        
        return new InvitationMessage (
            att.get ("room"), att.get ("members").split (",")
        );
    }
    
    private Message pullCommentMessage () throws IOException, XmlPullParserException {
        Map<String, String> att = new HashMap<> ();
        mParser.next ();
        
        while (mParser.getEventType () != XmlPullParser.END_TAG) {
            att.put (mParser.getName (), mParser.nextText ());
            mParser.next ();
        }
        
        return new CommentMessage (
            att.get ("room"), att.get ("author"), att.get ("text")
        );
    }
    
    private int countMessageData () {
        for (int i = 0; ; i++) {
            String filename;
            filename = "day" + String.valueOf (i);
            int id;
            id = mResources.getIdentifier (filename, "xml", mPackageName);
            if (id == 0) return i;
        }
    }
}
