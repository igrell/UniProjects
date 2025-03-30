#include <iostream>
#include <iomanip>
#include <cmath>
#include <vector>
#include <memory>
#include "vectalg.h"


inline std::ostream &operator<<(std::ostream &out, const Vector &m) {
    for (auto x: m) { out << x << "\n"; }
    return out;
}

inline std::istream &operator>>(std::istream &in, Vector &m) {
    for (auto &x: m) { in >> x; }
    return in;
}

class SparseMatrix {
    int N = 0;
    int M = -1;
    std::vector<Vector> strips;

public:
    SparseMatrix(int N, int M, std::vector<Vector> &strips) : N(N), M(M), strips(strips) {}

    inline size_t size() const { return N; }

    inline double operator()(int row, int col) const {
        if (col == row) return strips[M][col];
        int distFromDiag = std::abs(col - row);
        if (distFromDiag > M) return 0;
        return col < row ? strips[M - distFromDiag][col] : strips[M - distFromDiag][col - distFromDiag];
    }

    inline friend std::ostream &operator<<(std::ostream &ostream, SparseMatrix &matrix) {
        ostream << "\n";
        for (int i = 0; i < matrix.N; ++i) {
            for (int j = 0; j < matrix.N; ++j) ostream << matrix(i, j) << " ";
            ostream << "\n";
        }
        return ostream;
    }
};

inline Vector SOR(const SparseMatrix &A, const Vector &b, const Vector &_x0, int L, double w, int M) {
    Vector x0 = _x0;
    int N = A.size();
    for (int iter = 0; iter < L; iter++) {
        for (int row = 0; row < N; row++) {
            auto s = b[row];
            for (int col = std::max(0, row - M); col < row; col++) {
                s -= A(row, col) * x0[col];
            }
            for (int col = row + 1; col < std::min(N, row + M + 1); col++) {
                s -= A(row, col) * x0[col];
            }
            x0[row] = (1 - w) * x0[row] + w * s / A(row, row);
        }
    }
    return x0;
}

int main() {
    int N, M;
    std::cin >> N >> M;
    std::vector<Vector> strips;
    for (int i = 0; i < M + 1; ++i) {
        Vector strip(N - M + i);
        std::cin >> strip;
        strips.emplace_back(strip);
    }
    SparseMatrix matrix(N, M, strips);
    Vector y(N), x0(N);
    std::cin >> y;
    std::cin >> x0;
    double w;
    int L;
    std::cin >> w >> L;
    std::cout << std::fixed << std::scientific << std::setprecision(10) << SOR(matrix, y, x0, L, w, M);
    return 0;

}
