import java.util.*;
import java.io.Console;

// D. Özel Hata Yönetimi: KuantumCokusuException
class KuantumCokusuException extends Exception {
    public KuantumCokusuException(String id) {
        super("Kuantum Çöküşü! Patlayan Nesne: " + id);
    }
}

// A. Temel Yapı: KuantumNesnesi Abstract Class
abstract class KuantumNesnesi {
    private String id;
    private double stabilite;
    private int tehlikeSeviyesi;

    public KuantumNesnesi(String id, double stabilite, int ts) {
        this.id = id;
        // Stabiliteyi ayarlarken kapsülleme kontrolünü kullan
        setStabilite(stabilite);
        this.tehlikeSeviyesi = ts;
    }

    public String getID() { return id; }
    public double getStabilite() { return stabilite; }
    public int getTehlikeSeviyesi() { return tehlikeSeviyesi; }

    // Kapsülleme (Encapsulation) ile stabilite kontrolü
    public void setStabilite(double s) {
        if (s > 100) s = 100;
        this.stabilite = s;
    }

    // Stabilite düşürme ve Kuantum Çöküşü kontrolü
    public void stabiliteDusur(double miktar) throws KuantumCokusuException {
        double yeniStabilite = getStabilite() - miktar;

        // Önce yeni değeri atayalım
        setStabilite(yeniStabilite);

        // Eğer yeni stabilite 0 veya altına düştüyse, hata fırlat
        if (yeniStabilite <= 0)
            throw new KuantumCokusuException(id);
    }

    // Durum Bilgisi (Polimorfizm için)
    public String DurumBilgisi() {
        String tip = this.getClass().getSimpleName();
        return String.format("| %-15s | %-5s | %-12.2f | %-8d |",
                tip, id, stabilite, tehlikeSeviyesi);
    }

    // Analiz Et (Abstract Metot)
    public abstract void AnalizEt() throws KuantumCokusuException;
}

// B. Arayüz: IKritik Interface
interface IKritik {
    void AcilDurumSogutmasi();
}

// C. Nesne Çeşitleri: VeriPaketi (Güvenli, IKritik değil)
class VeriPaketi extends KuantumNesnesi {
    public VeriPaketi(String id) {
        super(id, 100, 1);
    }

    @Override
    public void AnalizEt() throws KuantumCokusuException {
        System.out.println(">>> [LOG] Veri içeriği okundu. Stabilite 5 birim düşüş.");
        stabiliteDusur(5);
    }
}

// C. Nesne Çeşitleri: KaranlikMadde (Tehlikeli, IKritik'i uygular)
class KaranlikMadde extends KuantumNesnesi implements IKritik {
    public KaranlikMadde(String id) {
        super(id, 100, 8);
    }

    @Override
    public void AnalizEt() throws KuantumCokusuException {
        System.out.println(">>> [UYARI] Karanlık madde titreşimleri analiz edildi. Stabilite 15 birim düşüş.");
        stabiliteDusur(15);
    }

    @Override
    public void AcilDurumSogutmasi() {
        // Stabilite +50 artar (Max 100 olacak şekilde)
        setStabilite(Math.min(100, getStabilite() + 50));
    }
}

// C. Nesne Çeşitleri: AntiMadde (Çok Tehlikeli, IKritik'i uygular)
class AntiMadde extends KuantumNesnesi implements IKritik {
    public AntiMadde(String id) {
        super(id, 100, 10);
    }

    @Override
    public void AnalizEt() throws KuantumCokusuException {
        System.out.println(">>> [KRİTİK UYARI] EVRENİN DOKUSU TİTRİYOR... Stabilite 25 birim düşüş!");
        stabiliteDusur(25);
    }

    @Override
    public void AcilDurumSogutmasi() {
        // Stabilite +50 artar (Max 100 olacak şekilde)
        setStabilite(Math.min(100, getStabilite() + 50));
    }
}

// 3. Oynanış Döngüsü
public class Main {

    // Yardımcı metot: ID ile envanterden nesne bulma
    private static KuantumNesnesi findObjectByID(List<KuantumNesnesi> envanter, String id) {
        return envanter.stream()
                .filter(n -> n.getID().equalsIgnoreCase(id)) // Büyük/küçük harf duyarsız arama
                .findFirst().orElse(null);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<KuantumNesnesi> envanter = new ArrayList<>(); // Generic List
        Random rnd = new Random();
        int idCounter = 1;

        try {
            while (true) {
                // Menü başlığı süslendi
                System.out.println("* KUANTUM AMBARI KONTROL PANELİ - OMEGA SEKTÖRÜ       *");
                System.out.println("* VARDİYA AMİRİ: AKTİF                     *");
                System.out.println(" [1] Yeni Nesne Kabul Et (Rastgele Kritiklik)");
                System.out.println(" [2] Tüm Envanteri Listele (Detaylı Durum Raporu)");
                System.out.println(" [3] Nesneyi Analiz Et (Stabilite Düşüşü Başlat)");
                System.out.println(" [4] Acil Durum Soğutması Yap (Sadece IKritik Nesneler)");
                System.out.println(" [5] VARDİYA SONU (Güvenli Çıkış)");
                System.out.print("Seçiminiz [1-5]: ");

                // Scanner hatasını düzeltmek için tüm girdiyi String olarak okuyup parse etme
                String secimStr = sc.nextLine().trim();
                if (secimStr.isEmpty()) continue;

                int sec = -1;
                try {
                    sec = Integer.parseInt(secimStr);
                } catch (NumberFormatException e) {
                    System.out.println("!!! HATA: Geçersiz giriş. Lütfen menüdeki bir sayıyı (1-5) giriniz. !!!");
                    continue;
                }

                switch (sec) {
                    case 1 -> {
                        int r = rnd.nextInt(3);
                        KuantumNesnesi nesne;
                        String id = "N-" + (idCounter++); // ID atama yöntemi

                        if (r == 0) nesne = new VeriPaketi(id);
                        else if (r == 1) nesne = new KaranlikMadde(id);
                        else nesne = new AntiMadde(id);

                        envanter.add(nesne);

                        // Çıktı geliştirildi
                        System.out.println("=== [KABUL BAŞARILI] Yeni nesne ambara kabul edildi. ID: " + id + " ===");
                        System.out.println("| Tip             | ID    | Stabilite    | Tehlike  |");
                        System.out.println(nesne.DurumBilgisi());
                        System.out.flush(); // Çıktının hemen basılmasını sağlar
                    }

                    case 2 -> {
                        System.out.println("\n**************************************************************");
                        System.out.println("*** ENVANTER DURUM RAPORU (Toplam Nesne: " + envanter.size() + ") ***");
                        System.out.println("**************************************************************");
                        if (envanter.isEmpty()) {
                            System.out.println("--- Envanterde kritik veya sıradan nesne bulunmamaktadır. ---");
                        } else {
                            System.out.println("| Tip             | ID    | Stabilite    | Tehlike  |");
                            System.out.println("|-----------------|-------|--------------|----------|");
                            // Polimorfizm: Her nesnenin DurumBilgisi() metodu çağrılır
                            for (KuantumNesnesi n : envanter)
                                System.out.println(n.DurumBilgisi());
                            System.out.println("--------------------------------------------------------------");
                        }
                    }

                    case 3 -> {
                        System.out.print("Analiz edilecek nesnenin ID'sini giriniz: ");
                        String id = sc.nextLine();
                        KuantumNesnesi hedef = findObjectByID(envanter, id);

                        if (hedef == null) {
                            System.out.println("!!! HATA: Belirtilen ID'ye sahip nesne (" + id + ") envanterde bulunamadı. !!!");
                        } else {
                            // Analiz Et metodu KuantumCokusuException fırlatabilir.
                            hedef.AnalizEt();
                            System.out.printf("ANALİZ BİTTİ. %s'nin yeni Stabilite değeri: %.2f%%\n", hedef.getID(), hedef.getStabilite());
                        }
                    }

                    case 4 -> {
                        System.out.print("Acil Soğutma uygulanacak nesnenin ID'sini giriniz: ");
                        String id = sc.nextLine();
                        KuantumNesnesi hedef = findObjectByID(envanter, id);

                        if (hedef == null) {
                            System.out.println("!!! HATA: Belirtilen ID'ye sahip nesne (" + id + ") envanterde bulunamadı. !!!");
                        } else {
                            // Type Checking: Sadece IKritik olanlar soğutulabilir
                            if (hedef instanceof IKritik) {
                                // Downcasting yapıldı
                                ((IKritik) hedef).AcilDurumSogutmasi();
                                System.out.printf("*** SOĞUTMA BAŞARILI ***. %s'nin stabilite değeri +50 arttırıldı. Yeni Stabilite: %.2f%%\n", hedef.getID(), hedef.getStabilite());
                            } else {
                                System.out.println("!!! HATA: Bu nesne (" + hedef.getID() + ") kritik değildir ve soğutma mekanizmasına sahip değildir. !!!");
                            }
                        }
                    }

                    case 5 -> {
                        System.out.println("\n<<< VARDİYA SONU >>> Güvenli Çıkış yapılıyor...");
                        return;
                    }

                    default -> System.out.println("!!! HATA: Geçersiz seçim! Lütfen 1 ile 5 arasında bir değer giriniz. !!!");
                }
            }
        }
        // Game Over: KuantumCokusuException yakalanırsa program sonlanır
        catch (KuantumCokusuException ex) {
            System.out.println("\n\n########################################################");
            System.out.println("!!! SİSTEM KRİTİK ÇÖKÜŞ !!!");
            System.out.println(ex.getMessage());
            System.out.println("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
            System.out.println("########################################################\n");
        }
        finally {
            sc.close();
        }
    }
}