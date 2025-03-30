#ifndef MAIN2
#define MAIN2
#include "utils.h"

void implicitSurface(const double *x, double *y, double *Df) {
    // funkcja dana jest wzorem f(a,b,c) = (a+b+c)/(a^2+b^2+c^2)-1
    // zmienne pomocnicze
    const double n = x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
    const double s = x[0] + x[1] + x[2];

    // obliczam wartosc funkcji
    *y = s / n - 1.;

    //obliczam pierwszy i jedyny wiersz macierzy
    const double r = 1. / n;
    const double r2 = 2. * (*y) * r;
    Df[0] = r - x[0] * r2;
    Df[1] = r - x[1] * r2;
    Df[2] = r - x[2] * r2;
}

int main() {
    double x[3] = {0.25 * (1. + sqrt(5.)), 0.25 * (1. - sqrt(5.)), 0.5};
    findSurface(implicitSurface, x, 4, 4, 1. / 32, 1. / 32);
    return 0;
}

#endif MAIN2