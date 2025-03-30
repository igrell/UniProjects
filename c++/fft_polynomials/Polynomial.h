#ifndef PROJEKT_POLYNOMIAL_H
#define PROJEKT_POLYNOMIAL_H

#include "ostream"
#include "iostream"
#include "complex"

using std::ostream, std::max, std::min, std::complex, std::cout;

/* recursive fast Fourier transform.
 f - function (polynomial) which is transformed
 f_size - size of the polynomial (corresponding with the number of roots of unity in the complex plane)
 is_invert - informs whether fft or inverse fft is performed
 */

void fft(complex<double> *f, int f_size, bool is_invert) { // recursive fast Fourier transform
    if (f_size < 2) return; // recursion stop condition

    auto *f_even = new complex<double>[f_size >> 1]; // divide f into even and odd terms
    auto *f_odd = new complex<double>[f_size >> 1];

    for (int i = 0, j = 0; i < f_size; i += 2, ++j) { // write the corresponding coeffs
        f_even[j] = f[i];
        f_odd[j] = f[i + 1];
    }

    fft(f_even, f_size >> 1, is_invert); // recursive transform for even terms
    fft(f_odd, f_size >> 1, is_invert); // -||- odd terms

    // angle from 0 to the first root of unity; negative is fft is inverted
    double ang = (2 * M_PI / f_size) * (is_invert ? -1 : 1);

    complex<double> w(1); // vector (1,0) in the complex plane
    complex<double> wn(cos(ang), sin(ang)); // the first root of unity

    for (int i = 0; i < (f_size >> 1); i++) {
        f[i] = f_even[i] + w * f_odd[i];
        f[i + (f_size >> 1)] = f_even[i] - w * f_odd[i];
        if (is_invert) f[i] /= 2, f[i + (f_size >> 1)] /= 2;
        w *= wn;
    }

    delete[]f_even;
    delete[]f_odd;

}

template<typename T>
struct Polynomial {
    int noOfCoeffs = 0;
    T *coeffs = nullptr;

    Polynomial() = default;

    explicit Polynomial(const int noOfCoeffs) : noOfCoeffs(noOfCoeffs) {
        if (noOfCoeffs < 0) return;
        coeffs = new T[noOfCoeffs];
        for (int i = 0; i < noOfCoeffs; ++i) coeffs[i] = 0;
    }

    Polynomial(const T *coeffs, const int noOfCoeffs) : noOfCoeffs(noOfCoeffs) {
        if (noOfCoeffs < 0) return;
        this->coeffs = new T[noOfCoeffs];
        for (int i = 0; i < noOfCoeffs; ++i) this->coeffs[i] = coeffs[i];
    }

    Polynomial(const Polynomial &polynomial) : noOfCoeffs(polynomial.noOfCoeffs) {
        if (polynomial.noOfCoeffs < 0) return;
        this->coeffs = new T[noOfCoeffs];
        for (int i = 0; i < noOfCoeffs; ++i) coeffs[i] = polynomial.coeffs[i];
    }

    ~Polynomial() {
        delete[]coeffs;
    }

    T evaluate(const T x) { // horner evaluation
        if (this->noOfCoeffs <= 0) {
            cout << "No polynomial to evaluate\n";
            return INT32_MIN; // cannot evaluate
        }
        T res = this->coeffs[noOfCoeffs - 1];
        for (int i = noOfCoeffs - 2; i >= 0; --i) res = res * x + this->coeffs[i];
        return res;
    }

    Polynomial &operator=(const Polynomial &polynomial) {
        if (this == &polynomial) return *this; // self-assignment
        this->noOfCoeffs = polynomial.noOfCoeffs;
        if (this->noOfCoeffs < 0) return *this;
        delete[]coeffs;
        this->coeffs = new T[polynomial.noOfCoeffs];
        for (int i = 0; i < noOfCoeffs; ++i) this->coeffs[i] = polynomial.coeffs[i];
        return *this;
    }

    friend ostream &operator<<(ostream &ostream, const Polynomial &polynomial) {
        if (polynomial.noOfCoeffs <= 0) return ostream;
        if (polynomial.noOfCoeffs == 1) {
            ostream << polynomial.coeffs[0];
            return ostream;
        }
        int lastIndex = polynomial.noOfCoeffs - 1;
        if (polynomial.coeffs[0] != 0) ostream << polynomial.coeffs[0] << "+";
        for (int i = 1; i < lastIndex; ++i) {
            if (polynomial.coeffs[i] != 0) {
                ostream << "(" << polynomial.coeffs[i] << ")x^" << i;
                if (polynomial.coeffs[i + 1] != 0) ostream << "+";
            }

        }
        if (polynomial.coeffs[lastIndex] != 0) ostream << "(" << polynomial.coeffs[lastIndex] << ")x^" << lastIndex;
        return ostream;
    }

    friend Polynomial operator+(const Polynomial &a, const Polynomial &b) {
        if (a.noOfCoeffs < 0) return b;
        if (b.noOfCoeffs < 0) return a;
        int newNoOfCoeffs = max(a.noOfCoeffs, b.noOfCoeffs);
        int minNoOfCoeffs = min(a.noOfCoeffs, b.noOfCoeffs);
        bool whichLonger = a.noOfCoeffs >= b.noOfCoeffs;
        Polynomial res(newNoOfCoeffs);
        for (int i = 0; i < minNoOfCoeffs; i++) res.coeffs[i] = a.coeffs[i] + b.coeffs[i];
        if (whichLonger) for (int i = minNoOfCoeffs; i < newNoOfCoeffs; ++i) res.coeffs[i] = a.coeffs[i];
        else for (int i = minNoOfCoeffs; i < newNoOfCoeffs; ++i) res.coeffs[i] = b.coeffs[i];
        return res;
    }

    friend Polynomial operator-(const Polynomial &a, const Polynomial &b) {
        if (b.noOfCoeffs < 0) return a;

        if (a.noOfCoeffs < 0) { // {} - b
            Polynomial res(b.noOfCoeffs);
            for (int i = 0; i < b.noOfCoeffs; ++i) res.coeffs[i] = -b.coeffs[i];
            return res;
        }

        int newNoOfCoeffs = max(a.noOfCoeffs, b.noOfCoeffs);
        int minNoOfCoeffs = min(a.noOfCoeffs, b.noOfCoeffs);
        bool whichLonger = a.noOfCoeffs >= b.noOfCoeffs;
        Polynomial res(newNoOfCoeffs);
        for (int i = 0; i < minNoOfCoeffs; i++) res.coeffs[i] = a.coeffs[i] - b.coeffs[i];
        if (whichLonger) for (int i = minNoOfCoeffs; i < newNoOfCoeffs; ++i) res.coeffs[i] = a.coeffs[i];
        else for (int i = minNoOfCoeffs; i < newNoOfCoeffs; ++i) res.coeffs[i] = -b.coeffs[i];
        return res;
    }

    friend Polynomial operator*(const Polynomial &a, const Polynomial &b) {
        int newNoOfCoeffs = 1;
        int maxNoOfCoeffs = a.noOfCoeffs + b.noOfCoeffs + 1;
        while (newNoOfCoeffs < maxNoOfCoeffs) newNoOfCoeffs <<= 1; // complementation to 2^k degree polynomial

        auto *f_a = new complex<double>[newNoOfCoeffs]; // complex coeffs defining a
        auto *f_b = new complex<double>[newNoOfCoeffs]; // complex coeffs defining b

        for (int i = 0; i < a.noOfCoeffs; i++) f_a[i] = a.coeffs[i];
        for (int i = 0; i < b.noOfCoeffs; i++) f_b[i] = b.coeffs[i];

        fft(f_a, newNoOfCoeffs, false); // transform a and b into roots of unity
        fft(f_b, newNoOfCoeffs, false);

        for (int i = 0; i < newNoOfCoeffs; ++i) f_a[i] *= f_b[i]; // multiplication per coefficient stored in f_a
        fft(f_a, newNoOfCoeffs, true); // inverse fft on f_a (now defining resulting polynomial)

        Polynomial res(newNoOfCoeffs);
        for (int i = 0; i < res.noOfCoeffs; i++) res.coeffs[i] = round(f_a[i].real());

        delete[]f_a;
        delete[]f_b;

        return res;
    }
};


#endif //PROJEKT_POLYNOMIAL_H
