import java.io.*;
import java.util.ArrayList;

public class SystemSave {
    private static final String NAMA_FILE = "SaveGame_RPG.dat";

    public static void saveGame(ArrayList<Musuh> dataMonster) {
        try {
            FileOutputStream fileOut   = new FileOutputStream(NAMA_FILE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(dataMonster);

            objectOut.close();
            fileOut.close();

            System.out.println("[SYSTEM] Progress permainan kamu berhasil disimpan!!");

        } catch (IOException e) {
            System.out.println("[SYSTEM ERROR] Gagal menyimpan game: " + e.getMessage());
        }
    }

    public static ArrayList<Musuh> loadGame() {
        ArrayList<Musuh> dataTerload = new ArrayList<>();

        try {
            FileInputStream fileIn     = new FileInputStream(NAMA_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            dataTerload = (ArrayList<Musuh>) objectIn.readObject(); 

            objectIn.close();
            fileIn.close();

            System.out.println("[SYSTEM] Progress permainan berhasil dimuat!!");

        } catch (IOException e) { 
            System.out.println("[SYSTEM] Tidak ada save data. Memulai game baru JMK48.");

        } catch (ClassNotFoundException e) { 
            System.out.println("[SYSTEM] Format save data tidak dikenali: " + e.getMessage());
        }
        return dataTerload;
    }
}