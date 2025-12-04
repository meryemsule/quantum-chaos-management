using System;
using System.Collections.Generic;
using System.Linq; // Find metodu için kullanılır
using System.Threading.Tasks;

// Konsol Girdisi için Yardımcı Sınıf (JS'teki readline'ı simüle eder)
public static class ConsoleHelper
{
    // C#'ta Console.ReadLine() blocking'dir (senkron). 
    // JS'teki async/await yapısına uyması için Task.FromResult kullanılır.
    public static Task<string> PromptAsync(string q)
    {
        Console.Write(q);
        return Task.FromResult(Console.ReadLine());
    }
}

// --- HATA SINIFI ---
public class KuantumCokusuException : Exception
{
    public KuantumCokusuException(string id) : base($"Kuantum Çöküşü! Patlayan nesne: {id}") { }
}

// --- TEMEL SINIF (BASE CLASS) ---
public abstract class KuantumNesnesi
{
    private readonly string _id;
    private double _stabilite;

    // Read-only property (Getter)
    public string ID => _id;

    public int Tehlike { get; }

    // Stabiliteyi 0-100 aralığında tutan Kapsülleme (Encapsulation)
    public double Stabilite
    {
        get => _stabilite;
        set
        {
            // Değer 100'den büyükse 100, 0'dan küçükse 0 olsun.
            if (value > 100) _stabilite = 100;
            else if (value < 0) _stabilite = 0;
            else _stabilite = value;
        }
    }

    public KuantumNesnesi(string id, double stabiliteInit, int tehlike)
    {
        _id = id;
        Stabilite = stabiliteInit;
        Tehlike = tehlike;
    }

    // Alt sınıfların uygulaması gereken Soyut Metot
    public abstract void AnalizEt();

    public string DurumBilgisi()
    {
        // C#'ta formatlama için Interpolated String kullanılır. F2 iki ondalık basamak demektir.
        return $"{ID} - Stabilite: {Stabilite:F2}";
    }

    protected void CheckCollapse()
    {
        if (Stabilite <= 0)
        {
            throw new KuantumCokusuException(ID);
        }
    }

    // Soğutma metodu, diğer sınıflarda override edilebilmesi için 'virtual' tanımlandı.
    public virtual void AcilDurumSogutmasi()
    {
        // Temel nesne (VeriPaketi) için boş uygulama
    }
}

// --- ALT SINIF 1: Veri Paketi ---
public class VeriPaketi : KuantumNesnesi
{
    public VeriPaketi(string id) : base(id, 100, 1) { }

    public override void AnalizEt()
    {
        Stabilite -= 5;
        Console.WriteLine("Veri içeriği okundu.");
        CheckCollapse();
    }
    // AcilDurumSogutmasi metodunu override etmez, temel sınıftaki boş metot kullanılır.
}

// --- ALT SINIF 2: Karanlık Madde ---
public class KaranlikMadde : KuantumNesnesi
{
    public KaranlikMadde(string id) : base(id, 100, 8) { }

    public override void AnalizEt()
    {
        Stabilite -= 15;
        Console.WriteLine("Karanlık madde analiz edildi.");
        CheckCollapse();
    }

    public override void AcilDurumSogutmasi()
    {
        Stabilite += 50; // Stabilite setleyici ile 100'de sınırlandırılır.
        Console.WriteLine("Acil soğutma uygulandı.");
    }
}

// --- ALT SINIF 3: Anti Madde ---
public class AntiMadde : KuantumNesnesi
{
    public AntiMadde(string id) : base(id, 100, 10) { }

    public override void AnalizEt()
    {
        Stabilite -= 25;
        Console.WriteLine("Evrenin dokusu titriyor...");
        CheckCollapse();
    }

    public override void AcilDurumSogutmasi()
    {
        Stabilite += 50; // Stabilite setleyici ile 100'de sınırlandırılır.
        Console.WriteLine("Acil soğutma uygulandı.");
    }
}

// --- ANA PROGRAM SINIFI ---
public class Program
{
    private static readonly List<KuantumNesnesi> env = new List<KuantumNesnesi>();
    private static readonly Random rnd = new Random();

    // ID üretimi (JS'teki Math.random yerine Guid kullanılır)
    private static string NewId(string prefix) => $"{prefix}-{Guid.NewGuid().ToString().Substring(0, 8)}";

    // Ana Uygulama Döngüsü
    public static async Task Main()
    {
        try
        {
            while (true)
            {
                Console.WriteLine("\nKUANTUM AMBARI KONTROL PANELİ");
                Console.WriteLine("1. Yeni Nesne Ekle");
                Console.WriteLine("2. Tüm Envanteri Listele");
                Console.WriteLine("3. Nesneyi Analiz Et (ID ile)");
                Console.WriteLine("4. Acil Durum Soğutması Yap (sadece kritik)");
                Console.WriteLine("5. Çıkış");

                string sec = (await ConsoleHelper.PromptAsync("Seçiminiz: ")).Trim();

                if (sec == "1")
                {
                    int t = rnd.Next(3); // 0, 1, 2 üretir
                    KuantumNesnesi n;

                    if (t == 0) n = new VeriPaketi(NewId("VP"));
                    else if (t == 1) n = new KaranlikMadde(NewId("KM"));
                    else n = new AntiMadde(NewId("AM"));

                    env.Add(n);
                    Console.WriteLine("Yeni nesne eklendi: " + n.DurumBilgisi());
                }
                else if (sec == "2")
                {
                    Console.WriteLine("Envanter Durum Raporu:");
                    // C#'ta ForEach kullanımı
                    env.ForEach(e => Console.WriteLine(e.DurumBilgisi()));
                }
                else if (sec == "3")
                {
                    string id = (await ConsoleHelper.PromptAsync("Analiz edilecek ID: ")).Trim();
                    // LINQ Find ile büyük/küçük harf duyarlılığı olmadan arama
                    KuantumNesnesi found = env.Find(x => x.ID.Equals(id, StringComparison.OrdinalIgnoreCase));

                    if (found == null)
                    {
                        Console.WriteLine("ID bulunamadı.");
                        continue;
                    }
                    found.AnalizEt();
                    Console.WriteLine(found.DurumBilgisi());
                }
                else if (sec == "4")
                {
                    string id = (await ConsoleHelper.PromptAsync("Soğutulacak ID: ")).Trim();
                    KuantumNesnesi found = env.Find(x => x.ID.Equals(id, StringComparison.OrdinalIgnoreCase));

                    if (found == null)
                    {
                        Console.WriteLine("ID bulunamadı.");
                        continue;
                    }

                    // Nesnenin soğutma metodunu override edip etmediğini kontrol etme
                    // Yani, VeriPaketi değil de Karanlık Madde veya AntiMadde olup olmadığını kontrol eder.
                    if (found is KaranlikMadde || found is AntiMadde)
                    {
                        found.AcilDurumSogutmasi();
                        Console.WriteLine(found.DurumBilgisi());
                    }
                    else
                    {
                        Console.WriteLine("Bu nesne soğutulamaz!");
                    }
                }
                else if (sec == "5")
                {
                    Console.WriteLine("Çıkılıyor...");
                    break;
                }
                else
                {
                    Console.WriteLine("Geçersiz seçim.");
                }
            }
        }
        catch (KuantumCokusuException ex)
        {
            // Custom hata yakalama
            Console.WriteLine("\nSİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
            Console.WriteLine(ex.Message);
        }
        catch (Exception ex)
        {
            // Beklenmeyen genel hata yakalama
            Console.WriteLine("Beklenmeyen hata: " + ex.Message);
        }
    }
}