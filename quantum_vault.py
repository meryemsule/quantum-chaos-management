import uuid
import random
from abc import ABC, abstractmethod

class KuantumCokusuException(Exception):
    pass

class IKritik:
    def AcilDurumSogutmasi(self): ...

class KuantumNesnesi(ABC):
    def __init__(self, id_: str, stabilite_init: float, tehlike: int):
        self._id = id_
        self._stabilite = None
        self.stabilite = stabilite_init
        self.tehlike = tehlike

    @property
    def ID(self):
        return self._id

    @property
    def stabilite(self):
        return self._stabilite

    @stabilite.setter
    def stabilite(self, value):
        if value > 100:
            self._stabilite = 100
        elif value < 0:
            self._stabilite = 0
        else:
            self._stabilite = value

    @abstractmethod
    def AnalizEt(self):
        pass

    def DurumBilgisi(self):
        return f"{self.ID} - Stabilite: {self.stabilite:.2f}"

    def _check_collapse(self):
        if self.stabilite <= 0:
            raise KuantumCokusuException(f"Kuantum Çöküşü! Patlayan nesne: {self.ID}")

class VeriPaketi(KuantumNesnesi):
    def __init__(self, id_):
        super().__init__(id_, 100, 1)
    def AnalizEt(self):
        self.stabilite -= 5
        print("Veri içeriği okundu.")
        self._check_collapse()

class KaranlikMadde(KuantumNesnesi, IKritik):
    def __init__(self, id_):
        super().__init__(id_, 100, 8)
    def AnalizEt(self):
        self.stabilite -= 15
        print("Karanlık madde analiz edildi.")
        self._check_collapse()
    def AcilDurumSogutmasi(self):
        self.stabilite += 50
        if self.stabilite > 100: self.stabilite = 100
        print("Acil soğutma uygulandı.")

class AntiMadde(KuantumNesnesi, IKritik):
    def __init__(self, id_):
        super().__init__(id_, 100, 10)
    def AnalizEt(self):
        self.stabilite -= 25
        print("Evrenin dokusu titriyor...")
        self._check_collapse()
    def AcilDurumSogutmasi(self):
        self.stabilite += 50
        if self.stabilite > 100: self.stabilite = 100
        print("Acil soğutma uygulandı.")

def new_id(prefix):
    return f"{prefix}-{str(uuid.uuid4())[:8]}"

env = []

def find_by_id(id_):
    for e in env:
        if e.ID.lower() == id_.lower():
            return e
    return None

def main():
    try:
        while True:
            print("\nKUANTUM AMBARI KONTROL PANELİ")
            print("1. Yeni Nesne Ekle")
            print("2. Tüm Envanteri Listele")
            print("3. Nesneyi Analiz Et (ID ile)")
            print("4. Acil Durum Soğutması Yap (sadece kritik)")
            print("5. Çıkış")
            sec = input("Seçiminiz: ").strip()
            if sec == "1":
                t = random.randint(0,2)
                if t==0:
                    n = VeriPaketi(new_id("VP"))
                elif t==1:
                    n = KaranlikMadde(new_id("KM"))
                else:
                    n = AntiMadde(new_id("AM"))
                env.append(n)
                print("Yeni nesne eklendi:", n.DurumBilgisi())
            elif sec == "2":
                print("Envanter Durum Raporu:")
                for e in env:
                    print(e.DurumBilgisi())
            elif sec == "3":
                id_ = input("Analiz edilecek ID: ").strip()
                obj = find_by_id(id_)
                if not obj:
                    print("ID bulunamadı.")
                    continue
                obj.AnalizEt()
                print(obj.DurumBilgisi())
            elif sec == "4":
                id_ = input("Soğutulacak ID: ").strip()
                obj = find_by_id(id_)
                if not obj:
                    print("ID bulunamadı.")
                    continue
                if isinstance(obj, IKritik) or hasattr(obj, "AcilDurumSogutmasi"):
                    obj.AcilDurumSogutmasi()
                    print(obj.DurumBilgisi())
                else:
                    print("Bu nesne soğutulamaz!")
            elif sec == "5":
                print("Çıkılıyor...")
                break
            else:
                print("Geçersiz seçim.")
    except KuantumCokusuException as ex:
        print("\nSİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...")
        print(ex)
if __name__ == "__main__":
    main()
