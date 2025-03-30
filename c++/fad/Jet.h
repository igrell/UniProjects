#ifndef BACA_E_FUNJET_H
#define BACA_E_FUNJET_H

#include "iostream"

class Jet {
    double fx;
    double dx, dy, dx2, dy2, dxdy;

public:

    Jet() : fx(0), dx(0), dy(0), dx2(0), dy2(0), dxdy(0) {}

    Jet(const double fx, const bool varFlag) : fx(fx), dx2(0), dy2(0), dxdy(0) {
        if (varFlag) {
            dx = 1;
            dy = 0;
        } else {
            dx = 0;
            dy = 1;
        }
    }

    Jet(const double fx, const double dx, const double dy, const double dx2, const double dxdy, const double dy2)
            : fx(fx), dx(dx),
              dy(dy),
              dx2(dx2),
              dy2(dy2),
              dxdy(dxdy) {}

    Jet(const Jet &jet) : fx(jet.fx), dx(jet.dx), dy(jet.dy), dx2(jet.dx2), dxdy(jet.dxdy), dy2(jet.dy2) {};

    inline Jet &operator=(const Jet &jet) {
        fx = jet.fx;
        dx = jet.dx;
        dy = jet.dy;
        dx2 = jet.dx2;
        dy2 = jet.dy2;
        dxdy = jet.dxdy;
        return *this;
    };

    inline friend std::ostream &operator<<(std::ostream &ostream, const Jet &funJet) {
        ostream << (funJet.fx == -0 ? 0 : funJet.fx) << " "
                << (funJet.dx == -0 ? 0 : funJet.dx) << " " << (funJet.dy == -0 ? 0 : funJet.dy) << " "
                << (funJet.dx2 == -0 ? 0 : funJet.dx2) << " " << (funJet.dxdy == -0 ? 0 : funJet.dxdy) << " "
                << (funJet.dy2 == -0 ? 0 : funJet.dy2);
        return ostream;
    }

    inline friend Jet operator+(const Jet &u, const Jet &v) {
        return Jet(u.fx + v.fx, u.dx + v.dx, u.dy + v.dy,
                      u.dx2 + v.dx2,
                      u.dxdy + v.dxdy,
                      u.dy2 + v.dy2);
    }

    inline friend Jet operator+(const double c, const Jet &u) { return u + Jet(c, 0, 0, 0, 0, 0); }

    inline friend Jet operator+(const Jet &u, const double c) { return c + u; }

    inline friend Jet operator-(const Jet &u, const Jet &v) {
        return Jet(u.fx - v.fx, u.dx - v.dx, u.dy - v.dy,
                      u.dx2 - v.dx2,
                      u.dxdy - v.dxdy,
                      u.dy2 - v.dy2);
    }

    inline friend Jet operator-(const double c, const Jet &u) { return Jet(c, 0, 0, 0, 0, 0) - u; }

    inline friend Jet operator-(const Jet &u, const double c) { return (-c + u); }

    inline friend Jet operator-(const Jet &u) { return (-1 * u); }

    inline friend Jet operator*(const Jet &u, const Jet &v) {
        return Jet((u.fx * v.fx),
                      (u.fx * v.dx) + (u.dx * v.fx),
                      (u.fx * v.dy) + (u.dy * v.fx),
                      u.fx * v.dx2 + 2 * u.dx * v.dx + u.dx2 * v.fx,
                      u.dxdy * v.fx + u.dx * v.dy + u.dy * v.dx + u.fx * v.dxdy,
                      u.fx * v.dy2 + 2 * u.dy * v.dy + u.dy2 * v.fx);
    }

    inline friend Jet operator*(const double c, const Jet &u) { return u * Jet(c, 0, 0, 0, 0, 0); }

    inline friend Jet operator*(const Jet &u, const double c) { return c * u; }

    inline friend Jet operator/(const Jet &u, const Jet &v) {
        double u_by_v = u.fx / v.fx;
        double res_dx = (u.dx - (u_by_v * v.dx)) / v.fx;
        double res_dy = (u.dy - (u_by_v * v.dy)) / v.fx;
        double minus_v = -v.fx;
        return Jet(u_by_v,
                   res_dx,
                   res_dy,
                      (2 * v.dx * res_dx + u_by_v * v.dx2 - u.dx2) / minus_v,
                      (v.dx * res_dy + v.dy * res_dx + u_by_v * v.dxdy - u.dxdy) / minus_v,
                      (2 * v.dy * res_dy + u_by_v * v.dy2 - u.dy2) / minus_v
        );
    }

    inline friend Jet operator/(const Jet &u, const double c) { return u / Jet(c, 0, 0, 0, 0, 0); }

    inline friend Jet operator/(const double c, const Jet &u) { return Jet(c, 0, 0, 0, 0, 0) / u; }

    inline friend Jet sin(const Jet &u) {
        double sin_u = std::sin(u.fx);
        double cos_u = std::cos(u.fx);
        return Jet(sin_u,
                      u.dx * cos_u,
                      u.dy * cos_u,
                      u.dx2 * cos_u - u.dx * u.dx * sin_u,
                      u.dxdy * cos_u - u.dx * u.dy * sin_u,
                      u.dy2 * cos_u - u.dy * u.dy * sin_u);
    }

    inline friend Jet cos(const Jet &u) {
        double sin_u = std::sin(u.fx);
        double cos_u = std::cos(u.fx);
        return Jet(cos_u,
                      -u.dx * sin_u,
                      -u.dy * sin_u,
                   -(u.dx2 * sin_u + u.dx * u.dx * cos_u),
                   -(u.dxdy * sin_u + u.dx * u.dy * cos_u),
                   -(u.dy2 * sin_u + u.dy * u.dy * cos_u));
    }

    inline friend Jet exp(const Jet &u) {
        double exp_u = std::exp(u.fx);
        return Jet(exp_u,
                      u.dx * exp_u,
                      u.dy * exp_u,
                      u.dx2 * exp_u + u.dx * u.dx * exp_u,
                      u.dxdy * exp_u + u.dx * u.dy * exp_u,
                      u.dy2 * exp_u + u.dy * u.dy * exp_u);
    }
};

#endif //BACA_E_FUNJET_H
