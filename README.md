# 🌌 Quantum Chaos Management

**Omega Sektörü'ndeki Kuantum Veri Ambarı'na hoş geldiniz, Vardiya Amiri!**

Bu proje; evrenin en kararsız maddelerini dijital ortamda saklayan, yöneten ve analiz eden bir **simülasyon yazılımıdır**.

Sistem, **Nesne Yönelimli Programlama (OOP)** prensiplerini uygulamalı olarak göstermek amacıyla geliştirilmiştir ve aynı mimari **4 farklı programlama dilinde** uygulanmıştır.

---

# 🎯 Project Goal

Amaç, Kuantum Veri Ambarı'ndaki maddelerin **stabilite seviyesini %0'ın üzerinde tutarak** bir **Quantum Collapse (Kuantum Çöküşü)** yaşanmasını engellemektir.

Simülasyon boyunca kullanıcı:

- yeni kuantum nesneleri ekler
- mevcut nesneleri analiz eder
- kritik durumlarda stabiliteyi artırır

Eğer herhangi bir nesnenin stabilitesi **0'ın altına düşerse**, sistem **Kuantum Çöküşü** ile sonlanır.

---

# 🧠 Object-Oriented Architecture

Proje temel OOP prensiplerini içerecek şekilde tasarlanmıştır.

## A. Base Structure & Inheritance

### `KuantumNesnesi` (Abstract Class)

Tüm kuantum maddelerinin temel sınıfıdır.

Özellikleri:

- `ID`
- `Stabilite` (%0 - %100)
- `TehlikeSeviyesi` (1 - 10)

### Encapsulation

Stabilite değerinin **0 ile 100 arasında kalması garanti altına alınmıştır**.

---

## B. Interface & Polymorphism

### `IKritik` (Interface)

Sadece tehlikeli maddeler için kullanılır.

Metot:

AcilDurumSogutmasi()


---

## Madde Türleri

### VeriPaketi
- Güvenli veri türü
- Stabilite kaybı yaşamaz

### KaranlikMadde
- Tehlikeli madde
- Analiz edildiğinde **15 stabilite kaybeder**

### AntiMadde
- En tehlikeli madde
- Analiz edildiğinde **25 stabilite kaybeder**

---

# ⚠ Exception Handling

### `KuantumCokusuException`

Bir nesnenin stabilitesi **0'ın altına düştüğünde** fırlatılan özel bir hata sınıfıdır.

Sistem mesajı:

"SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR..."


Bu durumda simülasyon sonlandırılır.

---

# 🎮 Simulation Loop

Program sonsuz bir kontrol döngüsü ile çalışır.

Kullanıcı paneli:

1️⃣ Yeni Nesne Ekle  
→ Rastgele kuantum maddesi üretir.

2️⃣ Envanter Listele  
→ Sistemdeki tüm nesnelerin stabilite durumlarını gösterir.

3️⃣ Analiz Et  
→ Belirli ID'ye sahip nesneyi analiz eder ve stabilitesini düşürür.

4️⃣ Acil Durum Soğutması  
→ Kritik nesnelerin stabilitesini **+50 artırır**.

5️⃣ Çıkış  
→ Simülasyonu sonlandırır.

---

# 🛠 Technologies

| Language | Description |
|--------|-------------|
| C# | .NET mimarisi ile güçlü tip kontrolü |
| Java | Interface ve OOP yapıları |
| Python | Dinamik OOP modeli |
| JavaScript | Node.js ortamında sınıf tabanlı yapı |

---

# 📂 Repository Structure

quantum-chaos-management
│
├── CSharp
│
├── Java
│
├── Python
│
└── JavaScript


Her klasör aynı simülasyon mantığının farklı dilde implementasyonunu içerir.

---

# 💻 Implementation Details

### C#

- Generic List kullanımı
- `is` / `as` operatörleri
- Interface implementasyonu

### Java

- Güçlü tip sistemi
- Interface kullanımı
- Exception handling

### Python

- Dinamik tip sistemi
- OOP prensipleri

### JavaScript

- Node.js ortamı
- ES6 class yapısı

---

# 🎯 Learning Objectives

Bu proje aşağıdaki konuların anlaşılmasını amaçlamaktadır:

- Object Oriented Programming
- Inheritance
- Encapsulation
- Polymorphism
- Interface kullanımı
- Custom Exception yazımı
- Çoklu dil implementasyonu

---

# 🚀 How to Run

İlgili dil klasörüne giderek projeyi çalıştırabilirsiniz.

Örnek:

### Python

python main.py


### Java

javac Main.java
java Main


### C#

dotnet run


### JavaScript

node main.js


---
