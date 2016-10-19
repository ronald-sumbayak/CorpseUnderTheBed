package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.*;

import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.MessageData;

public class FileIOHandler {
    
    // TODO: setup enclosing class for FileIOHandler.
    
    private Context mContext;
    
    public FileIOHandler (Context context) {
        mContext = context;
    }
    
    public boolean isGameDataExist () {
        Log.d ("cutb_debug", "at FileIOHandler.#isGameDataExist");
        try {
            InputStream is = mContext.openFileInput ("data.cutb");
            ObjectInputStream ois = new ObjectInputStream (is);
            GameData gameData = (GameData) ois.readObject ();
            ois.close ();
            Log.i ("cutb_debug", "    GameData exist");
            return true;
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace ();
            Log.e ("cutb_debug", "    GameData not found.");
            return false;
        }
    }
    
    public void saveGameData (GameData gameData) {
        Log.d ("cutb_debug", "at FileIOHandler.#saveGameData");
        try {
            OutputStream os = mContext.openFileOutput ("data.cutb", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream (os);
            oos.writeObject (gameData);
            oos.close ();
            Log.i ("cutb_debug", "   GameData saved.");
        }
        catch (IOException e) {
            e.printStackTrace ();
            Log.e ("cutb_debug", "   Failed saving GameData.");
        }
    }
    
    @NonNull
    public GameData loadGameData () {
        Log.d ("cutb_debug", "at FileIOHandler.#loadGameData");
        try {
            InputStream is = mContext.openFileInput ("data.cutb");
            ObjectInputStream ois = new ObjectInputStream (is);
            GameData data = (GameData) ois.readObject ();
            ois.close ();
            Log.i ("cutb_debug", "   GameData loaded.");
            return data;
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace ();
            Log.e ("cutb_debug", "   Failed loading GameData.");
            return new DataHandler (mContext);
        }
    }
    
    public void saveMessageData (MessageData messageData, Integer day) {
        Log.d ("cutb_debug", "at FileIOHandler.#saveMessageData");
        try {
            OutputStream os = mContext.openFileOutput ("day" + day + ".cutb", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream (os);
            oos.writeObject (messageData);
            oos.close ();
            Log.i ("cutb_debug", "   MessageData day" + day + " saved.");
        }
        catch (IOException e) {
            e.printStackTrace ();
            Log.e ("cutb_debug", "   Failed saving MessageData day" + day + ".");
        }
    }
    
    public MessageData loadMessageData (Integer day) {
        Log.d ("cutb_debug", "at FileIOHandler.#loadMessageData");
        try {
            InputStream is = mContext.openFileInput ("day" + String.valueOf (day) + ".cutb");
            ObjectInputStream ois = new ObjectInputStream (is);
            MessageData messageData = (MessageData) ois.readObject ();
            ois.close ();
            Log.i ("cutb_debug", "   MessageData day" + day + " loaded.");
            return messageData;
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace ();
            Log.e ("cutb_debug", "   Failed loading MessageData day" + day + ".");
            return null;
        }
    }
}
