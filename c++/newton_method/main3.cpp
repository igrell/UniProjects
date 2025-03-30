#ifndef MAIN3
#define MAIN3
#include "utils.h"
void henon(const double *x, double *y, double *Df) {
    // funkcja dana jest wzorem henon(x,y,a,b) = (1+y-a*x^2,b*x)
    const double x2 = x[0] * x[0];

    y[0] = 1 + x[1] - x[2] * x2;
    y[1] = x[3] * x[0];

    //obliczam pierwszy wiersz macierzy
    Df[0] = -2 * x[2] * x[0];
    Df[1] = 1.;
    Df[2] = -x2;
    Df[3] = 0.;

    //obliczam drugi wiersz macierzy
    Df[4] = x[3];
    Df[5] = 0.;
    Df[6] = 0.;
    Df[7] = x[0];
}

int main() {
    double x[4] = {-1.2807764064044151, -0.6403882032022076, 1.0000000000000000, 0.50000000000000000};
    findFixedPoints(henon, x, 4, 4, 1. / 16, 1. / 16);
    return 0;
}

#endif MAIN3