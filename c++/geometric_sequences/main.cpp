#include <cmath>
#include <iostream>
#include <iomanip>

void findValues(const float &prod, const float &sum) {
    float x1, x2, x3;
    x1 = x2 = x3 = 0.0;
    float MAX_FLOAT = 3.40282347e+38F; // copied from MAXFLOAT macro
    // assume correct input
    if (prod != 0.0 and prod < MAX_FLOAT and sum < MAX_FLOAT and prod > (-MAX_FLOAT) and sum > (-MAX_FLOAT)) {
        float prod_cbrt = cbrt(prod);
        float expr1 = sum - (3 * prod_cbrt);
        float expr2 = sum + prod_cbrt;
        if ((expr1 >= 0 and expr2 >= 0) or (expr1 <= 0 and expr2 <= 0)) { // same sign for both expressions
            float delta_sq = (sqrt(fabs(expr1)) * sqrt(fabs(expr2)));
            if (delta_sq >= 0) {
                float expr3 = sum - prod_cbrt;
                float prod_cbrt_sq = prod_cbrt * prod_cbrt;
                if (expr3 < 0) {
                    x3 = ((expr3 - delta_sq) / 2);
                    x1 = prod_cbrt_sq / x3;
                } else {
                    x1 = ((expr3 + delta_sq) / 2);
                    x3 = prod_cbrt_sq / x1;
                }
                x2 = prod_cbrt;
            }
        }
    }
    std::cout << std::setprecision(10) << std::scientific << x1 << " " << x2 << " " << x3 << "\n";
}

int main() {
    int N = -1;
    float prod, sum;
    prod = sum = 0.0;
    std::cin >> N;
    for (int i = 0; i < N; ++i) {
        std::cin >> prod >> sum;
        findValues(prod, sum);
    }
    return 0;
}
