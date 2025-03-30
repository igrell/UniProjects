#include "Uczestnik.h"
#include "iostream"

struct Wezel {
    Wezel(Uczestnik *osoba, const unsigned int id, Wezel *lewo, Wezel *prawo) :
            osoba(osoba), id(id), lewo(lewo), prawo(prawo) {}

    Uczestnik *osoba;
    const unsigned int id;
    Wezel *lewo, *prawo;
};


struct Wodzirej {
private:
    Uczestnik *osoba = nullptr;
    Wezel glowa = Wezel(osoba, 0, &glowa, &glowa);
    bool zabawa = false;
    Uczestnik *chusteczka = glowa.osoba;


public:
    Wodzirej() {
        osoba = new Uczestnik(Uczestnik::W);
        glowa.osoba = osoba;
    };

    ~Wodzirej() {
        Wezel *wezel = glowa.prawo;
        while (wezel != &glowa) {
            wezel = wezel->prawo;
            delete wezel->lewo;
        }
        glowa.lewo = glowa.prawo = &glowa;
        delete osoba;

    }

    unsigned int dolacz(Uczestnik *osoba);

    unsigned int dolacz(Uczestnik *osoba, unsigned int pozycja);

    bool rozpocznij();

    bool zakoncz();

    bool zrezygnuj(Uczestnik *osoba);

    bool zrezygnuj(unsigned int x);

    bool przekaz(Uczestnik *osoba);

    bool przekaz(unsigned int x);

    void uczestnicy() const;

    void uczestnicy(Uczestnik::Plec p) const;

    unsigned int liczba() const;

    const Uczestnik *wodzirej() const;

};


unsigned int Wodzirej::dolacz(Uczestnik *osoba) {
    if (osoba == 0 or !(osoba->plec == 0 or osoba->plec == 1)) return 0;
    Wezel *wezel = glowa.prawo;
    unsigned int id = 0;
    while (wezel != &glowa) {
        if (osoba == wezel->osoba) return 0;
        wezel = wezel->prawo;
        id++;
    }
    id++;
    auto *w = new Wezel(osoba, id, &glowa, glowa.prawo);
    glowa.prawo->lewo = w;
    glowa.prawo = w;
    return id;
}

unsigned int Wodzirej::dolacz(Uczestnik *osoba, unsigned int pozycja) {
    if (osoba == 0 or !(osoba->plec == 0 or osoba->plec == 1)) return 0;
    unsigned int id = 0;
    Wezel *wezel = glowa.lewo;
    while (wezel != &glowa) {
        if (wezel->osoba == osoba) return 0;
        wezel = wezel->lewo;
        id++;
    }
    id++;
    if (pozycja >= id) return 0;
    unsigned int licznik = 0;
    wezel = glowa.lewo;
    while (licznik != pozycja) {
        licznik++;
        wezel = wezel->lewo;
    }
    auto *w = new Wezel(osoba, id, wezel, wezel->prawo);
    wezel->prawo->lewo = w;
    wezel->prawo = w;
    return id;
}

bool Wodzirej::rozpocznij() {
    if (zabawa or (glowa.prawo == &glowa and glowa.lewo == &glowa)) return false;
    else {
        Wezel *wezel = glowa.prawo;
        int koedukacja = wezel->osoba->plec;
        while (wezel != &glowa) {
            if (wezel->osoba->plec != koedukacja) {
                chusteczka = glowa.prawo->osoba;
                zabawa = true;
                return true;
            }
            wezel = wezel->prawo;
        }
        return false;
    }
}

bool Wodzirej::zakoncz() {
    if (!zabawa) return false;
    else {
        chusteczka = glowa.osoba;
        zabawa = false;
        return true;
    }
}

bool Wodzirej::zrezygnuj(Uczestnik *osoba) {
    if (osoba == 0 or chusteczka == osoba or osoba == glowa.osoba) return false;
    Wezel *wezel = glowa.prawo;
    bool obecna = false;
    while (wezel != &glowa) {
        if (osoba == wezel->osoba) {
            obecna = true;
            break;
        }
        wezel = wezel->prawo;
    }
    if (!obecna) return false;
    wezel->prawo->lewo = wezel->lewo;
    wezel->lewo->prawo = wezel->prawo;
    wezel->lewo = nullptr;
    wezel->prawo = nullptr;
    delete wezel;
    return true;
}

bool Wodzirej::zrezygnuj(unsigned int x) {
    if (glowa.id == x) return false;
    Wezel *wezel = glowa.prawo;
    bool obecna = false;
    while (wezel != &glowa) {
        if (wezel->id == x) {
            obecna = true;
            if (wezel->osoba == chusteczka) return false;
            break;
        }
        wezel = wezel->prawo;
    }
    if (!obecna) return false;
    wezel->prawo->lewo = wezel->lewo;
    wezel->lewo->prawo = wezel->prawo;
    wezel->lewo = nullptr;
    wezel->prawo = nullptr;
    delete wezel;
    return true;
}

bool Wodzirej::przekaz(Uczestnik *osoba) {
    if (osoba == 0 or !zabawa or osoba == glowa.osoba or osoba->plec == chusteczka->plec) return false;
    Wezel *wezel = glowa.prawo;
    bool obecna = false;
    while (wezel != &glowa) {
        if (osoba == wezel->osoba) {
            obecna = true;
            chusteczka = osoba;
            break;
        }
        wezel = wezel->prawo;
    }
    return obecna;
}

bool Wodzirej::przekaz(unsigned int x) {
    if (glowa.id == x or !zabawa) return false;
    Wezel *wezel = glowa.prawo;
    bool obecna = false;
    while (wezel != &glowa) {
        if (wezel->id == x) {
            obecna = true;
            if (wezel->osoba->plec == chusteczka->plec) return false;
            chusteczka = wezel->osoba;
            break;
        }
        wezel = wezel->prawo;
    }
    return obecna;
}

void Wodzirej::uczestnicy() const {
    Wezel *wezel = glowa.lewo;
    std::cout << "plec: " << glowa.osoba->plec << "," << " nr: " << glowa.id << std::endl;
    while (wezel != &glowa) {
        std::cout << "plec: " << wezel->osoba->plec << "," << " nr: " << wezel->id << std::endl;
        wezel = wezel->lewo;
    }
}

void Wodzirej::uczestnicy(Uczestnik::Plec p) const {
    if (p > 2) return;
    if (glowa.osoba->plec == p) std::cout << "nr: " << glowa.id << std::endl;
    Wezel *wezel = glowa.prawo;
    while (wezel != &glowa) {
        if (wezel->osoba->plec == p) std::cout << "nr: " << wezel->id << std::endl;
        wezel = wezel->prawo;
    }
}

unsigned int Wodzirej::liczba() const {
    Wezel *wezel = glowa.prawo;
    unsigned int licznik = 1;
    while (wezel != &glowa) {
        wezel = wezel->prawo;
        licznik++;
    }
    return licznik++;
}

const Uczestnik *Wodzirej::wodzirej() const {
    return glowa.osoba;
}
