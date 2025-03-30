//
// Created by kapela on 08.04.2020.
//

#ifndef GAUSS_VECTALG_H
#define GAUSS_VECTALG_H

// Jeżeli odkomentujemy poniższą linię to indeksy (asercje) nie będą sprawdzane
// #define  NDEBUG

#include <memory>
#include <iostream>
#include <cassert>
#include <vector>
#include <cmath>

/**
 * Wektor wymiaru n.
 * Domyślnie nie jest wypełniany zerami.
 */


/**
 * Macierz n x n.
 * Domyślnie nie jest wypełniana zerami.
 */
class Matrix {
private:
    size_t n;
    std::unique_ptr<double[]> data = nullptr;
public:
    explicit Matrix(size_t n = 0) : n(n) {
        data.reset(new double[n * n]);
    }

    Matrix(const Matrix &m) : n(m.n), data(new double[m.n * m.n]) {
        std::copy(m.data.get(), m.data.get() + n * n, data.get());
    }

    Matrix(Matrix &&m) noexcept : n(m.n), data(std::move(m.data)) {
        m.n = 0;
    }

    Matrix(std::initializer_list<std::initializer_list<double> > list) {
        n = list.size();
        data.reset(new double[n * n]);
        auto it = data.get();
        for (const auto &row : list) {
            it = std::copy(row.begin(), row.end(), it);
        }
    }

    Matrix &operator=(const Matrix &m) {
        if (this != &m) {
            if (n != m.n) {
                data.reset(new double[m.n * m.n]);
                n = m.n;
            }
            std::copy(m.data.get(), m.data.get() + n * n, data.get());
        }
        return *this;
    }

    Matrix &operator=(Matrix &&m) noexcept {
        data = std::move(m.data);
        n = m.n;
        return *this;
    }

    size_t size() const {
        return n;
    }

    double operator()(size_t row, size_t col) const {
        assert(row < n && col < n);
        return data[row * n + col];
    }

    double &operator()(size_t row, size_t col) {
        assert(row < n && col < n);
        return data[row * n + col];
    }

    friend std::ostream &operator<<(std::ostream &out, const Matrix &m) {
        for (int row = 0; row < m.n; ++row) {
            for (int col = 0; col < m.n; ++col) {
                out << m(row, col) << " ";
            }
            out << std::endl;
        }
        return out;
    }

    friend std::istream &operator>>(std::istream &in, Matrix &m) {
        for (auto &x : m) {
            in >> x;
        }
        return in;
    }

    typedef double *iterator;

    iterator begin() {
        return data.get();
    }

    iterator end() {
        return data.get() + n * n;
    }

    typedef const double *const_iterator;

    const_iterator begin() const {
        return data.get();
    }

    const_iterator end() const {
        return data.get() + n;
    }
};

class Vector {
    size_t n;
    std::unique_ptr<double[]> data = nullptr;
public:

    inline explicit Vector(size_t n = 0) : n(n) { data.reset(new double[n]); }

    inline Vector(const Vector &m) : n(m.n), data(new double[m.n]) { std::copy(m.data.get(), m.data.get() + n, data.get()); }

    inline double operator[](size_t index) const { return data[index]; }

    inline double &operator[](size_t index) { return data[index]; }

    typedef double *iterator;

    inline iterator begin() { return data.get(); }

    inline iterator end() { return data.get() + n; }

    typedef const double *const_iterator;

    inline const_iterator begin() const { return data.get(); }

    inline const_iterator end() const { return data.get() + n; }
};

#endif //GAUSS_VECTALG_H
