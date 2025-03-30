#ifndef __UCZESTNIK_H__
#define __UCZESTNIK_H__

struct Uczestnik {
    enum Plec {
        K, M, W
    };

    Uczestnik(Plec p) : plec(p) {}

    Plec plec;
};

#endif
