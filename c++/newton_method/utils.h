// Igor Piechowiak
#ifndef SOURCE
#define SOURCE

#include "cstdio"
#include "cmath"
#include "iostream"

using std::abs;
using std::max;

typedef void (*FuncPointer)(const double *x, double *y, double *Df);

typedef bool (*BorderCond)(const double *y, const double *x);

typedef void (*MatrixInverseAlg)(const double *mat, double *inv);

typedef void (*MatrixRestrictAlg)(double *mat);

void printVector(const double *x, unsigned N) {
    for (unsigned i = 0; i < N; ++i) printf("%17.17f ", x[i]);
    printf("\n");
}

inline void get2by2MatrixInverse(const double *mat, double *inv) {
    double det = (mat[0] * mat[3]) - (mat[1] * mat[2]);
    if (det == 0) return;
    inv[0] = mat[3] / det;
    inv[1] = (-mat[1]) / det;
    inv[2] = (-mat[2]) / det;
    inv[3] = mat[0] / det;
}

inline void get1by1MatrixInverse(const double *mat, double *inv) { inv[0] = 1 / (mat[0]); }

inline void restrictMatrixTo2by2(double *mat) {
    mat[2] = mat[3];
    mat[3] = mat[4];
}

inline void restrictMatrixTo2by2fixedPoints(double *mat) {
    mat[2] = mat[4];
    mat[3] = mat[5];
}

inline void restrictMatrixTo1by1(double *mat) {}

inline bool curveBorderCond(const double *y, const double *x) { return max(abs(y[0]), abs(y[1])) <= 1e-14; }

inline bool surfaceBorderCond(const double *y, const double *x) { return abs(y[0]) <= 1e-14; }

inline bool fixedPointsBorderCond(const double *y, const double *x) {
    return max(abs(y[0]), abs(y[1])) <= 1e-14;
}

inline bool newtonMethod(FuncPointer f, double *x, double *y, double *Df, double *DfInv,
                         const int xSize, const int ySize, BorderCond borderCond, MatrixInverseAlg matrixInverseAlg,
                         MatrixRestrictAlg matrixRestrictAlg, bool fixedPointsFlag = false) {
    f(x, y, Df); // calculate y and Df in point x
    if (fixedPointsFlag) {
        y[0] -= x[0];
        y[1] -= x[1];
    }
    if (borderCond(y, x)) return true;
    matrixRestrictAlg(Df);
    if (fixedPointsFlag) {
        Df[0] -= 1;
        Df[3] -= 1;
    }
    matrixInverseAlg(Df, DfInv);
    double temp;
    for (int i = 0; i < xSize; ++i) {
        temp = 0.0;
        for (int j = 0; j < ySize; ++j) temp += DfInv[i * xSize + j] * y[j];
        x[i] -= temp;
    }
    return false;
}

bool ifDivergent(double *x, double &shortestWidth, unsigned &it, const int xSize) {
    double currWidth = 0.0;
    for (int i = 0; i < xSize; ++i) currWidth += fabs(x[i]);
    if (currWidth <= shortestWidth) shortestWidth = currWidth;
    else if (++it >= 10) return true;
    return false;
}

int findCurve(FuncPointer f, double *x, unsigned k, double h) {
    double y[2];
    double Df[6];
    double DfInv[4];
    unsigned it;
    double shortestWidth;
    for (int i = 1; i <= k; ++i) {
        x[2] += h;
        it = 0;
        shortestWidth = 3.40282347e+38F;
        while (!newtonMethod(f, x, y, Df, DfInv, 2, 2, curveBorderCond, get2by2MatrixInverse, restrictMatrixTo2by2)) {
            if (ifDivergent(x, shortestWidth, it, 2)) return i;
        }
        printVector(x, 3);
    }
    return 0;
}

int findSurface(FuncPointer f, double *x, unsigned k1, unsigned k2, double h1, double h2) {
    double y[1];
    double Df[3];
    double DfInv[1];
    double temp = x[2];
    for (int i = 1; i <= k1; ++i) {
        x[1] += h1;
        for (int j = 1; j <= k2; ++j) {
            x[2] += h2;
            while (!newtonMethod(f, x, y, Df, DfInv, 1, 1,
                                 surfaceBorderCond, get1by1MatrixInverse, restrictMatrixTo1by1)) {}
            printVector(x, 3);
        }
        if (i != k1) printf("\n");
        x[2] = temp;
    }
    return 0;
}

int findFixedPoints(FuncPointer f, double *x, unsigned k1, unsigned k2, double h1, double h2) {
    double y[2];
    double Df[8];
    double DfInv[4];
    double temp = x[3];
    unsigned it;
    double shortestWidth;
    for (int i = 1; i <= k1; ++i) {
        x[2] += h1;
        for (int j = 1; j <= k2; ++j) {
            x[3] += h2;
            it = 0;
            shortestWidth = 3.40282347e+38F;
            while (!newtonMethod(f, x, y, Df, DfInv, 2, 2, fixedPointsBorderCond, get2by2MatrixInverse,
                                 restrictMatrixTo2by2fixedPoints, true))
                if (ifDivergent(x, shortestWidth, it, 2)) return (i * k1) + j;
            printVector(x, 4);
        }
        x[3] = temp;
        if (i != k1) printf("\n");
    }
    return 0;
}

#endif