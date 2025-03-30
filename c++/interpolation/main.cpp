#include "iomanip"
#include "iostream"

inline long double horner(const int M, const long double *X, const long double *W, const long double t) {
    long double res = W[M - 1];
    for (int i = M - 2; i >= 0; --i) res = res * (t - X[i]) + W[i];
    return res;
}

inline long factorial(int k) {
    long res = k;
    for (int i = k - 1; i >= 2; --i) res *= i;
    return res;
}

void interpolate(const int M, const int N, const long double *X,
                 const long double *Y, const long double *T, long double *W,
                 long double *F) {
    long double diffQuots[M]; // helper array for difference quotients
    int j;
    diffQuots[0] = Y[0];
    for (int i = 1; i <= M; ++i) {
        if (X[i - 1] != X[i]) diffQuots[i] = Y[i];
        else {
            j = i;
            while (X[j - 1] == X[j]) { diffQuots[(j++)] = Y[i - 1]; }
            i = j - 1;
        }
    }
    W[0] = Y[0];
    int noOfRepetitions;
    for (int i = 1; i < M; ++i) {
        noOfRepetitions = 0;
        for (int k = 1; k < M; ++k) {
            if (X[k + i - 1] == X[k - 1]) {
                diffQuots[k - 1] = Y[k + i - 1 - noOfRepetitions++] / factorial(i);
            } else {
                diffQuots[k - 1] = (diffQuots[k] - diffQuots[k - 1]) / (X[k + i - 1] - X[k - 1]);
                noOfRepetitions = 0;
            }
        }
        W[i] = diffQuots[0];
    }
    for (int k = 0; k < N; ++k) F[k] = horner(M, X, W, T[k]);
}

/// @M -> no. of points
/// @N -> no. of points to calculate
/// @T -> points to calculate interpolation value in
/// @F -> interpolation result
int main() {
    std::cout << std::fixed << std::setprecision(17);
    int M, N;
    std::cin >> M >> N;
    long double X[M], Y[M], W[M], T[N], F[N];
    for (int i = 0; i < M; ++i) std::cin >> X[i];
    for (int i = 0; i < M; ++i) std::cin >> Y[i];
    for (int i = 0; i < N; ++i) std::cin >> T[i];
    interpolate(M, N, X, Y, T, W, F);
    for (int i = 0; i < M - 1; ++i) std::cout << W[i] << " ";
    std::cout << W[M - 1] << "\n";
    for (int i = 0; i < N - 1; ++i) std::cout << F[i] << " ";
    std::cout << F[N - 1] << "\n";
    return 0;
}
