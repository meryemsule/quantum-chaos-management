const readline = require('readline');
const rl = readline.createInterface({ input: process.stdin, output: process.stdout });

class KuantumCokusuException extends Error {
    constructor(id) { super(`Kuantum Çöküşü! Patlayan nesne: ${id}`); }
}

class KuantumNesnesi {
    constructor(id, stabiliteInit, tehlike) {
        this._id = id;
        this._stabilite = null;
        this.stabilite = stabiliteInit;
        this.tehlike = tehlike;
    }
    get ID() { return this._id; }
    get stabilite() { return this._stabilite; }
    set stabilite(v) {
        if (v > 100) this._stabilite = 100;
        else if (v < 0) this._stabilite = 0;
        else this._stabilite = v;
    }
    AnalizEt() { throw new Error("Abstract"); }
    DurumBilgisi() { return `${this.ID} - Stabilite: ${this.stabilite.toFixed(2)}`; }
    _checkCollapse() {
        if (this.stabilite <= 0) throw new KuantumCokusuException(this.ID);
    }
}

class VeriPaketi extends KuantumNesnesi {
    constructor(id) { super(id, 100, 1); }
    AnalizEt() {
        this.stabilite -= 5;
        console.log("Veri içeriği okundu.");
        this._checkCollapse();
    }
}

class KaranlikMadde extends KuantumNesnesi {
    constructor(id) { super(id, 100, 8); }
    AnalizEt() {
        this.stabilite -= 15;
        console.log("Karanlık madde analiz edildi.");
        this._checkCollapse();
    }
    AcilDurumSogutmasi() {
        this.stabilite += 50;
        if (this.stabilite > 100) this.stabilite = 100;
        console.log("Acil soğutma uygulandı.");
    }
}

class AntiMadde extends KuantumNesnesi {
    constructor(id) { super(id, 100, 10); }
    AnalizEt() {
        this.stabilite -= 25;
        console.log("Evrenin dokusu titriyor...");
        this._checkCollapse();
    }
    AcilDurumSogutmasi() {
        this.stabilite += 50;
        if (this.stabilite > 100) this.stabilite = 100;
        console.log("Acil soğutma uygulandı.");
    }
}

const env = [];
const rnd = { next: (n)=> Math.floor(Math.random()*n) };
const newId = (prefix) => `${prefix}-${Math.random().toString(36).slice(2,10)}`;

function prompt(q) {
    return new Promise(res => rl.question(q, ans => res(ans)));
}

async function main() {
    try {
        while (true) {
            console.log("\nKUANTUM AMBARI KONTROL PANELİ");
            console.log("1. Yeni Nesne Ekle");
            console.log("2. Tüm Envanteri Listele");
            console.log("3. Nesneyi Analiz Et (ID ile)");
            console.log("4. Acil Durum Soğutması Yap (sadece kritik)");
            console.log("5. Çıkış");
            let sec = (await prompt("Seçiminiz: ")).trim();
            if (sec === "1") {
                let t = rnd.next(3);
                let n;
                if (t===0) n = new VeriPaketi(newId("VP"));
                else if (t===1) n = new KaranlikMadde(newId("KM"));
                else n = new AntiMadde(newId("AM"));
                env.push(n);
                console.log("Yeni nesne eklendi:", n.DurumBilgisi());
            } else if (sec === "2") {
                console.log("Envanter Durum Raporu:");
                env.forEach(e => console.log(e.DurumBilgisi()));
            } else if (sec === "3") {
                let id = (await prompt("Analiz edilecek ID: ")).trim();
                let found = env.find(x => x.ID.toLowerCase() === id.toLowerCase());
                if (!found) { console.log("ID bulunamadı."); continue; }
                found.AnalizEt();
                console.log(found.DurumBilgisi());
            } else if (sec === "4") {
                let id = (await prompt("Soğutulacak ID: ")).trim();
                let found = env.find(x => x.ID.toLowerCase() === id.toLowerCase());
                if (!found) { console.log("ID bulunamadı."); continue; }
                if (typeof found.AcilDurumSogutmasi === 'function') {
                    found.AcilDurumSogutmasi();
                    console.log(found.DurumBilgisi());
                } else {
                    console.log("Bu nesne soğutulamaz!");
                }
            } else if (sec === "5") {
                console.log("Çıkılıyor...");
                break;
            } else console.log("Geçersiz seçim.");
        }
    } catch (ex) {
        if (ex instanceof KuantumCokusuException) {
            console.log("\nSİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
            console.log(ex.message);
        } else {
            console.log("Beklenmeyen hata:", ex);
        }
    } finally {
        rl.close();
    }
}

main();
