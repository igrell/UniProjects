#ifndef MAIN
#define MAIN
#include "utils.h"

void implicitCurve(const double *x, double *y, double *Df) {
    // funkcja dana jest wzorem f(a,b,c) = (1-a^2-b^2-c^2,(a+b+c)/(a^2+b^2+c^2)-1)
    // zmienne pomocnicze
    const double n = x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
    const double r = 1. / n;
    const double s = (x[0] + x[1] + x[2]) * r;

    // obliczam wartosc funkcji
    y[0] = 1. - n;
    y[1] = s - 1.;

    //obliczam pierwszy wiersz macierzy
    Df[0] = -2. * x[0];
    Df[1] = -2. * x[1];
    Df[2] = -2. * x[2];

    //obliczam drugi wiersz macierzy
    const double r2 = 2. * s * r;
    Df[3] = r - x[0] * r2;
    Df[4] = r - x[1] * r2;
    Df[5] = r - x[2] * r2;
}

int main() {
    double x[3] = {0.25 * (1. + sqrt(5.)), 0.25 * (1. - sqrt(5.)), 0.5};
    findCurve(implicitCurve, x, 10, 1. / 128);
    printf("\n");
    double x2[3] = {0.25 * (1. - sqrt(5.)), 0.25 * (1. + sqrt(5.)), 0.5};
    int i = findCurve(implicitCurve, x2, 10, 3. / 32);
    printf("%d", i);
    return 0;
}




#endif