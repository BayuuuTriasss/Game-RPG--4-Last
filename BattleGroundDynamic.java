import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class BattleGroundDynamic {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        ArrayList<Musuh> gerombolanJMK48 = new ArrayList<>();
        gerombolanJMK48.add(new Slime());
        gerombolanJMK48.add(new Naga());
        gerombolanJMK48.add(new Zombie());

        System.out.println();
        System.out.println("=====================================");
        System.out.println("    WELCOME DI BATTLEGROUND RPG 2D   ");
        System.out.println("=====================================\n");
        System.out.print("Awas! JMK48 Datang Menghadang!!!");

        boolean isiBermain = true;

        while (isiBermain && !gerombolanJMK48.isEmpty()) {

            System.out.println("\n>>> MONSTER JMK48 <<<");
            for (int i = 0; i < gerombolanJMK48.size(); i++) { 
                Musuh m = gerombolanJMK48.get(i);
                System.out.println((i + 1) + ". " + m.namaMusuhh + " (HP: " + m.healthPoint + ")");
            }

            System.out.println("------------------------");
            System.out.println((gerombolanJMK48.size() + 1) + ". Kabur dari pertarungan");
            System.out.println("5. [SAVE GAME] Simpan Progress Pertarungan dengan JMK48");
            System.out.println("6. [LOAD GAME] Simpan Progress Pertarungan dengan JMK48");
            System.out.print("\nPilih target monster JMK48 (1-" + gerombolanJMK48.size() + ") atau aksi lainnya : ");

            try {
                int pilihanTarget = input.nextInt();

                
                if (pilihanTarget == gerombolanJMK48.size() + 1) {
                    System.out.println("Anda lari dari kawanan JMK48 dari BattleGround...");
                    isiBermain = false;
                    continue;

                } else if (pilihanTarget == 4) {
                    try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SaveGame_RPG.dat"))) {
                        oos.writeObject(gerombolanJMK48);
                        System.out.println(">>> GAME Berhasil Disimpan <<<");

                    } catch (Exception e) {
                        System.out.println(">>> Terjadi ERROR Saat Menyimpan Game." + e.getMessage());
                    }
                    continue;
                
                } else if (pilihanTarget == 5) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SaveGame_RPG.dat")))  {
                        gerombolanJMK48 = (ArrayList<Musuh>) ois.readObject();
                        System.out.println(">>> BERHASIL: Game Telah Dimuat!!! <<<");
                    } catch (FileNotFoundException e) {
                        System.out.println(">>> GAGAL: Terjadi Kesalahan saat Membaca file Save" + e.getMessage());
                    }
                    continue;
                } 

                
                if (pilihanTarget < 1 || pilihanTarget > gerombolanJMK48.size()) {
                    System.out.println("Pilihan tidak valid! Anda membuang giliran.");

                } else {
                    System.out.print("Masukkan Serangan Anda (10 - 100) : ");
                    int power = input.nextInt();

                    if (power < 10 || power > 100) {
                        throw new SeranganTidakValidExc(
                            "Tindakan yang Salah: Kekuatan serangan harus di antara 10 - 100!!"
                        );
                    }

                    int indeksMonster = pilihanTarget - 1;
                    Musuh target = gerombolanJMK48.get(indeksMonster); 

                    
                    System.out.println("\n>>> HASIL SERANGAN ANDA <<<");
                    target.terimaDamage(power); 
                    
                    if (target.healthPoint <= 0) {
                        System.out.println(target.namaMusuhh + " TEWAS WOIII"); 

                        if (target instanceof canLoot) {
                            canLoot loot = (canLoot) target;
                            loot.jatuhkanItem();
                        }
                        gerombolanJMK48.remove(indeksMonster); 
                    }
                }

                
                if (gerombolanJMK48.isEmpty()) {
                    System.out.println("\nSelamat Anda telah menang dengan Gaya !!!");
                    isiBermain = false;
                    break;
                }

                
                System.out.println("\n>>> GILIRAN JMK48 MEMBALAS <<<");
                for (int i = 0; i < gerombolanJMK48.size(); i++) { 
                    Musuh monsterAktif = gerombolanJMK48.get(i);   
                    System.out.println();
                    monsterAktif.suaraKhas();

                    if (monsterAktif instanceof canFly) {
                        System.out.println("PERINGATAN SERANGAN UDARA TERDETEKSI!!!");
                        canFly monsterTerbang = (canFly) monsterAktif;
                        monsterTerbang.lepasLandas();
                        monsterTerbang.seranganUdara();
                    } else {
                        monsterAktif.serangPemain();
                    }
                }

            } catch (SeranganTidakValidExc e) {
                System.out.println("KESALAHAN GAME: " + e.getMessage());
                input.nextLine();

            } catch (Exception e) { 
                System.out.println("Terjadi Error Input, Try Lagi!");
                input.nextLine();
            }

        } 

        input.close();
        System.out.println("\nPermainan Berakhir.");
    }
}