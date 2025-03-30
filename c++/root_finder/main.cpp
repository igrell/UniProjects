#include <iostream>
#include "utils.h"

double fun(double x) {
    return x * x - 2;
}

int main() {
    double zero = findZero(fun, -10, 0, 10, 0.0001, 0.0001);
    std::cout << zero << std::endl;
    return 0;
}