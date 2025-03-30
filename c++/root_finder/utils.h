#include <cmath>
#include <iostream>

using namespace std;

struct Interval {
    double lo, hi;

    Interval(double lo, double hi) : lo(lo), hi(hi) {}

    double width() const { return std::abs(hi - lo); }

    double middle() const { return ((lo + hi) / 2); }

    friend ostream &operator<<(ostream &ostream, Interval interval) {
        ostream << "[" << interval.lo << " , " << interval.hi << "]";
        return ostream;
    }
};

// checks whether arg. x can be considered a zero within eps-delta precision
bool checkEpsDel(double x_val, double eps, double delta, Interval &res) {
    if (std::abs(x_val) <= eps) {
        if (x_val == 0.0) return true;
        return (res.width() <= delta);
    }
    return false;
}

// single bisection iteration returning interval
Interval
bisection(double (*f)(double), Interval &res, Interval &res_vals, int &fun_call_no, const double M, const double eps,
          const double delta, double &prev_mid, double &prev_mid_val) {
    double mid = (res.lo + res.hi) / 2;
    if (fun_call_no == M) {
        res.lo = res.hi = mid;
        return res;
    }
    fun_call_no++;
    double mid_val = f(mid);
    if (checkEpsDel(mid_val, eps, delta, res)) {
        res.lo = res.hi = mid;
        return res;
    }
    bool if_prev_is_lo = (prev_mid == res.lo);
    bool same_signs = (prev_mid_val * mid_val < 0);
    prev_mid = mid;
    prev_mid_val = mid_val;
    if (same_signs) {
        if (if_prev_is_lo) res.hi = mid;
        else res.lo = mid;
    } else if (if_prev_is_lo) res.lo = mid;
    else res.hi = mid;
    return res;
}

// single secant iteration returning res
/* In each iteration on input:
 * res = [x_(n-1),x_(n)]
 * We calculate x_(n+1) and output res = [x_(n),x_(n+1)]
 * */
Interval
secant(double (*f)(double), Interval &res, Interval &res_vals, int &fun_call_no, const double M, const double eps,
       const double delta,
       bool &same_signs) {
    same_signs = (res_vals.lo * res_vals.hi > 0);

    if (checkEpsDel(res_vals.lo, eps, delta, res)) {
        res.lo = res.hi = res.lo;
        return res;
    }

    if (checkEpsDel(res_vals.hi, eps, delta, res)) {
        res.lo = res.hi = res.hi;
        return res;
    }

    /* calculate x_(n+1) by intersection with x-axis formula */
    double x_n_plus_1 = res.hi - ((res_vals.hi * (res.hi - res.lo)) / (res_vals.hi - res_vals.lo));
    if (fun_call_no == M) {
        res.lo = res.hi = x_n_plus_1;
        return res;
    }
    fun_call_no++;
    double x_n_plus_1_val = f(x_n_plus_1);
    if (checkEpsDel(x_n_plus_1_val, eps, delta, res)) // check if x_n_plus_1 is root
    {
        res.lo = x_n_plus_1;
        res.hi = x_n_plus_1;
    } else {
        res.lo = res.hi;
        res.hi = x_n_plus_1;
    }
    res_vals.lo = res_vals.hi; // val of x_(n+1) passed to next iteration to save function call
    res_vals.hi = x_n_plus_1_val;
    return res;
}

double findZero(double (*f)(double), double a, double b, int M, double eps, double delta) {
    if (M < 2) return 3.40282347e+38F;
    int fun_call_no = 2;
    Interval res(a, b);
    Interval res_vals(f(a), f(b));
    if (checkEpsDel(res_vals.lo, eps, delta, res)) return a;
    if (checkEpsDel(res_vals.hi, eps, delta, res)) return b;
    bool same_signs = (res_vals.lo * res_vals.hi > 0);
    if (same_signs) { // secant while necessary
        while (same_signs and fun_call_no < M) {
            secant(f, res, res_vals, fun_call_no, M, eps, delta, same_signs);
            if (res.lo == res.hi) return res.lo; // root found or no more fun calls
        }
    }
    if (fun_call_no == M) return res.lo;
    double prev_mid = res.lo;
    double prev_mid_val = res_vals.lo;
    while (fun_call_no < M and res.width() >= 0.1) { // bisection while wide interval
        bisection(f, res, res_vals, fun_call_no, M, eps, delta, prev_mid, prev_mid_val);
        if (res.lo == res.hi) return res.lo;
    }
    if (fun_call_no == M) return res.middle();
    if (prev_mid == res.lo) res_vals.lo = prev_mid_val;
    else {
        res_vals.hi = prev_mid_val;
        fun_call_no++;
        res_vals.lo = f(res.lo);
    }
    while (fun_call_no < M) { // secant to quickly converge
        secant(f, res, res_vals, fun_call_no, M, eps, delta, same_signs);
        if (res.lo == res.hi) return res.lo;
    }
    return res.lo; // return best approx. of secant
}