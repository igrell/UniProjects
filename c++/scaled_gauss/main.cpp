#include <iostream>
#include "vectalg.h"

inline void swapRows(Matrix &A, Vector &b, const int& i, const int& j, const int& N) {
    if (i >= N or j >= N) return;
    for (int k = 0; k < N; ++k) std::swap(A(i, k), A(j, k));
    std::swap(b[i], b[j]);
}

Vector solveEquations(const Matrix &A, const Vector &b, double eps) {
    const int N = A.size();
    if (N > 3000) return b;
    Matrix A1 = A;
    Vector x = b;

    Vector rowNorms(N);
    Vector row(N);
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) row[j] = A1(i, j);
        rowNorms[i] = row.max_norm();
    }

    /* get main element */
    int mainId;
    for (int k = 0; k < N; ++k) {
        const int N = A1.size();
        double scaledVal = std::abs(A1(k, k)) / rowNorms[k];
        double maxVal = scaledVal;
        mainId = k;
        for (int i = k + 1; i < N; ++i) {
            scaledVal = std::abs(A1(i, k)) / rowNorms[i];
            if (scaledVal > maxVal) {
                maxVal = scaledVal;
                mainId = i;
            }
        }
        swapRows(A1, x,k,mainId,N);

        double scalingFactor;
        for (int i = k + 1; i < N; ++i) {
            scalingFactor = A1(i, k) / A1(k, k);
            for (int j = 0; j < N; ++j) A1(i, j) -= (scalingFactor * A1(k, j));
            x[i] -= (scalingFactor * x[k]);
        }
    }
    double temp;
    for (int i = N - 1; i >= 0; --i) {
        temp = 0;
        for (int j = N - 1; j > i; --j) temp += (A1(i, j) * x[j]);
        x[i] = ((x[i] - temp) / A1(i, i));
    }
    while (true) {
        Vector r = residual_vector(A, b, x);
        if (r.max_norm() < eps) return x;
        Vector error = solveEquations(A, r, eps);
        for (int i = 0; i < N; ++i) x[i] += error[i];
    }
}

int main() {
    Matrix m{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    Vector v{7, 5, 3};
    Vector res = solveEquations(m, v, 0.0001);
    std::cout << res << std::endl;
    return 0;
}