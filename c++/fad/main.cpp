#include <iostream>
#include <iomanip>
#include <cassert>
#include "funkcja.h"
#include "Jet.h"

int main() {
    unsigned int M;
    std::cin >> M;
    assert(M < 1000000 and M > 0);
    double x, y;
    std::cout << std::fixed << std::setprecision(15);
    for (int i = 0; i < M; ++i) {
        std::cin >> x >> y;
        std::cout << funkcja(Jet(x, true), Jet(y, false));
        if (i != M - 1) std::cout << "\n";
    }
    return 0;
}