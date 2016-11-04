package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.util.Log;

import java.io.*;

import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.MessageData;

public class IOHandler {
    
    private Context mContext;
    
    public IOHandler (Context context) {
        mContext = context;
    }
    
    public static IOHandler getIOInstance (Context context) {
        return new IOHandler (context);
    }
    
    public boolean isGameDataExist () {
        Log.d ("cutb_debug", "at IOHandler.#isGameDataExist");
        try {
            InputStream is = mContext.openFileInput ("data.cutb");
            is.close ();
            Log.i ("cutb_debug", "    GameData exist");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace ();
            Log.e ("cutb_debug", "    GameData not found.");
            return false;
        }
    }
    
    public boolean saveTempGameData (GameData gameData) {
        Log.d ("cutb_debug", "at IOHandler.#saveTempGameData");
        try {
            OutputStream os = mContext.openFileOutput ("temp.cutb", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream (os);
            oos.writeObject (gameData);
            oos.close ();
            Log.i ("cutb_debug", "   TempGameData saved.");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace ();
            Log.e ("cutb_debug", "   Failed saving tempGameData");
            return false;
        }
    }
    
    public boolean saveGameData (GameData gameData) {
        Log.d ("cutb_debug", "at IOHandler.#saveGameData");
        try {
            OutputStream os = mContext.openFileOutput ("data.cutb", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream (os);
            oos.writeObject (gameData);
            oos.close ();
            Log.i ("cutb_debug", "   GameData saved.");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace ();
            Log.e ("cutb_debug", "   Failed saving GameData.");
            return false;
        }
    }
    
    @SuppressWarnings ("ResultOfMethodCallIgnored")
    public void mergeGameData () {
        Log.d ("cutb_debug", "at IOHandler.#mergeGameData");
        String filesDir;
        filesDir = mContext.getFilesDir ().getAbsolutePath () + "/";
        
        File temp, data;
        temp = new File (filesDir + "temp.cutb");
        data = new File (filesDir + "data.cutb");
//        Log.d ("cutb_debug", "   --- data: " + loadGameData ().getIndex ());
//        Log.d ("cutb_debug", "   --- temp: " + loadTempData ().getIndex ());
        
        if (temp.exists ()) {
            Log.d ("cutb_debug", "   data replaced with temp_data");
            temp.renameTo (data);
            Log.d ("cutb_debug", "   data: " + loadGameData ());
            Log.d ("cutb_debug", "   temp: " + loadTempData ());
        }
    }
    
    public GameData loadGameData () {
        Log.d ("cutb_debug", "at IOHandler.#loadGameData");
        try {
            InputStream is = mContext.openFileInput ("data.cutb");
            ObjectInputStream ois = new ObjectInputStream (is);
            GameData data = (GameData) ois.readObject ();
            ois.close ();
            Log.i ("cutb_debug", "   GameData loaded.");
            return data;
        }
        catch (ClassNotFoundException | IOException e) {
            //e.printStackTrace ();
            Log.e ("cutb_debug", "   Failed loading GameData.");
            return null;
            //return new DataHandler (mContext);
        }
    }
    
    public GameData loadTempData () {
        Log.d ("cutb_debug", "at IOHandler.#loadTempData");
        try {
            InputStream is = mContext.openFileInput ("temp.cutb");
            ObjectInputStream ois = new ObjectInputStream (is);
            GameData data = (GameData) ois.readObject ();
            ois.close ();
            Log.i ("cutb_debug", "   TempData loaded.");
            return data;
        }
        catch (ClassNotFoundException | IOException e) {
            //e.printStackTrace ();
            Log.e ("cutb_debug", "   Failed loading TempData.");
            return null;
        }
    }
    
    public void saveMessageData (MessageData messageData, Integer day) {
        Log.d ("cutb_debug", "at IOHandler.#saveMessageData");
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
        Log.d ("cutb_debug", "at IOHandler.#loadMessageData");
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
